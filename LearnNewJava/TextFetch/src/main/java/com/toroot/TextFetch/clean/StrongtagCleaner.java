package com.toroot.TextFetch.clean;
/**
 * @author mg
 * strong去除标签
 * */
public class StrongtagCleaner extends NormalTagCleaner{
	
	protected void setTag() {
		TAG = "strong";
		TAG_END = "/strong";
		isAppendNewline = false;
	}
	
}
