package com.toroot.TextFetch.clean;
/**
 * img标签清理
 * @author mg
 * */
public class ImgtagCleaner extends NormalTagCleaner{
		@Override
		protected void setTag() {
			TAG = "img";
			TAG_END = "/img";
		}
}
