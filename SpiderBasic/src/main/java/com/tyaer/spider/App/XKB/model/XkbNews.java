package com.tyaer.spider.App.XKB.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.List;

/**
 * Created by Twin on 2016/6/24.
 */
@XStreamAlias("XkbNews")
public class XkbNews {
    //xml主要属性：标题，url，时间，版块，内容，图片地址列表
    private String title;
    private String url;
    private String pubDate;
    private String section;
    private String content;
//    @XStreamImplicit(itemFieldName="image")
    private List<String> images;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public XkbNews(String title, String url, String pubDate, String section, String content, List<String> images) {
        this.title = title;
        this.url = url;
        this.pubDate = pubDate;
        this.section = section;
        this.content = content;
        this.images = images;
    }

    @Override
    public String toString() {
        return "XkbNews{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", Section='" + section + '\'' +
                ", content='" + content + '\'' +
                ", images=" + images +
                '}';
    }
}
