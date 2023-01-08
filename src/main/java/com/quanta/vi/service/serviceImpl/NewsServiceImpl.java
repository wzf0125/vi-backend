package com.quanta.vi.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.quanta.vi.entity.Collection;
import com.quanta.vi.entity.News;
import com.quanta.vi.entity.NewsCollection;
import com.quanta.vi.entity.Sentence;
import com.quanta.vi.exception.ApiException;
import com.quanta.vi.mapper.NewsCollectionMapper;
import com.quanta.vi.mapper.NewsMapper;
import com.quanta.vi.service.NewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quanta.vi.utils.PageResult;
import com.quanta.vi.utils.RedisUtils;
import com.quanta.vi.vo.NewsListVo;
import com.quanta.vi.vo.NewsSearchVo;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站的新闻 服务实现类
 * </p>
 *
 * @author quanta
 * @since 2022-11-21
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements NewsService {


    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private NewsCollectionMapper newsCollectionMapper;

    @Override
    public PageResult selectByText(String text, Integer page, Integer size) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("vietnews");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MultiMatchQueryBuilder queryBuilder = QueryBuilders.multiMatchQuery(text,
                "title","summary","body");
        HighlightBuilder highlightBuilder = new HighlightBuilder(); // 生成高亮查询器
        highlightBuilder.field("title");// 高亮查询字段
        highlightBuilder.field("summary");// 高亮查询字段
        highlightBuilder.field("body");// 高亮查询字段
        highlightBuilder.requireFieldMatch(false); // 如果要多个字段高亮,这项要为false
        highlightBuilder.fragmentSize(800000); // 最大高亮分片数
        highlightBuilder.numOfFragments(0); // 从第一个分片获取高亮片段
        searchSourceBuilder.highlighter(highlightBuilder);
        searchSourceBuilder.from((page-1) * size);
        searchSourceBuilder.size(size);
        searchSourceBuilder.query(queryBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        TotalHits totalHits = hits.getTotalHits();
        long value = totalHits.value;
        List<NewsSearchVo> newsList = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            //设置高亮
            String string = hit.getSourceAsString();
            NewsSearchVo news = JSONObject.parseObject(string, NewsSearchVo.class);
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            HighlightField highlightTitle = hit.getHighlightFields().get("title");
            if (highlightTitle != null && highlightTitle.getFragments().length > 0) {
                news.setTitle(highlightTitle.getFragments()[0].toString());
            }
            HighlightField highlightBody = hit.getHighlightFields().get("body");
            if (highlightBody != null && highlightBody.getFragments().length > 0) {
                news.setBody(highlightBody.getFragments()[0].toString());
            }
            HighlightField highlightSummary = hit.getHighlightFields().get("summary");
            if (highlightSummary != null && highlightSummary.getFragments().length > 0) {
                news.setSummary(highlightSummary.getFragments()[0].toString());
            }
//            List<String> o = (List<String>)redisUtils.get(String.valueOf(news.getWebsiteId()));
            List<String> images = JSONObject.parseArray((String) sourceAsMap.get("images"),String.class);
            List<String> resources = new ArrayList<>(images);
            news.setResources(resources);
            List listKey1 = (List) redisTemplate.boundListOps("website_"+ String.valueOf(sourceAsMap.get("website_id"))).index(0);
            assert listKey1 != null;
//            System.out.println(listKey1);
            if(listKey1.get(1)!=null){
                news.setWebsiteName((String) listKey1.get(1));
            }else{
                news.setWebsiteName((String) listKey1.get(0));
            }

            newsList.add(news);
        }
        return new PageResult(newsList, value, size);
//        return newsList;
    }

    @Override
    public List<NewsListVo> selectByPubTime(Integer page, Integer size) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("vietnews");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        FieldSortBuilder pub_time = SortBuilders.fieldSort("pub_time").order(SortOrder.DESC);
        searchSourceBuilder.from((page-1) * size);
        searchSourceBuilder.size(size);
        searchSourceBuilder.sort(pub_time);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        List<NewsListVo> newsList = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            String string = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            NewsListVo news = JSONObject.parseObject(string, NewsListVo.class);
            List listKey1 = (List) redisTemplate.boundListOps("website_"+ String.valueOf(sourceAsMap.get("website_id"))).index(0);
