/**
 * 
 */
package com.toroot.TextFetch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.toroot.TextFetch.clean.TextCleaner;
import com.toroot.TextFetch.entity.SliceInfo;
import com.toroot.TextFetch.filter.TextSplitter;
import com.toroot.TextFetch.repair.HtmlRepair;

/**
 * html文本抽取
 * 
 * @author longkailiang
 *
 */
public class TextExtractor {

	private String seperation = "\n";

	private TextSplitter textSplitter = new TextSplitter();

	private TextCleaner textCleaner = new TextCleaner();
	
	private HtmlRepair htmlRepair=new HtmlRepair();

	private Map<String, Integer> rulesMap = new HashMap<String, Integer>();

	private Map<Integer, String> seperationMap = new HashMap<Integer, String>();

	public TextExtractor() {
		seperationMap.put(null, seperation);
	}

	public void setSeperation(String seperation) {
		this.seperation = seperation;
	}

	public void customizeTagFiter(String tags) {
		textSplitter.customizeTagFiter(tags);
	}

	public void addTagFiter(String tag) {
		textSplitter.addTagFiter(tag);
	}

	public void addTagCleaner(String tag) {
		textCleaner.addTagCleaner(tag);
	}

	public void customizeTagcleaner(String tags) {
		textCleaner.customizeTagcleaner(tags);
	}

	public void addRule(String tag, int num) {
		rulesMap.put(tag, num);
	}

	public void addRules(String... rules) {
		if (rules != null && rules.length > 0) {
			String[] rule;
			for (String str : rules) {
				rule = str.split(":");
				if (rule.length == 2)
					rulesMap.put(rule[0], Integer.parseInt(rule[1]));
			}
		}
	}

	private String getSeperation(String tag) {
		Integer num = rulesMap.get(tag);
		String sep = seperationMap.get(num);
		if (sep == null) {
			StringBuffer res = new StringBuffer();
			int i = 0;
			while (i < num) {
				res.append(seperation);
				i++;
			}
			sep = res.toString();
			seperationMap.put(num, sep);
		}
		return sep;
	}

	public String extract(String html) {
		StringBuffer ret = new StringBuffer();
		long startMili = System.currentTimeMillis();
		List<SliceInfo> sliceList = textSplitter.split(html);
		System.out.println("切割过滤用时" + (System.currentTimeMillis() - startMili)
				+ "ms");
		startMili = System.currentTimeMillis();
		sliceList=htmlRepair.repair(sliceList);
		System.out.println("修复用时" + (System.currentTimeMillis() - startMili)
				+ "ms");
		startMili = System.currentTimeMillis();
		List<SliceInfo> cleanedList = textCleaner.cleanTag(sliceList);
		System.out.println("清理用时" + (System.currentTimeMillis() - startMili)
				+ "ms");
		Stack<String> tagStack = new Stack<String>();
		int tagClass, len = cleanedList.size(), i = 0;
		SliceInfo slice;
		String tag;
		while (i < len) {
			slice = cleanedList.get(i);
			tagClass = classTag(slice.getTag());
			if (tagClass == 1) {
				if (slice.getContent().endsWith("/>")) {
					ret.append("\n");
				} else {
					tagStack.push(slice.getTag());
//					ret.append("\n");
				}
			} else if (tagClass == 2) {
				if (!tagStack.isEmpty()) {
					tag = tagStack.pop();
					if (!slice.getTag().endsWith(tag)) {
						continue;
					}
					ret.append(getSeperation(tag));
				}
			} else {
				ret.append(slice + " ");
			}
			i++;
		}
		return ret.toString();
	}

	private int classTag(String tag) {
		if (tag != null) {
			if (tag.startsWith("/") )
				return 2;
			else
				return 1;
		}
		return 0;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// String html =
			// Jsoup.connect("http://finance.ifeng.com/a/20150817/13912939_0.shtml").get().outerHtml();
			String html = "<html><body><a style='display:none;'>111111</a>2222</bory></html>";
			TextExtractor textExtractor = new TextExtractor();
			textExtractor.addRule("div", 2);
			textExtractor.addRule("br", 4);
			long startMili = System.currentTimeMillis();
			String str = textExtractor.extract(html);
			long time = System.currentTimeMillis() - startMili;
			// for(String str:ret)
			// {
			System.out.println(str);
			// System.out.println("******************");
			// }
			System.out.println("总用时" + time + "ms");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}