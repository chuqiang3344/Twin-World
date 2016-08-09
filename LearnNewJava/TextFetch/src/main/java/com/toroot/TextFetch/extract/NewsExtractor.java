/**
 * 
 */
package com.toroot.TextFetch.extract;

import java.util.List;

import com.toroot.TextFetch.custom.CustomProcess;
import com.toroot.TextFetch.custom.DivTagProcess;
import com.toroot.TextFetch.custom.EescapeCharProcess;
import com.toroot.TextFetch.custom.FormatTagProcess;
import com.toroot.TextFetch.custom.NullTagProcess;
import com.toroot.TextFetch.custom.PTagProcess;
import com.toroot.TextFetch.custom.TagMergeProcess;
import com.toroot.TextFetch.custom.TextMergeProcess;
import com.toroot.TextFetch.entity.SliceInfo;

/**
 * 新闻类网页文本抽取服务
 * @author longkailiang
 *
 */
public class NewsExtractor extends TextExtractor {

	/* (non-Javadoc)
	 * @see com.toroot.TextFetch.extract.TextExtractor#customProcess(java.util.List)
	 */
	@Override
	protected List<SliceInfo> customProcess(List<SliceInfo> sliceList) {
		 CustomProcess customProcess;
		 if(!"wml".equals(protocol)){
			 customProcess =new FormatTagProcess(
					         new EescapeCharProcess(
					         new NullTagProcess(
					         new PTagProcess(
							 new DivTagProcess(
							 new TagMergeProcess(null))))));
		 }
		 else {
			customProcess = new FormatTagProcess(new EescapeCharProcess(new TextMergeProcess(null)));
		 }
		 return customProcess.process(sliceList);
	}

}
