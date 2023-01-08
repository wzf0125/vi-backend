package com.quanta.vi.controller;

import com.quanta.vi.bean.JsonResponse;
import com.quanta.vi.constants.Roles;
import com.quanta.vi.constants.SearchType;
import com.quanta.vi.dto.BookParam;
import com.quanta.vi.entity.Book;
import com.quanta.vi.entity.Word;
import com.quanta.vi.entity.WordBook;
import com.quanta.vi.exception.ApiException;
import com.quanta.vi.interceptor.RequiredPermission;
import com.quanta.vi.service.BookService;
import com.quanta.vi.service.WordBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2022/11/21
 */
@RestController
@RequestMapping("/learn/book")
@RequiredPermission({Roles.ROLE_USER})
public class LearnBookController extends BaseController {
    @Autowired
    BookService bookService;
    @Autowired
    WordBookService wordBookService;

    /**
     * [L001]获取词书列表
     * GET /learn/book
     * 接口ID：50339998
     * 接口地址：https://www.apifox.cn/web/project/1916282/apis/api-50339998
     */
    @GetMapping
    public JsonResponse<Object> getBookList() {
        List<Book> list = bookService.getBookList();
        Map<String, Object> map = new HashMap<>();
        map.put("bookList", list);
        return JsonResponse.success(map);
    }

    /**
     * [L002]选择词书
     * PUT /learn/book
     * 接口ID：50337725
     * 接口地址：https://www.apifox.cn/web/project/1916282/apis/api-50337725
     */
    @PutMapping
    public JsonResponse<Object> choiceBook(@RequestBody @Validated BookParam bookParam) {
        bookService.choiceBook(getUid(), bookParam.getId());
        return JsonResponse.success();
    }

    /**
     * [L003]获取词书单词列表
     * GET /learn/book/word
     * 接口ID：50340026
     * 接口地址：https://www.apifox.cn/web/project/1916282/apis/api-50340026
     */
    @GetMapping("/word/{id}")
    public JsonResponse<Object> word(Integer currentPage, Integer pageSize, @PathVariable Long id) {
        return JsonResponse.success(wordBookService.getBookWordPage(id, currentPage, pageSize));
    }

    /**
     * [L003a]查询单词
     * GET /learn/book/word/search
     * 接口ID：52765796
     * 接口地址：https://www.apifox.cn/web/project/1916282/apis/api-52765796
     */
    @GetMapping("/word/search")
    public JsonResponse<Object> search(Integer currentPage, Integer pageSize, String keyWord, Integer type, Long bookId) {
        if (pageSize == null || currentPage == null || currentPage < 0 || pageSize < 0 || type == null) {
            throw new ApiException("参数错误");
        }
        if (keyWord == null) {
            throw new ApiException("关键词不能为空");
        }
        if (!type.equals(SearchType.ALL) && !type.equals(SearchType.IN_BOOK)) {
            throw new ApiException("检索类型参数错误");
        }
        return JsonResponse.success(wordBookService.search(currentPage, pageSize, type, getUid(), keyWord, bookId));
    }

    /**
     * [L004]获取词书详情
     * GET /learn/book/{id}
     * 接口ID：50340575
     * 接口地址：https://www.apifox.cn/web/project/1916282/apis/api-50340575
     */
    @GetMapping("/{id}")
    public JsonResponse<Object> bookDetail(@PathVariable Long id) {
        Book book = bookService.getById(id);
        HashMap<String, Object> res = new HashMap<>();
        res.put("book", book);
        return JsonResponse.success(res);
    }
}
