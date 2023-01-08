package com.quanta.vi.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author quanta
 * @since 2022-11-23
 */
@Getter
@Setter
@TableName("word_book")
public class WordBook implements Serializable {

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
     * 逻辑删除（1表示删除，0表示未删除）
     */
    @TableLogic
    private Integer isDeleted;


}
