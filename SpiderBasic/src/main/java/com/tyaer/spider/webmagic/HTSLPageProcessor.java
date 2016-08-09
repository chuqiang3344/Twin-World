package com.tyaer.spider.webmagic;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class HTSLPageProcessor implements PageProcessor {

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).addCookie("opid","010100155393").addCookie("itravel_skey","f2bb4f6a29c1ac2e1356eb7410ced63f")
            .addCookie("opname","%E5%88%98%E6%99%93%E6%B5%B7");

    @Override
    public void process(Page page) {// process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
        // 部分二：定义如何抽取页面信息，并保存下来
        System.out.println(page.getJson());
        page.putField("RoomName",page.getJson().jsonPath("$.RoomInfoList[*].RoomName"));
        page.putField("Breakfast",page.getJson().jsonPath("$.RoomInfoList[*].RatePlan.Breakfast"));
        page.putField("SoldPrice",page.getJson().jsonPath("$.RoomInfoList[*].RatePlan.SoldPrice"));
        page.putField("AccountType",page.getJson().jsonPath("$.RoomInfoList[*].RatePlan.AccountType"));
        page.putField("Text",page.getJson().jsonPath("$.RoomInfoList[*].RatePlan.Calendar[*][0].Text"));
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new HTSLPageProcessor())
                //从"https://github.com/code4craft"开始抓
                .addUrl("https://itravel.hwht.com/hotel/detail/ajax_rooms/100001418?s=958166f8c4aa3d6aa132239bcf126e2d")
                //开启5个线程抓取
                .thread(5)
                //启动爬虫
                .run();
    }
}