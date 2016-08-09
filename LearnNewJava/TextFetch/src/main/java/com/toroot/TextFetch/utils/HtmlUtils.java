package com.toroot.TextFetch.utils;
/**
 * html工具
 * @author mg
 * */
public class HtmlUtils {
	private static final String[][] filters = new String[][] {
		new String[] { "&lt;", "<" }, new String[] { "&gt;", ">" },
		new String[] { "&.{2,6};|&#.{2,6};", "" }
		};
	
	public static String clearText(String text)
	{
		for (String[] filter : filters) {
			text = text.replaceAll(filter[0], filter[1]);
		}
		return text;
	}
	 /**
	    * 判断是否是中文字符
	    * @param codePoint
	    * @return
	    */
	   protected static boolean isChineseChar(int codePoint){
		   Character.UnicodeScript sc = Character.UnicodeScript.of(codePoint);
	       if (sc == Character.UnicodeScript.HAN) {
	           return true;
	       }
		    return false;
		}
	   /**
	    * 判断是否有乱码
	    * @param value
	    * @return
	    */
	   public static boolean isGarbled(String value){
		   int count=value.codePointCount(0,value.length());
			for(int i=0;i<count;i++){
				int codePoint = value.codePointAt(i);
				if(!isChineseChar(codePoint)){
					return true;
				}
			}
			return false;
	   }
	   /**
	    * 获取中文字符长度
	    * @param value
	    * @return
	    */
	   public static int getChineseCharCount(String value){
			int count=value.codePointCount(0,value.length());
			int len=0;
			for(int i=0;i<count;i++){
				int codePoint = value.codePointAt(i);
				if(isChineseChar(codePoint)){
					len++;
				}
			}
			return len;
		}
	   /**
	    * 去除段落2端杂质
	    * */
	   public static String trimWhitespace(String str ) {
	        int len = str.length();
	        int st = 0;
	        while ((st < len) && (Character.isWhitespace(str.charAt(st)))) {
	            st++;
	        }
	        while ((st < len) && (Character.isWhitespace(str.charAt(len-1)))) {
	            len--;
	        }
	        return ((st > 0) || (len < str.length())) ? str.substring(st, len) : str.toString();
	    }
	   
	   public static String trimWhitespace(StringBuffer str ) {
	        int len = str.length();
	        int st = 0;
	        while ((st < len) && (Character.isWhitespace(str.charAt(st)))) {
	            st++;
	        }
	        while ((st < len) && (Character.isWhitespace(str.charAt(len-1)))) {
	            len--;
	        }
	        return ((st > 0) || (len < str.length())) ? str.substring(st, len) : str.toString();
	    }
	   
		/**
		 * 判断是否控制字符串
		 * @param str
		 * @return
		 */
	    public static boolean isWhitespace(String str){
			char[] seq = str.toCharArray();
			for (char c : seq) {
				if(!Character.isWhitespace(c)) return false;
			}
			return true;
		}
		
		/**
		 * 清除控制字符串
		 * @param str
		 * @return
		 */
	    public static String delWhitespace(StringBuffer str){
			StringBuffer ret = new StringBuffer();
			int len = str.length();
			char c;
			for (int i=0; i<len; i++) {
				c = str.charAt(i);
				if(!Character.isWhitespace(c)) ret.append(c);
			}
			return ret.toString();
		}
	    
	   /**
	    * 获取中文字符长度
	    * @param value
	    * @return
	    */
	   public static boolean hasChineseChar(String value){
			int count=value.codePointCount(0,value.length());
			for(int i=0;i<count;i++){
				int codePoint = value.codePointAt(i);
				if(isChineseChar(codePoint)){
					return true;
				}
			}
			return false;
		}
	   
	   /**
	    * 文本是否包字符数字
		* @param text
	    * @return
	    */
	   public static boolean hasLettersOrNumbers(String text){
		   int len = text.length(),i = 0;
		   char c;
		   while (i<len) {
			   c = text.charAt(i);
			   if(Character.isLetter(c)||Character.isDigit(c))
				   return true;
			   i++;
		   }
		   return false;
	   }
	   
	    
	    /**
	     * 获取文本的前N句的位置
	     * @param text
	     * @param num
	     * @return
	     */
	    public static int getFirstNSentencesPos(String text, int num){
	    	if(num<1) return -1;
	    	int len = text.length(), i=0, senNum = 0;
	    	char c;
	    	boolean begin = false;
	    	StringBuilder sb = new StringBuilder();
	    	while(i<len){
	    		c = text.charAt(i);    		
	    		if (sb.length() == 0 && (Character.isWhitespace(c) || c == ' ')) {
					continue;
				}
	    		
	    		if(Character.isLetterOrDigit(c)){
	    			if(!begin) begin = true;
					sb.append(c);
	    		}

				switch (c) {
					case '\t':
					case '。':
					case ';':
					case '；':
					case '!':
					case '！':
					case '?':
					case '？':
					case '\n':
					case '\r':
						if(begin&&sb.length()>0) senNum++;
						sb.setLength(0);
						break;
				}
				if(senNum==num-1) break;
	    		i++;
	    	}
	    	
	    	if(i==len) i--;
	    	if(senNum<num-1) i=-1;
	    	
	    	return i;
	    }
	    
	    /**
	     * 获取文本的后N句的位置
	     * @param text
	     * @param num
	     * @return
	     */
	    public static int getLastNSentencesPos(String text, int num){
	    	int len = text.length()-1;
	    	StringBuffer sb=new StringBuffer(text);
	    	int index  = getFirstNSentencesPos(sb.reverse().toString(), num);
	    	if(index==-1) return -1;
	    	return len-index;
	    }
	   
}
