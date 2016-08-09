/**
 * 
 */
package com.toroot.TextFetch.custom;

import java.util.ArrayList;
import java.util.List;

import com.toroot.TextFetch.entity.SliceInfo;
import com.toroot.TextFetch.utils.TagUtils;

/**
 * 对包含3个以上tr的Table直接过滤掉
 * @author longkailiang
 *
 */
public class TableTagProcess extends CustomProcess {

	public TableTagProcess(CustomProcess customProcess) {
		super(customProcess);
	}

	/* (non-Javadoc)
	 * @see com.toroot.TextFetch.custom.CustomProcess#custom(java.util.List)
	 */
	@Override
	protected List<SliceInfo> custom(List<SliceInfo> sliceList) {
        int i = 0, len = sliceList.size();
        SliceInfo slice;
        List<SliceInfo> ret = new ArrayList<SliceInfo>();
        while(i<len){
        	slice = sliceList.get(i);
        	if(TagUtils.classTag(slice.getTag())==1
        	    &&("table".equals(slice.getTag())
        	    		||"tbody".equals(slice.getTag()))){
        		if(slice.getTagNum()>3){
        			i = filterTag(sliceList, i, slice.getTag())+1;
        			continue;
        		}
        	}
        	ret.add(slice);
        	i++;
        }
		return ret;
	}

}
