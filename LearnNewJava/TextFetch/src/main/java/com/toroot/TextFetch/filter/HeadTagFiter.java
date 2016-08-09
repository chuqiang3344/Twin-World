/**
 * 
 */
package com.toroot.TextFetch.filter;

/**
 * @author John
 *
 */
public class HeadTagFiter extends NormalTagFiter {

	@Override
	protected void setTag() {
		TAG_END = "/head";	
	}

}
