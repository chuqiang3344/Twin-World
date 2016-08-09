/**
 * 
 */
package com.toroot.TextFetch.extract;

import java.util.ArrayList;
import java.util.List;

import com.toroot.TextFetch.clean.TextCleaner;
import com.toroot.TextFetch.entity.SliceInfo;
import com.toroot.TextFetch.filter.TextSplitter;
import com.toroot.TextFetch.repair.HtmlRepair;
import com.toroot.TextFetch.utils.TagUtils;

/**
 * html文本抽取接口
 * @author longkailiang
 *
 */
public abstract class TextExtractor {

	private TextSplitter textSplitter = new TextSplitter();
	
	private HtmlRepair htmlRepair=new HtmlRepair();
	
	private TextCleaner textCleaner = new TextCleaner();	
	
	protected String protocol = "html";
	
	public void customizeTagFiter(String tags) {
		textSplitter.customizeTagFiter(tags);
	}

	public void addTagFiter(String tag) {
		textSplitter.addTagFiter(tag);
	}
	
	public void customizeTagCleaner(String tags) {
		textCleaner.customizeTagcleaner(tags);
	}

	public void addTagCleaner(String tag) {
		textCleaner.addTagCleaner(tag);
	}
	
	/**
	 * html预处理
	 * @param html
	 * @return
	 */
	protected List<SliceInfo> preProcess(String html){
		List<SliceInfo> result = new ArrayList<SliceInfo>();
//		long startMili = System.currentTimeMillis();
		List<SliceInfo> sliceList = textSplitter.split(html);
//		System.out.println("切割过滤用时" + (System.currentTimeMillis() - startMili) + "ms");
//		startMili = System.currentTimeMillis();
		sliceList=htmlRepair.repair(sliceList);
//		System.out.println("修复用时" + (System.currentTimeMillis() - startMili) + "ms");
//		startMili = System.currentTimeMillis();

		if(sliceList.size()>0){
			//协议类型：html/wml
			protocol = sliceList.get(0).getTag();		
			result = textCleaner.cleanTag(sliceList);
		}
		
//		System.out.println("清理用时" + (System.currentTimeMillis() - startMili) + "ms");
		return result;
	}
	
	/**
	 * 个性化处理接口，由业务实现
	 * @param sliceList
	 * @return
	 */
	protected abstract List<SliceInfo> customProcess(List<SliceInfo> sliceList);
	
	/**
	 * 文本抽取接口，返回抽取的文本块列表
	 * @param html
	 * @return
	 */
	public List<SliceInfo> extract(String html){
		List<SliceInfo> sliceList = customProcess(preProcess(html));		
		List<SliceInfo> result = new ArrayList<SliceInfo>();
		int tagClass, len = sliceList.size(), i = 0;
		SliceInfo slice;
		while (i < len) {
			slice = sliceList.get(i);
			tagClass = TagUtils.classTag(slice.getTag());
			if(tagClass==0){
				result.add(slice);
			}
			i++;
		}
		return result;
	}
}
