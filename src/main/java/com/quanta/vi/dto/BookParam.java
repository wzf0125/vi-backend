package com.quanta.vi.dto;

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
public class BookParam {
    @Min(value = 0, message = "词书id非法")
    Long id; // 词书id
    @Min(value = 0, message = "页码非法")
    Integer currentPage; // 当前页

    @Min(value = 0, message = "页大小非法")
    @Max(value = 100, message = "页大小不能超过100")
    Integer pageSize; // 页大小
}
