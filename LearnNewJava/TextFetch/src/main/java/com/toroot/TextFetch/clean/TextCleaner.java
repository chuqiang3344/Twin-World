/**
 * 
 */
package com.toroot.TextFetch.clean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.toroot.TextFetch.entity.CleanerInfo;
import com.toroot.TextFetch.entity.SliceInfo;

/**
 * html文本标签清除
 * 
 * @author longkailiang
 *
 */
public class TextCleaner {

	/**
	 * 清除器列表，不需清除默认标签可使用customizeTagcleaner来生成自己的定制过滤器列表
	 */
	private Set<String> cleanerSet = new HashSet<String>(Arrays.asList("br","br/","/br"));
	
	public void addTagCleaner(String tag) {
		cleanerSet.add(tag.toLowerCase().trim());
	}

	/**
	 * 定制清除器列表
	 * 
	 * @param tags i.e. "script|link|head"
	 * 
	 */
	public void customizeTagcleaner(String tags) {
		if (tags != null) {
			cleanerSet.clear();
			String[] tagList = tags.split("\\|");
			for (String tag : tagList) {
				cleanerSet.add(tag.toLowerCase().trim());
			}
		}
	}

	private TagCleaner getCleaner(String tag) {

		if (cleanerSet.contains(tag)) {
			return CleanerRegister.getCleaner(tag);
		}
		return null;
	}

	public List<SliceInfo> cleanTag(List<SliceInfo> sliceList) {
		List<SliceInfo> cleanedList = new ArrayList<SliceInfo>();
		int i = 0, len = sliceList.size();
		String tag;
		TagCleaner tagCleaner;
		CleanerInfo cleanerInfo;
		SliceInfo sliceInfo;
		while (i < len) {
			sliceInfo = sliceList.get(i);
			tag = sliceInfo.getTag();
			if (tag != null) {
				tagCleaner = getCleaner(tag);
				if (tagCleaner != null) {
					cleanerInfo = tagCleaner.clean(sliceList, i + 1);
					i = cleanerInfo.getEnd();
					if (cleanerInfo.getSliceInfo().getContent().length() > 0) {
						if(cleanedList.size()>0){
							sliceInfo = cleanedList.get(cleanedList.size()-1);
							//如果上一个是文本块则合并到一起
							if(sliceInfo.getTag()==null){
								sliceInfo.setContent(sliceInfo.getContent()
										+cleanerInfo.getSliceInfo().getContent());
							}
							else {
								cleanedList.add(cleanerInfo.getSliceInfo());
							}
						}
						else
						    cleanedList.add(cleanerInfo.getSliceInfo());
					}
				}
				else
					cleanedList.add(sliceInfo);				
			}
			else {
				cleanedList.add(sliceInfo);
			}
			i++;
		}
		return cleanedList;
	}
}
