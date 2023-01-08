package com.quanta.vi.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author quanta
 * @since 2022-11-21
 */
@Getter
@Setter
@TableName("word")
public class Word implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id（单词）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 单词本身
     */
    @TableField("vi")
    private String vi;

    /**
     * 单词释义中文
     */
    @TableField("translation_ch")
    private String translationCh;

    /**
     * 单词释义英文
     */
    @TableField("translation_en")
    private String translationEn;

    /**
     * 分类
     */
    @TableField("tag")
    private String tag;

    /**
     * 单词发音链接
     */
    @TableField("pronunciation")
    private String pronunciation;

    /**
     * 逻辑删除（1表示删除，0表示未删除）
     */
    @TableLogic
    private Integer isDeleted;


}
