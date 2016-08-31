package com.tyaer.App;

import com.tyaer.basic.json.JsonHelper;
import com.tyaer.basic.net.helper.HttpHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Map;

/**
 * Created by Twin on 2016/7/28.
 */
public class test {
    public static void main(String[] args) {
        HttpHelper httpHelper = new HttpHelper();
        String html = httpHelper.sendRequest("http://36kr.com/p/5051957.html");
//        System.out.println(html);
//        System.out.println("=================================");
        Document document = Jsoup.parse(html);
        Elements scripts = document.select("script");
        for (Element script : scripts) {
            if(script.toString().contains("detailArticle|post")){
                String json = script.toString().replace("detailArticle|post","detailArticle");
//                System.out.println(json);
                List<Map<String, Object>> maps = JsonHelper.useJpathGetInfo(json, "detailArticle>id,catch_title,summary,content");
                JsonHelper.showResult(maps);
            }
        }
    }
}
