/**
 * 
 */
package com.toroot.TextFetch.clean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.toroot.TextFetch.entity.CleanerInfo;
import com.toroot.TextFetch.entity.SliceInfo;
import com.toroot.TextFetch.utils.HtmlUtils;



/**
 * @author longkailiang
 *
 */
public abstract class NormalTagCleaner extends TagCleaner {
	
	protected String TAG;
	
	protected String TAG_END;
	
	protected boolean isAppendNewline = true;
	
    private static Map<String,String> basicTag=new HashMap<String, String>();
	
	static
	{
		basicTag.put("tr", "。");
		basicTag.put("/p", "\n");
		basicTag.put("br", "\n\n");
		basicTag.put("br/", "\n\n");
		basicTag.put("anchor", "\n\n\n\n");
		basicTag.put("/anchor", "\n\n\n\n");
		basicTag.put("table", "\n\n\n\n");
		basicTag.put("/table", "\n\n\n\n");
	}

	@Override
	public CleanerInfo clean(List<SliceInfo> list, int begin) {
		setTag();
		int beginNum = 1, endNum = 0, i=begin;
		int len = list.size();
		StringBuffer content = new StringBuffer();
		String tag;
		SliceInfo sliceInfo;
		while (i < len) {
			sliceInfo = list.get(i);
			tag = sliceInfo.getTag();
			if(tag != null){
				if(TAG.equals(tag)) beginNum++;
				if(TAG_END.equals(tag)) endNum++;
				if(basicTag.containsKey(tag))
				{
					content.append(basicTag.get(tag));
				}
				if(beginNum<=endNum)
				{
					if(isAppendNewline)
					    content.append("\n"); 
					break;
				}
			}
			else {
				content.append(HtmlUtils.trimWhitespace(sliceInfo.getContent())+" ");
			}
			i++;
		}
		return new CleanerInfo(new SliceInfo(content.toString(), null), begin, i);
	}

	protected abstract void setTag();
	
	protected void init(){
		
	}
	
}
