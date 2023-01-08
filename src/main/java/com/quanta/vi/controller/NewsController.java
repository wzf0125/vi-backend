package com.quanta.vi.controller;

import com.quanta.vi.bean.JsonResponse;
import com.quanta.vi.constants.Roles;
import com.quanta.vi.entity.News;
import com.quanta.vi.interceptor.RequiredPermission;
import com.quanta.vi.service.NewsService;
import com.quanta.vi.utils.PageResult;
import com.quanta.vi.vo.NewsListVo;
import com.quanta.vi.vo.NewsSearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2022/11/21
 */
@RestController
@RequestMapping("/news")
@RequiredPermission({Roles.ROLE_USER})
public class NewsController extends BaseController{
    @Autowired
    NewsService newsService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public JsonResponse<Object> searchByText(@RequestParam("text") String text,
                                           @RequestParam(name="page",defaultValue = "1") Integer page,
                                           @RequestParam(name="size",defaultValue = "20") Integer size) throws Exception {

        PageResult news = newsService.selectByText(text, page, size);
        return JsonResponse.success(news);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public JsonResponse<Object> searchList(@RequestParam(name="page",defaultValue = "1") Integer page,
                                       @RequestParam(name="size",defaultValue = "20") Integer size) throws Exception {

        List<NewsListVo> news = newsService.selectByPubTime(page, size);
        return JsonResponse.success(news);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JsonResponse<Object> details(@PathVariable Integer id) throws Exception {

        News news = newsService.selectById(getUid(),id);
        return JsonResponse.success(news);
    }

    @RequestMapping(value = "/collection", method = RequestMethod.POST)
    public JsonResponse<Object> collectNews(Integer newsId){

        int i = newsService.collectNews(getUid(), newsId);
        return JsonResponse.success(i);
    }

    @RequestMapping(value = "/collection", method = RequestMethod.DELETE)
    public JsonResponse<Object> cancelCollectNews(Integer newsId){

        int i = newsService.cancelCollectNews(getUid(), newsId);
        return JsonResponse.success(i);
    }

    @RequestMapping(value = "/collection", method = RequestMethod.GET)
    public JsonResponse<Object> getCollectionList(@RequestParam(name="page",defaultValue = "1") Integer page,
                                                  @RequestParam(name="size",defaultValue = "20") Integer size) throws Exception {

        PageResult collectionList = newsService.getCollectionList(getUid(), page, size);
        return JsonResponse.success(collectionList);
    }
}
