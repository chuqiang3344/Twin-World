package com.toroot.TextFetch.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.lhcx.datetimeAnalyzer.DateTimeFinder;
import com.lhcx.datetimeAnalyzer.DateTimeInfo;
import com.lhcx.datetimeAnalyzer.DateTimeInfo.DateType;
import com.lhcx.datetimeAnalyzer.DateTimeInfo.TimeType;
import com.lhcx.datetimeAnalyzer.FetchErrorException;

/**
 * 时间工具类
 * 
 * @author mg
 * */
public class DateUtils {
	
	
	private static SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// 提取时间
	public String fetchDateTime(String tempHtml) {
		// 时间过滤类
		DateTimeFinder finder = new DateTimeFinder();
		try {
			// 提取中文时间,会从一段文本中提取很多个时间
			List<DateTimeInfo> dates = finder.findDateTime(tempHtml,
					Locale.SIMPLIFIED_CHINESE);
			// 因为我们是模板爬虫故确定只有一个时间
			if (dates.size() >= 1) {
				return getDateTime(dates.get(0));
			}
		} catch (FetchErrorException e) {
//			e.printStackTrace();
		}
		return null;
	}
	
	private String getDateTime(DateTimeInfo datetime){
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_YEAR);
		int hour = cal.get(Calendar.HOUR);
		int min = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		// 时间部分位绝对时间的情况
		if (datetime.getDateType() == DateType.Absolute) {
			year = datetime.getYear();
			if(year!=-1)
			{
//				if(year-2000<0)
//				{
//					return null;
//				}
				cal.set(Calendar.YEAR, year);
			}
			month = datetime.getMonth();
			if(month!=-1)
			{
				
				cal.set(Calendar.MONTH, month-1);
			}
			day = datetime.getDay();
			if(day!=-1)
			{
				cal.set(Calendar.DAY_OF_MONTH, day);
			}
			if(year==-1 || month==-1 ||day==-1)
			{
				return null;
			}
//			cal.set(year, month, day);
		}
		if (datetime.getDateType() == DateType.Yesterday) {
			cal.add(Calendar.DAY_OF_YEAR, -1);
		}
		if (datetime.getDateType() == DateType.TheDayBeforeYesterday) {
			cal.add(Calendar.DAY_OF_YEAR, -2);
		}
		if (datetime.getDateType() == DateType.DaysBefore) {
			cal.add(Calendar.DAY_OF_YEAR,
					-datetime.getDateRelativeNum());
		}
		if (datetime.getDateType() == DateType.MonthsBefore) {
			cal.add(Calendar.MONTH, -datetime.getDateRelativeNum());
		}
		if (datetime.getDateType() == DateType.YearsBefore) {
			cal.add(Calendar.YEAR, -datetime.getDateRelativeNum());
		}
		if (datetime.getTimeType() == TimeType.Absolute) {
			hour = datetime.getHour();
			min = datetime.getMinute();
			second = datetime.getSecond();
			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, min);
			cal.set(Calendar.SECOND, second);
		}
		if (datetime.getTimeType() == TimeType.HoursBefore) {
			cal.add(Calendar.HOUR_OF_DAY,-datetime.getTimeRelativeNum());
		}
		if (datetime.getTimeType() == TimeType.MinutesBefore) {
			cal.add(Calendar.MINUTE, -datetime.getTimeRelativeNum());
		}
		if (datetime.getTimeType() == TimeType.SecondsBefore) {
			cal.add(Calendar.SECOND, -datetime.getTimeRelativeNum());
		}
		return sf.format(cal.getTime());
	}
	
	/**
	 * 获取格式化后的时间
	 * */
	public String getDateFormat(Date date)
	{
		return sf.format(date);
	}
	
	/**
	 * 获取格式化时间
	 * */
	
	/**
	 * 得到当前时间的3位秒数
	 */
	public String getMilliSecond(){
		String currentTime=String.valueOf(System.currentTimeMillis());
		String ms=currentTime.substring(currentTime.length()-3,currentTime.length());
		return ms;
	}
	
	/**
	 * 两个时间之间相差距离多少
	 * 
	 * @return 相差分钟数
	 */
	public static long getDistanceMine(String publishTime,String createtime) {
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
			mins = diff / 1000 ;
			return mins;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;

	}
	
	public static String getStringTimeFormat(Date date,String dateformat)
	{
		SimpleDateFormat sdf=new SimpleDateFormat(dateformat);
		return sdf.format(date);
	}
	
	public static Date getDataTimeFormat(String date,String dateformat)
	{
		SimpleDateFormat sdf=new SimpleDateFormat(dateformat);
		try {
			Date da=sdf.parse(date);
			return da;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 提取最近的时间
	public String fetchLastDateTime(String tempHtml) {
		// 时间过滤类
		DateTimeFinder finder = new DateTimeFinder();
		try {
			// 提取中文时间,会从一段文本中提取很多个时间
			List<DateTimeInfo> dates = finder.findDateTime(tempHtml,
					Locale.SIMPLIFIED_CHINESE);
			if (dates.size() >= 1) {
				return getDateTime(dates.get(dates.size()-1));
			}
		} catch (FetchErrorException e) {
//			e.printStackTrace();
		}
		return null;
	}
	public static void main(String[] args) {
		DateUtils du = new DateUtils();
		System.out.println(du.fetchDateTime("来源：2014-10-13 13:58中国新闻网"));
	}
}
