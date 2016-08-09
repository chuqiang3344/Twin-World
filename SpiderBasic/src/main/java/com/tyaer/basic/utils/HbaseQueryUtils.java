package com.tyaer.basic.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.filter.TimestampsFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * 
 * @author Twin
 *
 */
public class HbaseQueryUtils {

	private static final Logger logger = Logger.getLogger(HbaseQueryUtils.class
			.getName());
	// 连接配置
	private Configuration confHbase = null;
	// 连接池
	private Connection htpool = null;
	// 连接池最大htable实例数
	private final int HTablePoolMaxSize = 9;

	private static final String ZookeeperAddress = "192.168.1.51,192.168.1.52,192.168.1.53,192.168.1.54,192.168.1.55";

	private static final String ZookeeperPort = "12181";

	synchronized public void init() {
	}

	public HbaseQueryUtils() {
		try {
			confHbase = getConf();
			// 初始化连接池
			htpool = ConnectionFactory.createConnection(confHbase);
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"hbase 加载连接配置失败 " + ExceptionUtils.getFullStackTrace(e));
		}
	}

	/**
	 * 释放hbase连接资源
	 */
	public void relese() {
		try {
			if (htpool != null)
				htpool.close();
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"hbase close " + ExceptionUtils.getFullStackTrace(e));
		}
	}

	/*
	 * public HTableInterface getTableInterface(String tabName) throws
	 * IOException { return htpool.getTable(tabName); }
	 * 
	 * public HTable getTable(String tabName) throws IOException { while (true)
	 * { try { return new HTable(getConf(), Bytes.toBytes(tabName)); } catch
	 * (IOException e) { logger.log(Level.SEVERE, "hbase 加载连接配置失败 " +
	 * ExceptionUtils.getFullStackTrace(e)); try { Thread.sleep(1000); } catch
	 * (InterruptedException e1) { e1.printStackTrace(); } } } }
	 */

	private Configuration getConf() {
		if (confHbase == null) {
			confHbase = HBaseConfiguration.create();
			confHbase.set("hbase.zookeeper.quorum", ZookeeperAddress);
			confHbase.set("hbase.zookeeper.property.clientPort", ZookeeperPort);
		}
		return confHbase;
	}

	/**
	 * 插入数据
	 * 
	 * @param tabName
	 * @param familyName
	 * @param key
	 * @param mapInfo
	 * @return
	 */
	public int put(String tabName, byte[] familyName, byte[] key,
			Map<byte[], byte[]> mapInfo) {
		Table ht = null;
		try {
			ht = htpool.getTable(TableName.valueOf(tabName));
			Put put = new Put(key);
			put.setWriteToWAL(false);
			for (byte[] fieldName : mapInfo.keySet()) {
				put.add(familyName, fieldName, mapInfo.get(fieldName));
			}
			ht.put(put);
			return 1;
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (Exception el) {
			try {
				throw el;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} finally {
			if (ht != null)
				try {
					ht.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return -1;
	}

	private int removeData(String tabName, byte[] key) {
		int i = 1;
		Table ht = null;
		while (i > 0) {
			try {
				ht = htpool.getTable(TableName.valueOf(tabName));
				Delete del = new Delete(key);
				ht.delete(del);
				return 1;
			} catch (Exception e) {
				logger.log(Level.SEVERE, "hbase delete Error try " + i++
						+ " times" + ExceptionUtils.getFullStackTrace(e));
			} finally {
				if (ht != null)
					try {
						ht.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
		return -1;
	}

	public void scan(String tabName, String familyName, String attribute,
			String condition, String dataAttribute, String dataValue)
			throws Exception {

		int j = 1;
		while (j < 1000) {
			ResultScanner scanner = null;
			try {
				Table ht = htpool.getTable(TableName.valueOf(tabName));

				FilterList filterList = new FilterList();
				if (!"".equals(condition)) {
					SingleColumnValueFilter filter = new SingleColumnValueFilter(
							Bytes.toBytes(familyName),
							Bytes.toBytes(attribute), CompareOp.EQUAL,
							new SubstringComparator(condition));
					filter.setFilterIfMissing(true);
					filterList.addFilter(filter);
				}
				// Filter filterNum = new PageFilter(20L);
				// filterList.addFilter(filterNum);
				if (!"".equals(dataValue)) {
					SingleColumnValueFilter dateFilter = new SingleColumnValueFilter(
							Bytes.toBytes(familyName),
							Bytes.toBytes(dataAttribute), CompareOp.EQUAL,
							new SubstringComparator(dataValue));
					dateFilter.setFilterIfMissing(true);
					filterList.addFilter(dateFilter);
				}

				Scan scan = new Scan();
				scan.setFilter(filterList);
				scanner = ht.getScanner(scan);
				int i = 0;
				String key;
				String value;
				for (Result sr : scanner) {
					i++;
					// 打印属性值
					System.out
							.println("---------------------------------------第 "
									+ i
									+ " 条--------------------------------------");
					System.out.println("Rowkey:" + new String(sr.getRow()));

					List<Cell> k = sr.listCells();
					for (Cell keyk : k) {
						key = Bytes.toString(CellUtil.cloneQualifier(keyk));
						value = Bytes.toString(CellUtil.cloneValue(keyk));
						if (!key.equals("content") && !key.equals("Text")) {
							System.out.println(key + ":" + value);
						} else {
							System.out.println(key + ":"
									+ value.replaceAll("\\s*|\t|\r|\n", ""));
						}
					}
				}
				System.out.println("当前条件下总共有 " + i + " 条记录！");
				break;
			} catch (Exception e) {
				logger.log(Level.SEVERE, "hbase scan Error try  " + j++
						+ " times " + ExceptionUtils.getFullStackTrace(e));
			} finally {
				if (scanner != null)
					scanner.close();
				// if (ht != null)
				// ht.close();
			}
		}
	}

	public void scan(String tabName, String familyName, String attribute,
			String startTime, String endTime) throws Exception {
		int j = 1;
		Table ht = null;
		ResultScanner scanner = null;
		try {
			ht = htpool.getTable(TableName.valueOf(tabName));
			List<Filter> filters = new ArrayList<Filter>();
			SingleColumnValueFilter f12 = new SingleColumnValueFilter(
					Bytes.toBytes(familyName), Bytes.toBytes(attribute),
					CompareOp.GREATER, Bytes.toBytes(startTime));
			SingleColumnValueFilter f13 = new SingleColumnValueFilter(
					Bytes.toBytes(familyName), Bytes.toBytes(attribute),
					CompareOp.LESS_OR_EQUAL, Bytes.toBytes(endTime));
			f12.setFilterIfMissing(true);
			f13.setFilterIfMissing(true);
			filters.add(f12);
			filters.add(f13);
			FilterList filterList = new FilterList(Operator.MUST_PASS_ALL,
					filters);
			Scan scan = new Scan();
			scan.setFilter(filterList);
			scanner = ht.getScanner(scan);
			int i = 0;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			long allp = 0;
//			long allc = 0;
//			int x = 0;
			for (Result sr : scanner) {
				i++;
				// 打印属性值
				System.out.println("---------------------------------------第 "
						+ i + " 条--------------------------------------");
				System.out.println("Rowkey:" + new String(sr.getRow()));
				List<Cell> k = sr.listCells();
				long l1 = 0;
				long l2 = 0;
				for (Cell keyk : k) {
					String key = Bytes.toString(CellUtil.cloneQualifier(keyk));
					String value = Bytes.toString(CellUtil.cloneValue(keyk));
					// String value = Bytes.toString(keyk.getValue());
					if (!key.equals("content") && !key.equals("Text")) {
						System.out.println(key + ":" + value);
					} else {
						 System.out.println(key + ":"  + value.replaceAll("\\s*|\t|\r|\n", ""));
						// System.out.println(key + ":" + value);
					}
					// if (key.equals("publishTime")) {
					// // System.out.println("publishTime:"+value);
					// l1 = sdf.parse(value).getTime();
					// allp = allp + l1;
					// }
					// if (key.equals("createTime")) {
					// // System.out.println("createTime:"+value);
					// l2 = sdf.parse(value).getTime();
					// allc = allc + l2;
					// } else {
					//
					// }
				}
				// System.out.println(l2 - l1);
				// if ((l2 - l1) > 3600000) {
				// x = x + 1;
				// }
			}
			// System.out.println("当前条件下总共有 " + i + " 条记录！");
			// System.out.println(allc + "---" + allp);
			// long l = (allc - allp) / i;
			// System.out.println("平均间隔：" + l);
			// SimpleDateFormat sdf2 = new SimpleDateFormat(
			// "yyyy-MM-dd HH:mm:ss ");
			// System.out.println("平均间隔：" + (l / 1000 / 60) + "分钟");
			// System.out.println("间隔超过一个小时的有：" + x);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "hbase scan Error try  " + j++ + " times "
					+ ExceptionUtils.getFullStackTrace(e));
		} finally {
			if (scanner != null)
				scanner.close();
			if (ht != null)
				ht.close();
		}
	}

	public void scanRowFilter(String tabName, String startTime, String endTime, String month) throws Exception {
		int j = 1;
		Table ht = null;
		ResultScanner scanner = null;
		try {
			ht = htpool.getTable(TableName.valueOf(tabName));
			Scan scan = new Scan();
//			scan.setCacheBlocks(true);
//			scan.setCaching(30000);
//			scan.setBatch(size);
			//1、根据日期、时间 过滤
			scan.setStartRow(Bytes.toBytes(startTime));
			scan.setStopRow(Bytes.toBytes(endTime));
			//2、再精确到具体哪一天
			RowFilter filter=new RowFilter(CompareOp.EQUAL, new SubstringComparator(month));
			scan.setFilter(filter);
			scanner = ht.getScanner(scan);
			int i = 0;
			for (Result sr : scanner) {
				i++;
				// 打印属性值
				System.out.println("---------------------------------------第 "
						+ i + " 条--------------------------------------");
				System.out.println("Rowkey:" + new String(sr.getRow()));
				List<Cell> k = sr.listCells();
				for (Cell keyk : k) {
					String key = Bytes.toString(CellUtil.cloneQualifier(keyk));
					String value = Bytes.toString(CellUtil.cloneValue(keyk));
//					String value = Bytes.toString(keyk.getValue());
					if (!key.equals("content") && !key.equals("Text")) {
						System.out.println(key + ":" + value);
					} else {
						 System.out.println(key + ":"  + value.replaceAll("\\s*|\t|\r|\n", ""));
//						 System.out.println(key + ":" + value);
					}
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "hbase scan Error try  " + j++ + " times "
					+ ExceptionUtils.getFullStackTrace(e));
		} finally {
			if (scanner != null)
				scanner.close();
			if (ht != null)
				ht.close();
		}
	}
	
	/***
	 * 根据主键rowKey查询一行数据 get 'student','010'
	 */
	public void get(String tabName, String rowkey) throws Exception {
		Table table = null;
		table = htpool.getTable(TableName.valueOf(tabName));
		Get get = new Get(rowkey.getBytes()); // 根据主键查询
		Result result = table.get(get);
		System.out.println("Rowkey:" + new String(result.getRow()));
		List<Cell> k = result.listCells();
		for (Cell keyk : k) {
			String key = Bytes.toString(CellUtil.cloneQualifier(keyk));
			String value = Bytes.toString(CellUtil.cloneValue(keyk));
			// String value = Bytes.toString(keyk.getValue());
			if (!key.equals("content") && !key.equals("Text")) {
				System.out.println(key + ":" + value);
			} else {
				System.out.println(key + ":" + value);
			}
		}

	}

	public void deleteData(String tabName, String familyName, String attribute,
			String condition) throws Exception {
		Table ht = null;
		int j = 1;
		while (j < 1000) {
			ResultScanner scanner = null;
			try {
				ht = htpool.getTable(TableName.valueOf(tabName));
				Filter filter = new SingleColumnValueFilter(
						Bytes.toBytes(familyName), Bytes.toBytes(attribute),
						CompareOp.EQUAL, new SubstringComparator(condition));
				Scan scan = new Scan();
				scan.setFilter(filter);
				scanner = ht.getScanner(scan);
				int i = 0;
				String key;
				String value;
				for (Result sr : scanner) {
					i++;
					// 打印rowKey，只能根据rowkey来删除数据
					System.out
							.println("---------------------------------------第 "
									+ i
									+ " 条--------------------------------------");
					byte[] by = sr.getRow();
					System.out.println(Bytes.toString(by));
					KeyValue[] k = sr.raw();
					for (KeyValue keyk : k) {
						key = Bytes.toString(keyk.getQualifier());
						value = Bytes.toString(keyk.getValue());
						if (!key.equals("content")) {
							System.out.println(key + ":"
									+ value.replaceAll("\\s*|\t|\r|\n", ""));
						}
					}
					// ！！！！！！！！！！！！！！！！！！开始删除！！！！！！！！！！！！！！！！！
					// Delete de=new Delete(by);
					// ht.delete(de);
				}
				System.out.println("当前条件下总共有 " + i + " 条记录,已删除成功！！！");
				break;
			} catch (Exception e) {
				logger.log(Level.SEVERE, "hbase delete Error try  " + j++
						+ " times " + ExceptionUtils.getFullStackTrace(e));
			} finally {
				if (scanner != null)
					scanner.close();
				if (ht != null)
					ht.close();
			}
		}
	}

	// 浏览新闻数据
	public void newsScan(String tabName, String familyName) throws Exception {
		byte[] tabname = Bytes.toBytes(tabName);
		byte[] time = Bytes.toBytes("intime_Url");
		Table ht = null;
		ResultScanner scanner = null;

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String dateto = "20150627";
		Date date2 = format.parse(dateto);
		String to = format.format(date2);

		Date today = new Date();
		today.setHours(0);
		today.setMinutes(0);
		today.setSeconds(0);
		String toda = format.format(today);

		try {
			ht = htpool.getTable(TableName.valueOf(tabName));
			Scan scan = new Scan();

			Filter timeFilter = new SingleColumnValueFilter(
					Bytes.toBytes(familyName), time, CompareOp.EQUAL,
					new SubstringComparator(to));
			Filter filter = new SingleColumnValueFilter(
					Bytes.toBytes(familyName), Bytes.toBytes("sitename"),
					CompareOp.EQUAL, new SubstringComparator(
							"finance.sina.com.cn"));
			FilterList list = new FilterList();
			list.addFilter(timeFilter);
			list.addFilter(filter);
			scan.setFilter(list);
			scanner = ht.getScanner(scan);

			int i = 0;
			for (Result sr : scanner) {
				i++;
				NavigableMap<byte[], byte[]> map = sr.getFamilyMap(Bytes
						.toBytes(familyName));
				for (byte[] bytes : map.keySet()) {
					String key = Bytes.toString(bytes);
					String value = Bytes.toString(map.get(bytes));
					System.out.println(key + ":" + value);
				}
				System.out.println();
			}
			System.out.println(i);
		} catch (Exception e) {

		} finally {
			if (scanner != null)
				scanner.close();
			if (ht != null)
				ht.close();
		}
	}

	public static void main(String[] args) throws Exception {
		int j = 1;
		Table ht = null;
		ResultScanner scanner = null;
		try {
			HbaseQueryUtils util=new HbaseQueryUtils();
			ht = util.htpool.getTable(TableName.valueOf("NewsContent"));
			Scan scan = new Scan();
//			scan.setCacheBlocks(true);
//			scan.setCaching(30000);
//			scan.setBatch(size);
			//1、根据日期、时间 过滤
			//匹配rowkey的前几位，25则是匹配前两位从25开始到前两位26结束
			scan.setStartRow(Bytes.toBytes("25"));
			scan.setStopRow(Bytes.toBytes("26"));
			//2、再精确到具体哪一天
			RowFilter filter=new RowFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator("201602"));
			scan.setFilter(filter);
//			SingleColumnValueFilter dateFilter = new SingleColumnValueFilter(
//					Bytes.toBytes("d"),
//					Bytes.toBytes("url"), CompareOp.EQUAL,
//					new SubstringComparator("http://stock.10jqka.com.cn/20160224/c588041821.shtml"));
//			dateFilter.setFilterIfMissing(true);
//			scan.setFilter(dateFilter);
			scanner = ht.getScanner(scan);
			int i = 0;
			for (Result sr : scanner) {
				i++;
				// 打印属性值
				System.out.println("---------------------------------------第 "
						+ i + " 条--------------------------------------");
				System.out.println("Rowkey:" + new String(sr.getRow()));
				List<Cell> k = sr.listCells();
				for (Cell keyk : k) {
					String key = Bytes.toString(CellUtil.cloneQualifier(keyk));
					String value = Bytes.toString(CellUtil.cloneValue(keyk));
//					String value = Bytes.toString(keyk.getValue());
					if (!key.equals("content") && !key.equals("Text")) {
						System.out.println(key + ":" + value);
					} else {
						 System.out.println(key + ":"  + value.replaceAll("\\s*|\t|\r|\n", ""));
//						 System.out.println(key + ":" + value);
					}
				}
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "hbase scan Error try  " + j++ + " times "
					+ ExceptionUtils.getFullStackTrace(e));
		} finally {
			if (scanner != null)
				scanner.close();
			if (ht != null)
				ht.close();
		}
	}

}
