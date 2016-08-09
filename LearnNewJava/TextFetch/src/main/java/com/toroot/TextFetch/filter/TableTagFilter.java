/**
 * 
 */
package com.toroot.TextFetch.filter;

/**
 * @author John
 *
 */
public class TableTagFilter extends NormalTagFiter {

	/* (non-Javadoc)
	 * @see cn.torrot.html.filter.NormalTagFiter#setTag()
	 */
	@Override
	protected void setTag() {
		TAG_END = "/table";
	}

}
