package com.toroot.TextFetch.clean;

import java.util.List;

import com.toroot.TextFetch.entity.CleanerInfo;
import com.toroot.TextFetch.entity.SliceInfo;

/**
 * 过滤ul标签
 * @author mg
 * */
public class UltagCleaner extends NormalTagCleaner{
	@Override
	protected void setTag() {
		TAG = "ul";
		TAG_END = "/ul";
	}
	
	@Override
	public CleanerInfo clean(List<SliceInfo> list, int begin) {
		setTag();
		int beginNum = 1, endNum = 0, i=begin;
		int len = list.size();
		StringBuffer content = new StringBuffer();
		String tag;
		SliceInfo sliceInfo;
		while (i < len) {
			sliceInfo = list.get(i);	
			tag = sliceInfo.getTag();
			if(tag != null){
				if(TAG.equals(tag)) beginNum++;
				if(TAG_END.equals(tag)) endNum++;
				if(beginNum<=endNum) break;
			}
//			else {
//				content.append(sliceInfo.getContent()+" ");
//			}
			i++;
		}
		
		return new CleanerInfo(new SliceInfo(content.toString(), null), begin, i);
	}
}
