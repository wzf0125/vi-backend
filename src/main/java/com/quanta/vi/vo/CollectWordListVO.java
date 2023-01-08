package com.quanta.vi.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * Description:
 * Author: 严仕鹏
 * Date: 2022/12/3
 */
public class CollectWordListVO {
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
}
