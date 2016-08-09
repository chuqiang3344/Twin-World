package com.tyaer.basic.utils;

import com.tyaer.basic.datebase.helper.MysqlHelper;
import com.tyaer.basic.net.helper.HttpHelper;
import com.tyaer.basic.net.helper.ProxyHelper;
import com.tyaer.basic.net.httptools.HttpClientManager;
import com.tyaer.bean.DTO;
import com.tyaer.bean.HttpResutBean;
import com.tyaer.bean.ProxyBean;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 
 * @author Twin
 *
 */
public class TestProxyUtils {
//	private static final String detectProxyUrl = "http://1212.ip138.com/ic.asp";
	public static final String detectProxyUrl = "http://1212.ip138.com/ic.asp";
	private static HttpHelper httpHelper=new HttpHelper();
	private static ProxyHelper proxyHelper=new ProxyHelper();
//	String url = "http://guba.eastmoney.com/list,600036.html";
	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static String hostAddress;
	static {
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			 hostAddress = localHost.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 随机获取一个代理测试
	 */
	private final static String website_proxy_sql = "select * from proxy where status=0";

	public void randomTesting() throws Exception {
		HttpHelper httpHelper = new HttpHelper();
		List<ProxyBean> proxyCache = proxyHelper.getProxyInfo(website_proxy_sql);
		HttpHost httpHost = proxyHelper.getRandomProxy(proxyCache);
		System.out.println(httpHelper.sendRequest(detectProxyUrl, httpHost));
	}

	/**
	 * 定向测试代理
	 * @param ip
	 * @param port
	 * @throws Exception
	 */
	public static String testProxy(String ip, int port) {
		String result;
		HttpHost proxy = new HttpHost(ip, port);
		HttpHelper httpHelper = new HttpHelper();
		long startTime = System.currentTimeMillis();
		HttpResutBean httpResutBean = httpHelper.sendRequest(detectProxyUrl, proxy);
		long time = System.currentTimeMillis()-startTime;
		System.out.println(httpResutBean);
		if(httpResutBean!=null){
			int statusCode = httpResutBean.getStatusCode();
			if (statusCode==200) {
				result=ip+"\t"+port+"\t"+"可用"+"\t"+sdf.format(startTime)+"\t"+time;
			}else{
				result=ip+"\t"+port+"\t"+"不可用"+"\t"+sdf.format(startTime)+"\t"+time;
			}
		}else{
			result=ip+"\t"+port+"\t"+"不可用"+"\t"+sdf.format(startTime)+"\t"+time;
		}
		return result;
	}

	/**
	 * 定向测试代理
	 * @param ip
	 * @param port
	 * @throws Exception
	 */
	public static HttpResutBean DetectProxy(String ip, int port) {
		HttpHost proxy = new HttpHost(ip, port);
		long startTime = System.currentTimeMillis();
		HttpResutBean httpResutBean = httpHelper.sendRequest(detectProxyUrl, proxy);
		long time = System.currentTimeMillis()-startTime;
		return httpResutBean;
	}

	/**
	 * 删除代理,status=0
	 */
	private final static String delete_proxy_sql = "delete from proxy where status=0";
	public static void deleteProxy(){
		MysqlHelper mysqlHelper= new MysqlHelper("","","");
		try {
			mysqlHelper.executeSql(delete_proxy_sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void testFileProxy(String url){
		StringBuilder sb = new StringBuilder();
		try {
			List<String> list = FileUtils.readLines(new File(url), "utf-8");
			System.out.println(hostAddress);
			for (String s : list) {
				String ip = s.split(":")[0];
				int port = Integer.valueOf( s.split(":")[1]);
				String result = testProxy(ip, port);
				System.out.println(result);
				sb.append(result+"\n");
				System.out.println("-------------------------------------------------------------");
			}
			System.out.println("ip"+"\t"+"port"+"\t"+"status"+"\t"+"检测时间"+"\t"+"响应速度");
			System.out.println(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		/*
		 * 测试数据库中的全部代理
		 */
//		proxyhelper.TestProxyForSql(DTO._PROXYHELPER.PROXY_SQL_1);
//		deleteProxy();
		/*
		 * 从数据库获取代理，随机测试
		 */
//		utils.randomTesting();
		/*
		 * 定向测试
		 */
		 String ip = "119.41.113.69";
		 int port = 8888;
		String s = testProxy(ip, port);
		System.out.println(s);


//		testFileProxy("./file/proxy.txt");

//		 HttpHost httpHost=new HttpHost(ip, port);
//		 String url="http://guba.eastmoney.com/list,600028.html";
//		 HttpResutBean result = httpHelper.sendRequest(url, httpHost);
//		 System.out.println(result);
		/*
		 * 删除无效代理
		 */
//		proxyhelper.deleteProxy();
//		proxyhelper.clearFailProxy();
	}
}
