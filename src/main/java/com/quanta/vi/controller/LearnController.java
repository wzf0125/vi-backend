package com.quanta.vi.controller;

import com.quanta.vi.bean.JsonResponse;
import com.quanta.vi.constants.Roles;
import com.quanta.vi.interceptor.RequiredPermission;
import com.quanta.vi.service.WordBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/learn")
@RequiredPermission({Roles.ROLE_USER})
public class LearnController extends BaseController {
    @Autowired
    WordBookService wordBookService;

    /**
     * [L007]获取本次学习数据
     * GET /learn/data
     * 接口ID：50339486
     * 接口地址：https://www.apifox.cn/web/project/1916282/apis/api-50339486
     */
    @GetMapping("/data")
    public JsonResponse<Object> data() {
        return JsonResponse.success(wordBookService.getLearnWord(getUid()));
    }

    /**
     * [L007a]获取待复习词汇
     * GET /learn/review
     * 接口ID：53116976
     * 接口地址：https://www.apifox.cn/web/project/1916282/apis/api-53116976
     */
    @GetMapping("/review")
    public JsonResponse<Object> review() {
        return JsonResponse.success(wordBookService.getReviewWord(getUid()));
    }

    /**
     * [L007b]完成复习
     * POST /learn/review
     * 接口ID：53384690
     * 接口地址：https://www.apifox.cn/web/project/1916282/apis/api-53384690
     */
    @PostMapping("/review/save")
    public JsonResponse<Object> saveReviewData() {
        wordBookService.saveReviewData(getUid());
        return JsonResponse.success();
    }

    /**
     * [L008]记录学习数据
     * POST /learn/save
     * 接口ID：50340542
     * 接口地址：https://www.apifox.cn/web/project/1916282/apis/api-50340542
     */
    @PostMapping("/save")
    public JsonResponse<Object> save() {
        wordBookService.saveLearnData(getUid());
        return JsonResponse.success();
    }
}
