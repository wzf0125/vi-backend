package com.quanta.vi.service;

import com.quanta.vi.entity.News;
import com.baomidou.mybatisplus.extension.service.IService;
import com.quanta.vi.entity.Word;
import com.quanta.vi.utils.PageResult;
import com.quanta.vi.vo.NewsListVo;
import com.quanta.vi.vo.NewsSearchVo;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 网站的新闻 服务类
 * </p>
 *
 * @author quanta
 * @since 2022-11-21
 */
public interface NewsService extends IService<News> {

    PageResult selectByText(String text, Integer page, Integer size) throws IOException;

    List<NewsListVo> selectByPubTime(Integer page, Integer size) throws IOException;

    News selectById(long userId,int id) throws IOException;

    PageResult getCollectionList(long userId,Integer page, Integer size) throws IOException;

    int collectNews(long userId, long newsId);

    int cancelCollectNews(long userId, long wordId);


}
