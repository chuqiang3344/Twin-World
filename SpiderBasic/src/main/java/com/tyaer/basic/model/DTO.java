package com.tyaer.basic.model;

import org.apache.log4j.PropertyConfigurator;

/**
 * Created by Twin on 2016/6/23.
 */
public class DTO {
    /**
     * 本地：项目工程的根目录
     */
    public static final String root=System.getProperty("user.dir");

    /**
     * log4j的配置文件
     */
    public static final String log4jProperties=root+"/src/main/resources/log4j.properties";

    static{
        //加载log4j的配置文件
        PropertyConfigurator.configure(log4jProperties);
    }

    public static void main(String[] args) {
        System.out.println(DTO.root);
    }
}
