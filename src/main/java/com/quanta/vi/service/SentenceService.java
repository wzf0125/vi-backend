package com.quanta.vi.service;

import com.quanta.vi.entity.Sentence;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author quanta
 * @since 2022-11-21
 */
public interface SentenceService extends IService<Sentence> {
    // 获取每日例句（五句）
    List<Sentence> getDailySentence();

    // 根据单词id获取例句
    List<Sentence> getSentenceByWordId(long id);
}
