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
@TableName("sentence")
public class Sentence implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id（单词例句）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属单词id（逻辑外键）
     */
    @TableField("word_id")
    private Long wordId;

    /**
     * 越南语例句
     */
    @TableField("vi_sentence")
    private String viSentence;

    /**
     * 中文例句
     */
    @TableField("ch_sentence")
    private String chSentence;

    /**
     * 逻辑删除（1表示删除，0表示未删除）
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 音频url
     */
    @TableField("audio")
    private String audio;

}
