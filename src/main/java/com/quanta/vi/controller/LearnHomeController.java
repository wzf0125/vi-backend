package com.quanta.vi.controller;

import com.quanta.vi.bean.JsonResponse;
import com.quanta.vi.constants.Roles;
import com.quanta.vi.interceptor.RequiredPermission;
import com.quanta.vi.service.SentenceService;
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
@RequestMapping("/learn/home")
@RequiredPermission({Roles.ROLE_USER})
public class LearnHomeController extends BaseController {

    @Autowired
    private SentenceService sentenceService;
    @Autowired
    private StatisticsService statisticsService;

    /**
     * [L005]获取每日例句
     * GET /learn/home/dailySentence
     * 接口ID：50337739
     * 接口地址：https://www.apifox.cn/web/project/1916282/apis/api-50337739
     */
    @GetMapping("/dailySentence")
    public JsonResponse<Object> dailySentence() {
        return JsonResponse.success(sentenceService.getDailySentence());
    }

    /**
     * [L006]获取用户待学和需复习单词数量
     * GET /learn/home/plan
     * 接口ID：50337752
     * 接口地址：https://www.apifox.cn/web/project/1916282/apis/api-50337752
     */
    @GetMapping("/plan")
    public JsonResponse<Object> plan() {
        return JsonResponse.success(statisticsService.getUnLearnAndReviewCount(getUid()));
    }
}
