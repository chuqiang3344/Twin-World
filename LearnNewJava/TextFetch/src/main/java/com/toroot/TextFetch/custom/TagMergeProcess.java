/**
 * 
 */
package com.toroot.TextFetch.custom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.toroot.TextFetch.entity.CleanerInfo;
import com.toroot.TextFetch.entity.SliceInfo;
import com.toroot.TextFetch.utils.TagUtils;

/**
 * 合并标签中的文字块
 * 当标签下直接邻接的文字块数量>2时直接合并文字块并清除其中包含的子标签
 * @author longkailiang
 *
 */
public class TagMergeProcess extends CustomProcess {
	
	private Set<String> cleanTagSet = new HashSet<String>(Arrays.asList("a"));

	public TagMergeProcess(CustomProcess customProcess) {
		super(customProcess);
	}

	@Override
	protected List<SliceInfo> custom(List<SliceInfo> sliceList) {
		sliceList = featureProcess.process(sliceList);
		int len = sliceList.size(), i = 0;
		SliceInfo slice;
		//对直接邻接的文字块数量>=2的标签，合并其下的文字块并清除其中包含的子标签
		List<SliceInfo> ret = new ArrayList<SliceInfo>();
		CleanerInfo cleanerInfo;
		while (i < len) {
			slice = sliceList.get(i);
			ret.add(slice);
			if(needMerge(slice)){
				cleanerInfo = merge(sliceList, i);
				ret.add(cleanerInfo.getSliceInfo());
				i = cleanerInfo.getEnd();
				if(i<len)
				   ret.add(sliceList.get(i));
			}
            i++;
		}
		return ret;
	}
	
	private boolean needMerge(SliceInfo slice){

		 if(slice.getTagNum()==0&&slice.getTextNum()>1){
			 if(slice.getDelimiterNum()>0){
				 int delimiter = -1;
				 delimiter = slice.getTextNum()-slice.getDelimiterNum();
				 if(delimiter==1)
					 return false;
			 }
			 return true;
		 }
		 if (slice.getTextNum()>5
				 ||slice.getTextNum()*1.0/slice.getTagNum()> 1.0
				 ||(slice.getTextNum()>2&&slice.getTextNum()*1.0/slice.getTagNum()>=1.0)) {
			 return true;
		 }		 
		 return  false;
	}
	
	/**
	 * 合并文本
	 * @param sliceList
	 * @param begin
	 * @return
	 */
	private CleanerInfo merge(List<SliceInfo> sliceList, int begin){
		int i=begin+1,len = sliceList.size(),tagClass,lastTagClass=-1;
        StringBuffer buffer = new StringBuffer();
        SliceInfo slice;
        CleanerInfo cleanerInfo;
        boolean needNewline;
		while (i < len) {
			slice = sliceList.get(i);
			tagClass = TagUtils.classTag(slice.getTag());
			if (tagClass==1) {
				if (needClean(sliceList,i)) {
					cleanerInfo = cleanerTag(sliceList, i, slice.getTag());
					needNewline = needNewline(slice);
					if(!needNewline&&buffer.length()>0&&lastTagClass==0)
						buffer.deleteCharAt(buffer.length()-1);					
					buffer.append(cleanerInfo.getSliceInfo().getContent());
					if(needNewline)
						buffer.append("\n");
					i = cleanerInfo.getEnd();
				}
				else {
					i = filterTag(sliceList,i,slice.getTag());
				}
			}
			else if (tagClass==0){
				buffer.append(slice.getContent()+"\n");
			}
			else {
				break;
			}
			lastTagClass = tagClass;
			i++;
		}
		SliceInfo sliceInfo = new SliceInfo(buffer.toString(),null);
		sliceInfo.setMode(sliceList.get(begin).getMode());
		return new CleanerInfo(sliceInfo, i, i);
	}
	
	private boolean needClean(List<SliceInfo> sliceList, int begin){
		int i = begin;
		SliceInfo slice = sliceList.get(i);
		if(cleanTagSet.contains(slice.getTag()))
			return true;
//		if(slice.getTextNum()==0&&slice.getATagNum()==1
//						&&slice.getATagNum()==slice.getTagNum())
//			return true;
		if(slice.getTagNum()==0&&slice.getTextNum()>1){
			return true;
		}
		if (slice.getTextNum()>5
				 ||slice.getTextNum()*1.0/slice.getTagNum()> 1.0) {
			return true;
		}	
		
		if(slice.getTextNum()==0&&slice.getTagNum()==1){
			int end = filterTag(sliceList, begin, slice.getTag()), tagClass;
			i++;
			boolean isOnlyText = true;
			while(i<end){
				slice  = sliceList.get(i);
				tagClass = TagUtils.classTag(slice.getTag());
				if(tagClass==1){
					if(slice.getTagNum()>1){
						isOnlyText = false;
						break;
					}
				}
				i++;
			}
			if(isOnlyText)
				return true;
		}		
		return false;
	}
	
    private boolean needNewline(SliceInfo slice){
		if(cleanTagSet.contains(slice.getTag()))
			return false;
		if(slice.getTextNum()==0&&slice.getATagNum()==1
						&&slice.getATagNum()==slice.getTagNum())
			return false;
		return true;
    }
}
