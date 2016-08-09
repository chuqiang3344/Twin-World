/**
 * 
 */
package com.toroot.TextFetch.custom;

import java.util.ArrayList;
import java.util.List;

import com.toroot.TextFetch.entity.SliceInfo;
import com.toroot.TextFetch.utils.TagUtils;

/**
 * 清楚空标签
 * @author longkailiang
 *
 */
public class NullTagProcess extends CustomProcess {

	public NullTagProcess(CustomProcess customProcess) {
		super(customProcess);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.toroot.TextFetch.custom.CustomProcess#custom(java.util.List)
	 */
	@Override
	protected List<SliceInfo> custom(List<SliceInfo> sliceList) {
		sliceList = featureProcess.process(sliceList);
		int tagClass, len = sliceList.size(), i = 0;
		List<SliceInfo> ret = new ArrayList<SliceInfo>();
		SliceInfo slice;
		while (i < len) {
			slice = sliceList.get(i);
			tagClass = TagUtils.classTag(slice.getTag());
			if (tagClass == 1 && slice.isNull()) {
				i = filterTag(sliceList,i,slice.getTag());
				if(slice.getDelimiterNum()>0)
					if(slice.getpIndex()!=-1)
				       sliceList.get(slice.getpIndex()).incrDelimiterNum();
			}
			else {
				ret.add(slice);
			}
			i++;
		}
		return ret;
	}

}
