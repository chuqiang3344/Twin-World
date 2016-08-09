package com.tyaer.basic.utils;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 数学相关算法
 * 
 * @author mg
 * */
public class MathUtils {

	/**
	 * 根据指定最小值与最大值生成随机数
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandom(int min, int max) {
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}

	public static long getRandomPassTime(long max, long range) {
		long time = max - getRandom(0, (int) range);
		return time;
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

	public static void main(String[] args) {
		MathUtils mt=new MathUtils();
		Set<Integer> set =mt.getRandomNums(10, 3);
		for (Integer i : set) {
			System.out.println(i);
		}
		// Random r=new Random();
		// while (true) {
		// // System.out.println(getRandom(5, 230));
		// System.out.println(r.nextInt(5));
		// }
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// try {
		// // long date_time = sdf.parse("2016-05-01 00:00:00").getTime();
		// System.out.println(sdf.parse("2016-05-01 07:30:00").getTime());
		// System.out.println(sdf.parse("2016-05-02 07:30:00").getTime());
		// System.out.println(sdf.parse("2016-05-03 07:30:00").getTime());
		// System.out.println(sdf.parse("2016-05-04 07:30:00").getTime());
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
//		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
//		Date currentTime = new Date();
//		String dateString = formatter.format(currentTime);
//		System.out.println(dateString);
//		try {
//			System.out.println(formatter.parse(dateString).getTime());
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
