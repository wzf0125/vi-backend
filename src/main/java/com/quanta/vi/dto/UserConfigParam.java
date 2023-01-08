package com.quanta.vi.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2022/11/23
 */
@Data
public class UserConfigParam {
    /**
     * 每组单词学习数量
     */
    @Min(value = 5, message = "每组学习单词数量不得小于5")
    @Max(value = 200, message = "每组学习单词数量不得大于200")
    private Integer groupSize;

    /**
     * 第一次学习单词类型 0 看词选义
     */
    @Min(value = 0, message = "选项非法")
    @Max(value = 2, message = "选项非法")
    private Integer firstType;

    /**
     * 第二次学习单词类型 1 看词识义
     */
    @Min(value = 0, message = "选项非法")
    @Max(value = 2, message = "选项非法")
    private Integer secondType;

    /**
     * 第三次学习单词类型 2 看义识词
     */
    @Min(value = 0, message = "选项非法")
    @Max(value = 2, message = "选项非法")
    private Integer thirdType;

    /**
     * 看词识义 看义识词持续时间
     */
    @Min(value = 0, message = "你看你吗呢")
    @Max(value = 1000 * 60, message = "持续时长不得超过一分钟")
    private Integer timingDuration;
}
