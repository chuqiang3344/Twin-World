package com.tyaer.basic.json;

import com.tyaer.basic.net.helper.HttpHelper;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.*;

/**
 * json解析器
 * @author Twin
 */
public class JsonHelper {
	static HttpHelper httpHelper=new HttpHelper();

	/** JSONObject */
	private static final String JSON_TYPE_OBJECT = "JSON_TYPE_OBJECT";
	/** JSONArray */
	private static final String JSON_TYPE_ARRAY = "JSON_TYPE_ARRAY";
	/** 不是JSON格式的字符串 */
	private static final String JSON_TYPE_ERROR = "JSON_TYPE_ERROR";

	public static String getJsonStr(String JsonInput) {
		String jsonStr = null;
		if (JsonInput.contains("{")) {	
			// 1、输入的就是json字符串
			jsonStr = JsonInput;
		} else if (JsonInput.toLowerCase().contains("http")) {
			// 2、从HTTP获取jsonString
//			jsonStr = HttpHelper.sendRequest(JsonInput);
//			String cookie="emstat_bc_emcount=27495646272011548984; st_pvi=71524286922067; st_si=41084968019276; emstat_ss_emcount=14_1460021485_218161544; HAList=a-sh-601258-%u5E9E%u5927%u96C6%u56E2%2Ca-sh-603993-%u6D1B%u9633%u94BC%u4E1A%2Ca-sh-601608-%u4E2D%u4FE1%u91CD%u5DE5%2Ca-sz-000005-%u4E16%u7EAA%u661F%u6E90%2Cf-0-000001-%u4E0A%u8BC1%u6307%u6570%2Ca-sz-000002-%u4E07%u79D1A%2Ca-sh-600636-%u4E09%u7231%u5BCC%2Ca-sz-002570-%u8D1D%u56E0%u7F8E; em_hq_fls=old";
			jsonStr = httpHelper.sendRequest(JsonInput);
		} else {
			// 3、从文件获取jsonString
//			jsonStr = FileHelper.readFile("./configure/jsRes");
			try {
				jsonStr = FileUtils.readFileToString(new File("./configure/jsRes"),"utf-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(jsonStr.contains("(")){
			jsonStr=jsonStr.substring(jsonStr.indexOf("{"), jsonStr.lastIndexOf("}") + 1);
		}
		return jsonStr;
	}

	/**
	 * 根据Jpath语法，定位json中的字段信息，若jsonStr为单个json，则list的size为1
	 * 
	 * @param jsonStr
	 * @param Jpath
	 *            *>title
	 * @return
	 */
	public static List<Object> useJpathGetOneInfo(String jsonStr, String Jpath) {
		if (jsonStr != null && !jsonStr.equals("") && Jpath != null
				&& !Jpath.equals("")) {
			List<Object> resultList = new ArrayList<Object>();
			Map<String, Object> jsonMap = JsonHelper.toJsonMap(jsonStr);
			// showJonMap(jsonMap);
			String[] Jpaths = Jpath.split(">");
			resultList = JsonHelper
					.recursionSearch(resultList, jsonMap, Jpaths);
			return resultList;
		} else {
			return null;
		}
	}

	/**
	 * 根据Jpath获取多个属性 多个属性必须属于同一级下！
	 * 
	 * @param jsonStr
	 * @param Jpath
	 *            = "list>*>user>screen_name,description";
	 * @return
	 */
	public static List<Map<String, Object>> useJpathGetManyInfo(String jsonStr,
			String Jpath) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		// 切割为路径和字段数组
		String[] Jpaths = null, keys = null;
		if (Jpath.contains(">")) {
			Jpaths = Jpath.substring(0, Jpath.lastIndexOf(">")).split(">");
			keys = Jpath.substring(Jpath.lastIndexOf(">") + 1).split(",");
		} else {
			keys = Jpath.split(",");
		}
		List<Object> mapList = new ArrayList<Object>();
		Map<String, Object> jsonMap = JsonHelper.toJsonMap(jsonStr);
		// showJonMap(jsonMap);
		if (Jpaths != null) {
			mapList = recursionSearch(mapList, jsonMap, Jpaths);
		} else {
			mapList.add(jsonMap);
		}
		// System.out.println(mapList);
		for (Object jsonObj : mapList) {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			if (jsonObj instanceof Map) {
				Map<String, Object> map = (Map<String, Object>) jsonObj;
				for (int i = 0; i < keys.length; i++) {
					Object value = searchJsonMap(map, keys[i]);
					if (value != null) {
						resultMap.put(keys[i], value);
					} else {
						resultMap = null;
					}
				}
			} else {
				resultMap = null;
			}
			resultList.add(resultMap);
		}

		return resultList;
	}

	/**
	 * 根据Jpath获取多个属性！
	 * 
	 * @param jsonStr
	 * @param Jpath
	 *            = "list>*>description,source,timeBefore|user>screen_name";
	 * @return
	 */
	public static List<Map<String, Object>> useJpathGetInfo(String jsonStr,
			String Jpath) {
        jsonStr = getJsonStr(jsonStr);
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		System.out.println("Jpath:"+Jpath);
		if (Jpath.contains("|")) {
			int split = Jpath.substring(0, Jpath.indexOf("|")).lastIndexOf(">");
			String com = Jpath.substring(0, split + 1);
			Jpath = Jpath.substring(split + 1, Jpath.length());
			String[] jpathProcess = Jpath.split("\\|");
			List<List<Map<String, Object>>> processList = new ArrayList<List<Map<String, Object>>>();
			int len = 0;
			for (int i = 0; i < jpathProcess.length; i++) {
				String jpath = com + jpathProcess[i];
				System.out.println(jpath);
				List<Map<String, Object>> currentProcessResult = useJpathGetManyInfo(
						jsonStr, jpath);
				len = currentProcessResult.size();
				processList.add(currentProcessResult);
			}
			for (int i = 0; i < len; i++) {
				Map<String, Object> mergerMap = new HashMap<String, Object>();
				for (List<Map<String, Object>> list : processList) {
					Map<String, Object> map=list.get(i);
					if(map!=null){
						mergerMap.putAll(map);
					}
				}
				resultList.add(mergerMap);
			}
		}else{
			resultList=useJpathGetManyInfo(jsonStr, Jpath);
		}
		return resultList;
	}

	/**
	 * 递归搜索数据
	 * 
	 * @param resultList
	 * @param jsonMap
	 * @param Jpaths
	 * @return
	 */
	public static List<Object> recursionSearch(List<Object> resultList,
			Map<String, Object> jsonMap, String[] Jpaths) {
		int JpLen = Jpaths.length;
		Object obj;
		for (int i = 0; i < Jpaths.length; i++) {
			String key = Jpaths[i];
			obj = searchJsonMap(jsonMap, key);
			// System.out.println(jsonMap);
			// System.out.println("步骤" + i + ", " + key + "：" + obj);
			// System.out.println(resultList);
			if (obj != null && i != JpLen - 1) {
				if (obj.equals(jsonMap)) {
					// 截取数组
					Jpaths = Arrays.copyOfRange(Jpaths, i + 1, JpLen);
					for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
						jsonMap = (Map<String, Object>) entry.getValue();
						resultList = recursionSearch(resultList, jsonMap,
								Jpaths);
					}
					break;
				} else if (obj instanceof Map) {
					jsonMap = (Map<String, Object>) obj;
				} else if (obj instanceof String && i == JpLen - 1) {
					resultList.add(obj);
				}
			} else if (obj != null && obj.equals(jsonMap) && i == JpLen - 1) {
				// 如果是*则增加所有的map
				for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
					resultList.add(entry.getValue());
				}
			} else {
				// 如果最后Jpath搜索完毕还不是字符串，则添加到结果NULL。
				resultList.add(obj);
				break;
			}
		}
		return resultList;
	}

