/**
 * 
 */
package com.toroot.TextFetch.custom;

import java.util.ArrayList;
import java.util.List;

import com.toroot.TextFetch.entity.SliceInfo;
import com.toroot.TextFetch.utils.TagUtils;

/**
 * 将只包含文本的div标签清理
 * @author longkailiang
 *
 */
public class DivTagProcess extends CustomProcess {

	public DivTagProcess(CustomProcess customProcess) {
		super(customProcess);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.toroot.TextFetch.custom.CustomProcess#custom(java.util.List)
	 */
	@Override
	protected List<SliceInfo> custom(List<SliceInfo> sliceList) {
		sliceList = featureProcess.process(sliceList);
		int len = sliceList.size(), i = 0, num,j;
		SliceInfo slice;
		List<SliceInfo> ret = new ArrayList<SliceInfo>();
		StringBuffer buffer = new StringBuffer();
		while (i < len) {
			slice = sliceList.get(i);
			if(TagUtils.classTag(slice.getTag())==1
			    &&"div".equals(slice.getTag())
				&&slice.getTagNum()==0
				&&slice.getTextNum()>0){
				num = i+slice.getTextNum();
				j = i+1;
				buffer.setLength(0);
				while(j<=num){
					buffer.append(sliceList.get(j).getContent()+"\n");
					j++;
				}				
				ret.add(new SliceInfo(buffer.toString(),null));
				i = j;
			}
			else {
				ret.add(slice);
			}
			i++;
		}
		return ret;
	}

}
