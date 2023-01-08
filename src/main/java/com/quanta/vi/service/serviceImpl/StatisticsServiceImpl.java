package com.quanta.vi.service.serviceImpl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.quanta.vi.entity.Book;
import com.quanta.vi.entity.LearnRecord;
import com.quanta.vi.entity.Statistics;
import com.quanta.vi.entity.UserConfig;
import com.quanta.vi.mapper.*;
import com.quanta.vi.service.StatisticsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quanta.vi.vo.StatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author quanta
 * @since 2022-11-21
 */
@Service
public class StatisticsServiceImpl extends ServiceImpl<StatisticsMapper, Statistics> implements StatisticsService {

    @Autowired
    UserConfigMapper userConfigMapper;
    @Autowired
    StatisticsMapper statisticsMapper;
    @Autowired
    BookMapper bookMapper;
    @Autowired
    LearnRecordMapper learnRecordMapper;

    @Override
    public Statistics getStatistics(Long uid) {
        QueryWrapper<UserConfig> userConfigQueryWrapper = new QueryWrapper<>();
        userConfigQueryWrapper.eq("user_id", uid);
        UserConfig userConfig = userConfigMapper.selectOne(userConfigQueryWrapper);
        Long bookId = userConfig.getBookId();

        QueryWrapper<Statistics> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", uid);
        wrapper.eq("book_id", bookId);
        Statistics statistics = statisticsMapper.selectOne(wrapper);
        // 如果不存在学习记录 则插入一条新的
        if (statistics == null) {
            statistics = new Statistics(uid, bookId);
            statisticsMapper.insert(statistics);
        }

        return statistics;
    }

    @Override
    public StatisticsVO getUnLearnAndReviewCount(Long uid) {
        UserConfig userConfig = userConfigMapper.selectOne(
                new LambdaQueryWrapper<UserConfig>()
                        .eq(UserConfig::getUserId, uid));
        Book book = bookMapper.selectById(userConfig.getBookId());
        Statistics statistics = statisticsMapper.selectOne(
                new LambdaQueryWrapper<Statistics>()
                        .eq(Statistics::getBookId, userConfig.getBookId())
                        .eq(Statistics::getUserId, userConfig.getUserId())
        );
        Long reviewed = learnRecordMapper.selectCount(
                new LambdaQueryWrapper<LearnRecord>()
                        .eq(LearnRecord::getIsReviewed, 0)
                        .eq(LearnRecord::getBookId, userConfig.getBookId())
                        .gt(LearnRecord::getGmtCreate, DateUtil.offset(new Date(), DateField.HOUR_OF_DAY, -24))
                        .eq(LearnRecord::getUserId, uid)
        );
        Long review = learnRecordMapper.selectCount(
                new LambdaQueryWrapper<LearnRecord>()
                        .eq(LearnRecord::getIsReviewed, 1)
                        .eq(LearnRecord::getBookId, userConfig.getBookId())
                        .eq(LearnRecord::getUserId, uid)
        );

        StatisticsVO res = new StatisticsVO();
        // 已学习
        res.setLearned(statistics.getWordNumber());
        // 待学习
        res.setUnlearn((book.getWordNumber() - statistics.getWordNumber()));
        // 已复习
        res.setReviewed(reviewed);
        // 待复习
        res.setReview(review);
        return res;
    }

}
