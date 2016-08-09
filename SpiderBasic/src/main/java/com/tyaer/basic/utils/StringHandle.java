package com.tyaer.basic.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Twin on 2016/8/1.
 */
public class StringHandle {

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

    public static void main(String[] args) {
        StringHandle stringHandle = new StringHandle();
        String[] strings = stringHandle.praseRegex("<center>您的IP是：[183.61.236.54] 来自：广东省东莞市 电信</center>", "IP是：\\[(.*?)\\] 来自：(.+?) (.+?)</center>");
        System.out.println(ArrayUtils.toString(strings));
    }

}
