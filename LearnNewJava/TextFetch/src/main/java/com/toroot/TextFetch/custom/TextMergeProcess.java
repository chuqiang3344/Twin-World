/**
 * 
 */
package com.toroot.TextFetch.custom;

import java.util.ArrayList;
import java.util.List;

import com.toroot.TextFetch.entity.SliceInfo;

/**
 * 邻接文字块合并
 * @author longkailiang
 *
 */
public class TextMergeProcess extends CustomProcess {

	public TextMergeProcess(CustomProcess customProcess) {
		super(customProcess);
	}

	/* (non-Javadoc)
	 * @see com.toroot.TextFetch.custom.CustomProcess#custom(java.util.List)
	 */
	@Override
	protected List<SliceInfo> custom(List<SliceInfo> sliceList) {
		int len = sliceList.size(), i = 0;
		SliceInfo slice, temp;
		List<SliceInfo> ret = new ArrayList<SliceInfo>();
		while (i < len) {
			slice = sliceList.get(i);
			//若当前切片是文本并且前一个切块也是文本则合并到前一个切块
			if(slice.getTag()==null&&(i-1)>=0
					&&sliceList.get(i-1).getTag()==null){
				temp = ret.get(ret.size()-1);
				temp.setContent(temp.getContent()+"\n"+slice.getContent());
			}
			else
			    ret.add(slice);
			i++;
	    }
		return ret;
	}

}
