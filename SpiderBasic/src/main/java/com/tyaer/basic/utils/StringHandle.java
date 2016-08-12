package com.tyaer.basic.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Twin on 2016/8/1.
 */
public class StringHandle {

    /**
     * 使用regex截取
     * @param str
     * @param regex
     * @return
     */
    public String[] praseRegex(String str,String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        int groupCount = matcher.groupCount();
        String[] result=new String[groupCount];
        while(matcher.find()){
            for (int i = 1; i < groupCount+1; i++) {
                result[i-1] = matcher.group(i);
            }
        }
        return result;
    }

    /**
     * 判断字符是否是中文
     *
     * @param c 字符
     * @return 是否是中文
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否是乱码
     *
     * @param strName 字符串
     * @return 是否是乱码
     */
    public static boolean isMessyCode(String strName) {
        Pattern p = Pattern.compile("\\s*|\\t*|\\r*|\\n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = ch.length;
        float count = 0;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    count = count + 1;
                }
            }
        }
        float result = count / chLength;
        if (result > 0.4) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 判断是否为汉字
     *
     * @param str
     * @return
     */
    public static boolean isGBK(String str) {
        char[] chars = str.toCharArray();
        boolean isGBK = false;
        for (int i = 0; i < chars.length; i++) {
            byte[] bytes = ("" + chars[i]).getBytes();
            if (bytes.length == 2) {
                int[] ints = new int[2];
                ints[0] = bytes[0] & 0xff;
                ints[1] = bytes[1] & 0xff;
                if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40
                        && ints[1] <= 0xFE) {
                    isGBK = true;
                    break;
                }
            }
        }
        return isGBK;
    }

    /**
     * 判断是否为乱码
     *
     * @param str
     * @return
     */
    public static boolean isMessyCode2(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            // 当从Unicode编码向某个字符集转换时，如果在该字符集中没有对应的编码，则得到0x3f（即问号字符?）
            //从其他字符集向Unicode编码转换时，如果这个二进制数在该字符集中没有标识任何的字符，则得到的结果是0xfffd
            //System.out.println("--- " + (int) c);
            if ((int) c == 0xfffd) {
                // 存在乱码
                System.out.println("存在乱码 " + (int) c);
                return true;
            }
        }
        return false;
    }


    /**
     * 判断字符串是否为双整型数字
     *
     * @param str
     * @return
     */
    public static boolean isDouble(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        Pattern p = Pattern.compile("-*\\d*.\\d*");
        // Pattern p = Pattern.compile("-*"+"\\d*"+"."+"\\d*");
        return p.matcher(str).matches();
    }

    /**
     * 判断字符串是否为整字
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        Pattern p = Pattern.compile("-*\\d*");
        return p.matcher(str).matches();
    }

    /**
     * 判断字符串是否为整字
     *
     * @Object object
     * @return
     */
    public static boolean isNumeric(Object object) {
        String str = object.toString();
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str)
    {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        StringHandle stringHandle = new StringHandle();
//        String[] strings = stringHandle.praseRegex("<center>您的IP是：[183.61.236.54] 来自：广东省东莞市 电信</center>", "IP是：\\[(.*?)\\] 来自：(.+?) (.+?)</center>");
//        System.out.println(ArrayUtils.toString(strings));

        System.out.println(isMessyCode("Ã©Å¸Â©Ã©Â¡ÂºÃ¥Â¹Â³"));
        System.out.println(isMessyCode2("杈撳叆鐨勫弬鏁颁笉瀹屾暣锛岃　杈撳叆鐢ㄦ埛鍚?!"));
        System.out.println(isMessyCode("你好"));
    }

}
