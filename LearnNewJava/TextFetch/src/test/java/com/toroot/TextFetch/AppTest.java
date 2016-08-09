//package com.toroot.TextFetch;
//
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import com.toroot.TextFetch.utils.HtmlUtils;
//import com.toroot.TextFetch.utils.TagUtils;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//
///**
// * Unit test for simple App.
// */
//public class AppTest {
//    private static Pattern sourcePatt = Pattern
//            .compile(".*?[关键字|标签]\\s?[:：]\\s?((\\S*\\s*){1,3})\\s");
//
//    public static void main(String[] args) {
//        String textStr = "标签： 群星   香港证监会  纸业  ";
//        Matcher matcher = sourcePatt.matcher(textStr);
//        boolean b = matcher.find();
//        if (b && matcher.groupCount() >= 1) {
//            System.out.println(matcher.group(1));
//        }
//
//        Pattern sourcePatt = Pattern
//                .compile(".*?[来源|来自]\\s?[:：]\\s?(\\S*)\\s{0,1}");
//        matcher = sourcePatt.matcher("来源:");
//        b = matcher.find();
//        if (b && matcher.groupCount() >= 1) {
//            System.out.println(matcher.group(1));
//        }
//
//        System.out.println(Character.isWhitespace('='));
//    }
//}
