package com.tyaer.spider.App.XKB.run;

import com.thoughtworks.xstream.XStream;
import com.tyaer.basic.net.helper.HttpHelper;
import com.tyaer.spider.App.XKB.model.XkbNews;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Twin on 2016/6/24.
 */
public class XKB_Spider {
    public static void main(String[] args) throws IOException {
        String url = "http://epaper.xkb.com.cn/index.php";
        HttpHelper httpHelper = new HttpHelper();

        HashMap<String, String> prams = new HashMap<String, String>();
        prams.put("act", "list");
        prams.put("date", "2016-06-24");//设置要获取哪一天的报纸新闻
        String html = httpHelper.sendPostRequest(url, prams);
//        System.out.println(html);
        JSONArray jsonArray = new JSONArray(html);
        for (Object jsonObject : jsonArray) {
            Object id = ((JSONObject) jsonObject).get("id");
            Object edition = ((JSONObject) jsonObject).get("edition");
            Object version = ((JSONObject) jsonObject).get("version");
//            System.out.println(id+" "+edition+" "+version);
            prams.clear();
            prams.put("act", "img");
            prams.put("id", id.toString());
            String html1 = httpHelper.sendPostRequest(url, prams);
            html1 = html1.substring(html1.indexOf("<MAP"));
//            System.out.println(html1);
            String newsHtml = null;
            if (!html1.contains("return false")) {
                newsHtml = html1;
            } else {
                Pattern pattern = Pattern.compile("get_plateimg\\((.*?)\\)");
                Matcher matcher = pattern.matcher(html1);
                while (matcher.find()) {
                    String str = matcher.group(1);
//                    System.out.println(str);
                    prams.clear();
                    prams.put("act", "img");
                    prams.put("id", str);
                    String html2 = httpHelper.sendPostRequest(url, prams);
                    newsHtml = html2.substring(html2.indexOf("<MAP"));
                }
            }
//            System.out.println(newsHtml);
            Pattern pattern1 = Pattern.compile("href=\\\\\"\\\\(.*?)\\\\\">");
            Matcher matcher1 = pattern1.matcher(newsHtml);
            while (matcher1.find()) {
                String str2 = matcher1.group(1).replace("\\", "");
                String newsUrl = "http://epaper.xkb.com.cn" + str2;
//                    String newsUrl="http://epaper.xkb.com.cn/view/1040898";
//                    System.out.println(newsUrl);
                Document document = Jsoup.connect(newsUrl).get();
                String title = document.select("div.wordTitle>span").text();
                String wordTitle = document.select("div.wordTitle").text();
                String pubDate = useRegex("日期:\\[(.*?)\\]", wordTitle);
                String section = useRegex("版名:\\[(.*?)\\]", wordTitle);
                String content = document.select("div#news_content").text();
                List<String> images = new ArrayList<String>();
                Elements selects = document.select("div#news_content").select("img[src]");
                for (Element select : selects) {
                    String attr = select.attr("abs:src");
                    images.add(attr);
                }
                XkbNews xkbNews = new XkbNews(title, newsUrl, pubDate, section, content, images);
//                System.out.println(xkbNews);
                XStream xStream = new XStream();
                xStream.autodetectAnnotations(true);
                String xml = xStream.toXML(xkbNews);
                System.out.println(xml);
                File file = new File("./news/" + title.replaceAll("[/:：?？]", " ") + ".xml");
                try {
                    FileUtils.write(file, xml, "utf-8");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String useRegex(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
