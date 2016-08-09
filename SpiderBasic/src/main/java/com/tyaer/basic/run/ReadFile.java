package com.tyaer.basic.run;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.poi.ss.formula.WorkbookDependentFormula;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Twin on 2016/6/2.
 */
public class ReadFile {

    /**
     * 判断文件的编码格式
     * @param fileName :file
     * @return 文件编码格式
     * @throws Exception
     */
    public static String codeString(String fileName) throws Exception{

        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));
        int p = (bin.read() << 8) + bin.read();
        String code = null;

        switch (p) {
            case 0xefbb:
                code = "UTF-8";
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            default:
                code = "GBK";
        }

        return code;
    }

    public static void main(String[] args) {
        try {
            String s = new String("files/table.doc");
            String s1 = codeString(s);
            System.out.println(s1);
            byte[] strings = FileUtils.readFileToByteArray(new File(s));
            String s2 = new String(strings);
            System.out.println(s2);
//            for (String string : strings) {
//                System.out.println(string);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
