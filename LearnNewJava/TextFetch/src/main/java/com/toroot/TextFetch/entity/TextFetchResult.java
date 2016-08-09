/**
 * 
 */
package com.toroot.TextFetch.entity;

/**
 * @author John
 *
 */
public class TextFetchResult {
	private String pulishtime=null;
	
	private String content=null;
	
	private String source=null;
	
	private String author=null;
	
	private String keyword=null;
	
	public TextFetchResult(String pulishtime, String content, String source,
			String author, String keyword) {
		this.pulishtime = pulishtime;
		this.content = content;
		this.source = source;
		this.author = author;
		this.keyword = keyword;
	}

	public String getPulishtime() {
		return pulishtime;
	}

	public void setPulishtime(String pulishtime) {
		this.pulishtime = pulishtime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Override
	public String toString() {
		return "NewsResut [\r\npulishtime=" + pulishtime + "\r\n content=" + content
				+ "\r\n source=" + source + "\r\n author=" + author + "\r\n keyword="
				+ keyword + "\r\n]";
	}
}
