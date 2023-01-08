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
@TableName("statistics")
public class Statistics implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id（打卡记录）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属用户id（逻辑外键）
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 词书id
     */
    @TableField("book_id")
    private Long bookId;

    /**
     * 已学数量
     */
    @TableField("word_number")
    private Integer wordNumber;

    /**
     * 逻辑删除（1表示删除，0表示未删除）
     */
    @TableLogic
    private Integer isDeleted;

    public Statistics() {
    }

    public Statistics(Long userId, Long bookId) {
        this.userId = userId;
        this.bookId = bookId;
        this.wordNumber = 0;
    }
}
