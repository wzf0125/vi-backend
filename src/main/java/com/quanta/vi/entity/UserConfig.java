package com.quanta.vi.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2022/11/22
 */
@Data
@TableName("user_config")
public class UserConfig {
    private static final long serialVersionUID = 1L;

    /**
     * 自增id（用户）
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     * */
    @TableField("user_id")
    private Long userId;

    /**
     * 每组单词学习数量
     * */
    @TableField("group_size")
    private Integer groupSize;

    /**
     * 第一次学习单词类型 0 看词选义
     * */
    @TableField("first_type")
    private Integer firstType;

    /**
     * 第二次学习单词类型 1 看词识义
     * */
    @TableField("second_type")
    private Integer secondType;

    /**
     * 第三次学习单词类型 2 看义识词
     * */
    @TableField("third_type")
    private Integer thirdType;

    /**
     * 看词识义 看义识词持续时间
     */
    @TableField("timing_duration")
    private Integer timingDuration;

    /**
     * 选择的词书id
     * */
    @TableField("book_id")
    private Long bookId;

    /**
     * 逻辑删除（1表示删除，0表示未删除）
     */
    @TableLogic
    private Integer isDeleted;

}
