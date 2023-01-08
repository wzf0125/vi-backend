package com.quanta.vi.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.quanta.vi.entity.Word;
import com.quanta.vi.exception.ApiException;
import com.quanta.vi.mapper.WordMapper;
import com.quanta.vi.service.WordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author quanta
 * @since 2022-11-21
 */
@Service
public class WordServiceImpl extends ServiceImpl<WordMapper, Word> implements WordService {

    @Autowired
    private WordMapper wordMapper;

    @Override
    public Word getWordDetail(long id) {
        // 获取单词详情
        QueryWrapper<Word> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id)
                .select("id", "vi", "translation_ch", "translation_en", "tag", "pronunciation");
        Word word = wordMapper.selectOne(wrapper);
        if (word == null) {
            throw new ApiException("单词不存在");
        }
        return word;
    }
}
