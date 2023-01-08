package com.quanta.vi.mapper;

import com.quanta.vi.entity.Collection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quanta.vi.entity.Word;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author quanta
 * @since 2022-11-21
 */
@Mapper
public interface CollectionMapper extends BaseMapper<Collection> {

    // 查询收藏单词列表
    List<Word> getCollectionList(long userId);
}
