package com.quanta.vi.controller;

import com.quanta.vi.bean.JsonResponse;
import com.quanta.vi.constants.Roles;
import com.quanta.vi.entity.Statistics;
import com.quanta.vi.interceptor.RequiredPermission;
import com.quanta.vi.service.BookService;
import com.quanta.vi.service.StatisticsService;
import com.quanta.vi.vo.StatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2022/11/21
 */
@RestController
@RequestMapping("/learn/statistics")
@RequiredPermission({Roles.ROLE_USER})
public class LearnStatisticsController extends BaseController{

    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private BookService bookService;

    /**
     * [L012]获取总览数据
     * GET /learn/statistics
     * 接口ID：50340667
     * 接口地址：https://www.apifox.cn/web/project/1916282/apis/api-50340667
     * */
    @GetMapping()
    public JsonResponse<Object> getStatistics() {
        // 获取用户统计信息
        Statistics statistics = statisticsService.getStatistics(getUid());

        // 获取词书单词总数
        Integer wordNumber = bookService.getBookDetail(statistics.getBookId()).getWordNumber();

        // 数据转换
        StatisticsVO statisticsVO = new StatisticsVO();
        statisticsVO.setLearned(statistics.getWordNumber());
        statisticsVO.setUnlearn(wordNumber - statistics.getWordNumber());

        return JsonResponse.success(statisticsVO);
    }

}
