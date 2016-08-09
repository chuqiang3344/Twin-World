/**
 * 
 */
package com.toroot.TextFetch;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.io.FileUtils;

import com.toroot.TextFetch.entity.TextFetchResult;
import com.toroot.TextFetch.parse.news.NewsHtmlParser;

/**
 * @author longkailiang
 *
 */
public class AllDataTest {
	
	public static void main(String[] args) throws IOException {
		File fr = new File(
				"F:/爬虫/test");
		File errorFile =  new File("F:/爬虫/error.txt");
        if (!errorFile.exists()) {
        	errorFile.createNewFile();
        }
		PrintWriter writer = new PrintWriter(errorFile,"UTF-8");
		int num = 0;
		long totalTime = 0;
		if (fr != null && fr.exists()) {
			File[] dirs = fr.listFiles();
			for (File d : dirs) {
				if (d.isDirectory()) {
					System.out.println("------处理网站"+d.getName()+"---------");
					File[] files = d.listFiles();
					for (File f : files) {
						if (f.isFile()&&f.getName().endsWith("html")){
							System.out.println("处理网页"+d.getName()+"/"+f.getName()+":");
							try {
								String html = FileUtils.readFileToString(f, "UTF-8");
								NewsHtmlParser np = new NewsHtmlParser();
								long startTime = System.currentTimeMillis();
								TextFetchResult nr =null;
								try {
									 nr=np.parse(html);
								} catch (Exception e) {
									e.printStackTrace();
									System.out.println(d.getName()+"/"+f.getName() + "此源码可能存在问题");
									writer.println(d.getName()+"/"+f.getName());
									continue;
								}
								num++;
								long endTime = System.currentTimeMillis();
								long useTime = endTime - startTime;
								totalTime += useTime;
								System.out.println("消耗了:" + useTime
										+ "毫秒");
								String path = "F:/爬虫/test_result/"+d.getName()+"/"
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
		System.out.println("平均耗时:" + totalTime/num
				+ "毫秒");
		writer.close();
	}
}
