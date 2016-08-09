package com.tyaer.spider.run;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tyaer.basic.net.helper.HttpHelper;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 测试整体xml中的模板
 * */
public class Article {

	static final String modelPath = Article.getModelFile("articleXpath.xml");
	static final String articleUrlList = Article
			.getModelFile("./configure/articleUrlList");
	static final HttpHelper httpHelper=new HttpHelper();

	public static void main(String[] args) {
		 String url =
		 "http://www.zhicheng.com/n/20160324/59036.html";
		 Article.articleXpathTest(url, modelPath);

//		Article.articleXpathTestForList(modelPath);

	}

	/**
	 * 检测单个Url的xpath
	 * 
	 * @param url
	 * @param modelPath
	 * @return
	 */
	public static boolean articleXpathTest(String url, String modelPath) {
		String html = null;
		html = httpHelper.sendRequest(url, "");
//		 System.out.println(html);
		if (html.equals("false")) {
			System.out.println("访问异常：" + url);
		} else {
			try {
				String model = FileUtils.readFileToString(new File(modelPath));
				Document doc = Jsoup.parse(model);
				Elements els = doc.select("ChannelModel");
				Document docHtml = Jsoup.parse(html);
				for (Element el : els) {
					String filedname = el.select("filedname").text();
					String filedxpath = el.select("filedxpath").text();
					String filedregex = el.select("filedregex").text();
					if (!filedxpath.equals("")) {
						Elements htmlEls = docHtml.select(filedxpath);
						if (!htmlEls.isEmpty()) {
							String result = "regex未识别！";
							String content = "";
							for (Element htmlel : htmlEls) {
								Pattern pattern = Pattern.compile(filedregex);
								String str = htmlel.toString();
//								str = str.replaceAll("\\n", "");
								Matcher matcher = pattern.matcher(str);
								if (matcher.find() && matcher.groupCount() >= 1) {
									result = matcher.group(1);
								}
								if (filedname.equals("content")) {
									content = content + result;
								}
							}
							if (filedname.equals("content")) {
								System.out.println(filedname + "：" + content);
							} else {
								System.out.println(filedname + "：" + result);
							}
						} else {
							System.out.println("当前文章的Xpath不匹配，请修正！	"
									+ filedname + "：" + filedxpath);
//							System.out.println(html);
							return false;
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	/**
	 * 检测url文件内所有的url
	 * 
	 * @param modelPath
	 * @return
	 */
	public static boolean articleXpathTestForList(String modelPath) throws IOException {
		boolean result = true;
		List<String> urlList =FileUtils.readLines(new File("./configure/articleUrlList"),"utf-8");
		System.out.println("==================当前种子页面总共有 " + urlList.size()
				+ " 篇==================");
		int i = 1;
		for (String url : urlList) {
			System.out.println("——————————— 第  " + i + " 篇：" + url);
			result = Article.articleXpathTest(url, modelPath);
			if (!result)
				break;
			i++;
		}
		return result;
	}

	public static String getModelFile(String modelname) {
		String root = System.getProperty("user.dir");
		// System.out.println(root);
		String path = root + "\\" + modelname;
		// System.out.println(path);
		return path;
	}

	/**
	 * 通过复制源代码到本地，来验证Xpath
	 * 
	 * @param modelPath
	 * @param textPath
	 * @return
	 */
	public static String getAllModelAndValue(String modelPath, String textPath) {
		try {
			String model = FileUtils.readFileToString(new File(modelPath));
			String html = FileUtils.readFileToString(new File(textPath));
			// System.out.println(html);
			Document doc = Jsoup.parse(model);
			Elements els = doc.select("ChannelModel");
			Document docHtml = Jsoup.parse(html);
			for (Element el : els) {
				String filedname = el.select("filedname").text();
				String filedxpath = el.select("filedxpath").text();
				String filedregex = el.select("filedregex").text();
				Elements htmlEls = docHtml.select(filedxpath);
				for (Element htmlel : htmlEls) {
					Pattern pattern = Pattern.compile(filedregex);
					String str = htmlel.toString();
					str = str.replaceAll("\\n", "");
					Matcher matcher = pattern.matcher(str);
					String result = "未识别";
					if (matcher.find() && matcher.groupCount() >= 1) {
						result = matcher.group(1);
					}
					System.out.println(filedname + "：" + result);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
