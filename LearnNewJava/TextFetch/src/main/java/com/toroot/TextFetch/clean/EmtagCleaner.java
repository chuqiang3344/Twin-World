/**
 * 
 */
package com.toroot.TextFetch.clean;


/**
 * @author longkailiang
 *
 */
public class EmtagCleaner extends NormalTagCleaner {

	@Override
	protected void setTag() {
		TAG = "em";
		TAG_END = "/em";
		isAppendNewline = false;
	}

}
