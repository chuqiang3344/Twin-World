/**
 * 
 */
package com.toroot.TextFetch.filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.toroot.TextFetch.entity.SliceInfo;
import com.toroot.TextFetch.entity.TagInfo;
import com.toroot.TextFetch.utils.HtmlUtils;
import com.toroot.TextFetch.utils.TagUtils;


/**
 * html文本切割过滤
 * @author longkailiang
 *
 */
public class TextSplitter {
       
	/**
	 * 过滤器列表，不需过滤默认标签可使用customizeTagFiter来生成自己的定制过滤器列表
	 */
	private Set<String> fiterSet= new HashSet<String>(Arrays.asList("script", "link","head",
			"style","!","noscript","?xml","select"));
	
	public void addTagFiter(String tag){
		fiterSet.add(tag.toLowerCase().trim());
	}
	
	/**
	 * 定制过滤器列表
	 * @param tags i.e. "script|link|head"
	 */
	public void customizeTagFiter(String tags){
		if(tags!=null){
			fiterSet.clear();
			String[] tagList = tags.split("\\|"); 
			for (String tag : tagList) {
				fiterSet.add(tag.toLowerCase().trim());
			}
		}
	}
	
	public List<SliceInfo> split(String html){
		List<SliceInfo> sliceList = new ArrayList<SliceInfo>();
		StringBuffer sliceBuffer = new StringBuffer();
		TagInfo tagInfo;
		char[] seq = html.toCharArray();
		int i = 0, len = seq.length;
		TagFilter tagFilter = null;
		String str;
		String tag = null;
		QuotationInfo quotationInfo;
		boolean contact = false;
		while (i<len) {
			if(seq[i]=='<'){
				str = HtmlUtils.trimWhitespace(sliceBuffer);
				if(str.length()>0){
					if(sliceList.size()>0){
						SliceInfo sliceInfo = sliceList.get(sliceList.size()-1);
						if(contact&&sliceInfo.getTag()==null){
							sliceInfo.setContent(sliceInfo.getContent()+str);
							contact = false;
						}
						else
						    sliceList.add(new SliceInfo(str, null));
					}
					else
					    sliceList.add(new SliceInfo(str, null));
					sliceBuffer.setLength(0);
				}
				tagInfo = TagUtils.findTag(seq, i+1);	
				tagFilter = getFiter(tagInfo.getTag());
				if(tagFilter!=null){
					i = tagFilter.fiter(seq, tagInfo.getEnd()+1)+1;
					continue;
				}
				tag = tagInfo.getTag();
			}
		    sliceBuffer.append(seq[i]);
		    
			//<>属性引号中的文本单独读取 避免引号中的符号污染
			if(sliceBuffer.charAt(0)=='<'
					&&(seq[i]=='"'||seq[i]=='\'')&&isQuotation(seq,i)){
				quotationInfo = getQuotationEnd(seq,i);
				i = quotationInfo.index;
				sliceBuffer.append(quotationInfo.content);
			}
			
			if(i<len&&seq[i]=='>'){
				if(tag!=null&&tag.length()>0){
					sliceList.add(new SliceInfo(sliceBuffer.toString(), tag.toLowerCase()));
					sliceBuffer.setLength(0);
					tag = "";
				}
				else {
					contact = true;
				}
			}

			i++;
		}
		
		return sliceList;
	}
	
	/**
	 *引号中包含的文本
	 */
	private class QuotationInfo{
		
		public String content;
		
		public int index;
		
		public QuotationInfo(String content,int index){
			this.content = content;
			this.index = index;
		}
	}
	
	private QuotationInfo getQuotationEnd(char[] seq, int position){
		char quotation = seq[position];
		int i = position + 1;
		StringBuffer buffer = new StringBuffer();
		while (i < seq.length) {
			buffer.append(seq[i]);
            if(seq[i]==quotation&&isQuotation(seq,i))
                  break;
            i++;
		}
		if(seq[i-1]=='='){
			i = i-1;
		}
		return new QuotationInfo(buffer.toString(), i);
	}
	
	private boolean isQuotation(char[] seq, int position){
		if(seq[position-1]!='\\') return true;
		return false;
	}
	
	private TagFilter getFiter(String tag){
		if(fiterSet.contains(tag)){
			return FiterRegister.getFiter(tag);
		}
		return null;
	}
	
}
