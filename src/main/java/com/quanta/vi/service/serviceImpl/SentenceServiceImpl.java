package com.quanta.vi.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.quanta.vi.constants.CachePrefix;
import com.quanta.vi.entity.Sentence;
import com.quanta.vi.mapper.SentenceMapper;
import com.quanta.vi.service.SentenceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quanta.vi.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author quanta
 * @since 2022-11-21
 */
@Service
public class SentenceServiceImpl extends ServiceImpl<SentenceMapper, Sentence> implements SentenceService {

    @Autowired
    private SentenceMapper sentenceMapper;
    @Autowired
    private RedisUtils redisUtils;

    // 每日例句总数
    private static final int SENTENCE_NUMBER = 5;
    // 过滤例句：没有音频、例句过长等
    private static final String filteredSentenceKey = "filtered_sentence_ids";

    @Override
    public List<Sentence> getDailySentence() {
        String key = String.format(CachePrefix.DAILY_SENTENCE,
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        // 查询redis是否有记录
        String sentenceListString = (String) redisUtils.get(key);
        if (sentenceListString != null) {
            return JSON.parseObject(sentenceListString, new TypeReference<ArrayList<Sentence>>(){});
        }

        // 从redis中取出过滤例句的id集合
        String filteredSentenceIdsString = (String) redisUtils.get(filteredSentenceKey);
        Set<Long> filteredSentenceIds = JSON.parseObject(filteredSentenceIdsString, new TypeReference<HashSet<Long>>() {});

        // 查询例句总数
        Long count = sentenceMapper.selectCount(new QueryWrapper<>());
        // 生成 SENTENCE_NUMBER 个随机数
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < SENTENCE_NUMBER; i++) {
            long number = (long) (Math.random() * count) + 1;
            // 防止生成重复随机数，防止取到被过滤的例句
            while (filteredSentenceIds.contains(number) || ids.contains(number)) {
                number = (long) (Math.random() * count) + 1;
            }
            ids.add(number);
        }

        // 从数据库中查出 SENTENCE_NUMBER 条记录
        QueryWrapper<Sentence> wrapper = new QueryWrapper<>();
        wrapper.in("id", ids)
                .select("vi_sentence", "ch_sentence", "audio");
        List<Sentence> sentences = sentenceMapper.selectList(wrapper);

        // 存入Redis，24小时过期
        redisUtils.set(key, JSON.toJSONString(sentences), 60 * 60 * 24 + 1);

        return sentences;
    }

    @Override
    public List<Sentence> getSentenceByWordId(long id) {
        QueryWrapper<Sentence> wrapper = new QueryWrapper<>();
        wrapper.eq("word_id", id)
                .select("id", "word_id", "vi_sentence", "ch_sentence");
        return sentenceMapper.selectList(wrapper);
    }
}
