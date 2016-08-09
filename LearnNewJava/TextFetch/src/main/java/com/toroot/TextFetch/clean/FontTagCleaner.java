/**
 * 
 */
package com.toroot.TextFetch.clean;

/**
 * @author John
 *
 */
public class FontTagCleaner extends NormalTagCleaner {

	/* (non-Javadoc)
	 * @see com.toroot.TextFetch.clean.NormalTagCleaner#setTag()
	 */
	@Override
	protected void setTag() {
		TAG = "font";
		TAG_END = "/font";
		isAppendNewline = false;
	}

}
