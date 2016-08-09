/**
 * 
 */
package com.toroot.TextFetch.filter;

/**
 * @author longkailiang
 *
 */
public abstract class TagFilter {
	
	/**
	 * 从begin位置开始过滤seq中tag标签所包含的内容
	 * @param seq
	 * @param begin
	 * @return
	 */
	public abstract int fiter(char[] seq, int begin);
	
}
