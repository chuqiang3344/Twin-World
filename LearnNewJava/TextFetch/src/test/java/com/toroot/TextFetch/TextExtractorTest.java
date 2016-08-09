/**
 * 
 */
package com.toroot.TextFetch;

import java.io.File;

import org.apache.commons.io.FileUtils;

/**
 * @author longkailiang
 *
 */
public class TextExtractorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String html = FileUtils.readFileToString(new File("F:/爬虫/test/business.sohu.com/17.html"), "UTF-8");
			TextExtractor textExtractor = new TextExtractor();
			textExtractor.addRule("div", 2);
			textExtractor.addRule("br", 4);
			long startMili = System.currentTimeMillis();
			String str = textExtractor.extract(html);
			long time = System.currentTimeMillis() - startMili;
			System.out.println(str);
			System.out.println("总用时" + time + "ms");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
