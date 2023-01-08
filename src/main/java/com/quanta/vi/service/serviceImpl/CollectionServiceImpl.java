package com.quanta.vi.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.quanta.vi.entity.Collection;
import com.quanta.vi.entity.Word;
import com.quanta.vi.exception.ApiException;
import com.quanta.vi.mapper.CollectionMapper;
import com.quanta.vi.service.CollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author quanta
 * @since 2022-11-21
 */
@Service
public class CollectionServiceImpl extends ServiceImpl<CollectionMapper, Collection> implements CollectionService {

    @Autowired
    private CollectionMapper collectionMapper;

    @Override
    public boolean wordIsCollected(long userId, long wordId) {
        QueryWrapper<Collection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("word_id", wordId);
        return collectionMapper.selectOne(queryWrapper) != null;
    }

    @Override
    public void collectWord(long userId, long wordId) {
        // 查询单词是否已存在
        if (wordIsCollected(userId, wordId)) {
            throw new ApiException("单词已收藏");
        }

        // 收藏单词
        Collection collection = new Collection();
        collection.setUserId(userId);
        collection.setWordId(wordId);
        collectionMapper.insert(collection);
    }

    @Override
    public void cancelCollectWord(long userId, long wordId) {
        QueryWrapper<Collection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("word_id", wordId);
        collectionMapper.delete(queryWrapper);
    }

    @Override
    public List<Word> getCollectionList(long userId) {
        List<Word> collections = collectionMapper.getCollectionList(userId);
        Collections.reverse(collections);
        return collections;
    }
}
