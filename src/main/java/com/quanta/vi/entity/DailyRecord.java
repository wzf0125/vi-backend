package com.quanta.vi.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("daily_record")
public class DailyRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * pk
     */
    @TableId("id")
    private Integer id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 学习单词数量
     */
    @TableField("learn_sum")
    private Integer learnSum;

    /**
     * 复习单词数量
     */
    @TableField("review_sum")
    private Integer reviewSum;

    /**
     * 逻辑删除（1表示删除，0表示未删除）
     */
    @TableLogic
    private Integer isDeleted;


}
