package com.tyaer.basic.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 时间工具类
 *
 * @author mg
 */
public class DateUtils {

    private static SimpleDateFormat sf = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    /**
     * 获取格式化后的时间
     */
    public String getDateFormat(Date date) {
        return sf.format(date);
    }

    /**
     * 获取格式化时间
     * */

    /**
     * 得到当前时间的3位秒数
     */
    public String getMilliSecond() {
        String currentTime = String.valueOf(System.currentTimeMillis());
        String ms = currentTime.substring(currentTime.length() - 3,
                currentTime.length());
        return ms;
    }

    /**
     * 两个时间之间相差距离多少
     *
     * @return 相差分钟数
     */
    public long getDistanceMine(String publishTime, String createtime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date one = new Date();
            if (publishTime != null) {
                one = df.parse(publishTime);
            }
            Date two = df.parse(createtime);
            long mins = 0;
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            mins = diff / 1000;
            return mins;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 比较两个时间
     *
     * @return 前者大返回fasle，后者大返回true
     */
    public boolean compareTime(String publishTime, String createtime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date one = new Date();
            if (publishTime != null) {
                one = df.parse(publishTime);
            }
            Date two = df.parse(createtime);
            long time1 = one.getTime();
            long time2 = two.getTime();
            if (time1 > time2) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 比较两个时间
     *
     * @return 前者大返回1，后者大返回-1
     */
    public int compareToTime(String publishTime, String createtime) {
        int result = 0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date ptime = df.parse(publishTime);
            Date ctime = df.parse(createtime);
            Calendar Calendar1 = Calendar.getInstance();
            Calendar1.setTime(ptime);
            Calendar Calendar2 = Calendar.getInstance();
            Calendar2.setTime(ctime);
            result = Calendar1.compareTo(Calendar2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getStringTimeFormat(Date date, String dateformat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
        return sdf.format(date);
    }

    public static Date getDataTimeFormat(String date, String dateformat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateformat);
        try {
            Date da = sdf.parse(date);
            return da;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 由时间戳转变成时间
    public static String getTimeByStamp(String timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(new Date(Long.parseLong(timeStamp)));
        return date;
    }

    /**
     * 获取当前时间的前一天时间
     *
     * @param cl
     * @return
     */
    public static Calendar getBeforeDay(Calendar cl) {
        // 使用roll方法进行向前回滚
        // cl.roll(Calendar.DATE, -1);
        // 使用set方法直接进行设置
        int day = cl.get(Calendar.DATE);
        cl.set(Calendar.DATE, day - 1);
        return cl;
    }

    /**
     * 获取当前时间的后一天时间
     *
     * @param cl
     * @return
     */
    public static Calendar getAfterDay(Calendar cl) {
        // 使用roll方法进行回滚到后一天的时间
        // cl.roll(Calendar.DATE, 1);
        // 使用set方法直接设置时间值
        int day = cl.get(Calendar.DATE);
        cl.set(Calendar.DATE, day + 1);
        return cl;
    }

    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";


    public static String beforeTime(Date date) {
        long delta = new Date().getTime() - date.getTime();
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 48L * ONE_HOUR) {
            return "昨天";
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
        }
    }

    private static final DateFormat[] ACCEPT_DATE_FORMATS = {
            new SimpleDateFormat("dd/MM/yyyy"),
            new SimpleDateFormat("yyyy-MM-dd"),
            new SimpleDateFormat("yyyy/MM/dd"),
            new SimpleDateFormat("yyyy年MM月dd日 HH:mm")}; // 支持转换的日期格式
    static SimpleDateFormat sdf_Normal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static SimpleDateFormat sdf_ZH = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    private static Pattern pattern=Pattern.compile("\\d+");
    public static Date manifoldTimeToNormal(String manifoldTime) {
        Date pubDate=null;
        for (DateFormat format : ACCEPT_DATE_FORMATS) {
            try {
                return format.parse(manifoldTime);// 遍历日期支持格式，进行转换
            } catch (Exception e) {
                continue;
            }
        }
        if(manifoldTime.contains("前")){
            Matcher matcher = pattern.matcher(manifoldTime);
            int n=0;
            if(matcher.find()){
                n=Integer.valueOf(matcher.group());
            }
            Calendar calendar=Calendar.getInstance();
            if(manifoldTime.contains(ONE_HOUR_AGO)){
                calendar.set(Calendar.HOUR_OF_DAY,calendar.get(Calendar.HOUR_OF_DAY)-n);
            }else if(manifoldTime.contains(ONE_MINUTE_AGO)) {
                calendar.set(Calendar.MINUTE,calendar.get(Calendar.MINUTE)-n);
            }
            pubDate = calendar.getTime();
        }
        return pubDate;
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }

    public static void main(String[] args) throws ParseException {
//		DateUtils du = new DateUtils();
//		// System.out.println(du.fetchDateTime("07:36"));
//		String a = "2015-10-14 23:46:45";
//		String b = "2015-10-14 00:11:13";
//		boolean c = du.compareTime(a, b);
//		System.out.println(c);
//		int d = du.compareToTime(a, b);
//		System.out.println(d);
//        System.out.println(sf.format(new Date()));
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
//        Date date = format.parse("2013-11-11 18:35:35");
//        System.out.println(beforeTime(date));
//        System.out.printf(beforeTime(new Date()));
        String s=" 2016年08月08日 08:05";
        Date s1 = manifoldTimeToNormal(s);
        System.out.printf(sdf_Normal.format(s1));
    }
}
