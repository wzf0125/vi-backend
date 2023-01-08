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
@TableName("collection")
public class Collection implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id（收藏）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属用户id（逻辑外键）
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 所属单词id（逻辑外键）
     */
    @TableField("word_id")
    private Long wordId;

    /**
     * 逻辑删除（1表示删除，0表示未删除）
     */
    @TableLogic
    private Integer isDeleted;


}
