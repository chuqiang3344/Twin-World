/**
 * 
 */
package com.toroot.TextFetch.filter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.toroot.TextFetch.entity.TagInfo;
import com.toroot.TextFetch.utils.TagUtils;



/**
 * 过滤Script标签
 * @author longkailiang
 *
 */
public class ScriptTagFilter extends TagFilter {
	
	private static final String TAG = "script";
	
	private static final String TAG_END = "/script";
	
	private static final Set<String> special = new HashSet<String>(Arrays.asList("+","=","(",",",";"));

	/* (non-Javadoc)
	 * @see cn.torrot.tool.TagFilter#fiter(char[], java.lang.String, int)
	 */
	@Override
	public int fiter(char[] seq, int begin) {
		int i=begin;
		boolean isSingleEnd = true;
		TagInfo tagInfo;
		
		while (i < seq.length) {
			if(isSingleEnd && seq[i]=='>') {
				if( seq[i-1]=='/') break;
				else isSingleEnd = false;
			}
			//过滤"..."和'...'中可能出现的script
			else if((seq[i]=='"'||seq[i]=='\'')&&isQuotation(seq,i)){
				i = getQuotationEnd(seq,i);
			}
			else if(seq[i]=='<'){
				tagInfo = TagUtils.findTag(seq, i+1);			
				if(TAG_END.equals(tagInfo.getTag())){
					i = tagInfo.getEnd();
					break;
				}
			}
			i++;
		}

		return i;
	}
	
	private boolean isQuotation(char[] seq, int position){
		if(seq[position-1]!='\\') return true;
		return false;
	}
	
	private int getQuotationEnd(char[] seq, int position){
		char quotation = seq[position];
		int i = position + 1;
		while (i < seq.length) {
            if((seq[i]==quotation&&isQuotation(seq,i))||seq[i]==10)
                  break;
            i++;
		}
		if(seq[i]!=10){
			String c = String.valueOf(seq[i-1]);
			String cn = null;
			if(i+1<seq.length)
			    cn = String.valueOf(seq[i+1]);
			if((Character.isWhitespace(seq[i-1])
					||special.contains(c))&&!(cn!=null&&(
				Character.isWhitespace(seq[i+1])
					||special.contains(cn)))){
				i--;
			}
		}
		return i;
	}

}
