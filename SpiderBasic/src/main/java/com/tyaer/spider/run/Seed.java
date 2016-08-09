package com.tyaer.spider.run;

import com.tyaer.basic.net.helper.HttpHelper;
import com.tyaer.basic.net.httptools.HttpClientManager;
import com.tyaer.basic.utils.HtmlUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.ProxyClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 测试模板
 * 
 * @author twin
 * */
public class Seed {

	protected Logger logger = Logger.getLogger(HttpHelper.class);
	static final HttpHelper httpHelper=new HttpHelper();
	public static void main(String[] args) throws ClientProtocolException,
			IOException {
		String xpath = "div#main a";

		String searchUrl = "http://www.cnblogs.com/xdp-gacl/";
		
		String regex = "<a[\\s\\S]*?href[\\s]?=\\s*[\"\"'](?<href>[^>\"\"'].*?)[\"\"']\\s*[^>]*[/]?>([\\s\\S]*?)<\\s*/a\\s*>";
//		String regex = "<a[\\s\\S]*?href[\\s]?=\\s*[\"'](.*?)[\"']\\s*[/]?>[\\s\\S]*?h4>(.*?)</h4>";
		String html = httpHelper.sendRequest(searchUrl, "");
//		System.out.println(html);
		Document doc = Jsoup.parse(html);
		Elements els = doc.select(xpath);
		System.out.println("总共："+els.size());
		if (els.size() > 0) {
			for (Element el : els) {
				// System.out.println(el.toString());
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(el.toString());
				if (matcher.find() && matcher.groupCount() >= 2) {
					String url = matcher.group(1);
					String title = matcher.group(2);
					url = HtmlUtils.repairUrl(url, searchUrl);
					System.out.println(title + " ： " + url);
				}
			}
		}else{
			System.out.println(html);
			System.out.println("模板提取不到内容页连接:" + xpath);
			System.out.println(els.size());
		}
	}



	public static Map<String, String> seedXpathTest(String searchUrl,
			String xpath) {
		Map<String, String> seedMap = new HashMap<String, String>();

		// String regex =
		// "<a[\\s\\S]*?href[\\s]?=\\s*[\"\"'](?<href>[^>\"\"'].*?)[\"\"']\\s*[^>]*[/]?>([\\s\\S]*?)<";
		String regex = "<a[\\s\\S]*?href[\\s]?=\\s*[\"\"'](?<href>[^>\"\'].*?)[\"\"']\\s*[^>]*[/]?>([\\s\\S]*?)<\\s*/a\\s*>";

		// CharsetDecoder de=Charset.forName("utf-8").newDecoder();
		String html = null;

		html = httpHelper.sendRequest(searchUrl, "");
		// System.out.println(html);
		// html=de.decode(html.getBytes());

		Document doc = Jsoup.parse(html);
		Elements els = doc.select(xpath);
		// System.out.println(els.toString());
		if (!els.isEmpty()) {
			for (Element el : els) {
				// System.out.println(el.toString());
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(el.toString());
				if (matcher.find() && matcher.groupCount() >= 2) {
					String url = matcher.group(1);
					String title = matcher.group(2);
					url = HtmlUtils.repairUrl(url, searchUrl);
					// System.out.println(title + "：" + url);
					seedMap.put(title, url);
				} else {
					System.out.println("regex错误！");
				}
			}
		} else {
			System.out.println("当前种子页面的Xpath不匹配，请核实！	" + xpath);
			System.out.println(html);
			return null;
		}
		return seedMap;
	}

}