	public static Object searchJsonMap(Map<String, Object> jsonMap, String key) {
		Object result = null;
		if (key.equals("*")) {
			result = jsonMap;
		} else {
			result = jsonMap.get(key);
		}
		return result;
	}

	/**
	 * 显示Map
	 * 
	 * @param jsonMap
	 */
	public static void showJonMap(Map<String, Object> jsonMap) {
		System.out.println("Map:" + jsonMap);
		for (Map.Entry<String, Object> entry : jsonMap.entrySet()) {
			System.out.println(entry.getKey() + "：" + entry.getValue());
			Object obj = entry.getValue();
			if (obj instanceof Map) {
				showJonMap((Map) obj);
			}
		}
	}

	/**
	 * 将Javabean转换为Map
	 * 
	 *            javaBean
	 * @return Map对象
	 */
	public static String getJSONType(String str) {
		if (StringUtils.isEmpty(str)) {
			return JsonHelper.JSON_TYPE_ERROR;
		}
		final char[] strChar = str.substring(0, 1).toCharArray();
		final char firstChar = strChar[0];
		if (firstChar == '{') {
			return JsonHelper.JSON_TYPE_OBJECT;
		} else if (firstChar == '[') {
			return JsonHelper.JSON_TYPE_ARRAY;
		} else {
			return JsonHelper.JSON_TYPE_ERROR;
		}
	}

