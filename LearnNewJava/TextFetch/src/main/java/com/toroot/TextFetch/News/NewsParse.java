package com.toroot.TextFetch.News;

import com.toroot.TextFetch.TextExtractor;
import com.toroot.TextFetch.utils.DateUtils;
import com.toroot.TextFetch.utils.HtmlUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 新闻提取
 * @author mg
 * */
public class NewsParse {

		private static Pattern sourcePatt = Pattern
			.compile(".*?[来源|来自]\\s?[:：]\\s?(\\S*)\\s{0,1}");
	
		private static Pattern keywordPatt = Pattern
			.compile(".*?[关键字|标签|Tags]\\s?[:：]\\s?((\\S*\\s*){1,3})\\s");
		
		private static Pattern authorPatt = Pattern
				.compile(".*?作\\s?者\\s?[:：]\\s?(\\S*)\\s{0,1}");
		
		private static String stockCodes[] = new String[] { 
			"支持单位",
			"版权所有",
			"关于我们",
			"上一篇",
			"下一篇",
//			"Tags",
			"延伸阅读",
			"免责声明",
			"官方推荐",
			"相关公告",
			"缩小文字",
			"放大文字",
			"相关链接",
			"相关阅读",
			"联系我们",
			"责任编辑",
			"用微信扫描二维码",
//			"标签",
//			"关键字",
			"下载文件",
//			"作者",
			"All Rights Reserved",
			"Copyright",
			"凡本站及其子站",
			"我的收藏",
//			"来源"
		};
		
		/**
		 * 获取来源
		 * **/
		private String getSource(List<String> list, int start, int end) {
			int count = list.size();
			if (start <= 0 || start - 10 < 0) {
				start = 0;
			}
			if (end >= count) {
				end = count - 1;
			}
			for (int i = start; i <= end; i++) {
				String text = list.get(i);
				if(!text.contains("来源"))
				{
					continue;
				}
				Matcher math = sourcePatt.matcher(text);
				if (math.find() && math.groupCount() >=1) {
					String source = math.group(1);
					if (StringUtils.isNotBlank(source)) {
						return source;
					}
				}
			}
			return null;
		}
	
		/**
		 * 获取发布时间
		 * */
		private String getPublishTime(List<String> list, int start, int end) {
			int count = list.size();
			if (start <= 0 ) {
				start = 0;
			}
			if (end >= count) {
				end = count - 1;
			}
			for (int i = start; i <= end; i++) {
				String text = list.get(i);
				DateUtils du = new DateUtils();
				String time = du.fetchDateTime(text);
				if (StringUtils.isNotBlank(time)) {
					return time;
				}
			}
			return null;
		}
	
		/**
		 * 获取作者
		 * */
		private String getAuthor(List<String> list, int start, int end) {
			int count = list.size();
			if (start <= 0 || start - 10 < 0) {
				start = 0;
			}
			if (end >= count) {
				end = count - 1;
			}
			for (int i = start; i <= end; i++) {
				String text = list.get(i);
				if(!text.contains("作者"))
				{
					continue;
				}
				Matcher math = authorPatt.matcher(text);
				if (math.find() && math.groupCount() >=1) {
					String source = math.group(1);
					if (StringUtils.isNotBlank(source)) {
						return source;
					}
				}
			}
			return null;
		}
	
