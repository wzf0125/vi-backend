package com.quanta.vi.service.serviceImpl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quanta.vi.bean.LearnWordDo;
import com.quanta.vi.constants.SearchType;
import com.quanta.vi.entity.*;
import com.quanta.vi.exception.ApiException;
import com.quanta.vi.mapper.*;
import com.quanta.vi.service.LearnRecordService;
import com.quanta.vi.service.StatisticsService;
import com.quanta.vi.service.WordBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quanta.vi.utils.PageResult;
import com.quanta.vi.vo.LearnDataInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author quanta
 * @since 2022-11-23
 */
@Service
public class WordBookServiceImpl extends ServiceImpl<WordBookMapper, WordBook> implements WordBookService {
    @Autowired
    WordBookMapper wordBookMapper;
    @Autowired
    StatisticsService statisticsService;
    @Autowired
    UserConfigMapper userConfigMapper;
    @Autowired
    WordMapper wordMapper;
    @Autowired
    StatisticsMapper statisticsMapper;
    @Autowired
    LearnRecordService learnRecordService;

    @Override
    public PageResult getBookWordPage(Long bookId, int currentPage, int pageSize) {
        List<Word> bookWordByPage = wordBookMapper.getBookWordByPage(bookId, (currentPage - 1) * pageSize, pageSize);
        Long total = wordBookMapper.getBookWordByPageCount(bookId);
        return new PageResult(bookWordByPage, total, pageSize);
    }

    @Override
    public LearnDataInfoVo getLearnWord(Long uid) {
        UserConfig userConfig = userConfigMapper.selectOne(new LambdaQueryWrapper<UserConfig>().eq(UserConfig::getUserId, uid));
        // 获取学习记录
        Statistics statistics = statisticsService.getStatistics(uid);
        List<Word> learnData = wordBookMapper.getBookWordByPage(statistics.getBookId(), statistics.getWordNumber(), userConfig.getGroupSize());
        return buildLearnDataInfoVo(userConfig, learnData);
    }

    @Override
    @Transactional
    public void saveReviewData(Long uid) {
        // 先获取用户配置
        UserConfig userConfig = userConfigMapper.selectOne(
                new LambdaQueryWrapper<UserConfig>()
                        .eq(UserConfig::getUserId, uid));
        // 更新用户复习数据
        Integer groupSize = userConfig.getGroupSize();
        learnRecordService.update(
                new LambdaUpdateWrapper<LearnRecord>()
                        .set(LearnRecord::getIsReviewed, 1)
                        .eq(LearnRecord::getIsReviewed, 0)
                        .eq(LearnRecord::getUserId, uid)
                        .eq(LearnRecord::getBookId, userConfig.getBookId())
                        .gt(LearnRecord::getGmtCreate, DateUtil.offset(new Date(), DateField.HOUR_OF_DAY, -24))
                        .last("limit " + groupSize)
        );

    }

    private LearnDataInfoVo buildLearnDataInfoVo(UserConfig userConfig, List<Word> learnData) {
        List<Long> ids = learnData.stream().map(Word::getId).collect(Collectors.toList());
        LearnDataInfoVo data = new LearnDataInfoVo();
        // 获取混淆单词列表
        QueryWrapper<Word> wrapper = new QueryWrapper<>();
        wrapper.notIn("id", ids);
        wrapper.last("limit " + userConfig.getGroupSize() * 4);
        List<Word> option = wordMapper.selectList(wrapper);

        data.setLearnWord(new ArrayList<>());
        // 编辑单词
        learnData.forEach(e -> {
            LearnWordDo tmp = new LearnWordDo();
            tmp.setWord(e);
            int correct = new Random().nextInt(4);
            tmp.setCorrect(correct);
            Word[] choice = new Word[4];
            choice[correct] = e;
            // 从混淆单词列表中提取3个混淆词汇
            AtomicInteger count = new AtomicInteger();
            new Random().ints(0, option.size()).distinct().limit(3).forEach(
                    i -> {
                        if (choice[count.get()] == null) {
                            choice[count.getAndIncrement()] = option.get(i);
                        } else {
                            count.getAndIncrement();
                            choice[count.getAndIncrement()] = option.get(i);
                        }
                    }
            );
            tmp.setOption(choice);
            data.getLearnWord().add(tmp);
        });
        return data;
    }

    @Override
    @Transactional
    public void saveLearnData(Long uid) {
        // 先获取用户配置
        UserConfig userConfig = userConfigMapper.selectOne(
                new LambdaQueryWrapper<UserConfig>()
                        .eq(UserConfig::getUserId, uid));
        if (userConfig.getBookId() == null || userConfig.getGroupSize() == null) {
            throw new ApiException("参数错误");
        }

        // 查询用户词书学习记录
        Statistics statistics = statisticsService.getStatistics(uid);
        int wordNumber = statistics.getWordNumber();
        // 设置用户学习进度
        statistics.setWordNumber(wordNumber + userConfig.getGroupSize());
        statisticsMapper.updateById(statistics);

        // 记录要复习的词汇
        List<Word> bookList = wordBookMapper.getBookWordByPage(userConfig.getBookId(), wordNumber, userConfig.getGroupSize());
        List<LearnRecord> collect = bookList.stream().map(e -> {
            LearnRecord record = new LearnRecord();
            record.setBookId(userConfig.getBookId());
            record.setUserId(uid);
            record.setWordId(e.getId());
            return record;
        }).collect(Collectors.toList());
        // 保存学习记录
        learnRecordService.saveBatch(collect);
    }

    @Override
    public LearnDataInfoVo getReviewWord(Long uid) {
        UserConfig userConfig = userConfigMapper.selectOne(
                new LambdaQueryWrapper<UserConfig>()
                        .eq(UserConfig::getUserId, uid));
        List<LearnRecord> list = learnRecordService.list(
                new LambdaQueryWrapper<LearnRecord>()
                        .eq(LearnRecord::getUserId, uid)
                        .eq(LearnRecord::getBookId, userConfig.getBookId())
                        .eq(LearnRecord::getIsReviewed, "0")
                        .gt(LearnRecord::getGmtCreate, DateUtil.offset(new Date(), DateField.HOUR_OF_DAY, -24))
                        .last("LIMIT " + userConfig.getGroupSize())
        );
        if (list == null || list.isEmpty()) {
            throw new ApiException("暂无单词需要复习");
        }
        List<Long> ids = list.stream().map(LearnRecord::getWordId).collect(Collectors.toList());
        List<Word> words = wordMapper.selectList(
                new LambdaQueryWrapper<Word>()
                        .in(Word::getId, ids)
        );
        return buildLearnDataInfoVo(userConfig, words);
    }

    @Override
    public PageResult search(int currentPage, int pageSize, int type, Long uid, String keyWord, Long bookId) {
        // 全文检索
        if (type == SearchType.ALL) {
            List<Word> words = wordBookMapper.searchWord(null, (currentPage - 1) * pageSize, pageSize, keyWord);
            Long total = wordBookMapper.searchWordCount(null, keyWord);
            return new PageResult(words, total, pageSize);
        } else if (type == SearchType.IN_BOOK) {
            List<Word> words = wordBookMapper.searchWord(bookId, (currentPage - 1) * pageSize, pageSize, keyWord);
            Long total = wordBookMapper.searchWordCount(bookId, keyWord);
            return new PageResult(words, total, pageSize);
        } else {
            throw new ApiException("检索类型错误");
        }
    }
}
