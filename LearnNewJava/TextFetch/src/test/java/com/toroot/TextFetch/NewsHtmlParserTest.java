/**
 * 
 */
package com.toroot.TextFetch;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.toroot.TextFetch.entity.TextFetchResult;
import com.toroot.TextFetch.parse.news.NewsHtmlParser;

/**
 * @author longkailiang
 *
 */
public class NewsHtmlParserTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File fr = new File(
				"F:/爬虫/test/stock.sohu.com");
		if (fr != null && fr.exists()) {
			File[] files = fr.listFiles();
			for (File f : files) {
				if (f.isFile()&&f.getName().endsWith("html")) {
					try {
						System.out.println("处理文件:" + f.getName());
						String html = FileUtils.readFileToString(f, "UTF-8");
						NewsHtmlParser np = new NewsHtmlParser();
						long startTime = System.currentTimeMillis();
						TextFetchResult nr =null;
						try {
							 nr=np.parse(html);
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println(f.getName() + "此源码可能存在问题");
							continue;
						}
						long endTime = System.currentTimeMillis();
						System.out.println("消耗了:" + (endTime - startTime)
								+ "毫秒");

						String path = "F:/爬虫/test/stock.sohu.com/result/"
								+ f.getName() + ".txt";
						FileUtils.write(new File(path), nr.toString(),
								"UTF-8");

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
