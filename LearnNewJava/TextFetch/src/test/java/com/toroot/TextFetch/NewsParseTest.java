/**
 * 
 */
package com.toroot.TextFetch;

import java.io.File;

import org.apache.commons.io.FileUtils;

import com.toroot.TextFetch.News.NewsParse;
import com.toroot.TextFetch.News.NewsResut;

/**
 * @author John
 *
 */
public class NewsParseTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String html = FileUtils.readFileToString(new File("F:/爬虫/test/stock.sohu.com/5.html"), "UTF-8");
			NewsParse np = new NewsParse();
			long startTime = System.currentTimeMillis();
			NewsResut nr = np.parse(html);
			long endTime = System.currentTimeMillis();
			System.out.println("消耗了:" + (endTime - startTime)
					+ "毫秒");
			System.out.println(nr.toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

}
