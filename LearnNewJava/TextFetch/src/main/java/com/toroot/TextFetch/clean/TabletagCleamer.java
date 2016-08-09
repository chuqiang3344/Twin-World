/**
 * 
 */
package com.toroot.TextFetch.clean;

/**
 * @author John
 *
 */
public class TabletagCleamer extends NormalTagCleaner {

	/* (non-Javadoc)
	 * @see cn.torrot.html.clean.NormalTagCleaner#setTag()
	 */
	@Override
	protected void setTag() {
		TAG = "table";
		TAG_END = "/table";
	}

}
