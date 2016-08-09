package com.izhonghong.App;

import com.tyaer.basic.datebase.helper.SSHHelper;
import com.tyaer.basic.net.helper.HttpHelper;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 * Created by Twin on 2016/7/28.
 */
public class ServerLogin {

    private static int page=1;
    private static String pageSize="100";

    public static void main(String[] args) {
        String url = "http://61.172.249.230:8080/mas/downma/queryDownmaPager.xhtml";
        HashMap<String, String> prams = new HashMap<String, String>();
        /**全部验证**/
//        prams.put("logStatus", "0");
        /**服务器异常**/
//        prams.put("dm_dmType", "-2");
        /**数据异常**/
        prams.put("dm_dmType", "0");
        prams.put("logStatus", "0");

        prams.put("pageSize",pageSize);
        checkServer(url, prams);
        System.out.println("异常服务器访问排查完毕！");
    }

    private static void checkServer(String url, HashMap<String, String> prams) {
        HttpHelper httpHelper = new HttpHelper();
        SSHHelper sshHelper = new SSHHelper();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String html = httpHelper.sendPostRequest(url, prams);
//        System.out.println(html);
        Document document = Jsoup.parse(html);
        Elements select = document.select("div.tab-list>table tr");
        int size = select.size();
        System.out.println(size);
        for (int i = 0; i < size; i++) {
            Element e = select.get(i);
            if (i == 0) {
                String id = e.select("th:nth-child(1)").text();
                String name = e.select("th:nth-child(2)").text();
                String type = e.select("th:nth-child(3)").text();
                String disk = e.select("th:nth-child(4)").text();
                String ip = e.select("th:nth-child(5)").text();
                String mac = e.select("th:nth-child(6)").text();
                String status = e.select("th:nth-child(7)").text();
                String dataStatus = e.select("th:nth-child(8)").text();
                String taskNum = e.select("th:nth-child(9)").text();
                String endTime = e.select("th:nth-child(10)").text();
                System.out.println(id + "\t" + name + "\t" + type + "\t" + disk + "\t" + ip + "\t" + mac + "\t" + status + "\t" + dataStatus + "\t" + taskNum + "\t" + endTime + "\t" + "排查结果" + "\t" + "排查时间");
            } else {
                String id = e.select("td:nth-child(1)").text();
                String name = e.select("td:nth-child(2)").text();
                String img = e.select("td:nth-child(3)>img").attr("src");
                String type;
                if (img.contains("tencent.png")) {
                    type = "QQ";
                } else if (img.contains("sina.png")) {
                    type = "Sina";
                } else if (img.contains("twitter.png")) {
                    type = "Twitter";
                } else {
                    type = "Null";
                }
                String disk = e.select("td:nth-child(4)").text();
                String ip = e.select("td:nth-child(5)").text();
                String mac = e.select("td:nth-child(6)").text();
                String status = e.select("td:nth-child(7)").text();
                String dataStatus = e.select("td:nth-child(8)").text();
                String taskNum = e.select("td:nth-child(9)").text();
                String endTime = e.select("td:nth-child(10)").text();
                boolean result=true;
//                result = sshHelper.connectVerification(ip, 5522, "pi", "raspberry");
                String explain = "";
                if(!result){
                    explain="服务器无法访问";
                }
                if(disk.equals("0.0MB")){
                    explain="服务器磁盘空间不足";
                }
                if(StringUtils.isEmpty(explain)){
                    explain="十分钟内没有采集到新的数据";
                }
                String checkTime = sdf.format(System.currentTimeMillis());
                System.out.println(id + "\t" + name + "\t" + type + "\t" + disk + "\t" + ip + "\t" + mac + "\t" + status + "\t" + dataStatus + "\t" + taskNum + "\t" + endTime + "\t"+explain+"\t" + checkTime);
            }
        }
        String text = document.select("div#myuserpage>a:nth-last-child(3)").text();
        String pageNumber=String.valueOf(page+1);
        if(StringUtils.isNotEmpty(text)){
            url="http://61.172.249.230:8080/mas/downma/queryDownmaPager.xhtml?top=down";
            prams.clear();
            prams.put("logStatus", "0");
            prams.put("pageNumber",pageNumber);
            prams.put("pageSize",pageSize);
            checkServer(url,prams);
        }
    }
}

