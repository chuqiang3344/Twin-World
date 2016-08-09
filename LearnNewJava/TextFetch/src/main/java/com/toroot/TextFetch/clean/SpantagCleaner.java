package com.toroot.TextFetch.clean;
/**
 * span处理
 * @author mg
 * **/
public class SpantagCleaner extends NormalTagCleaner {
	
	@Override
	protected void setTag() {
		TAG = "span";
		TAG_END = "/span";
	}
}
