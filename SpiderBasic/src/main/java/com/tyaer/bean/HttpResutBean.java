package com.tyaer.bean;

/**
 * 
 * @author Twin
 *
 */
public class HttpResutBean {
	/**
	 * 返回码
	 */
	private int statusCode;
	/**
	 * 字符编码
	 */
	private String charset;
	/**
	 * html
	 */
	private String html;

	/**
	 * message
	 */
	private  String message;

	public HttpResutBean() {
		super();
	}

	public HttpResutBean(int statusCode, String charset, String html) {
		super();
		this.statusCode = statusCode;
		this.charset = charset;
		this.html = html;
	}

	public HttpResutBean(int statusCode, String charset, String html, String message) {
		this.statusCode = statusCode;
		this.charset = charset;
		this.html = html;
		this.message = message;
	}

	public String getCharset() {
		return charset;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	@Override
	public String toString() {
		return "HttpReturnBean [statusCode=" + statusCode + ", charset="
				+ charset + ", html=" + html + "]";
	}
}
