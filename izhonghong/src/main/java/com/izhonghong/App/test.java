package com.izhonghong.App;

import com.tyaer.basic.utils.ExcelReader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Twin on 2016/8/5.
 */
public class test {
    public static void main(String[] args) {
        ExcelReader excelReader = new ExcelReader();
        List<String> list2=new ArrayList<String>();
        String[][] table = excelReader.readExceltoTable("./file/MAC心跳监控异常审查.xls",2);
        System.out.println(table.length);
        for (int i = 1; i < table.length; i++) {
            String name = table[i][1];
            list2.add(name);
//            System.out.println(name);
        }
        List<String> listResult=new ArrayList<String>();
        for (String s : list2) {
            listResult.add(s);
        }


        table = excelReader.readExceltoTable("./file/MAC心跳监控异常审查.xls",3);
        System.out.println(table.length);
        List<String> list3=new ArrayList<String>();
        for (int i = 1; i < table.length; i++) {
            String name = table[i][1];
            list3.add(name);
//            System.out.println(name);
        }
        for (String s : list2) {
            if(!list3.contains(s)){
                listResult.remove(s);
            }
        }

        table = excelReader.readExceltoTable("./file/MAC心跳监控异常审查.xls",4);
        System.out.println(table.length);
        List<String> list4=new ArrayList<String>();
        for (int i = 1; i < table.length; i++) {
            String name = table[i][1];
            list4.add(name);
//            System.out.println(name);
        }
        for (String s : list2) {
            if(!list4.contains(s)){
                listResult.remove(s);
            }
        }

        table = excelReader.readExceltoTable("./file/MAC心跳监控异常审查.xls",5);
        System.out.println(table.length);
        List<String> list5=new ArrayList<String>();
        for (int i = 1; i < table.length; i++) {
            String name = table[i][1];
            list5.add(name);
//            System.out.println(name);
        }
        for (String s : list2) {
            if(!list5.contains(s)){
                listResult.remove(s);
            }
        }



        System.out.println("---------------重复的：");
        listResult.remove("ZHSP-MAS002_QQ");
        listResult.remove("ZHSP-MAS023_QQ");
        System.out.println(listResult.size());
        for (String s : listResult) {
            System.out.println(s);
        }
    }
}
