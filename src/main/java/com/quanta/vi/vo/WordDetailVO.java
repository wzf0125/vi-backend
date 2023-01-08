package com.quanta.vi.vo;

import lombok.Data;

/**
 * Description: 单词详情
 * Author: 严仕鹏
 * Date: 2022/12/3
 */
@Data
public class WordDetailVO {
    /**
     * 自增id（单词）
     */
    private Long id;

    /**
     * 单词本身
     */
    private String vi;

    /**
     * 单词释义中文
     */
    private String translationCh;

    /**
     * 单词释义英文
     */
    private String translationEn;

    /**
     * 分类
     */
    private String tag;

    /**
     * 单词发音链接
     */
    private String pronunciation;

    /**
     * 越南语例句
     */
    private String viSentence;

    /**
     * 中文例句
     */
    private String chSentence;

    /**
     * 单词是否收藏
     */
    private Integer isFavorite;
}
