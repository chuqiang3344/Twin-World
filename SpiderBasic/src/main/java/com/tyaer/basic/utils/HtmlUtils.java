package com.tyaer.basic.utils;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * html帮助类
 * 
 * @author mg
 * */
public class HtmlUtils {
	private static String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
																											// }
	private static String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
																											// }
	private static String regEx_html = "<!--.*?-->"; // 定义HTML标签的正则表达式
	private static Pattern p_script = Pattern.compile(regEx_script,
			Pattern.CASE_INSENSITIVE);
	private static Pattern p_style = Pattern.compile(regEx_style,
			Pattern.CASE_INSENSITIVE);
	private static Pattern p_html = Pattern.compile(regEx_html,
			Pattern.CASE_INSENSITIVE);

	private static String regex_url = "http://.+?";
	private static Pattern p_url = Pattern.compile(regex_url,
			Pattern.CASE_INSENSITIVE);

	/**
	 * 这个函数是用来对输入字符的HTML代码进行过滤
	 * @param inputString
	 *            : string's byte width
	 * @return String Object
	 */
	public static String htmlFilterToText(String inputString) {
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		try {
//			htmlStr = htmlStr.replaceAll("\n", "");
		    Matcher m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); //过滤script标签

			Matcher m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			Matcher m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤style标签

			return htmlStr;
		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}
		return textStr;// 返回文本字符串
	}

	/**
	 * 根据正则活取文本中的信息
	 * */
	public static String getStringByRegex(String html, String regex, int index) {
		html = html.replaceAll("\n", "");//如果不去掉换行符，用正则表达式就提取不到
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(html);
		String str = null;
		if (matcher.find()) {
			if (index >= 0) {
				str = matcher.group(index);
			} else {
				matcher.group();
			}
		}
		if (StringUtils.isNotBlank(str)) {
			str = str.replaceAll("<.*?>", "");
		}
		return str;
	}

	public static Map<String, String> getMapInfoByJsonStr(String json) {
		JSONObject indexCrawl = (JSONObject) JSONObject.stringToValue(json);
		HashMap<String, String> map = new HashMap<String, String>();
		for (String key : (Set<String>) (indexCrawl.keySet())) {
			map.put(key, indexCrawl.getString(key));
		}
		return map;
	}

	/**
	 * 过滤一些垃圾编码
	 * 
	 * @author mg
	 * */
	public static String stringFilter(String str) {
		if (StringUtils.isBlank(str)) {
			return str;
		}
		str = str.replaceAll("\\n", "\r\n");
		str = str.replaceAll("</p>|<br>|<br/>", "\r\n");
		str = str.replaceAll("<.*?>", "");
		str = str.replaceAll("&nbsp;|&nbsp", " ");
		str = str.replaceAll("&amp;|&amp", "&");
		return str;
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
	 * 根据页面body获取字符编码
	 * 
	 * @param html
	 * @param charset
	 * @return
	 */
	public static String getCharSetByBody(String html, String charset) {
		Document document = Jsoup.parse(html);
		Elements elements = document.select("meta");
		for (int i = 0; i < elements.size(); i++) {
			Element metaElement = elements.get(i);
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
		if (StringUtils.isNotBlank(charset)) {
			return charset.toUpperCase();
		}
		return "UTF-8";
	}

	/**
	 * 将流数据读取为byte[]数据
	 * */
	public static final byte[] input2byte(InputStream inStream)
			throws IOException {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int rc = 0;
		while ((rc = inStream.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, rc);
		}
		byte[] in2b = swapStream.toByteArray();
		return in2b;
	}

	/**
	 * 根据搜索页修复提取到的不完整页面 String url="post-stocks-1215032-1.shtml"; String
	 * searchUrl="http://bbs.tianya.cn/bbs/list-stocks-1.shtml";
	 * */
	public static String repairUrl(String url, String searchUrl) {
		String urlz = url;
		if (url.contains("http")) {
			return urlz;
		} else {
			String domain = getDomain(searchUrl);
			if (url.startsWith("/")) {
				urlz = "http://" + domain + url;
			} else {
				urlz = getPrevUrl(searchUrl) + "/" + url;
			}
		}
		return urlz;
	}

	public static String getDomain(String url) {
		url = url.trim().toLowerCase();
		int i = url.indexOf("://");
		if (i > 0)
			url = url.substring(i + 3);
		int p = url.indexOf('/');
		if (p > 0)
			url = url.substring(0, p);
		int n = url.indexOf(':');
		if (n > 0)
			url = url.substring(0, n);
		return url;
	}

	public static String getDomainNotHttp(String url) {
		url = url.trim().toLowerCase();
		int i = url.indexOf("//");
		if (i > 0)
			url = url.substring(i + 2);
		int p = url.indexOf('/');
		if (p > 0)
			url = url.substring(0, p);
		return url;
	}

	public static String getPrevUrl(String searchUrl) {
		String url = searchUrl.substring(0, searchUrl.lastIndexOf("/"));
		return url;
	}

	/**
	 * 验证url是否正确
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isUrlLegitimate(String url) {
		boolean flag = true;
		Matcher m_url = p_url.matcher(url);
		if (!m_url.matches()) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 判断str长度是否超过length
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static boolean isUrlLength(String str, Integer length) {
		boolean flag = false;
		if (str.length() > length) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 判断是否是中文字符
	 * 
	 * @param codePoint
	 * @return
	 */
	protected static boolean isChineseChar(int codePoint) {
		Character.UnicodeScript sc = Character.UnicodeScript.of(codePoint);
		if (sc == Character.UnicodeScript.HAN) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否有乱码
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isGarbled(String value) {
		int count = value.codePointCount(0, value.length());
		int len = 0;
		for (int i = 0; i < count; i++) {
			int codePoint = value.codePointAt(i);
			if (!isChineseChar(codePoint)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取中文字符长度
	 * 
	 * @param value
	 * @return
	 */
	public static int getChineseCharCount(String value) {
		int count = value.codePointCount(0, value.length());
		int len = 0;
		for (int i = 0; i < count; i++) {
			int codePoint = value.codePointAt(i);
			if (isChineseChar(codePoint)) {
				len++;
			}
		}
		return len;
	}

	private static final Pattern pattern=Pattern.compile(".pdf|.mp3|.avi|.wma",Pattern.CASE_INSENSITIVE);
	/**
	 * 过滤非新闻页面链接
	 * @param url
	 */
	public static boolean judgeIsNewUrl(String url){
		//去重非新闻页面链接，不区分大小写
		Matcher m=pattern.matcher(url);
		if(m.find()){
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		HtmlUtils util = new HtmlUtils();
		String url = "post-stocks-1215032-1.shtml";
		String url1 = "/post-stocks-1215032-1.shtml";
		String searchUrl = "http://bbs.tianya.cn/bbs/list-stocks-1.shtml";
		// String result=util.repairUrl(url, searchUrl);
		// System.out.println(result);
		// String result1=util.repairUrl(url1, searchUrl);
		// System.out.println(result1);
		System.out.println(util.repairUrl(url1, searchUrl));
		System.out.println(getDomainNotHttp("http://kuaixun.eastmoney.com/"));
	}

}
