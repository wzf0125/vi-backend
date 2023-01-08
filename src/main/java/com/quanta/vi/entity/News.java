package com.quanta.vi.entity;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 网站的新闻
 * @TableName news
 */
@Data
public class News implements Serializable {
    /**
     * 新闻自身的id，自增
     */
    private Integer id;

    /**
     * 外键：新闻表网站地址
     */
    private String websiteUrl;

    private String websiteName;

    /**
     * 新闻的请求链接
     */
    private String requestUrl;

    /**
     * 新闻网站的响应链接
     */
    private String responseUrl;

    /**
     * 一级类别
     */
    private String category1;

    /**
     * 二级类别
     */
    private String category2;

    /**
     * 标题
     */
    private String title;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 正文
     */
    private String body;

    /**
     * 发布时间例2017-01-01 00:00:00,
没有发布时间的则为0000-00-00 00:00:00
     */
    private Date pubTime;

    /**
     * 新闻图片列表，使用json的[]列表，没有则为NULL
     */
//    private String images;
    private List<String> resources;

    private Integer isFavorite;


    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        News other = (News) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
//            && (this.getWebsiteId() == null ? other.getWebsiteId() == null : this.getWebsiteId().equals(other.getWebsiteId()))
            && (this.getRequestUrl() == null ? other.getRequestUrl() == null : this.getRequestUrl().equals(other.getRequestUrl()))
            && (this.getResponseUrl() == null ? other.getResponseUrl() == null : this.getResponseUrl().equals(other.getResponseUrl()))
            && (this.getCategory1() == null ? other.getCategory1() == null : this.getCategory1().equals(other.getCategory1()))
            && (this.getCategory2() == null ? other.getCategory2() == null : this.getCategory2().equals(other.getCategory2()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getSummary() == null ? other.getSummary() == null : this.getSummary().equals(other.getSummary()))
            && (this.getBody() == null ? other.getBody() == null : this.getBody().equals(other.getBody()))
            && (this.getPubTime() == null ? other.getPubTime() == null : this.getPubTime().equals(other.getPubTime()));
//            && (this.getImages() == null ? other.getImages() == null : this.getImages().equals(other.getImages()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
//        result = prime * result + ((getWebsiteId() == null) ? 0 : getWebsiteId().hashCode());
        result = prime * result + ((getRequestUrl() == null) ? 0 : getRequestUrl().hashCode());
        result = prime * result + ((getResponseUrl() == null) ? 0 : getResponseUrl().hashCode());
        result = prime * result + ((getCategory1() == null) ? 0 : getCategory1().hashCode());
        result = prime * result + ((getCategory2() == null) ? 0 : getCategory2().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getSummary() == null) ? 0 : getSummary().hashCode());
        result = prime * result + ((getBody() == null) ? 0 : getBody().hashCode());
        result = prime * result + ((getPubTime() == null) ? 0 : getPubTime().hashCode());
//        result = prime * result + ((getColeTime() == null) ? 0 : getColeTime().hashCode());
//        result = prime * result + ((getImages() == null) ? 0 : getImages().hashCode());
//        result = prime * result + ((getLanguageId() == null) ? 0 : getLanguageId().hashCode());
//        result = prime * result + ((getMd5() == null) ? 0 : getMd5().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
//        sb.append(", websiteId=").append(websiteId);
        sb.append(", requestUrl=").append(requestUrl);
        sb.append(", responseUrl=").append(responseUrl);
        sb.append(", category1=").append(category1);
        sb.append(", category2=").append(category2);
        sb.append(", title=").append(title);
        sb.append(", summary=").append(summary);
        sb.append(", body=").append(body);
        sb.append(", pubTime=").append(pubTime);
//        sb.append(", coleTime=").append(coleTime);
//        sb.append(", images=").append(images);
//        sb.append(", languageId=").append(languageId);
//        sb.append(", md5=").append(md5);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}