//            List<String> o = (List<String>)redisUtils.get(String.valueOf(news.getWebsiteId()));
            List<String> images = JSONObject.parseArray((String) sourceAsMap.get("images"),String.class);
            List<String> resourses = new ArrayList<>(images);
            news.setResources(resourses);
            assert listKey1 != null;
            if(listKey1.get(1)!=null){
                news.setWebsiteName((String) listKey1.get(1));
            }else{
                news.setWebsiteName((String) listKey1.get(0));
            }
            newsList.add(news);
        }
        return newsList;
    }

    @Override
    public News selectById(long userId,int id) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("vietnews");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("id",String.valueOf(id));
        searchSourceBuilder.query(queryBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = searchResponse.getHits();
        SearchHit hit = hits.getHits()[0];
        String string = hit.getSourceAsString();
        Map<String, Object> sourceAsMap = hit.getSourceAsMap();
        News news = JSONObject.parseObject(string, News.class);
        List listKey1 = (List) redisTemplate.boundListOps("website_"+ String.valueOf(sourceAsMap.get("website_id"))).index(0);
        List<String> images = JSONObject.parseArray((String) sourceAsMap.get("images"),String.class);
        List<String> resourses = new ArrayList<>(images);
        news.setResources(resourses);
        assert listKey1 != null;
        news.setWebsiteName((String) listKey1.get(1));
        news.setWebsiteUrl((String) listKey1.get(0));
        QueryWrapper<NewsCollection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("news_id", id).eq("is_deleted",0);
        NewsCollection newsCollection = newsCollectionMapper.selectOne(queryWrapper);
        if(newsCollection!=null){
            news.setIsFavorite(1);
        }else{
            news.setIsFavorite(0);
        }
        return news;
    }

    @Override
    public int collectNews(long userId, long newsId) {
        QueryWrapper<NewsCollection> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("news_id", newsId);
        NewsCollection newsCollection = newsCollectionMapper.selectOne(queryWrapper);
        if(newsCollection!=null){
            if(newsCollection.getIsDeleted()==1){
                UpdateWrapper<NewsCollection> wrapper = new UpdateWrapper<>();
                wrapper.set("is_deleted",0).eq("user_id", userId)
                        .eq("news_id", newsId);
                return newsCollectionMapper.update(null,wrapper);
            }
            else{
                throw new ApiException("该新闻已收藏");
            }
        }
        // 收藏新闻
        NewsCollection collection = new NewsCollection();
        collection.setUserId((int) userId);
        collection.setNewsId((int) newsId);
        return newsCollectionMapper.insert(collection);
    }

    @Override
    public int cancelCollectNews(long userId, long newsId) {

        UpdateWrapper<NewsCollection> wrapper = new UpdateWrapper<>();
        wrapper.set("is_deleted",1).eq("user_id", userId)
                .eq("news_id", newsId);

        return newsCollectionMapper.update(null,wrapper);
    }

    @Override
    public PageResult getCollectionList(long userId,Integer page,Integer size) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("vietnews");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        QueryWrapper<NewsCollection> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).eq("is_deleted",0)
                .select("news_id");
        List<NewsCollection> newsCollections=newsCollectionMapper.selectList(wrapper);
        List<Integer> ids=new ArrayList<>();
        for(NewsCollection newsCollection:newsCollections){
            ids.add(newsCollection.getNewsId());
        }
        Long total = newsCollectionMapper.selectCount(wrapper);
        boolQueryBuilder.filter(QueryBuilders.termsQuery("id", ids));
        searchSourceBuilder.from((page-1) * size);
        searchSourceBuilder.size(size);
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        List<NewsListVo> newsList = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            String string = hit.getSourceAsString();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            NewsListVo news = JSONObject.parseObject(string, NewsListVo.class);
            List listKey1 = (List) redisTemplate.boundListOps("website_"+ String.valueOf(sourceAsMap.get("website_id"))).index(0);
            List<String> images = JSONObject.parseArray((String) sourceAsMap.get("images"),String.class);
            List<String> resourses = new ArrayList<>(images);
            news.setResources(resourses);
            assert listKey1 != null;
            if(listKey1.get(1)!=null){
                news.setWebsiteName((String) listKey1.get(1));
            }else{
                news.setWebsiteName((String) listKey1.get(0));
            }
            newsList.add(news);
        }
        return new PageResult(newsList, total, size);
    }


}
