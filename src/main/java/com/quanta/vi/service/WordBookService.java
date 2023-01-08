package com.quanta.vi.service;

import com.quanta.vi.entity.Word;
import com.quanta.vi.entity.WordBook;
import com.baomidou.mybatisplus.extension.service.IService;
import com.quanta.vi.utils.PageResult;
import com.quanta.vi.vo.LearnDataInfoVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author quanta
 * @since 2022-11-23
 */
public interface WordBookService extends IService<WordBook> {
    // 查询词书单词
    PageResult getBookWordPage(Long bookId, int currentPage, int pageSize);

    // 搜索单词
    PageResult search(int currentPage, int pageSize, int type, Long uid, String keyWord,Long bookId);

    // 获取学习计划
    LearnDataInfoVo getLearnWord(Long uid);

    // 保存复习记录
    void saveReviewData(Long uid);

    // 保存一次学习进度
    void saveLearnData(Long uid);

    // 获取复习单词
    LearnDataInfoVo getReviewWord(Long uid);
}
