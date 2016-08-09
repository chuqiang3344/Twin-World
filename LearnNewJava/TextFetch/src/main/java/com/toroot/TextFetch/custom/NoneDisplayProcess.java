/**
 * 
 */
package com.toroot.TextFetch.custom;

import java.util.ArrayList;
import java.util.List;

import com.toroot.TextFetch.entity.SliceInfo;
import com.toroot.TextFetch.utils.TagUtils;

/**
 * 对不可见标签进行过滤
 * @author longkailiang
 *
 */
public class NoneDisplayProcess extends CustomProcess {
	
	public NoneDisplayProcess(CustomProcess customProcess) {
		super(customProcess);
	}

	@Override
	protected List<SliceInfo> custom(List<SliceInfo> sliceList) {
		int tagClass, len = sliceList.size(), i = 0;
		List<SliceInfo> ret = new ArrayList<SliceInfo>();
		SliceInfo slice;
		while (i < len) {
			slice = sliceList.get(i);
			tagClass = TagUtils.classTag(slice.getTag());
			if (tagClass == 1 && 
					slice.getContent().indexOf("display:none") != -1) {
				i = filterTag(sliceList,i,slice.getTag());
			}
			else{
				ret.add(slice);
			}
			i++;
		}
		return ret;
	}
}
