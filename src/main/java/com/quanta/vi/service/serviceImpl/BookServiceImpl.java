package com.quanta.vi.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.quanta.vi.entity.Book;
import com.quanta.vi.entity.Statistics;
import com.quanta.vi.entity.UserConfig;
import com.quanta.vi.exception.ApiException;
import com.quanta.vi.mapper.BookMapper;
import com.quanta.vi.mapper.StatisticsMapper;
import com.quanta.vi.mapper.UserConfigMapper;
import com.quanta.vi.service.BookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author quanta
 * @since 2022-11-21
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {
    @Autowired
    BookMapper bookMapper;
    @Autowired
    UserConfigMapper userConfigMapper;
    @Autowired
    StatisticsMapper statisticsMapper;

    // 获取词书列表
    @Override
    public List<Book> getBookList() {
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        wrapper.select("id", "name", "introduction", "word_number", "cover");
        return bookMapper.selectList(wrapper);
    }

    // 选择词书
    @Override
    @Transactional
    public void choiceBook(Long uid, Long bookId) {
        Book book = bookMapper.selectById(bookId);
        if (book == null) {
            throw new ApiException("词书不存在");
        }

        UpdateWrapper<UserConfig> wrapper = new UpdateWrapper<>();
        wrapper.eq("user_id", uid);
        wrapper.set("book_id", bookId);
        int row = userConfigMapper.update(null, wrapper);

        Statistics statistics = statisticsMapper.selectOne(
                new LambdaQueryWrapper<Statistics>()
                        .eq(Statistics::getUserId, uid)
                        .eq(Statistics::getBookId, bookId)
        );
        if (statistics == null) {
            statistics = new Statistics(uid, bookId);
            statisticsMapper.insert(statistics);
        }

        if (row != 1) {
            throw new ApiException("选择失败");
        }
    }

    @Override
    public Book getBookDetail(long id) {
        return bookMapper.selectById(id);
    }
}
