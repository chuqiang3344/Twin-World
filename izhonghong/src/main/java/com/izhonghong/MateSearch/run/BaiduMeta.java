package com.izhonghong.MateSearch.run;

import com.izhonghong.MateSearch.bean.SeedResultBean;
import com.tyaer.basic.datebase.helper.MysqlHelper;
import com.tyaer.basic.net.helper.HttpHelper;
import com.tyaer.basic.utils.DateUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Twin on 2016/8/8.
 */
public class BaiduMeta {
    protected static Logger logger = Logger.getLogger(BaiduMeta.class);
    private MysqlHelper mysqlHelper = new MysqlHelper("root", "123456", "jdbc:mysql://127.0.0.1:3306/meta_search?characterEncoding=UTF-8&rewriteBatchedStatements=true&generateSimpleParameterMetadata=true");

    public List<String> getKeywords() {
        List<Map<String, Object>> keywords = mysqlHelper.findModeResult("select * from keywords", null);
        List<String> result = new ArrayList<String>();
        for (Map<String, Object> keyword : keywords) {
            String key = (String) keyword.get("keywords");
            result.add(key);
        }
        return result;
    }

    static String searchUrlx = "http://news.baidu.com/ns?ct=1&rn=20&ie=utf-8&bs=(keywords)&rsv_bp=1&sr=0&cl=2&f=8&prevct=no&tn=news&word=%E5%A5%A5%E8%BF%90%E4%BC%9A&rsv_sug3=1&rsv_sug4=231&rsv_sug1=1&rsv_sug=1";

    static SimpleDateFormat sdf_Normal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static void main(String[] args) {
        String moduleRootDir=Class.class.getClass().getResource("/").getPath()+"../../";
        String log4jProperties=moduleRootDir+"configure/log4j.properties";
        PropertyConfigurator.configure(log4jProperties);
        BaiduMeta baiduMeta = new BaiduMeta();
        HttpHelper httpHelper = new HttpHelper();
        List<String> keywords = baiduMeta.getKeywords();
        SeedResultBean seedResultBean;
        for (String keyword : keywords) {
            logger.info(keyword);
            String searchUrl = searchUrlx.replace("(keywords)", keyword);
            System.out.println(searchUrl);
            String html = httpHelper.sendRequest(searchUrl);
            Document document = Jsoup.parse(html);
            Elements select = document.select("div#content_left>div>div.result");
            for (Element element : select) {
                String title = element.select("h3>a").text();
                String url = element.select("h3>a").attr("abs:href");
                String[] split = element.select("div>p").text().split("  ");
                String source=split[0];
                Date date = DateUtils.manifoldTimeToNormal(split[1]);
                String text = element.select("div>div").text();
                seedResultBean=new SeedResultBean(title,url,source,new Timestamp(date.getTime()),text);
                System.out.println(seedResultBean);
                break;
            }
        }
    }
}
