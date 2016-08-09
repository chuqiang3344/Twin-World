/**
 * 
 */
package com.toroot.TextFetch.filter;

import com.toroot.TextFetch.entity.TagInfo;
import com.toroot.TextFetch.utils.TagUtils;



/**
 * @author longkailiang
 *
 */
public abstract class NormalTagFiter extends TagFilter {
	
	protected String TAG_END;
	/* (non-Javadoc)
	 * @see cn.torrot.html.clean.TagFilter#fiter(char[], int)
	 */
	@Override
	public int fiter(char[] seq, int begin) {
		setTag();
		int i = begin;
        TagInfo tagInfo;
		while (i < seq.length) {
			if(seq[i]=='<'){
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
	
	protected abstract void setTag();

}
