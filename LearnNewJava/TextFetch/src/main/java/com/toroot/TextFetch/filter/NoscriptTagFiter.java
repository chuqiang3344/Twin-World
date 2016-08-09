/**
 * 
 */
package com.toroot.TextFetch.filter;

/**
 * @author John
 *
 */
public class NoscriptTagFiter extends NormalTagFiter {

	/* (non-Javadoc)
	 * @see cn.torrot.html.clean.NormalTagFiter#setTag()
	 */
	@Override
	protected void setTag() {
		TAG_END = "/noscript";
	}

}
