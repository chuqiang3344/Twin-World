/**
 * 
 */
package com.toroot.TextFetch.clean;

/**
 * @author longkailiang
 *
 */
public class PtagCleaner extends NormalTagCleaner {

	@Override
	protected void setTag() {
		TAG = "p";
		TAG_END = "/p";
	}
    	
}