		/**
		 * 获取关键字
		 * */
		private String getKeyword(List<String> list, int start, int end) {
			
			int count = list.size();
			if (start <= 0 || start - 10 < 0) {
				start = 0;
			}
			if (end >= count) {
				end = count - 1;
			}
			for (int i = start; i <= end; i++) {
				String text = list.get(i);
				Matcher math = keywordPatt.matcher(text);
				if (math.find() && math.groupCount() > 1) {
					String keyword = math.group(1);
					if (StringUtils.isNotBlank(keyword)) {
						return keyword;
					}
				}
			}
			return null;
		}
		// 字符限定数，当分析的文本数量达到限定数则认为进入正文内容
		// 默认8个字符数
		private static int limitCount = 4;
		// 确定文章正文头部时，向上查找，连续的空行到达 ，则停止查找
		private static int headEmptyLines = 7;
		/**
		 * 核心聚合文章块
		 *  
		 * */
		public List<String> core(String[] blocks) {
			int size = blocks.length;
			if(size>=70)
			{
				size=size/6*5;
			}
			int start = 0;
			List<String> textList = new ArrayList<String>();
			for (int i = 0; i < size; i++) {
				String key = blocks[i];
//				System.out.println(key);
				int charCount=HtmlUtils.getChineseCharCount(key);
				if (charCount >= limitCount) {
					start = i;
					int end = i;
					StringBuffer buffer = new StringBuffer();
					int emptyCount = 1;
					for (; end < size; end++) {
						String temp = blocks[end];
						boolean isboolean=false;
						// 去掉不合法的文章段落
						for (String regex : stockCodes) {
							if (temp.indexOf(regex) != -1) {
//								emptyCount++;
								textList.add("\n");
								textList.add("\n");
								textList.add("\n");
								textList.add("\n");
								textList.add("\n");
								isboolean=true;
								break;
							}
						}
						if(isboolean)
						{
							continue;
						}
						if (StringUtils.isNotBlank(temp)) {
							buffer.append(temp + " \n");
							emptyCount =0;
						} else {
							emptyCount++;
						}
						if (emptyCount >= headEmptyLines || end==size-1) {
							textList.add(buffer.toString());
							i = end;
							break;
						}
					}
				}
				else
				{
					textList.add(key+" ");
				}

			}
			return textList;
		}
		
		
		private String findLikelyMostContent(List<String> list) {
			if(list.size()==0|| list==null)
			{
				return null;
			}
			String temp = "";
			int z = 0;
			int index=0;
			for (String content : list) {
				if (HtmlUtils.getChineseCharCount(content) > HtmlUtils.getChineseCharCount(temp)) {
					temp = content;
					index=z;
				}
				z++;
			}
			return list.get(index);
		}
		
		public NewsResut parse(String html)
		{
			TextExtractor textExtractor = new TextExtractor();
			textExtractor.addRule("div", 4);
			textExtractor.addRule("a", 0);
			textExtractor.addRule("tr", 4);
			textExtractor.addRule("h1", 8);
			textExtractor.addRule("br", 4);
			textExtractor.addRule("span", 2);
			textExtractor.addRule("td", 0);
			textExtractor.addRule("tbody", 0);
			textExtractor.addRule("table", 1);
			textExtractor.addRule("strong", 0);
			long startMili = System.currentTimeMillis();
			String ret = textExtractor.extract(html);
			ret=HtmlUtils.clearText(ret);
			String []blocks=ret.split("\n");
//			for(String str:blocks)
//			{
//				System.out.println(str.length()+"------"+str);
//				System.out.println("=============================");
//			}
			List<String> reust=null;
			reust=core(blocks);
			long time = System.currentTimeMillis()-startMili;
			int len=reust.size();
			String content=findLikelyMostContent(reust);
			String publishtime= getPublishTime(reust, 0, len-1);
			String source=getSource(reust, 0, len-1);
			String author=getAuthor(reust, 0, len-1);
			String keyword=getKeyword(reust, 0, len-1);
//			for(String str:reust)
//			{
//				System.out.println(str.length()+"----"+str);
//				System.out.println("=============================");
//			}
			return new NewsResut(publishtime, content, source, author,keyword);
		}
		
		public static void main(String[] args) {
			String searchUrl = "/home/escore/test/算法测试/test/industry.caijing.com.cn/129"+ ".html";
			try {
				String html = FileUtils.readFileToString(new File(searchUrl),
						"UTF-8");
				NewsParse np=new NewsParse();
//				Document doc=Jsoup.connect("http://stock.cnstock.com/stock/smk_gszbs/201507/3510424.htm").get();
//				doc.select("p~div").remove();
//				html=doc.html();
				long startTime=System.currentTimeMillis();
				NewsResut nr = np.parse(html);
				System.out.println(nr.toString());
				long endTime=System.currentTimeMillis();
				System.out.println("消耗了:"+(endTime-startTime)+"毫秒");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
}
