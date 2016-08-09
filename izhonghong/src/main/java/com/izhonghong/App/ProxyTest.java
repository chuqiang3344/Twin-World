package com.izhonghong.App;


import com.tyaer.basic.datebase.helper.MysqlHelper;
import com.tyaer.basic.net.helper.HttpHelper;
import com.tyaer.basic.utils.StringHandle;
import com.tyaer.basic.utils.TestProxyUtils;
import com.tyaer.bean.HttpResutBean;
import com.tyaer.bean.ProxyBean;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by Twin on 2016/7/29.
 */
public class ProxyTest {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        ProxyTest proxyTest = new ProxyTest();
        proxyTest.detectSqlProxy("select * from proxy");
//        proxyTest.crawlProxy_kuaidaili_free();
    }

    public void crawlProxy_kuaidaili() {
        String url = "http://www.kuaidaili.com/getproxy/?orderid=986975552897624&num=100&area=%E4%B8%AD%E5%9B%BD&area_ex=&port=&port_ex=&ipstart=&ipstart_ex=&carrier=2&an_ha=1&an_an=1&protocol=1&method=2&quality=0&sort=0&b_pcchrome=1&b_pcie=1&b_pcff=1&showtype=1";
        System.out.println("代理IP提供网站：" + url);
        System.out.println("检测网站：" + TestProxyUtils.detectProxyUrl);
        StringBuilder sb = new StringBuilder();
        sb.append("ip" + "\t" + "port" + "\t" + "status" + "\t" + "检测时间" + "\t" + "响应速度(ms)" + "\n");
        HttpHelper httpHelper = new HttpHelper();
        String html = httpHelper.sendRequest(url);
        Document document = Jsoup.parse(html);
        String text = document.select("div#content").text();
        String s = text.substring(text.indexOf("下载为文本") + 5).trim();
        String[] proxys = s.split(" ");
        System.out.println("获取代理数：" + proxys.length);
        for (int i = 0; i < proxys.length; i++) {
            String proxy = proxys[i];
            String[] split = proxy.split(":");
            String ip = split[0];
            int port = Integer.valueOf(split[1]);
            System.out.println("------------------当前测试代理 " + i + " ：" + ip + "\t" + port);
            String result = TestProxyUtils.testProxy(ip, port);
            System.out.println(result);
            sb.append(result + "\n");
        }
        System.out.println(sb);
    }

    public void crawlProxy_kuaidaili_free() {
        String url = "http://www.kuaidaili.com/";
        System.out.println("代理IP提供网站：" + url);
        System.out.println("检测网站：" + TestProxyUtils.detectProxyUrl);
        StringBuilder sb = new StringBuilder();
        sb.append("ip" + "\t" + "port" + "\t" + "status" + "\t" + "检测时间" + "\t" + "响应速度(ms)" + "\n");
        HttpHelper httpHelper = new HttpHelper();
        String html = httpHelper.sendRequest(url);
        System.out.println(html);
        Document parse = Jsoup.parse(html);
        Elements select = parse.select("tr.odd");
        System.out.println(select.size());
        for (Element element : select) {
            element.select("td:");
        }
    }

    /**
     * 测试查询数据库找到的代理IP
     *
     * @param sql
     */
    public void detectSqlProxy(String sql) {
        StringHandle stringHandle = new StringHandle();
        String regex = "<center>您的IP是：\\[(.*?)\\] 来自：(.*?) (.*?)</center></body>";
        MysqlHelper mysqlHelper = new MysqlHelper("root", "123456", "jdbc:mysql://127.0.0.1:3306/crawlsinaweibo?characterEncoding=UTF-8&rewriteBatchedStatements=true&generateSimpleParameterMetadata=true");
        String replaceSql = "REPLACE INTO proxy(id,proxy_ip,proxy_port,proxy_type,proxy_address,update_time,ping,status,fail_num) values (?,?,?,?,?,?,?,?,?)";
        List<Map<String, Object>> proxys = mysqlHelper.findModeResult(sql, null);
        System.out.println(proxys.size());
        for (Map<String, Object> proxy : proxys) {
            int id = (Integer) proxy.get("id");
            String proxy_ip = (String) proxy.get("proxy_ip");
            int proxy_port = (Integer) proxy.get("proxy_port");
            System.out.println("----------------当前测试代理" + id + "：" + proxy_ip + "\t" + proxy_port);
            int fail_num = (Integer) proxy.get("fail_num");
            long startTime = System.currentTimeMillis();
            HttpResutBean httpResutBean = TestProxyUtils.DetectProxy(proxy_ip, proxy_port);
            long time = System.currentTimeMillis() - startTime;
            Timestamp update_time = new Timestamp(System.currentTimeMillis());
            String html = httpResutBean.getHtml();
            ProxyBean proxyBean;
            int status;
            if (httpResutBean.getStatusCode() == 200) {
                status = 1;
                String[] strings = stringHandle.praseRegex(html, regex);
                proxyBean = new ProxyBean(id, proxy_ip, proxy_port, strings[2], strings[1], update_time, status, fail_num, time);
                mysqlHelper.replace(replaceSql, proxyBean);
            } else {
                System.out.println(html);
                fail_num++;
                status = 0;
                proxyBean = new ProxyBean(id, proxy_ip, proxy_port, null, null, update_time, status, fail_num, time);
                mysqlHelper.replace(replaceSql, proxyBean);
            }
        }
    }
}



