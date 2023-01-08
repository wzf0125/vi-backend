package com.quanta.vi.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.quanta.vi.bean.JsonResponse;
import com.quanta.vi.constants.Roles;
import com.quanta.vi.entity.Sentence;
import com.quanta.vi.interceptor.RequiredPermission;
import com.quanta.vi.service.CollectionService;
import com.quanta.vi.service.SentenceService;
import com.quanta.vi.service.WordService;
import com.quanta.vi.vo.WordDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2022/11/21
 */
@RestController
@RequestMapping("/learn/word")
@RequiredPermission({Roles.ROLE_USER})
public class LearnWordController extends BaseController {

    @Autowired
    private WordService wordService;
    @Autowired
    private SentenceService sentenceService;
    @Autowired
    private CollectionService collectionService;

    /**
     * [L009]获取单词详情
     * GET /learn/word/{id}
     * 接口ID：50340050
     * 接口地址：https://www.apifox.cn/web/project/1916282/apis/api-50340050
     */
    @GetMapping("/{id}")
    public JsonResponse<Object> word(@PathVariable Long id) {
        // 获取单词信息
        WordDetailVO wordDetailVO =
                JSONObject.parseObject(JSON.toJSONString(wordService.getWordDetail(id)), WordDetailVO.class);

        // 获取例句信息
        List<Sentence> sentences = sentenceService.getSentenceByWordId(id);
        if (sentences != null && !sentences.isEmpty()) {
            Sentence sentence = sentences.get(new Random().nextInt(sentences.size()));
            wordDetailVO.setViSentence(sentence.getViSentence());
            wordDetailVO.setChSentence(sentence.getChSentence());
        }

        // 查询单词是否已收藏
        wordDetailVO.setIsFavorite(collectionService.wordIsCollected(getUid(), id) ? 1 : 0);

        return JsonResponse.success(wordDetailVO);
    }

    /**
     * [L010]收藏单词
     * POST /learn/word/collection
     * 接口ID：50340453
     * 接口地址：https://www.apifox.cn/web/project/1916282/apis/api-50340453
     */
    @PostMapping("/collection")
    public JsonResponse<Object> collectWord(Long wordId) {
        collectionService.collectWord(getUid(), wordId);
        return JsonResponse.success();
    }

    /**
     * [L010-1]取消收藏单词
     * DELETE /learn/word/collection
     * 接口ID：52836982
     * 接口地址：https://www.apifox.cn/web/project/1916282/apis/api-52836982
     */
    @DeleteMapping("/collection")
    public JsonResponse<Object> cancelCollectWord(Long wordId) {
        collectionService.cancelCollectWord(getUid(), wordId);
        return JsonResponse.success();
    }

    /**
     * [L011]获取收藏夹单词列表
     * GET /learn/word/collection
     * 接口ID：50340672
     * 接口地址：https://www.apifox.cn/web/project/1916282/apis/api-50340672
     */
    @GetMapping("/collection")
    public JsonResponse<Object> getCollectionList() {
        return JsonResponse.success(collectionService.getCollectionList(getUid()));
    }
}
