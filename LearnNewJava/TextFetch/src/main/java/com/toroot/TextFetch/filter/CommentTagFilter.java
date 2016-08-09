/**
 * 
 */
package com.toroot.TextFetch.filter;

/**
 * @author John
 *
 */
public class CommentTagFilter extends TagFilter {

	/* (non-Javadoc)
	 * @see cn.torrot.html.clean.TagFilter#fiter(char[], int)
	 */
	@Override
	public int fiter(char[] seq, int begin) {
		int i = begin;
		
		while (i < seq.length) {
			if(seq[i]=='>'){
				if(seq[begin]=='-'){
					if(seq[i-1]=='-'&&seq[i-2]=='-') break; 
				}
				else break;
			}
			i++;
		}
		
		return i;
	}

}
