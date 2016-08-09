/**
 * 
 */
package com.toroot.TextFetch.parse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.toroot.TextFetch.entity.SliceInfo;
import com.toroot.TextFetch.entity.TextFetchResult;
import com.toroot.TextFetch.utils.DateUtils;
import com.toroot.TextFetch.utils.HtmlUtils;

/**
 * 获取Html正文接口
 * @author longkailiang
 *
 */
public abstract class HtmlParser {
	
	private static Pattern sourcePatt = Pattern
			.compile(".*?[来源|来自]\\s?[:：]\\s?(\\S*)\\s{0,1}");
	
    private static Pattern keywordPatt = Pattern
			.compile(".*?[关键字|标签|Tags|关键词]\\s?[:：]\\s?((\\S*\\s*){1,3})\\s");
		
    private static Pattern authorPatt = Pattern
			.compile(".*?作\\s?者\\s?[:：]\\s?(\\S*)\\s{0,1}");
    
	protected static String filterWords[] = new String[] { 
		"免责声明",
		"扫描二维码",
		"我的收藏",
		"凡本站及其子站",
		"版权所有",
		"意见反馈留言板",
		"本站郑重声明",
		"有关合作事宜",
		"郑重声明",
		"独家稿件声明",
		"声明：",
		"本网站內容",
		"投稿邮箱"
	};
	
    /**
     * Html解析接口
     * @param html
     * @return
     */
    public abstract TextFetchResult parse(String html);
    
    protected class SliceIndex {
    	public SliceInfo slice;
    	
    	public int index;

		public SliceIndex(SliceInfo slice, int index) {
			super();
			this.slice = slice;
			this.index = index;
		}  	
    }
    
    /**
     * 在begin~end范围内寻找最可能的正文文本块
     * @param sliceList
     * @param begin
     * @param end 
     * @return
     */
    protected int findMostLikelyContent(List<SliceIndex> sliceList, int begin, int end) { 	
    	int i = begin, j = end;
    	if(end == sliceList.size())
    		j--;  
    	SliceIndex sliceIndex;
//		String temp = "";
		int index=0;
		List<SliceRankInfo> rankList = new ArrayList<SliceRankInfo>();
		SliceRankInfo rankInfo;
		while (i <= j) {
			sliceIndex = sliceList.get(i);
			rankInfo = new SliceRankInfo(sliceIndex, i, 
					HtmlUtils.getChineseCharCount(sliceIndex.slice.getContent()));
			rankList.add(rankInfo);
			i++;
		}
		
		Collections.sort(rankList, new Comparator<SliceRankInfo>() {
			public int compare(SliceRankInfo o1, SliceRankInfo o2) {
				return o2.charNum-o1.charNum;
			}
		});
		
		i = 0;
		j = rankList.size()>3?3:rankList.size();
		while (i<j) {
			rankInfo = rankList.get(i);
			if(!isEmbodyFilterWords(rankInfo.sliceIndex.slice.getContent())){
				break;
			}
			i++;
		}
		
		if(i==j) i=0;
		
		index = rankList.get(i).index;
		
//    	while( i < end ){
//    		slice = sliceList.get(i);
//			if (HtmlUtils.getChineseCharCount(slice.getContent()) 
//					> HtmlUtils.getChineseCharCount(temp)) {
//				temp = slice.getContent();
//				index = i;
//			}
//    		i++;
//    	}

		return index;
	}
    
	private boolean isEmbodyFilterWords(String content){
		int index;
		for (String regex : filterWords) {
			index = content.indexOf(regex);
			if(index!=-1&&index<100)
				return true;
		}
		return false;
	}
    
    private class SliceRankInfo {
    	
    	public SliceIndex sliceIndex;
    	
    	public int index;
    	
    	public int charNum;

		public SliceRankInfo(SliceIndex sliceIndex, int index, int charNum) {
			super();
			this.sliceIndex = sliceIndex;
			this.index = index;
			this.charNum = charNum;
		}
    	  	
    }
    
	/**
	 * 在begin~end范围内寻找最靠近end的发布时间
	 * @param sliceList
	 * @param start
	 * @param end
	 * @return
	 */
    protected String getPublishTime(List<SliceInfo> sliceList, int start, int end) {
		int count = sliceList.size();
		if (start <= 0 ) {
			start = 0;
		}
		if (end >= count) {
			end = count - 1;
		}
		String text;
		for (int i = end; i >= start; i--) {
			text = sliceList.get(i).getContent();
			DateUtils du = new DateUtils();
			String time = du.fetchDateTime(text);
			if (StringUtils.isNotBlank(time)) {
				return time;
			}
		}
		return null;
	}
	
    /**
     * 获取来源
     * @param sliceList
     * @param start
     * @param end
     * @return
     */
    protected String getSource(String text) {
    	Matcher math = sourcePatt.matcher(text);
		if (math.find() && math.groupCount() >=1) {
			String source = math.group(1);
			if (StringUtils.isNotBlank(source)) {
				return source;
			}
		}
        return null;
	}
    
	/**
	 * 获取关键字
	 * @param text
	 * @param isPre
	 * @return
	 */
    protected String getKeyword(String text) {
		Matcher math = keywordPatt.matcher(text);
		if (math.find() && math.groupCount() > 1) {
			String keyword;
			keyword = math.group(1);
			if (StringUtils.isNotBlank(keyword)) {
				return keyword;
			}
		}
		return null;
	}
	
	/**
	 * 获取作者
	 * @param text
	 * @param isPre
	 * @return
	 */
    protected String getAuthor(String text) {		
		Matcher math = authorPatt.matcher(text);
		if (math.find() && math.groupCount() >=1) {
			String source;
			source = math.group(1);				
			if (StringUtils.isNotBlank(source)) {
				return source;
			}
		}
		return null;
	}
}
