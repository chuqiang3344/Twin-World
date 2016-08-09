package com.tyaer.bean;

import com.tyaer.basic.datebase.helper.MysqlHelper;
import com.tyaer.basic.net.helper.ProxyHelper;
import com.tyaer.basic.net.helper.WebClientUtils;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DTO {

	/**
	 * 本地：项目工程的根目录
	 */
	public static final String root=System.getProperty("user.dir");
	
	/**
	 * log4j的配置文件
	 */
	public static final String log4jProperties=root+"/configure/log4j.properties";
	
	/**
	 * 配置文件目录
	 */
	public static final String configure=root+"/configure/pro.properties";

	public static String MysqlUsername = "root";
	public static String MysqlPassword = "123456";
	public static String MysqlDriver = "com.mysql.jdbc.Driver";
	public static String RedisIp = "192.168.1.58";
	public static String RedisPwd = "hainiu";
	public static int RedisPort = 6379;

	/**
	 * 正式运行
	 */
	public static String PROCESSNAME = "FORMAL PROCESS";
	public static String MysqlUrl = "jdbc:mysql://192.168.1.51:3306/crawlInfo";//还需切换配置文件中的值
	public static String _STR_REDISINDEXQUEUE="IndexCrawl";
	public static String _STR_REDISCONTENTQUEUE="ContentCrawl";
	
	/**
	 * 测试运行
	 */
//	public static String PROCESSNAME = "TEST PROCESS";
//	public static String MysqlUrl = "jdbc:mysql://192.168.1.51:3306/crawlInfoTest";//还需切换配置文件中的值
//	public static String _STR_REDISINDEXQUEUE="IndexCrawlTest";
//	public static String _STR_REDISCONTENTQUEUE="ContentCrawlTest";

	
	static {
		//加载log4j的配置文件
		PropertyConfigurator.configure(log4jProperties);
		Properties pro = new Properties();
		InputStream in = null;
		try {
			in = new FileInputStream(new File(configure));
			pro.load(in);
			if (pro != null) {
				DTO.RedisIp = pro.getProperty("REDISIP");
				DTO.RedisPort = Integer.parseInt(pro.getProperty("REDISPORT"));
				DTO.RedisPwd = pro.getProperty("REDISPWD");
				DTO.MysqlDriver=pro.getProperty("MYSQLDRIVER");
				DTO.MysqlUrl = pro.getProperty("MYSQLURL");
				DTO.MysqlUsername = pro.getProperty("MYSQLUSERNAME");
				DTO.MysqlPassword = pro.getProperty("MYSQLPASSWORD");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	/**
	 * cookie操作工具
	 */
	public final static WebClientUtils _WebClientUtils = new WebClientUtils();
	 
	 /**
	  * redis操作工具
	  */
//	public final static RedisHelper _REDISHELPER=new RedisHelper();

	/**
	 * mysql数据库操作工具
	 */
	public final static MysqlHelper _MYSQLHELPER = new MysqlHelper("","","");
	
	/**
	 * Proxy工具类
	 * */
	public final static ProxyHelper _PROXYHELPER = new ProxyHelper();
}
