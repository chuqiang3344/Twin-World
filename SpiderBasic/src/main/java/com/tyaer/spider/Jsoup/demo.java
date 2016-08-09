package com.tyaer.spider.Jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by Twin on 2016/6/22.
 */
public class demo {
    public static void main(String[] args) throws IOException {
        String url = "http://www.open-open.com/jsoup/example-list-links.htm";
        Document doc = Jsoup.connect(url).get();
        doc.select("");
    }
}
