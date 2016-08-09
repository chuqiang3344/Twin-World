/**
 * 
 */
package com.toroot.TextFetch.custom;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.toroot.TextFetch.entity.SliceInfo;

/**
 * 定制处理接口
 * @author longkailiang
 *
 */
public interface IcustomProcess {
	
	static Set<String> tagExclude= new HashSet<String>(Arrays.asList("br", "br/"));
	
	/**
	 * 定制处理
	 * @param sliceList
	 * @return
	 */
	List<SliceInfo> process(List<SliceInfo> sliceList);
	
}
