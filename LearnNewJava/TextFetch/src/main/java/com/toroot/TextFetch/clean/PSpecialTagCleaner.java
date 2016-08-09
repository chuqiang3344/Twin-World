package com.toroot.TextFetch.clean;
/**
 * 特殊P标签处理
 * 
 * */
public class PSpecialTagCleaner extends NormalTagCleaner{
	@Override
	protected void setTag() {
		TAG = "/p";
		TAG_END = "p";
	}
}
