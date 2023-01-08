package com.quanta.vi.mapper;

import com.quanta.vi.entity.Word;
import com.quanta.vi.entity.WordBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author quanta
 * @since 2022-11-23
 */
@Mapper
public interface WordBookMapper extends BaseMapper<WordBook> {
    // 分页获取词书单词
    List<Word> getBookWordByPage(Long bookId, int start, int size);

    // 统计词书单词数量
    Long getBookWordByPageCount(Long bookId);

    // 分页搜索单词
    List<Word> searchWord(Long bookId, int start, int size, String keyWord);

    // 统计检索到的单词数量
    Long searchWordCount(Long bookId, String keyWord);
}

