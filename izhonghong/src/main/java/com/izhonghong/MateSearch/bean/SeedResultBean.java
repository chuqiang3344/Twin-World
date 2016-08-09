package com.izhonghong.MateSearch.bean;

import java.sql.Timestamp;

/**
 * Created by Twin on 2016/8/8.
 */
public class SeedResultBean {
    private String title;
    private String url;
    private String source;
    private Timestamp pubDate;
    private String description;

    public SeedResultBean(String title, String url, String source, Timestamp pubDate, String description) {
        this.title = title;
        this.url = url;
        this.source = source;
        this.pubDate = pubDate;
        this.description = description;
    }

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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Timestamp getPubDate() {
        return pubDate;
    }

    public void setPubDate(Timestamp pubDate) {
        this.pubDate = pubDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SeedResultBean{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", source='" + source + '\'' +
                ", pubDate=" + pubDate +
                ", description='" + description + '\'' +
                '}';
    }
}
