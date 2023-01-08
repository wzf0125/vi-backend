package com.quanta.vi.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;
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
@TableName("learn_record")
public class LearnRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * pk
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 单词id
     */
    @TableField("word_id")
    private Long wordId;

    /**
     * 词书id
     */
    @TableField("book_id")
    private Long bookId;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 是否已经复习
     */
    @TableField("is_reviewed")
    private int isReviewed;

    /**
     * 逻辑删除（1表示删除，0表示未删除）
     */
    @TableLogic
    private Integer isDeleted;

    @TableField("gmt_create")
    private Date gmtCreate;

}
