/**
 * 
 */
package com.toroot.TextFetch;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.toroot.TextFetch.entity.SliceInfo;
import com.toroot.TextFetch.extract.NewsExtractor;
import com.toroot.TextFetch.extract.TextExtractor;

/**
 * @author longkailiang
 *
 */
public class NewsExtractorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
//			 String html =
//						 Jsoup.connect("http://finance.ifeng.com/a/20150817/13912939_0.shtml").get().outerHtml();
			String html = FileUtils.readFileToString(new File("F:/爬虫/test/www.xcf.cn/1.html"), "UTF-8");
			TextExtractor textExtractor = new NewsExtractor();
			long startMili = System.currentTimeMillis();
			List<SliceInfo> sliceList = textExtractor.extract(html);
			long time = System.currentTimeMillis() - startMili;
			for (SliceInfo sliceInfo : sliceList) {
				System.out.println(sliceInfo.getMode()+" :");
				System.out.println(sliceInfo.getContent());
			}
			System.out.println("总用时" + time + "ms");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
