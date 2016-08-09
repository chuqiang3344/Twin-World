/**
 * 
 */
package com.toroot.TextFetch.clean;


/**
 * 过滤b标签
 * @author longkailiang
 *
 */
public class BtagCleaner extends NormalTagCleaner {

	@Override
	protected void setTag() {
		TAG = "b";
		TAG_END = "/b";
		isAppendNewline = false;
	}

}