	/**
	 * 将Json对象转换成Map
	 * 
	 * @return Map对象
	 * @throws JSONException
	 */
	public static Map toMap(Object javaBean) {
		Map result = new HashMap();
		Method[] methods = javaBean.getClass().getDeclaredMethods();
		for (Method method : methods) {
			try {
				if (method.getName().startsWith("get")) {

					String field = method.getName();
					field = field.substring(field.indexOf("get") + 3);
					field = field.toLowerCase().charAt(0) + field.substring(1);
					Object value = method.invoke(javaBean, (Object[]) null);
					result.put(field, null == value ? "" : value.toString());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 将Json字符串转换为Map
	 * 
	 * @param jsonString
	 * @return
	 */
	public static Map<String, Object> toJsonMap(String jsonString) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		String JSON_TYPE = getJSONType(jsonString);
		// System.out.println(type);
		try {
			switch (JSON_TYPE) {
			case JSON_TYPE_ARRAY:
				JSONArray jsonArray = JSONArray.fromObject(jsonString);
				jsonMap = jsonArrayToMap(jsonArray);
				break;
			case JSON_TYPE_OBJECT:
				JSONObject jsonObject = JSONObject.fromObject(jsonString);
				jsonMap = jsonObjectToMap(jsonObject, jsonMap);
				break;
			case JSON_TYPE_ERROR:
				System.out.println("JSON_TYPE_ERROR！！！");
			default:
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonMap;
	}

	/**
	 * JSONArray转换为Map
	 * 
	 * @param jsonArray
	 * @return
	 * @throws JSONException
	 */
	public static Map<String, Object> jsonArrayToMap(JSONArray jsonArray)
			throws JSONException {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		for (int i = 0; i < jsonArray.size(); i++) {
			if(!"".equals(jsonArray.get(i))){
				Object obj=jsonArray.get(i);
				//TODO []为数组
				if(obj instanceof JSONObject){
					JSONObject jsonObject = (JSONObject) jsonArray.get(i);
					Map<String, Object> jsonMap1 = new HashMap<String, Object>();
					Map<String, Object> map = jsonObjectToMap(jsonObject, jsonMap1);
					jsonMap.put(i + "", map);
				}
			}
		}
		return jsonMap;
	}

	/**
	 * JSONObject转换为Map
	 * 
	 * @param jsonObject
	 * @param jsonMap
	 * @return
	 * @throws JSONException
	 */
	public static Map<String, Object> jsonObjectToMap(JSONObject jsonObject,
			Map<String, Object> jsonMap) throws JSONException {
		String key = null;
		Object value = null;
		try {
			Iterator iterator = jsonObject.keys();
			while (iterator.hasNext()) {
				key = (String) iterator.next();
				value = jsonObject.get(key);
				// System.out.println(key+"："+value);
				if (value instanceof JSONArray) {
					// System.out.println(JSON_TYPE_ARRAY);
					jsonMap.put(key, jsonArrayToMap((JSONArray) value));
				} else if (value instanceof JSONObject) {
					// System.out.println(JSON_TYPE_OBJECT);
					Map<String, Object> jsonMap1 = new HashMap<String, Object>();
					jsonMap.put(key,
							jsonObjectToMap((JSONObject) value, jsonMap1));
				} else {
					// System.out.println("字符串,数字");
					jsonMap.put(key, value);
				}
				// System.out.println("jsonObjectToMap2："+key + "：" + value);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// System.out.println("jsonMap:"+jsonMap);
		return jsonMap;
	}

	public static boolean judgeResult1(List<Object> results) {
		boolean b = false;
		for (Object o : results) {
			if (o != null) {
				b = true;
				break;
			}
		}
		return b;
	}

	public static boolean judgeResult(List<Map<String, Object>> results) {
		boolean b = false;
		for (Object o : results) {
			if (o != null) {
				b = true;
				break;
			}
		}
		return b;
	}


	/**
	 * 将Map转换成Javabean
	 * 
	 * @param javabean
	 *            javaBean
	 * @param data
	 *            Map数据
	 */
	public static Object toJavaBean(Object javabean, Map data) {

		Method[] methods = javabean.getClass().getDeclaredMethods();
		for (Method method : methods) {
			try {
				if (method.getName().startsWith("set")) {
					String field = method.getName();
					field = field.substring(field.indexOf("set") + 3);
					field = field.toLowerCase().charAt(0) + field.substring(1);
					method.invoke(javabean, new Object[] { data.get(field) });
				}
			} catch (Exception e) {
			}

		}

		return javabean;

	}

	/**
	 * JSONObject到JavaBean
	 * 
	 * @return json对象
	 * @throws ParseException
	 *             json解析异常
	 * @throws JSONException
	 */
	public static Map toMap(String jsonString) throws JSONException {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		Map result = new HashMap();
		Iterator iterator = jsonObject.keys();
		String key = null;
		Object value = null;
		while (iterator.hasNext()) {
			key = (String) iterator.next();
			value = jsonObject.get(key);
			System.out.println(key + "：" + value);
			result.put(key, value);
		}
		return result;
	}

	public static void toJavaBean(Object javabean, String jsonString)
			throws ParseException, JSONException {

		JSONObject jsonObject = JSONObject.fromObject(jsonString);

		Map map = toMap(jsonObject.toString());

		toJavaBean(javabean, map);

	}

	public static String replaceIndex(int index, String res, String str) {
		return res.substring(0, index) + str + res.substring(index + 1);
	}

	public static void showResult(List<Map<String, Object>> maps){
        for (Map<String, Object> map : maps) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                System.out.println(entry.getKey()+"："+entry.getValue());
            }
        }
    }

}
