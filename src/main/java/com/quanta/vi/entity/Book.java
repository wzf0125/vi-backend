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
 * @since 2022-11-21
 */
@Getter
@Setter
@TableName("book")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id（词书）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 词书名
     */
    @TableField("name")
    private String name;

    /**
     * 词书介绍
     */
    @TableField("introduction")
    private String introduction;

    /**
     * 词书单词数（必要冗余，方便查询）
     */
    @TableField("word_number")
    private Integer wordNumber;

    /**
     * 封面图
     * */
    @TableField("cover")
    private String cover;

    /**
     * 逻辑删除（1表示删除，0表示未删除）
     */
    @TableLogic
    private Integer isDeleted;


}
