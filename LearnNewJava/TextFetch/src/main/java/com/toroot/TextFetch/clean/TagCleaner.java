/**
 * 
 */
package com.toroot.TextFetch.clean;

import java.util.List;

import com.toroot.TextFetch.entity.CleanerInfo;
import com.toroot.TextFetch.entity.SliceInfo;



/**
 * @author longkailiang
 *
 */
public abstract class TagCleaner {
	
	/**
	 * 从begin位置开始清除list中tag标签所包含的所有标签
	 * @param seq
	 * @param begin
	 * @return
	 */
	public abstract CleanerInfo clean(List<SliceInfo> list, int begin);
	
}
