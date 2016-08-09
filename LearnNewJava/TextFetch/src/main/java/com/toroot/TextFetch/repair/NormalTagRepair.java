package com.toroot.TextFetch.repair;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import com.toroot.TextFetch.entity.RepairTagInfo;
import com.toroot.TextFetch.entity.SliceInfo;
import com.toroot.TextFetch.utils.TagUtils;

/**
 * 一般修复器
 * 
 * @author mg
 * **/
public  class NormalTagRepair extends TagRepair {
	
    private static Set<String> tagExclude= new HashSet<String>(Arrays.asList("br", "br/","/br"));
    
    private Stack<RepairTagInfo> stack = new Stack<RepairTagInfo>();
	
	public HashSet<Integer> repair(List<SliceInfo> sliceInfos) {
		HashSet<Integer> waitCleaner = new HashSet<Integer>();
		int len = sliceInfos.size();
		for (int i = 0; i < len; i++) {
			SliceInfo slice = sliceInfos.get(i);
			String tag = slice.getTag();
			if (tag != null) {
				int classtag = TagUtils.classTag(tag);
				if (classtag == 1) {
					if(!tagExclude.contains(tag))
					{
						stack.push(new RepairTagInfo(i, slice));
					}				    
				} else if (classtag == 2 && !tagExclude.contains(tag)) {
					if(isCoExistence(tag)){
						while (!stack.isEmpty()) {
							RepairTagInfo repairTag = stack.pop();
							if (!tag.endsWith(repairTag.getSliceInfo().getTag()) ) {
								  waitCleaner.add(repairTag.getSign());
							} 
							else {
								break;
							}
						}
					}
					else {
						waitCleaner.add(i);
					}
				}
			}
		}
		
		return waitCleaner;
	}
	
	/**
	 * 判断结束标签在标签栈中出现
	 * @param tagEnd
	 * @return
	 */
	private boolean isCoExistence(String tagEnd){
		if(!stack.isEmpty()){
			int i = stack.size()-1;
			RepairTagInfo tagInfo;
			String tag = tagEnd.substring(1);
			while (i>=0) {
				tagInfo = stack.get(i);
				if(tag.endsWith(tagInfo.getSliceInfo().getTag()))
					return true;
				i--;
			}
		}
		return false;
	}
}
