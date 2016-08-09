package com.tyaer.basic.datebase.helper;

import com.tyaer.bean.ProxyBean;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mysql操作工具
 * */
public class MysqlHelper extends DatabaseImpl{
	// 驱动信息
	private static final String DRIVER = "com.mysql.jdbc.Driver";

	public MysqlHelper(String USERNAME, String PASSWORD, String URL) {
		super(DRIVER, USERNAME, PASSWORD, URL);
	}

	public static void main(String[] args) throws SQLException {
		MysqlHelper mysqlHelper = new MysqlHelper("root","123456","jdbc:mysql://127.0.0.1:3306/crawlsinaweibo");
//		String sql="select * from hh_v_acd_dutysimple";
//		List<Map<String, Object>> modeResult = mysqlHelper.findModeResult(sql, null);
//		mysqlHelper.showQueryResult(modeResult);
		mysqlHelper.replace("x",new ProxyBean());
	}

	public void replace(String sql,Object obj){
		Class<?> objClass = obj.getClass();
		Field[] fields = objClass.getDeclaredFields();
		ArrayList<Object> grams = new ArrayList<Object>();
		for (int i = 0; i < fields.length; i++) {
			String name = fields[i].getName();
			try {
				Field f = objClass.getDeclaredField(name);
				f.setAccessible(true);
				Object value = f.get(obj);
				grams.add(value);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		System.out.println(grams);
		updateByPreparedStatement(sql,grams);
	}
}
