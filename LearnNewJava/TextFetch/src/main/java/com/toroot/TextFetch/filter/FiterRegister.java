/**
 * 
 */
package com.toroot.TextFetch.filter;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

/**
 * 过滤器注册
 * @author longkailiang
 *
 */
public class FiterRegister {
	
	private static Map<String, TagFilter> tagFilterMap;
	
	static{
		tagFilterMap = new HashedMap();
		tagFilterMap.put("script", new ScriptTagFilter());
		tagFilterMap.put("link", new LinkTagFilter());
		tagFilterMap.put("head", new HeadTagFiter());
		tagFilterMap.put("style", new StyleTagFiter());
		tagFilterMap.put("!", new CommentTagFilter());
		tagFilterMap.put("?xml", new CommentTagFilter());
		tagFilterMap.put("noscript", new NoscriptTagFiter());
		tagFilterMap.put("table", new TableTagFilter());
		tagFilterMap.put("select", new SelectTagFilter());
	}
	
	public static TagFilter getFiter(String tag){
		return tagFilterMap.get(tag);
	}
	
	public static void register(String tag, TagFilter tagFilter){
		tagFilterMap.put(tag.toLowerCase().trim(), tagFilter);
	}
}
