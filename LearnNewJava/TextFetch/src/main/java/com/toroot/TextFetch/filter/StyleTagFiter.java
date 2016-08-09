/**
 * 
 */
package com.toroot.TextFetch.filter;

/**
 * @author longkailiang
 *
 */
public class StyleTagFiter extends NormalTagFiter {

	@Override
	protected void setTag() {
		TAG_END = "/style";		
	}

}
