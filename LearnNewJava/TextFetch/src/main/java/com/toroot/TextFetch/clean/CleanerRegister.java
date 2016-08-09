/**
 * 
 */
package com.toroot.TextFetch.clean;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

/**
 * 清理器注册
 * @author longkailiang
 *
 */
public class CleanerRegister {
	
	private static Map<String, TagCleaner> tagCleanerMap;
	
	static{
		tagCleanerMap = new HashedMap();
		tagCleanerMap.put("p", new PtagCleaner());
		tagCleanerMap.put("a", new AtagCleaner());
		tagCleanerMap.put("table", new TabletagCleamer());
		tagCleanerMap.put("ul", new UltagCleaner());
		tagCleanerMap.put("strong", new StrongtagCleaner());
		tagCleanerMap.put("span", new SpantagCleaner());
		tagCleanerMap.put("br", new BrTagCleaner());
		tagCleanerMap.put("br/", new BrTagCleaner());
		tagCleanerMap.put("/br", new BrTagCleaner());
		tagCleanerMap.put("b", new BtagCleaner());
		tagCleanerMap.put("em", new EmtagCleaner());
//		tagCleanerMap.put("/p", new PSpecialTagCleaner());
		
	}
	
	public static TagCleaner getCleaner(String tag) {
		return tagCleanerMap.get(tag);
	}
	
	public static void register(String tag, TagCleaner tagCleaner){
		tagCleanerMap.put(tag.toLowerCase().trim(), tagCleaner);
	}
}
