/**
 * 
 */
package com.toroot.TextFetch.custom;

import java.util.List;
import java.util.Stack;

import com.toroot.TextFetch.entity.SliceInfo;
import com.toroot.TextFetch.utils.TagUtils;

/**
 * 构造slice的各种特征
 * @author longkailiang
 *
 */
public class FeatureProcess implements IcustomProcess {

	public List<SliceInfo> process(List<SliceInfo> sliceList) {
		int tagClass, len = sliceList.size(), i = 0;
		Stack<SliceInfo> tagStack = new Stack<SliceInfo>();
		SliceInfo slice, slice1;
		String tag;
		while (i < len) {
			slice = sliceList.get(i);
			slice.setIndex(i);
			slice.Init();
			tagClass = TagUtils.classTag(slice.getTag());
			if (tagClass == 1) {
				if (!slice.getContent().endsWith(" />")&&!tagExclude.contains(slice.getTag())) {
					if (!tagStack.isEmpty()) {
						slice.setMode(tagStack.lastElement().getMode()+"|");
					}		
					slice.setMode(slice.getMode()+slice.getTag());
					tagStack.push(slice);
				}
			}
			else if(tagClass == 2){
				if (!tagStack.isEmpty()) {
					slice1 = tagStack.pop();
					tag = slice1.getTag();
					if (!tagStack.isEmpty()) {						
						slice = tagStack.lastElement();
						if(!slice1.isNull())
						    slice.setNull(false);
						slice.incrTagNum();
						if("a".equals(tag)){
							slice.incrATagNum();;
						}
						slice1.setpIndex(slice.getIndex());
					}
				}
			}
			else {
				if(!tagStack.isEmpty()){
					//标记文字的dom结构
					slice.setMode(tagStack.lastElement().getMode());
					//对标签下包含的文字块进行计数
					tagStack.lastElement().incrTextNum();
					tagStack.lastElement().setNull(false);
					slice.setpIndex(tagStack.lastElement().getIndex());
				}
			}

			i++;
		}
		return sliceList;
	}

}
