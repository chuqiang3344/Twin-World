/**
 * 
 */
package com.toroot.TextFetch;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.toroot.TextFetch.entity.TextFetchResult;
import com.toroot.TextFetch.parse.news.NewsHtmlParser;

/**
 * @author longkailiang
 *
 */
public class NewsHtmlParserTest1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
//			 String html =
//					 Jsoup.connect("http://data.stcn.com/2015/0912/12438077.shtml").get().outerHtml();
			String html = FileUtils.readFileToString(new File("F:/爬虫/test/www.xcf.cn/3.html"), "UTF-8");
			NewsHtmlParser np = new NewsHtmlParser();
			long startMili = System.currentTimeMillis();
			TextFetchResult nr  = np.parse(html);
			long time = System.currentTimeMillis() - startMili;
			System.out.println(nr);
			System.out.println("总用时" + time + "ms");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
