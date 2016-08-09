/**
 * 
 */
package com.toroot.TextFetch.custom;

import java.util.ArrayList;
import java.util.List;

import com.toroot.TextFetch.entity.CleanerInfo;
import com.toroot.TextFetch.entity.SliceInfo;
import com.toroot.TextFetch.utils.TagUtils;

/**
 * 对p标签的内容进行特殊处理
 * @author longkailiang
 *
 */
public class PTagProcess extends CustomProcess {

	public PTagProcess(CustomProcess customProcess) {
		super(customProcess);
	}

	/* (non-Javadoc)
	 * @see com.toroot.TextFetch.custom.CustomProcess#custom(java.util.List)
	 */
	@Override
	protected List<SliceInfo> custom(List<SliceInfo> sliceList) {
		sliceList = featureProcess.process(sliceList);
		int tagClass, len = sliceList.size(), i = 0;
		SliceInfo slice;
        CleanerInfo cleanerInfo;
		List<SliceInfo> ret = new ArrayList<SliceInfo>();
		while (i < len) {
			slice = sliceList.get(i);
			tagClass = TagUtils.classTag(slice.getTag());
			if(tagClass==1&&"p".equals(slice.getTag())){
				if(needFiter(slice)){
					i = filterTag(sliceList,i,slice.getTag());
				}
				else {
					//清除P标签，合并文本
					cleanerInfo = merge(sliceList, i);
					i = cleanerInfo.getEnd();
					ret.add(cleanerInfo.getSliceInfo());
				}
			}
			else {
				ret.add(slice);
			}
			i++;
		}
		return ret;
	}
	
	private boolean needFiter(SliceInfo slice ){
		 //只包含a标签直接过滤
		 if(slice.getTagNum()==slice.getATagNum()
				 &&(slice.getTextNum()==0
				||slice.getATagNum()*1.0/(slice.getATagNum()+slice.getTextNum())>0.75)) 
			 return true;	 
		 return  false;
	}
	
	/**
	 * 合并文本
	 * @param sliceList
	 * @param begin
	 * @return
	 */
	private CleanerInfo merge(List<SliceInfo> sliceList, int begin){
		int i=begin+1,len = sliceList.size(),tagClass;
        StringBuffer buffer = new StringBuffer();
        SliceInfo slice;
        CleanerInfo cleanerInfo;
		while (i < len) {
			slice = sliceList.get(i);
			tagClass = TagUtils.classTag(slice.getTag());
			if (tagClass==1) {
				cleanerInfo = cleanerTag(sliceList, i, slice.getTag());
//				if(buffer.length()>0)
//					buffer.deleteCharAt(buffer.length()-1);					
				buffer.append(cleanerInfo.getSliceInfo().getContent());
				i = cleanerInfo.getEnd();
			}
			else if (tagClass==0){
				buffer.append(slice.getContent());
			}
			else {
				break;
			}
			i++;
		}
		SliceInfo sliceInfo = new SliceInfo(buffer.toString()+"\n",null);
		sliceInfo.setMode(sliceList.get(begin).getMode());
		return new CleanerInfo(sliceInfo, i, i);
	}

}
