package com.quanta.vi.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Description
 * Param
 * return
 * Author:86184
 * Date: 2022/12/6
 */
@Data
public class NewsListVo {
    /**
     * id
     */
    private Integer id;

    /**
     * 网站
     */
    private String websiteName;

    /**
     * 标题
     */
    private String title;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 发布时间
     */
    private Date pubTime;

    /**
     * 图片
     */
    private List<String> resources;

    private String category1;

    private String category2;

//    private String body;
}
