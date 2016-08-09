package com.tyaer.basic.net.helper;

import com.tyaer.basic.utils.MathUtils;
import com.tyaer.bean.DTO;
import com.tyaer.bean.HttpResutBean;
import com.tyaer.bean.ProxyBean;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpHost;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * 代理帮助类
 *
 * @author Twin
 */
public class ProxyHelper {

    protected static Logger LOGGER = Logger.getLogger(ProxyHelper.class
            .getName());

    private static final String detectProxyUrl = "http://proxy.goubanjia.com/tool/ip/info.shtml";

    public List<ProxyBean> getProxyInfo(String sql) {
        List<ProxyBean> proxyBeans = new ArrayList<ProxyBean>();
        try {
            proxyBeans = DTO._MYSQLHELPER.findMoreRefResult(
                    sql, null, ProxyBean.class);
            return proxyBeans;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public final String PROXY_SQL_ALL = "select * from proxy";
    public final String PROXY_SQL_1 = "select * from proxy where status=1";
    public final String PROXY_SQL_0 = "select * from proxy where status=0";

    /**
     * 验证代理的可用性，可以变更条件
     */
    public void TestProxyForSql(String querySql) {
        HttpHelper httpHelper = new HttpHelper();
        List<ProxyBean> proxyList = DTO._PROXYHELPER.getProxyInfo(querySql);
        System.out.println("------------------代理总数： " + proxyList.size());
        for (ProxyBean proxy : proxyList) {
            HttpHost httpHost = new HttpHost(proxy.getProxy_ip(), proxy.getProxy_port());
            int id = proxy.getId();
            HttpResutBean result = null;
            System.out.println("----------------------------- " + httpHost.toString());
            try {
                long startTime = System.currentTimeMillis();
                result = httpHelper.sendRequest(detectProxyUrl, httpHost);
                long ping = System.currentTimeMillis() - startTime;
                System.out.println("请求时间：" + String.valueOf(ping));
                System.out.println(result);
                if (result != null && result.getStatusCode() == 200) {
                    // 更改状态为可用
                    String sql = "UPDATE proxy SET status=1,ping=" + ping + " where id=" + id + ";";
//					System.out.println(sql);
                    DTO._MYSQLHELPER.executeSql(sql);
                } else {
                    String sql = "UPDATE proxy SET status=0 where id=" + id + ";";
                    DTO._MYSQLHELPER.executeSql(sql);
                }
//				break;
//				Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("--------------- 检查完毕！");
    }

    /**
     * 代理访问失败 fail_num+1
     */
    public void addProxyFailNum(HttpHost httpHost) {
        String ip = httpHost.getHostName();
        addProxyFailNum(ip);
    }

    /**
     * 代理访问失败 fail_num+1
     */
    public void addProxyFailNum(String ip) {
        String sql = "update proxy set fail_num=fail_num+1 where proxy_ip='" + ip + "'";
        try {
            DTO._MYSQLHELPER.executeSql(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除代理,status=0
     */
    public void deleteProxy() {
        String DELETE_PROXY_SQL = "delete from proxy where status=0";
        try {
            DTO._MYSQLHELPER.executeSql(DELETE_PROXY_SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清理失效代理,fail_num>3
     */
    public void clearFailProxy() {
        String PROXY_SQL_Fail = "delete from proxy where fail_num>500";
        try {
            DTO._MYSQLHELPER.executeSql(PROXY_SQL_Fail);
            LOGGER.info("代理清理完毕...");
        } catch (SQLException e) {
            LOGGER.error(ExceptionUtils.getFullStackTrace(e));
        }
    }


    /**
     * 获取一个随机代理
     *
     * @param proxyCache
     */
    public HttpHost getRandomProxy(List<ProxyBean> proxyCache) {
        int n = MathUtils.getRandom(0, proxyCache.size() - 1);
        ProxyBean proxy = proxyCache.get(n);
        LOGGER.info("使用代理，" + proxy);
        HttpHost httpHost = new HttpHost(proxy.getProxy_ip(),
                proxy.getProxy_port());
        return httpHost;
    }

    /**
     * 定向测试代理
     *
     * @param ip
     * @param port
     * @throws Exception
     */
    public void testProxy(String ip, int port) throws Exception {
        HttpHost proxy = new HttpHost(ip, port);
        HttpHelper httpHelper = new HttpHelper();
        System.out.println(httpHelper.sendRequest(detectProxyUrl, proxy));
    }

/*	public List<ProxyBean> getProxy(){
		List<ProxyBean> proxyList=new ArrayList<ProxyBean>();
		String xpath = "table#dataTables-example tr.gradeA";
		String searchUrl = "http://www.kjson.com/proxy/so/?q=%E4%B8%AD%E5%9B%BD+http";
		String regex="<td>(.*?)</td>\\s+<td class=\"enport\">(.*?)</td>\\s+<td>\\w*</td>\\s+<td>\\w*</td>\\s+<td>(.*?)</td>\\s+<td>.*?</td>\\s+<td>(.*?)</td>";
		
//		String xpath = "table.proxy_table tr";
//		String searchUrl = "http://www.haodailiip.com/best/201510/1";
//		String regex="<td>(.*?)</td>\\s+<td class=\"enport\">(.*?)</td>\\s+<td>\\w*</td>\\s+<td>\\w*</td>\\s+<td>(.*?)</td>\\s+<td>.*?</td>\\s+<td>(.*?)</td>";

		HttpHelper httpHelper=new HttpHelper();
		String html = httpHelper.sendRequest(searchUrl);
//		System.out.println(html);
		Document doc = Jsoup.parse(html);
		Elements els = doc.select(xpath);
		System.out.println("总共："+els.size());
		if (els.size() > 0) {
			for (Element el : els) {
				System.out.println(el.toString());
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(el.toString());
//				System.out.println(matcher.groupCount());
				if (matcher.find() && matcher.groupCount() >= 2) {
					String ip = matcher.group(1);
					String port = matcher.group(2);
					Properties pro=new Properties();
					InputStream is;
					try {
						is = new FileInputStream(DTO.proxyConfigure);
						pro.load(is);
					} catch (Exception e) {
						e.printStackTrace();
					}
					port=pro.getProperty(port);
					String updateTime=matcher.group(3);
					updateTime=DTO.DATEUTILS.fetchDateTime(updateTime);
					ProxyBean proxy=new ProxyBean(ip, Integer.valueOf(port));
					System.out.println(proxy);
					proxyList.add(proxy);
				}
				break;
			}
		}else{
			System.out.println("模板提取不到内容页连接:" + xpath);
			System.out.println(els.size());
		}
		return proxyList;
	}*/

    public static void main(String[] args) {
        ProxyHelper ph = new ProxyHelper();
        ph.addProxyFailNum("122.96.59.99");
//		String url="http://www.ip138.com/ips138.asp";
//		String url1="http://city.ip138.com/ip2city.asp";
//		String url2="http://www.ip.cn/";
//		HttpHelper httpHelper=new HttpHelper();
//		Map<String, String> map=null;
//		List<ProxyBean> proxyList=ph.getProxy();
//		proxyList.add(null);
//		System.out.println(proxyList);
//		for (ProxyBean proxy:proxyList){
//			System.out.println(proxy);
//			HttpHost httpHost=ph.getHttpHost(proxy);
//			httpHelper.sendRequest(url,httpHost);
//			try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		} 
    }

}
