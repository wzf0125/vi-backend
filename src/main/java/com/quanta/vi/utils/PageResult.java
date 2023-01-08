package com.quanta.vi.utils;

import lombok.Data;

/**
 * Description:
 * Param:
 * return:
 * Author: wzf
 * Date: 2022/12/6
 */
@Data
public class PageResult {
    /**
     * 数据
     */
    Object dataList;

    /**
     * 数据总数
     */
    Long total;

    /**
     * 页数
     */
    Integer pageCount;

    public PageResult(Object dataList, Long total, int pageSize) {
        this.total = total;
        this.dataList = dataList;
        this.pageCount = (int) Math.ceil((double) total / (double) pageSize);
    }
}
