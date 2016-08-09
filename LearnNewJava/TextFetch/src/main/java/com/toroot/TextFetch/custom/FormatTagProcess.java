/**
 * 
 */
package com.toroot.TextFetch.custom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.toroot.TextFetch.entity.SliceInfo;
import com.toroot.TextFetch.utils.TagUtils;

/**
 * 格式类标签处理
 * @author longkailiang
 *
 */
public class FormatTagProcess extends CustomProcess {
	
	private static Set<String> tagSet = new HashSet<String>(
			Arrays.asList("b","em","strong","font","sub","big"
					,"center","s","small","strike","sup","i","q"));

	public FormatTagProcess(CustomProcess customProcess) {
		super(customProcess);
	}

	/* (non-Javadoc)
	 * @see com.toroot.TextFetch.custom.CustomProcess#custom(java.util.List)
	 */
	@Override
	protected List<SliceInfo> custom(List<SliceInfo> sliceList) {
		sliceList = featureProcess.process(sliceList);
		int len = sliceList.size(), i = 0,j,tagClass;
		SliceInfo slice;
		List<SliceInfo> ret = new ArrayList<SliceInfo>();
		int end;
		String tag;
		while (i < len) {
			slice = sliceList.get(i);
			if(TagUtils.classTag(slice.getTag())==1
			  &&tagSet.contains(slice.getTag())){
				end = filterTag(sliceList, i, slice.getTag());
				j = i+1;
				if(end>j){
					if(slice.getTagNum()==0){
						if(ret.size()>0){
							slice = ret.get(ret.size()-1);
							//如果上一个是文本块则合并到一起
							if(slice.getTag()==null){
								slice.setContent(slice.getContent()
										+mergeText(sliceList,j,end));
							}
							else {
								ret.add(mergeText(sliceList,j,end));
							}
						}
						else
						    ret.add(mergeText(sliceList,j,end));
					}
					else{
						int m = end;
						if(slice.getTagNum()>1){
							j = i;
							m = end+1; 
						}
						while(j<m){
							slice = sliceList.get(j);
							tagClass = TagUtils.classTag(slice.getTag());
							if(tagClass!=0){
								tag = slice.getTag();
								if(tagClass==2){
									tag = tag.substring(1);
								}
								if(!tagSet.contains(tag)||j==i||j==end){
									ret.add(sliceList.get(j));
								}
							}
							else
							   ret.add(sliceList.get(j));
							j++;
						}
					}
				}
				i = end;
			}
			else {
				ret.add(slice);
			}
			i++;
		}
		return ret;
	}
	
	private SliceInfo mergeText(List<SliceInfo> list,int begin, int end){
		int i = begin;
		StringBuffer buffer = new StringBuffer();
		while (i < end) {
			buffer.append(list.get(i).getContent()+" ");
			i++;
		}
		return new SliceInfo(buffer.toString(),null);
	}

}
