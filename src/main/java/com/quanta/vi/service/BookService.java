package com.quanta.vi.service;

import com.quanta.vi.entity.Book;
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
public interface BookService extends IService<Book> {
    // 获取词书列表
    public List<Book> getBookList();

    void choiceBook(Long uid, Long bookId);

    // 获取词书详情
    Book getBookDetail(long id);
}
