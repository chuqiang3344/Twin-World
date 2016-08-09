/**
 * 
 */
package com.toroot.TextFetch.custom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.toroot.TextFetch.entity.SliceInfo;
import com.toroot.TextFetch.utils.HtmlUtils;

/**
 * 控制字符串删除处理
 * @author longkailiang
 *
 */
public class EescapeCharProcess extends CustomProcess {
	
	private static Set<String> escapeCharSet = new HashSet<String>(
			Arrays.asList("&nbsp;","&gt;","&lt;","&ensp;","&emsp;","&amp;","&copy;"
					,"&quot;","&ldquo;","&rdquo;","&mdash;","&hellip;","&#160;"
					,"&#8194;","&#8195;","&#60;","&#62;","&#38;","&#34;","&#169;"));
	
	private static Set<String> interpunctionSet = new HashSet<String>(
			Arrays.asList("、","‘","”","？","；","："
					,"！","，","。","\n","（","）","【","】","{","}"));
	
	private static int maxEscapeLen;
	
	static{
		maxEscapeLen = -1;
		for (String str : escapeCharSet) {
			if(maxEscapeLen<str.length())
				maxEscapeLen = str.length();
		}
	}

	public EescapeCharProcess(CustomProcess customProcess) {
		super(customProcess);
	}

	/* (non-Javadoc)
	 * @see com.toroot.TextFetch.custom.CustomProcess#custom(java.util.List)
	 */
	@Override
	protected List<SliceInfo> custom(List<SliceInfo> sliceList) {
		sliceList = featureProcess.process(sliceList);
		int len = sliceList.size(), i = 0;
		SliceInfo slice;
		List<SliceInfo> ret = new ArrayList<SliceInfo>();
		String text;
		while (i < len) {
			slice = sliceList.get(i);
			if(slice.getTag()==null){
				text = deleteEescapeChar(slice.getContent());
				if(!text.isEmpty()){
					if(interpunctionSet.contains(text)
							||HtmlUtils.hasLettersOrNumbers(text)){
						slice.setContent(text);
						ret.add(slice);
					}
					else if(slice.getpIndex()!=-1){
						sliceList.get(slice.getpIndex()).incrDelimiterNum();
					}
				}
			} 
			else {
				ret.add(slice);
			}
			i++;
		}
		return ret;
	}
	
	private String deleteEescapeChar(String text){
		int len = text.length(), i=0;
		char c;
		StringBuffer result = new StringBuffer();
		String escape;
		while(i<len){
			c = text.charAt(i);
			if(c=='&'){
				escape = findEescapeChar(text,i);
				if(escape!=null&&escapeCharSet.contains(escape)){
					i +=  escape.length();
					continue;
				}
			}
			result.append(c);
			i++;
		}
		return HtmlUtils.trimWhitespace(result);
	}
	
	private String findEescapeChar(String text, int begin){
		int i = begin, end = begin+maxEscapeLen;
		if(end>text.length()) end = text.length();
		StringBuffer buffer = new StringBuffer();
		char c;
		while(i<end){
			c = text.charAt(i);
			buffer.append(c);
			if(c==';') break;
			i++;
		}
		
		String ret = buffer.toString();
		if(ret.endsWith(";"))
			return ret;
		return null;
	}

}
