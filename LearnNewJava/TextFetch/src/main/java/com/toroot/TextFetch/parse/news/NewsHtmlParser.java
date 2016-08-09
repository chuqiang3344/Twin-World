/**
 * 
 */
package com.toroot.TextFetch.parse.news;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.toroot.TextFetch.entity.SliceInfo;
import com.toroot.TextFetch.entity.TextFetchResult;
import com.toroot.TextFetch.extract.NewsExtractor;
import com.toroot.TextFetch.extract.TextExtractor;
import com.toroot.TextFetch.parse.HtmlParser;
import com.toroot.TextFetch.utils.TagUtils;

/**
 * 新闻类网页解析器
 * @author longkailiang
 *
 */
public class NewsHtmlParser extends HtmlParser {

	private static Set<String> filterTagSet = new HashSet<String>(Arrays.asList("a","li"));
	
	/* (non-Javadoc)
	 * @see com.toroot.TextFetch.parse.HtmlParser#parse(java.lang.String)
	 */
	@Override
	public TextFetchResult parse(String html) {
		String content = null, publishtime = null, source = null, author = null, keyword = null;
		if(StringUtils.isBlank(html))
			return new TextFetchResult(publishtime,content,source,author,keyword);
		TextExtractor textExtractor = new NewsExtractor();
//		long startMili = System.currentTimeMillis();
		List<SliceInfo> sliceList = textExtractor.extract(html);
//		System.out.println("文本抽取用时" + (System.currentTimeMillis()-startMili) + "ms");
//		startMili = System.currentTimeMillis();
		List<SliceIndex> sliceIndexList = filter(sliceList);
//		System.out.println("二次过滤用时" + (System.currentTimeMillis()-startMili) + "ms");
		
		if(sliceIndexList.size()>0){
//			startMili = System.currentTimeMillis();
			int contentIndex = findMostLikelyContent(sliceIndexList, 0, sliceIndexList.size());
			content = sliceIndexList.get(contentIndex).slice.getContent();
//			System.out.println("提取正文用时" + (System.currentTimeMillis()-startMili) + "ms");
			String preText = mergeSliceIndexText(sliceIndexList, 0, contentIndex);
			
			String postText = mergeSliceIndexText(sliceIndexList, contentIndex+1, sliceIndexList.size());
			
//			startMili = System.currentTimeMillis();
			//在0-contentIndex-1范围内寻找发布时间
			publishtime = getPublishTime(sliceList, 0, sliceIndexList.get(contentIndex).index-1);
//			System.out.println("提取发布时间用时" + (System.currentTimeMillis()-startMili) + "ms");
//			startMili = System.currentTimeMillis();
			//获取作者
			author = getAuthor(preText);		
			if(author==null) author = getAuthor(postText);
//			System.out.println("提取作者用时" + (System.currentTimeMillis()-startMili) + "ms");
			
			int index = sliceIndexList.get(contentIndex).index;
			preText = mergeSliceText(sliceList, 0, index);
			postText  = mergeSliceText(sliceList, index+1, sliceList.size());
//			startMili = System.currentTimeMillis();
			//获取来源
			source = getSource(preText);
			if(StringUtils.isBlank(source))
			{
				source = getSource(content);
			}
//			System.out.println("获取来源用时" + (System.currentTimeMillis()-startMili) + "ms");
//			startMili = System.currentTimeMillis();
			//获取关键字
			keyword = getKeyword(preText);
			if(keyword==null) keyword = getKeyword(postText);
//			System.out.println("获取关键字用时" + (System.currentTimeMillis()-startMili) + "ms");
		}	
		
		return new TextFetchResult(publishtime,content,source,author,keyword);
	}
	
	/**
	 * 过滤部分噪声文本
	 * @param sliceList
	 * @return
	 */
	private List<SliceIndex> filter(List<SliceInfo> sliceList){
		int i = 0, len = sliceList.size();
		SliceInfo slice;
		List<SliceIndex> ret = new ArrayList<SliceIndex>();
		while (i < len ) {
			slice = sliceList.get(i);
			if(!isFilter(slice.getMode())&&!isContainFilterWords(slice.getContent())){
				ret.add(new SliceIndex(slice,i));
			}
			i++;			
		}
		return ret;
	}
	
	private boolean isFilter(String mode){
		String[] tags = mode.split("\\|");
		int i = tags.length -1;
		while(i>=0){
			if(TagUtils.isHTag(tags[i])||filterTagSet.contains(tags[i])){
				return true;
			}
			i--;
		}
		return false;
	}
	
	private boolean isContainFilterWords(String content){
		int index;
		for (String regex : filterWords) {
			index = content.indexOf(regex);
			if(content.length()<100&&index!=-1&&index<20)
				return true;
		}
		return false;
	}
	
	/**
	 * 合并指定区间文本块
	 * @param sliceList
	 * @param begin
	 * @param end
	 * @return
	 */
	private String mergeSliceText(List<SliceInfo> sliceList, int begin, int end){
		int i = begin, j = end;
		if(i<0) i = 0;
		if(j > sliceList.size()) j = sliceList.size();
		StringBuffer buffer = new StringBuffer();
		while(i<j){
			buffer.append(sliceList.get(i).getContent()+" ");
			i++;
		}
		return buffer.toString();
	}
	
	/**
	 * 合并指定区间文本块
	 * @param sliceList
	 * @param begin
	 * @param end
	 * @return
	 */
	private String mergeSliceIndexText(List<SliceIndex> sliceList, int begin, int end){
		int i = begin, j = end;
		if(i<0) i = 0;
		if(j > sliceList.size()) j = sliceList.size();
		StringBuffer buffer = new StringBuffer();
		while(i<j){
			buffer.append(sliceList.get(i).slice.getContent()+" ");
			i++;
		}
		return buffer.toString();
	}

	public static void main(String[] args) {
//		ContentParseResult contentResult = new ContentParseResult();
//		NewsHtmlParser np = new NewsHtmlParser();
//		TextFetchResult fetchResult = np.parse(html);
	}

}
