/**
 * 
 */
package com.toroot.TextFetch.custom;

import java.util.List;

import com.toroot.TextFetch.entity.CleanerInfo;
import com.toroot.TextFetch.entity.SliceInfo;
import com.toroot.TextFetch.utils.HtmlUtils;

/**
 * 定制特殊处理接口
 * @author longkailiang
 *
 */
public abstract class CustomProcess implements IcustomProcess{
	
	protected IcustomProcess customProcess = null;
	
	protected static FeatureProcess featureProcess = new FeatureProcess();
	
	public CustomProcess(IcustomProcess customProcess)
    {
        this.customProcess = customProcess;
    
    }
	
	/**
	 * 对切片列表进行特殊处理
	 * @param sliceList
	 * @return
	 */
	protected abstract List<SliceInfo> custom(List<SliceInfo> sliceList);
	
	
	public List<SliceInfo> process(List<SliceInfo> sliceList) {		
		 if(customProcess == null){
			 return custom(sliceList);
		 }
		 return customProcess.process(custom(sliceList));
	}

	/**
	 * 过滤指定的标签
	 * @param list
	 * @param begin
	 * @param tag
	 * @return
	 */
	protected int filterTag(List<SliceInfo> list, int begin, String tag){
		if(list.get(begin).getContent().endsWith(" />")
				||tagExclude.contains(list.get(begin).getTag())){
			return begin;
		}
		int beginNum = 1, endNum = 0, i=begin+1;
		int len = list.size();
		String tag_end = "/" + tag;
		String t;
		while (i < len) {
			t = list.get(i).getTag();
			if(t != null){
				t = t.toLowerCase();
				if(tag.equals(t)) beginNum++;
				if(tag_end.equals(t)) endNum++;
				if(beginNum<=endNum)
				{
					break;
				}
			}
			i++;
		}
		return i;
	}

	/**
	 * 清除指定的标签
	 * @param list
	 * @param begin
	 * @param tag
	 * @return
	 */
	protected CleanerInfo cleanerTag(List<SliceInfo> list, int begin, String tag){
		int beginNum = 1, endNum = 0, i=begin+1;
		int len = list.size();
		String tag_end = "/" + tag;
		String t;
		StringBuffer content = new StringBuffer();
		SliceInfo slice;
		while (i < len) {
			slice = list.get(i);
			t = slice.getTag();
			if(t != null){
				t = t.toLowerCase();
				if(tag.equals(t)) beginNum++;
				if(tag_end.equals(t)) endNum++;
				if(tagExclude.contains(t)){
					content.append("\n");
				}
				if(beginNum<=endNum)
				{
					break;
				}
			}
			else {
				content.append(slice.getContent());
			}
			i++;
		}
		return new CleanerInfo(new SliceInfo(HtmlUtils
				.trimWhitespace(content.toString()), null), begin, i);
	}
}
