/**
 * 
 */
package com.toroot.TextFetch.entity;

/**
 * @author longkailiang
 *
 */
public class TagInfo {
    
	private String tag;
	
	private int begin;
	
	private int end;

	public TagInfo(String tag, int begin, int end) {
		super();
		this.tag = tag;
		this.begin = begin;
		this.end = end;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
	
}
