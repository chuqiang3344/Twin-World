package com.tyaer.data.simulation;

import com.tyaer.basic.datebase.helper.MysqlHelper;
import com.tyaer.basic.utils.MathUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SotoData extends TimerTask {
	static MysqlHelper mh = new MysqlHelper("root","123456", "jdbc:mysql://192.168.1.51:3306/traffic");
//	static MysqlHelper mh = new MysqlHelper(DTO.MysqlUsername,DTO.MysqlPassword,"jdbc:mysql://43.122.101.79:3306/traffic");
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat timeSdf = new SimpleDateFormat("HH:mm:ss");
	static SimpleDateFormat weekSdf = new SimpleDateFormat("EEEE");
	static List<Map<String, Object>> mapList;
	static List<Map<String, Object>> stree_road_mapList;
	// 早高峰
	static long l0730;
	static long l0930;
	// 最严重的早高峰段
	static long l0825;
	static long l0850;
	// 晚高峰
	static long l1630;
	static long l1900;
	// 最严重的晚高峰段
	static long l1740;
	static long l1815;
	// 午夜
	static long l2200;
	static long l0700;

	//周23456的普通高峰期红线的条数
	static int red_road_num;
	//周23456的普通高峰期橙色线的条数
	static int orange_road_num;
	
	private static int red_Lower_limit=1;
	private static int orange_Lower_limit=4;
	
	static Set<Integer> red_set =null;
	static Set<Integer> orange_set =null;

	//数据库最新数据时间
	private static long date_time = 0;

	static {
		String querySql = "SELECT road.road_code, road.road_name, area.area_code, area.area_name, SUM(trs.LENGTH) AS SUM_LENGTH FROM traffic.ts_road road, traffic.ts_road_area ra, traffic.ts_area area, traffic.ts_road_gis_map trgm, traffic.ts_road_segment trs WHERE road.road_code = ra.road_code AND ra.area_code = area.area_code AND area.area_code IN ('431201', '431202') AND road.road_code = trgm.ROAD_CODE AND trgm.GISROAD_CODE = trs.ROAD_CODE AND area.area_code = trs.AREA_CODE GROUP BY road.road_code";
		mapList = mh.findModeResult(querySql, null);
		System.out.println(mapList.size());
		String query_stress_sql = "SELECT DISTINCT trsm.Segment_code FROM ts_road_segment_map AS trsm, ts_roadsegment_tollgate AS trt WHERE trsm.Segment_code = trt.SEGMENT_CODE AND trsm.road_code IN ( SELECT trgm.GISROAD_CODE FROM traffic.ts_road_gis_map trgm WHERE trgm.ROAD_CODE IN ( SELECT a.road_code FROM ( SELECT road.road_code FROM traffic.ts_road road, traffic.ts_road_area ra, traffic.ts_area area, traffic.ts_road_gis_map trgm, traffic.ts_road_segment trs WHERE road.road_code = ra.road_code AND ra.area_code = area.area_code AND area.area_code IN ('431201', '431202') AND road.road_code = trgm.ROAD_CODE AND trgm.GISROAD_CODE = trs.ROAD_CODE AND area.area_code = trs.AREA_CODE GROUP BY road.road_code ) AS a ) )";
		stree_road_mapList = mh.findModeResult(query_stress_sql, null);
		System.out.println(stree_road_mapList.size());
		// 早高峰
		l0730 = getTime("07:30:00");
		l0930 = getTime("09:30:00");
		// 最严重的早高峰段
		l0825 = getTime("08:25:00");
		l0850 = getTime("08:50:00");
		// 晚高峰
		l1630 = getTime("16:30:00");
		l1900 = getTime("19:00:00");
		// 最严重的晚高峰
		l1740 = getTime("17:40:00");
		l1815 = getTime("18:15:00");
		//底峰段
		l2200 = getTime("22:00:00");
		l0700 = getTime("07:00:00");
		
		//程序运行初始化
		red_road_num= MathUtils.getRandom(red_Lower_limit, red_Lower_limit+4);
		orange_road_num=MathUtils.getRandom(orange_Lower_limit, orange_Lower_limit+6);
		red_set =getRandomNums(stree_road_mapList.size(), red_road_num);
		orange_set =getRandomNums(stree_road_mapList.size(), orange_road_num);
		
		Object date_time_obj = mh.findSimpleResult(
				"select max(date_time) date_time_max from dm_road_congest_5",
				null).get("date_time_max");
		if ("".equals(date_time_obj) || date_time_obj == null) {
			try {
				date_time = sdf.parse("2016-05-01 07:00:00").getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			date_time = ((Date) date_time_obj).getTime() + 5 * 60 * 1000;
		}
	}

	@Override
	public void run() {
		simulationData(date_time);
		date_time += 5 * 60 * 1000;
	}


	public static long getTime(String time) {
		try {
			return timeSdf.parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public boolean cancel() {
		return super.cancel();
	}

	protected SotoData() {
		super();
	}

	private void simulationData(long start_time) {
		red_Lower_limit=1;
		orange_Lower_limit=4;
		Date time = new Date(start_time);
		String inDate = sdf.format(time);
		long cruTime = getTime(timeSdf.format(time));
		String week = weekSdf.format(time);
		System.out.println(week);
		//过了高峰期重置ts_road_segment_map
		if(cruTime==l0930||cruTime==l1900){
			String reset_sql = "UPDATE ts_road_segment_map SET ts_road_segment_map.ratio = null ";
			mh.updateByPreparedStatement(reset_sql, null);
			red_set.clear();
			orange_set.clear();
		}
		//进入高峰期时，随机出要变拥堵的路段
		if(l0730==cruTime||l1630==cruTime){
			if (week.equals("星期五") || week.equals("星期一")) {
				red_Lower_limit+=2;
				orange_Lower_limit+=2;
			}
			red_road_num=MathUtils.getRandom(red_Lower_limit, red_Lower_limit+4);
			orange_road_num=MathUtils.getRandom(orange_Lower_limit, orange_Lower_limit+6);
			red_set =getRandomNums(stree_road_mapList.size(), red_road_num);
			orange_set =getRandomNums(stree_road_mapList.size(), orange_road_num);
		}
		//进入严重高峰期时，增加拥堵路段条数 2
		if(l0825==cruTime||l1740==cruTime){
			red_set=addRandomNums(red_set, stree_road_mapList.size(), 2);
			orange_set=addRandomNums(orange_set, stree_road_mapList.size(), 2);
		}
		//离开严重高峰期时，减少拥堵路段条数 2
		if(l0850==cruTime||l1815==cruTime){
			red_set=cutRandomNums(red_set, 2);
			orange_set=cutRandomNums(orange_set, 2);
		}
		
		float scala=1;
		//平滑线条
		if(cruTime==l0700||cruTime==l2200){
			scala=0.75f;
		}
		if(cruTime==(l0700+300000)||cruTime==(l2200-300000)){
			scala=0.9f;
		}
		float randomNum;
		float ratio;
		double SUM_LENGTH;
		String ROAD_CODE;
		List<List<Object>> paramsList = new ArrayList<List<Object>>();
		if (week.equals("星期五") || week.equals("星期一")) {
			if ((l0730 < cruTime && cruTime < l0930) || (l1630 < cruTime && cruTime < l1900)) {
				System.out.println("---------------------------------------偏大："
						+ inDate);
				//高峰期，生成拥堵路段数据
				update_trsm(red_set, orange_set);
				for (Map<String, Object> map : mapList)
					for (int i = 0; i < 2; i++) {
						List<Object> params = new ArrayList<Object>();
						params.add(inDate);
						params.add(map.get("area_code"));
						params.add( map.get("area_name"));
						ROAD_CODE = (String) map.get("ROAD_CODE");
						params.add(ROAD_CODE);
						params.add(map.get("ROAD_NAME"));
						if (i == 0) {
							params.add("上行");
						} else {
							params.add("下行");
						}
						SUM_LENGTH = (Double) map.get("SUM_LENGTH");
						params.add(SUM_LENGTH);
						randomNum = MathUtils.getRandom(1, 14);
						ratio = randomNum / 100;
						params.add(SUM_LENGTH * ratio);
						params.add(ratio);
						params.add(countSpeeduseRatio(ratio));
						paramsList.add(params);
					}
			} else if (l2200 < cruTime || cruTime < l0700) {
				System.out.println("---------------------------------------偏小："
						+ inDate);
				for (Map<String, Object> map : mapList) {
					for (int i = 0; i < 2; i++) {
						List<Object> params = new ArrayList<Object>();
						params.add(inDate);
						params.add(map.get("area_code"));
						params.add( map.get("area_name"));
						ROAD_CODE = (String) map.get("ROAD_CODE");
						params.add(ROAD_CODE);
						params.add( map.get("ROAD_NAME"));
						if (i == 0) {
							params.add("上行");
						} else {
							params.add("下行");
						}
						SUM_LENGTH = (Double) map.get("SUM_LENGTH");
						params.add(SUM_LENGTH);
						randomNum = MathUtils.getRandom(1, 8);
						ratio = randomNum / 100;
						params.add(SUM_LENGTH * ratio);
						params.add(ratio);
						params.add(countSpeeduseRatio(ratio));
						// System.out.println(params);
						paramsList.add(params);
					}
				}
			} else {
				System.out.println("---------------------------------------正常："
						+ inDate);
				//非高峰期
				int yellow_road_num=MathUtils.getRandom(3, 8);
				update_trsm_normal(2, yellow_road_num);
				for (Map<String, Object> map : mapList) {
					for (int i = 0; i < 2; i++) {
						List<Object> params = new ArrayList<Object>();
						params.add(inDate);
						params.add( map.get("area_code"));
						params.add( map.get("area_name"));
						ROAD_CODE = (String) map.get("ROAD_CODE");
						params.add(ROAD_CODE);
						params.add( map.get("ROAD_NAME"));
						if (i == 0) {
							params.add("上行");
						} else {
							params.add("下行");
						}
						SUM_LENGTH = (Double) map.get("SUM_LENGTH");
						params.add(SUM_LENGTH);
						randomNum = MathUtils.getRandom(1, 11);
						ratio = randomNum / 100 * scala;
						params.add(SUM_LENGTH * ratio);
						params.add(ratio);
						params.add(countSpeeduseRatio(ratio));
						// System.out.println(params);
						paramsList.add(params);
					}
				}
			}
		} else {
			// 星期二
			if ((l0730 < cruTime && cruTime < l0930) || (l1630 < cruTime && cruTime < l1900)) {
				System.out.println("---------------------------------------偏大："+ inDate);
				//高峰期，生成拥堵路段数据
				update_trsm(red_set, orange_set);
				for (Map<String, Object> map : mapList) {
					for (int i = 0; i < 2; i++) {
						List<Object> params = new ArrayList<Object>();
						params.add(inDate);
						params.add( map.get("area_code"));
						params.add( map.get("area_name"));
						ROAD_CODE = (String) map.get("ROAD_CODE");
						params.add(ROAD_CODE);
						params.add( map.get("ROAD_NAME"));
						if (i == 0) {
							params.add("上行");
						} else {
							params.add("下行");
						}
						SUM_LENGTH = (Double) map.get("SUM_LENGTH");
						params.add(SUM_LENGTH);
						randomNum = MathUtils.getRandom(1, 12);
						ratio = randomNum / 100;
						params.add(SUM_LENGTH * ratio);
						params.add(ratio);
						params.add(countSpeeduseRatio(ratio));
						// System.out.println(params);
						paramsList.add(params);
					}
				}
			} else if (l2200 < cruTime || cruTime < l0700) {
				System.out.println("---------------------------------------偏小："
						+ inDate);
				for (Map<String, Object> map : mapList) {
					for (int i = 0; i < 2; i++) {
						List<Object> params = new ArrayList<Object>();
						params.add(inDate);
						params.add( map.get("area_code"));
						params.add( map.get("area_name"));
						ROAD_CODE = (String) map.get("ROAD_CODE");
						params.add(ROAD_CODE);
						params.add( map.get("ROAD_NAME"));
						if (i == 0) {
							params.add("上行");
						} else {
							params.add("下行");
						}
						SUM_LENGTH = (Double) map.get("SUM_LENGTH");
						params.add(SUM_LENGTH);
						randomNum = MathUtils.getRandom(1, 6);
						ratio = randomNum / 100;
						params.add(SUM_LENGTH * ratio);
						params.add(ratio);
						params.add(countSpeeduseRatio(ratio));
						// System.out.println(params);
						paramsList.add(params);
					}
				}
			} else {
				System.out.println("---------------------------------------正常："
						+ inDate);
				//非高峰期
				int yellow_road_num=MathUtils.getRandom(1, 6);
				update_trsm_normal(1, yellow_road_num);
				for (Map<String, Object> map : mapList) {
					for (int i = 0; i < 2; i++) {
						List<Object> params = new ArrayList<Object>();
						params.add(inDate);
						params.add( map.get("area_code"));
						params.add( map.get("area_name"));
						ROAD_CODE = (String) map.get("ROAD_CODE");
						params.add(ROAD_CODE);
						params.add( map.get("ROAD_NAME"));
						if (i == 0) {
							params.add("上行");
						} else {
							params.add("下行");
						}
						SUM_LENGTH = (Double) map.get("SUM_LENGTH");
						params.add(SUM_LENGTH);
						randomNum = MathUtils.getRandom(1, 9);
						ratio = randomNum / 100 * scala;
						params.add(SUM_LENGTH * ratio);
						params.add(ratio);
						params.add(countSpeeduseRatio(ratio));
						// System.out.println(params);
						paramsList.add(params);
					}
				}
			}
		}
		String insert_sql = "insert into dm_road_congest_5(date_time,area_code,area_name,road_code,road_name,DIRECTION,ROAD_LENGTH,CONGEST_LENGTH,ratio,SPEED_AVG) values(?,?,?,?,?,?,?,?,?,?)";
		mh.batchUpdateByPreparedStatement(insert_sql, paramsList);
	}

	private static String update_trsm="UPDATE ts_road_segment_map SET ratio = ? where Segment_code=?";
	/**
	 * 增加卡口路段的拥堵系数
	 * @param red_set 红
	 * @param orange_set 黄
	 */
	private void update_trsm(Set<Integer> red_set,Set<Integer> orange_set) {
		List<List<Object>> updateParamsList = new ArrayList<List<Object>>();
		String Segment_code;
		for (Integer i : red_set) {
			Segment_code=(String) stree_road_mapList.get(i).get("Segment_code");
			//变红
			float randomNum = MathUtils.getRandom(15, 23);
			float RATIO = randomNum / 100;
			List<Object> params = new ArrayList<Object>();
			params.add(RATIO);
			params.add(Segment_code);
			updateParamsList.add(params);
		}

		for (Integer i : orange_set) {
			Segment_code=(String) stree_road_mapList.get(i).get("Segment_code");
			//变橙色
			float randomNum = MathUtils.getRandom(9, 14);
			float RATIO = randomNum / 100;
			List<Object> params = new ArrayList<Object>();
			params.add(RATIO);
			params.add(Segment_code);
			updateParamsList.add(params);
		}
		mh.batchUpdateByPreparedStatement(update_trsm, updateParamsList);
		System.out.println("更新完毕，红线："+red_set.size()+" 橙色线："+orange_set.size());
	}
	
	//非高峰期有2-4条黄色，1条橙色
	private void update_trsm_normal(int orange_road_num,int yellow_road_num) {
		List<List<Object>> updateParamsList = new ArrayList<List<Object>>();
		String Segment_code;
		Set<Integer> orange_set =getRandomNums(stree_road_mapList.size(), orange_road_num);
		for (Integer i : orange_set) {
			Segment_code=(String) stree_road_mapList.get(i).get("Segment_code");
			//变橙色
			float randomNum = MathUtils.getRandom(12, 14);
			float RATIO = randomNum / 100;
			List<Object> params = new ArrayList<Object>();
			params.add(RATIO);
			params.add(Segment_code);
			updateParamsList.add(params);
		}
		Set<Integer> yellow_set =getRandomNums(stree_road_mapList.size(), yellow_road_num);
		for (Integer i : yellow_set) {
			Segment_code=(String) stree_road_mapList.get(i).get("Segment_code");
			//变橙色
			float randomNum = MathUtils.getRandom(8, 11);
			float RATIO = randomNum / 100;
			List<Object> params = new ArrayList<Object>();
			params.add(RATIO);
			params.add(Segment_code);
			updateParamsList.add(params);
		}
		
		mh.batchUpdateByPreparedStatement(update_trsm, updateParamsList);
		System.out.println("更新完毕，橙色线："+orange_road_num+" 黄色线："+yellow_road_num);
	}
	
	private float countSpeeduseRatio(float ratio) {
		float speed;
		if(ratio<=0.04){
			speed=(float) (35+(1-ratio/0.04)*(80-35));
		}else if(ratio<=0.08){
			speed=(float) (25+(1-ratio/0.08)*(35-25));
		}else if(ratio<=0.11){
			speed=(float) (15+(1-ratio/0.11)*(25-15));
		}else if(ratio<=0.14){
			speed=(float) (5+(1-ratio/0.14)*(15-5));
		}else{
			speed=(float)((1-ratio/0.24)*5);
		}
		return speed;
	}
	
	public static Set<Integer> getRandomNums(int range, int num) {
		Random random = new Random();
		Set<Integer> set = new HashSet<Integer>();
		while (true) {
			int n = random.nextInt(range);
			if (!set.contains(n)) {
				set.add(n);
			}
			if (set.size() == num) {
				return set;
			}
		}
	}
	
	public static Set<Integer> addRandomNums(Set<Integer> set, int range,int addNum) {
		int sum=set.size()+addNum;
		Random random = new Random();
		while (true) {
			int n = random.nextInt(range);
			if (!set.contains(n)) {
				set.add(n);
			}
			if (set.size() == sum) {
				return set;
			}
		}
	}
	
	public static Set<Integer> cutRandomNums(Set<Integer> set,int cutNum) {
		List<Integer> list=new ArrayList<Integer>();
		Random random = new Random();
		for(Integer i:set){
			list.add(i);
		}
		for (int i = 0; i < cutNum; i++) {
			set.remove(list.get(random.nextInt(list.size())));
		}
		return set;
	}

	// private static void update_trsm_23456(String ROAD_CODE) {
	// float randomNum=MathUtils.getRandom(14,23);
	// float RATIO = randomNum/100;
	// List<Object> params1 = new ArrayList<Object>();
	// params1.add(RATIO);
	// params1.add(ROAD_CODE);
	// mh.updateByPreparedStatement(update_sql, params1);
	// }
	//
	// private static void update_trsm_17(String area_code) {
	// float randomNum=MathUtils.getRandom(16,24);
	// float RATIO = randomNum/100;
	// List<Object> params1 = new ArrayList<Object>();
	// params1.add(RATIO);
	// params1.add(area_code);
	// mh.updateByPreparedStatement(update_sql, params1);
	// }

	public static void main(String[] args) {
		SotoData sotoData = new SotoData();

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(sotoData, new Date(SotoData.date_time),
				5 * 60 * 1000);

		// sd.update_ts_road_segment_map("56511");
	}
}
