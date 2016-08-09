package com.tyaer.bean;

import java.sql.Timestamp;

/**
 * 代理类
 *
 * @author Twin
 */
public class ProxyBean {
    /**
     * id
     */
    private int id;

    /**
     * IP
     */
    private String proxy_ip;

    /**
     * port
     */
    private int proxy_port;

    /**
     * port
     */
    private String proxy_type;

    /**
     * port
     */
    private String proxy_address;

    //更新时间
    private Timestamp update_time;

    //延迟
    private long ping;

    //状态
    private int status;

    //失败次数
    private int fail_num;

    public ProxyBean(int id,String proxy_ip, int proxy_port,
                     String proxy_type, String proxy_address, Timestamp update_time,
                     int status, int fail_num, long ping) {
        super();
        this.id=id;
        this.proxy_ip = proxy_ip;
        this.proxy_port = proxy_port;
        this.proxy_type = proxy_type;
        this.proxy_address = proxy_address;
        this.update_time = update_time;
        this.status = status;
        this.fail_num = fail_num;
        this.ping = ping;
    }

    public ProxyBean(String proxy_ip, int proxy_port) {
        super();
        this.proxy_ip = proxy_ip;
        this.proxy_port = proxy_port;
    }

    public ProxyBean() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getProxy_ip() {
        return proxy_ip;
    }

    public void setProxy_ip(String proxy_ip) {
        this.proxy_ip = proxy_ip;
    }

    public int getProxy_port() {
        return proxy_port;
    }

    public void setProxy_port(int proxy_port) {
        this.proxy_port = proxy_port;
    }

    public String getProxy_type() {
        return proxy_type;
    }

    public void setProxy_type(String proxy_type) {
        this.proxy_type = proxy_type;
    }

    public String getProxy_address() {
        return proxy_address;
    }

    public void setProxy_address(String proxy_address) {
        this.proxy_address = proxy_address;
    }

    public Timestamp getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Timestamp update_time) {
        this.update_time = update_time;
    }

    public int getFail_num() {
        return fail_num;
    }

    public void setFail_num(int fail_num) {
        this.fail_num = fail_num;
    }

    public long getPing() {
        return ping;
    }

    public void setPing(int ping) {
        this.ping = ping;
    }

    @Override
    public String toString() {
        return "ProxyBean{" +
                "id=" + id +
                ", proxy_ip='" + proxy_ip + '\'' +
                ", proxy_port=" + proxy_port +
                ", proxy_type='" + proxy_type + '\'' +
                ", proxy_address='" + proxy_address + '\'' +
                ", update_time=" + update_time +
                ", status=" + status +
                ", fail_num=" + fail_num +
                ", ping=" + ping +
                '}';
    }
}
