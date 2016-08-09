package com.tyaer.spider.run;

import java.util.Map;

public class XpathParseRun {

	public static void main(String[] args) {
		// 1、频道链接
		String searchUrl = "http://www.cb.com.cn/economy/";

		// 2、种子页面的Xpath
		String seedXpath = "div.container h3>a";

		System.out.println("当前种子页面: " + searchUrl);
		// 一、匹配种子页面的Xpath
		Map<String, String> seedMap = Seed.seedXpathTest(searchUrl, seedXpath);
		if (seedMap != null) {
			System.out.println("==================当前种子页面总共有 " + seedMap.size()
					+ " 篇：" + searchUrl);
			int i = 1;
			for (Map.Entry<String, String> entry : seedMap.entrySet()) {
				System.out.println("——————————— 第  " + i + " 篇："
						+ entry.getKey() + " = " + entry.getValue());
				// System.out.println(entry.getKey() + " = " +
				// entry.getValue());
				String url = entry.getValue();
				// 3、内容页面的xpath路径，一套模版
				String modelPath = Article.getModelFile("articleXpath.xml");

				// 二、匹配内容页面的Xpath
				boolean isContinue = Article.articleXpathTest(url, modelPath);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				i++;
				if (!isContinue) {
					break;
				}
			}
		}
	}
}
