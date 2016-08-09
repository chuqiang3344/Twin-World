package com.tyaer.basic.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Twin on 2016/6/21.
 */
public class test {
    public static void main(String[] args) throws IOException {
        File file = new File("//43.122.101.77/root/program/TrafficAnalysis/bin/logs/vehicleData/vehicleData.log");
//        File file = new File("F:/工作结果表/周楚强29.txt");
        String s = FileUtils.readFileToString(file,"gbk");
        System.out.println(s);
    }
}
