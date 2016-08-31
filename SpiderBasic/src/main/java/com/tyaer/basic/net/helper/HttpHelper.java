package com.tyaer.basic.net.helper;

import com.tyaer.basic.net.httptools.HttpClientManager;
import com.tyaer.basic.net.httptools.HttpClientManagerParams;
import com.tyaer.basic.utils.HtmlUtils;
import com.tyaer.basic.utils.StringHandle;
import com.tyaer.bean.HttpResutBean;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Http工具类
 *
 * @author Twin
 */
public class HttpHelper {

    protected Logger logger = Logger.getLogger(HttpHelper.class);

    public static final String REQUEST_TYPE_GET = "get";
    public static final String REQUEST_TYPE_POST = "post";

    /**
     * 模拟发出Http请求
     *
     * @param url    请求资源,如：http://www.baidu.com/,注意严谨的格式
     * @param params 请求参数
     * @param type   请求方式,目前只支持get/post
     * @return HttpResponseBody
     * @throws IOException
     * @throws ClientProtocolException
     */
    public String sendRequest(String url, String type, Map<String, String> params) {
        String html = "";
        // 搜索链接
        HttpClient httpClient = HttpClientManager.getHttpClient();
        // HttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = null;
        InputStream is = null;
        HttpGet httpGet = null;
        HttpPost httpPost = null;
        try {
            // GET方式请求
            if (HttpHelper.REQUEST_TYPE_GET.equals(type)) {
                // 加入请求参数
                if (params != null) {
                    if (url.indexOf("?") != -1) {
                        url += "&";
                    } else {
                        url += "?";
                    }
                    for (String key : params.keySet()) {
                        url += key + "=" + params.get(key) + "&";
                    }
                }
                httpGet = new HttpGet(url);
                // 模拟浏览器
                setHeader(httpGet);
                httpResponse = httpClient.execute(httpGet);
            } else if (HttpHelper.REQUEST_TYPE_POST.equals(type)) {
                // POST方式请求
                httpPost = new HttpPost(url);
                // 加入请求参数
                if (params != null) {
                    List<BasicNameValuePair> paramList = new ArrayList<BasicNameValuePair>();
                    for (String key : params.keySet()) {
                        if (key != null) {
                            paramList.add(new BasicNameValuePair(key, params
                                    .get(key)));
                        }
                    }
                    UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
                            paramList, "UTF-8");
                    httpPost.setEntity(formEntity);
                }
                httpResponse = httpClient.execute(httpPost);
            }
            responseGetHtml(httpResponse);
        } catch (Exception e) {
            logger.error("logger，searchUrl: " + url);
            logger.error(ExceptionUtils.getMessage(e));
        } finally {

            // 关闭操作类和数据流释放内存
            colseRequestStream(httpGet);
        }
        return html;
    }

    public String sendRequest(String url) {
        String html = null;
        // 搜索链接
        HttpClient httpClient = HttpClientManager.getHttpClient();
        HttpResponse httpResponse = null;
        HttpGet httpGet = null;
        try {
            httpGet = new HttpGet(url);
            // 模拟浏览器
            setHeader(httpGet);
            // 获取返回内容
            httpResponse = httpClient.execute(httpGet);
            html = responseGetHtml(httpResponse);
        } catch (Exception e) {
            logger.error("logger，searchUrl: " + url);
            logger.error(ExceptionUtils.getMessage(e));
        } finally {
            // 关闭操作类和数据流释放内存
            colseRequestStream(httpGet);
        }
        return html;
    }

    public String sendPostRequest(String url, Map<String, String> prams) {
        String html = null;
        HttpClient httpClient = HttpClientManager.getHttpClient();
        HttpPost httppost = new HttpPost(url);
        // All the parameters post to the web site
        List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
        for (String key : prams.keySet()) {
            nvps.add(new BasicNameValuePair(key, prams.get(key)));
        }
        try {
            // 模拟浏览器
            String userAgent = HttpClientManagerParams.getRandomUsersAgent();
            httppost.setHeader("User-Agent", userAgent);
            // httpGet.addHeader("Accept-Encoding", "gzip,deflate,sdch");//乱码
            httppost.setHeader("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, application/QVOD, application/QVOD, */*");
            httppost.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
            httppost.setHeader("Cache-Control", "no-cache");
            httppost.setHeader("Connection", "Keep-Alive");
//            hotpot.setHeader("Origin", "http://epaper.xkb.com.cn");
//            hotpot.setHeader("Referer", "http://epaper.xkb.com.cn/index.php");
//            hotpot.setHeader("Host", "epaper.xkb.com.cn");
//            hotpot.setHeader("X-Requested-With", "XMLHttpRequest");
            httppost.setHeader("Cookie", "JSESSIONID=2E7CA6D24BF0CD7213DB27E5A63C5D65");
            httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            HttpResponse httpResponse = httpClient.execute(httppost);
            html = responseGetHtml(httpResponse);
        } catch (Exception e) {
            logger.error("logger，searchUrl: " + url);
            logger.error(ExceptionUtils.getMessage(e));
        } finally {
            // 关闭操作类和数据流释放内存
            colseRequestStream(httppost);
        }
        return html;
    }


    public String sendRequest(String url, String cookie) {
        String html = "";
        // 搜索链接
        HttpClient httpClient = HttpClientManager.getHttpClient();
        HttpGet httpGet = null;
        try {
            httpGet = new HttpGet(url);
            // 模拟浏览器
            setHeader(httpGet);
            if (StringUtils.isNotBlank(cookie)) {
                httpGet.setHeader("Cookie", cookie);
            }
            // 获取返回内容
            HttpResponse httpResponse = httpClient.execute(httpGet);
            html = responseGetHtml(httpResponse);
        } catch (ClientProtocolException e) {
            logger.error("logger，searchUrl: " + url);
            logger.error(ExceptionUtils.getMessage(e));
        } catch (IOException e) {
            logger.error("logger，searchUrl: " + url);
            logger.error(ExceptionUtils.getMessage(e));
        } finally {
            // 关闭操作类和数据流释放内存
            colseRequestStream(httpGet);
        }
        return html;
    }

    /**
     * 使用代理的get请求
     *
     * @throws Exception
     */
    public String sendRequest(String url, HttpHost httpHost, String cookie) {
        String html = "";
        // 搜索链接
        HttpClient httpClient = HttpClientManager.getHttpClient();
        // 设置代理
        httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, httpHost);
        httpClient.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, false);
        HttpGet httpGet = null;
        try {
            httpGet = new HttpGet(url);
            // 模拟浏览器
            setHeader(httpGet);
//            RequestConfig config = RequestConfig.custom().setProxy(httpHost) .build();
//            httpGet.setConfig(config);
            //设置cookies
            if (StringUtils.isNotBlank(cookie)) {
                httpGet.setHeader("Cookie", cookie);
            }
            // 获取返回内容
            HttpResponse httpResponse = httpClient.execute(httpGet);
            html = responseGetHtml(httpResponse);
        } catch (Exception e) {
            logger.error("logger，searchUrl: " + url);
            logger.error(ExceptionUtils.getMessage(e));
            /*
             * 代理访问失败 fail_num+1
			 */
//			DTO.PROXYHELPER.addProxyFailNum(httpHost);
        } finally {
//			httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,null);
            // 关闭操作类和数据流释放内存
            colseRequestStream(httpGet);
        }
        return html;
    }

    /**
     * 使用代理的get请求
     */
    public HttpResutBean sendRequest(String url, HttpHost httpHost) {
        HttpResutBean httpResut = null;
        // 搜索链接
        HttpClient httpClient = HttpClientManager.getHttpClient();
        HttpResponse httpResponse = null;
        HttpGet httpGet = null;
        String html = null;
        try {
            httpGet = new HttpGet(url);
            // 模拟浏览器
            setHeader(httpGet);
            // 设置代理1
//            RequestConfig config = RequestConfig.custom().setProxy(httpHost) .build();
//            httpGet.setConfig(config);
            //设置代理2
            httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, httpHost);
            httpResponse = httpClient.execute(httpGet);
            html = responseGetHtml(httpResponse);
        } catch (IOException e) {
            logger.error("logger，searchUrl:" + url);
            html = ExceptionUtils.getMessage(e);
            logger.error(html);
        } finally {
            // 关闭操作类和数据流释放内存
            colseRequestStream(httpGet);
        }
        httpResut = new HttpResutBean(getStatusCode(httpResponse), getCharset(httpResponse),
                html);
        return httpResut;
    }

    private String responseGetHtml(HttpResponse httpResponse) throws IOException {
        String html = null;
        HttpEntity entity = httpResponse.getEntity();
        if (entity == null) {
            return null;
        }
        String charset1 = getCharset(entity.getContentType().toString());
        if (StringUtils.isNotBlank(charset1)) {
            html = EntityUtils.toString(entity, charset1);
        }else{
            charset1="UTF-8";
            html = EntityUtils.toString(entity,charset1);
        }
        /*如果出现乱码则换一种方式解析html*/
        if (StringHandle.isMessyCode(html)) {
            String charset2 = getCharSetByBody(html, charset1);
            html = new String(html.getBytes(), charset2);
        }
        EntityUtils.consume(entity);
        return html;
    }

    /*private String responseGetHtml(HttpResponse httpResponse) throws IOException {
        String html = null;
        HttpEntity entity = httpResponse.getEntity();
        if(entity == null){
            return null;
        }
        String charset = getCharset(entity.getContentType().toString());
        InputStream is = null;
        try {
            is = entity.getContent();
            ByteBuffer byteBuffer = readToByteBuffer(is, is.available());
            if (byteBuffer != null) {
                html = Charset.forName("ISO-8859-1").decode(byteBuffer).toString();
                charset = getCharSetByBody(html, charset);
                byteBuffer.rewind();
                html = Charset.forName(charset).decode(byteBuffer).toString();
            }
        } catch (IOException e) {
            throw e;
        } finally {
            colseRequestStream(is);
        }
        return html;
    }*/

    private void colseRequestStream(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void colseRequestStream(HttpRequestBase httpRequestBase) {
        if (httpRequestBase != null) {
            httpRequestBase.abort();
        }
    }

    private void colseRequestStream(InputStream is, HttpRequestBase httpRequestBase) {
        colseRequestStream(is);
        colseRequestStream(httpRequestBase);
    }

    public void setHeader(HttpGet httpGet) {
        // 搜索头
        String userAgent = HttpClientManagerParams.getRandomUsersAgent();
        httpGet.setHeader("User-Agent", userAgent);
        // httpGet.addHeader("Accept-Encoding", "gzip,deflate,sdch");//乱码
        httpGet.setHeader(
                "Accept",
                "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, application/QVOD, application/QVOD, */*");
        httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
        httpGet.setHeader("Cache-Control", "no-cache");
        httpGet.setHeader("Connection", "Keep-Alive");
        // httpGet.setHeader(
        // "Cookie",
        // "IPLOC=CN4301; SUV=0046143B71F0E36355B056DAD9261573; SUID=63E3F0712208990A0000000055C81AAE; weixinIndexVisited=1; pgv_pvi=5842396160; ssuid=9278728185; _ga=GA1.2.922012714.1440738001; ld=rZllllllll2qilhflllllVg6RaollllltKR0YZllll9lllll4fLCLK@@@@@@@@@@; ABTEST=0|1441784311|v1; ad=QBidiyllll2qE@HTlllllVgsMitllllltKR0YZllllkllllljgDll5@@@@@@@@@@; usid=Wy_BkzTOYtCkvvW7; SNUID=8F081C9DEBEEF347CE7CC0ADECF7B256; sct=23; wapsogou_qq_nickname=");
        // httpGet.addHeader("Host", "mp.weixin.qq.com");
        // httpGet.setHeader("Referer",
        // "http://weixin.sogou.com/gzh?openid=oIWsFtwp3kf5gF5wevv47pPr8RIE");
    }

    /**
     * 将流数据读取为byte[]数据
     */
    public final byte[] input2byte(InputStream inStream) throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[256];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 256)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] in2b = swapStream.toByteArray();
        return in2b;
    }

    private static final int bufferSize = 0x20000; // ~130K.

    private ByteBuffer readToByteBuffer(InputStream inStream, int maxSize)
            throws IOException {
        ByteBuffer byteData;
        Validate.isTrue(maxSize >= 0, "maxSize must be 0 (unlimited) or larger");
        final boolean capped = maxSize > 0;
        byte[] buffer = new byte[bufferSize];
        ByteArrayOutputStream outStream = null;
        try {
            outStream = new ByteArrayOutputStream(bufferSize);
            int read;
            int remaining = maxSize;
            while (true) {
                read = inStream.read(buffer);
                if (read == -1)
                    break;
                if (capped) {
                    if (read > remaining) {
                        outStream.write(buffer, 0, remaining);
                        break;
                    }
                    remaining -= read;
                }
                outStream.write(buffer, 0, read);
            }
            byteData = ByteBuffer.wrap(outStream.toByteArray());
            return byteData;
        } catch (Exception e) {
            logger.error(ExceptionUtils.getMessage(e));
        } finally {
            if (outStream != null) {
                outStream.close();
            }
        }
        return null;
    }

    private static final String[] CHARSETS = {"UTF-8", "GBK", "GB2312", "GB18030", "BIG5"};

    /**
     * 根据页面body获取字符编码
     *
     * @param html
     * @param charset
     * @return
     */
    public String getCharSetByBody(String html, String charset) {
        Document document = Jsoup.parse(html);
        Elements elements = document.select("meta");
        for (Element metaElement : elements) {
            if (metaElement != null
                    && StringUtils.isNotBlank(metaElement.attr("http-equiv"))
                    && metaElement.attr("http-equiv").toLowerCase()
                    .equals("content-type")) {
                String content = metaElement.attr("content");
                charset = getCharSet(content);
                break;
            }
            if (metaElement != null
                    && StringUtils.isNotBlank(metaElement.attr("charset"))) {
                charset = metaElement.attr("charset");
                break;
            }
        }
//		System.out.println("html charset："+charset);
        if (StringUtils.isNotBlank(charset)) {
            charset = charset.toUpperCase();
            for (String s : CHARSETS) {
                if (charset.indexOf(s) > -1) {
                    charset = s;
                    break;
                }
            }
            return charset;
        }
        return "UTF-8";
    }

    /**
     * 正则获取字符编码
     *
     * @param content
     * @return
     */
    private static String getCharSet(String content) {
        String regex = ".*charset=([^;]*).*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        if (matcher.find())
            return matcher.group(1);
        else
            return null;
    }

    /**
     * 得到网站的编码，我的
     */
    public static String getCharset(HttpResponse httpResponse) {
        String encoding = null;
        if (httpResponse != null) {
            HttpEntity entity = httpResponse.getEntity();
            encoding = getCharset(entity);
        }
        return encoding;
    }

    /**
     * 得到网站的编码，我的
     */
    public static String getCharset(HttpEntity entity) {
        String encoding = null;
        if (entity != null) {
            String contentType = entity.getContentType().toString();
            encoding = getCharset(contentType);
        }
        return encoding;
    }

    public static String getCharset(String contentType) {
        String encoding = null;
//		System.out.println("contentType:"+contentType);
        if (contentType.contains("charset")) {
            encoding = contentType.substring(contentType.lastIndexOf("=") + 1);
        }
//		System.out.println("HttpEntity charset："+encoding);
        return encoding;
    }

    // 得到网站的返回编号
    public static int getStatusCode(HttpResponse httpResponse) {
        if (httpResponse != null) {
            int StatusCode = httpResponse.getStatusLine().getStatusCode();
            return StatusCode;
        } else {
            return 0;
        }
    }

    /**
     * 根据搜索页修复提取到的不完整页面 String url="post-stocks-1215032-1.shtml"; String
     * searchUrl="http://bbs.tianya.cn/bbs/list-stocks-1.shtml";
     */
    public static String repairUrl(String url, String searchUrl) {
        String urlz = url;
        if (!url.contains("http")) {
            String domain = HtmlUtils.getDomain(searchUrl);
            if (url.startsWith("/")) {
                urlz = "http://" + domain + url;
            } else {
                urlz = getPrevUrl(searchUrl) + "/" + url;
            }
        } else {
            return urlz;
        }
        return urlz;
    }

    public static String getPrevUrl(String searchUrl) {
        String url = searchUrl.substring(0, searchUrl.lastIndexOf("/"));
        return url;
    }

    /**
     * 设置启用代理服务器 需要通过反爬时，在search方法中把HttpClient使用本方法设置代理
     */
    public static void main(String[] args) throws ClientProtocolException,
            IOException {
        HttpHelper httpHelper = new HttpHelper();
//        String url1 = "http://weixin.sogou.com/websearch/art.jsp?sg=CBf80b2xkga1-x-JOUbvmfJGWBRr3oe4r1AWURLuzjaY6zyCOWAw2vimkZrrG_tHjOKhvbU29Y52oFq6F8jRjZXieEuop7MLgRnvvNGhvUcJlYhvutf3ini9esapE8KSNMHWHow-EulC1ZpLwDhV3D0a7rmrVa5c&url=p0OVDH8R4SHyUySb8E88hkJm8GF_McJfBfynRTbN8wiw6XbQ_dH3k28rGEZHMAK1M-zqhvKlj3fZ6CCyUuxg1mQ3JxMQ3374XkTDDVnuGzRZgYB4tjQYmmRD01RPo_y7ojblXIV1SkZYy-5x5In7jJFmExjqCxhpkyjFvwP6PuGcQ64lGQ2ZDMuqxplQrsbk";
//        String url = "http://weixin.sogou.com/websearch/art.jsp?sg=CBf80b2xkgZfvufhmRzuMLkO81fqI_dQc6yOUe72-J_BlSRSRZWMnQm6TPEHznBEHCZbiXAAbSQ7Sk3TiznY_pxOlleD-hsA0C_ZHJHPImM9Gu65q1WuXA..&url=p0OVDH8R4SHyUySb8E88hkJm8GF_McJfBfynRTbN8whSbnodspRrBbmEeKJbEMVnXY6RLCSg2kSHyrrukhPPrmQ3JxMQ33748SsxTNb5mg5U9LVfWcAZq9TpmCl5RT9LEp1sHMFWE9RYy-5x5In7jJFmExjqCxhpkyjFvwP6PuGcQ64lGQ2ZDMuqxplQrsbk";
//        String url2 = "http://xueqiu.com/statuses/search.json?count=30&comment=0&symbol=SH600012&hl=0&source=trans&page=1&_=1430818569024";
//        String cookie = "ABTEST=8|1442285401|v1;domain=weixin.sogou.com;path=/;expires=Thu Oct 15 10:50:01 CST 2015;SNUID=2EAEBE3F4E48518D734A6ADE4EDE3AA9;domain=sogou.com;path=/;expires=Fri Sep 25 10:50:01 CST 2015;IPLOC=CN4301;domain=sogou.com;path=/;expires=Wed Sep 14 10:50:01 CST 2016;SUID=63E3F0716B20900A0000000055F78759;domain=weixin.sogou.com;path=/;expires=Mon Sep 10 10:50:01 CST 2035";
//        // String html = sendRequestCookie(url, "get", null,cookie);
//        // System.out.println(html);
//        String url3 = "http://weixin.sogoasdu.com/gzhjs?cb=sogou.weixin.gzasdhcb&openid=oIWsFt7AecfMHG2t1Eb6JIYYmP6g&eqs=kHsqoh5guuOWo5hMghaDnum0dZlLUAHfi9JCUGVEegvUx3JINBOcD7Puoyxvy9fSH6Yiq&ekv=7&page=1&t=1439949732268";
        String url4 = "http://idea.iteblog.com/key.php";
//        System.out.println(httpHelper.sendRequest(url4, "_ga=GA1.2.1316111355.1469069817; __utma=226521935.789957612.1469087638.1469087638.1469087638.1; __utmz=226521935.1469087638.1.1.utmccn=(referral)|utmcsr=sogou.com|utmcct=/sogou|utmcmd=referral; lzstat_uv=267233954925302956|365205; CNZZDATA4324368=cnzz_eid%3D583459682-1469770477-null%26ntime%3D1469770477"));
        System.out.println(httpHelper.sendRequest(url4));
////        HttpHost proxy = new HttpHost("219.141.225.108", 80);
    }

}
