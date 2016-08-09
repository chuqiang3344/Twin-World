/**
 * 
 */
package com.toroot.TextFetch.utils;

import com.toroot.TextFetch.entity.TagInfo;

/**
 * @author longkailiang
 *
 */
public class TagUtils {
    
	public static TagInfo findTag(char[] seq, int begin){
		StringBuffer tag = new StringBuffer();
		int i;
		for (i = begin; i < seq.length; i++) {
			if(seq[i]==' '){
				if(tag.length()>0)
				    break;
			}
			else if(seq[i]=='>'){
				break;
			}
			else {
				tag.append(seq[i]);
				if(seq[i]=='!') break;
			}
		}
		return new TagInfo(tag.toString().toLowerCase(), begin, i);
	}	
		
	public static String findTag(String str){
		StringBuffer tag = new StringBuffer();
		char[] seq = str.toCharArray();
		int i, len = seq.length;
		for (i = 1; i < len; i++) {
			if(seq[i]==' '){
				if(tag.length()>0)
				    break;
			}
			else if(seq[i]=='>'){
				break;
			}
			else {
				tag.append(seq[i]);
				if(seq[i]=='!') break;
			}			
		}	
		return tag.toString();
	}
	
	public static boolean isTag(String str){
		if(str.length()>2&&str.startsWith("<")&&str.endsWith(">"))
			return true;
	    return false;		
	}
	
	public static int classTag(String tag) {
		if (tag != null) {
			if (tag.startsWith("/") )
				return 2;
			else
				return 1;
		}
		return 0;
	}
	
	public static boolean isHTag(String tag){
		if(tag.startsWith("h")&&tag.length()==2
				&&Character.isDigit(tag.charAt(1)))
			return true;
		return false;
	}
	
}
