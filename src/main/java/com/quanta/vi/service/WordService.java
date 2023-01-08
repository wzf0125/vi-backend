package com.quanta.vi.service;

import com.quanta.vi.entity.Word;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author quanta
 * @since 2022-11-21
 */
public interface WordService extends IService<Word> {

    // 获取单词详情
    Word getWordDetail(long id);
}
