/**
 * 
 */
package com.toroot.TextFetch.entity;


/**
 * @author longkailiang
 *
 */
public class SliceInfo {
 
	private String content;
	
	private String tag;
	
	/**
	 * 直接包含的文字块数量
	 */
	private int textNum = 0;
	
	private String mode = "";
	
	private boolean isNull = true;

	/**
	 * 直接包含的标签数量
	 */
	private int tagNum = 0;
	
	/**
	 *  直接包含的a标签数量
	 */
	private int aTagNum = 0;
	
	/**
	 * 自己的索引位置
	 */
	private int index;
	
	/**
	 * 父标签的索引位置
	 */
	private int pIndex = -1;
	
	/**
	 * 包含的分割符数量，e.g. "|", "-"
	 */
	private int delimiterNum = 0;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public SliceInfo(String content, String tag) {
		super();
		this.content = content;
		this.tag = tag;
	}

	public SliceInfo() {

	}
	
	public int getTextNum() {
		return textNum;
	}

	public void incrTextNum() {
		this.textNum++;
	}

	public int getTagNum() {
		return tagNum;
	}

	public void incrTagNum() {
		this.tagNum++;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public boolean isNull() {
		return isNull;
	}

	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}

	public int getATagNum() {
		return aTagNum;
	}

	public void incrATagNum() {
		this.aTagNum++;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getpIndex() {
		return pIndex;
	}

	public void setpIndex(int pIndex) {
		this.pIndex = pIndex;
	}

	public int getDelimiterNum() {
		return delimiterNum;
	}

	public void incrDelimiterNum() {
		this.delimiterNum++;
	}

	public void Init(){
		this.textNum = 0;
		this.tagNum = 0;
		this.mode = "";
		this.isNull = true;
		this.aTagNum = 0;
	}

	@Override
	public String toString() {
		return content;
	}
	
}
