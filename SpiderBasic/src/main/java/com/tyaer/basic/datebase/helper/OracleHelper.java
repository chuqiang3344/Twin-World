package com.tyaer.basic.datebase.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class OracleHelper extends DatabaseImpl{
	// 驱动信息
	// private static final String DRIVER = DTO.OracleDriver;
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";

	// 数据库地址
	private String url;
	// 数据库用户名
	private String userName;
	// 数据库密码
	private String passWord;

	public OracleHelper(String USERNAME, String PASSWORD, String URL) {
		super(DRIVER, USERNAME, PASSWORD, URL);
	}

	/**
	 * 获得数据库的连接
	 * 
	 */
	public Connection getConnection(String url, String username, String password) {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			System.out.println("数据库连接失败！！");
			e.printStackTrace();
		}
		return connection;
	}

	public void showQueryResult(List<Map<String, Object>> list) {
		for (Object obj : list) {
			System.out.println(obj.toString());
		}
	}
	public static void main(String[] args) {
		OracleHelper oracleHelper = new OracleHelper("1","2","3");
	}
	
}
