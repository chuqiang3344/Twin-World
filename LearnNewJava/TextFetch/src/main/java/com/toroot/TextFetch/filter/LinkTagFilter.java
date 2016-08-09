/**
 * 
 */
package com.toroot.TextFetch.filter;

/**
 * @author longkailiang
 *
 */
public class LinkTagFilter extends TagFilter {

	/* (non-Javadoc)
	 * @see cn.torrot.tool.TagFilter#fiter(char[], int)
	 */
	@Override
	public int fiter(char[] seq, int begin) {
		int i = begin;
		
		while (i < seq.length) {
			if(seq[i]=='>') break;
			i++;
		}
		
		return i;
	}

}
