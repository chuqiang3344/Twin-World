/**
 * 
 */
package com.toroot.TextFetch.filter;

/**
 * @author John
 *
 */
public class SelectTagFilter extends NormalTagFiter {

	/* (non-Javadoc)
	 * @see com.toroot.TextFetch.filter.NormalTagFiter#setTag()
	 */
	@Override
	protected void setTag() {
		TAG_END = "/select";
	}

}
