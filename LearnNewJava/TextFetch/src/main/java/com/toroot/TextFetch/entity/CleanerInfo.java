/**
 * 
 */
package com.toroot.TextFetch.entity;

/**
 * @author longkailiang
 *
 */
public class CleanerInfo {
	
	private SliceInfo sliceInfo;
	
	private int begin;
	
	private int end;

	public CleanerInfo(SliceInfo sliceInfo, int begin, int end) {
		super();
		this.sliceInfo = sliceInfo;
		this.begin = begin;
		this.end = end;
	}

	public SliceInfo getSliceInfo() {
		return sliceInfo;
	}

	public void setSliceInfo(SliceInfo sliceInfo) {
		this.sliceInfo = sliceInfo;
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
