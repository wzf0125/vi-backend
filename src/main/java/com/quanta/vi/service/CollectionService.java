package com.quanta.vi.service;

import com.quanta.vi.entity.Collection;
import com.baomidou.mybatisplus.extension.service.IService;
import com.quanta.vi.entity.Word;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author quanta
 * @since 2022-11-21
 */
public interface CollectionService extends IService<Collection> {

    // 查询单词是否被收藏过
    boolean wordIsCollected(long userId, long wordId);

    // 收藏单词
    void collectWord(long userId, long wordId);

    // 取消收藏单词
    void cancelCollectWord(long userId, long wordId);

    // 获取收藏单词列表
    List<Word> getCollectionList(long userId);
}
