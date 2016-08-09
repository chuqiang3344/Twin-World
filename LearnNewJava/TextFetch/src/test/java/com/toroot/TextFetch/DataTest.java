package com.toroot.TextFetch;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.toroot.TextFetch.News.NewsParse;
import com.toroot.TextFetch.News.NewsResut;

/**
 * @author mg 批量数据测试
 * */
public class DataTest {

	public static void main(String[] args) {
		File fr = new File(
				"F:/爬虫/test/company.cnstock.com");
		if (fr != null && fr.exists()) {
			File[] files = fr.listFiles();
			for (File f : files) {
				if (f.isFile()) {
					try {
						System.out.println("处理文件:" + f.getName());
						String html = FileUtils.readFileToString(f, "UTF-8");
						NewsParse np = new NewsParse();
						long startTime = System.currentTimeMillis();
						NewsResut nr =null;
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

						String path = "F:/爬虫/test/company.cnstock.com/result1/"
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
