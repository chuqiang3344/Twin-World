package com.tyaer.basic.net.httptools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;

///
// IP地址查询器
//
// @author iPan
// @version 2014-2-16
//
public final class IPParser {
    private static final String DATA_PATH = "qqip_data/qqwry.dat";

    // 获取指定IP的城市
    public static String getCountry(String ip) {
        return new SubIPParser(DATA_PATH).seek(ip).getCountry();
    }

    // 获取指定IP的位置
    public static String getLocal(String ip) {
        return new SubIPParser(DATA_PATH).seek(ip).getLocation();
    }

    // 获取指定IP的城市+位置
    public static String getForSeparator(String ip, String sep) {
        SubIPParser parser = new SubIPParser(DATA_PATH).seek(ip);
        return parser.getCountry() + sep + parser.getLocation();
    }

    private IPParser() {
    }

    // 对原版代码做了重构，逻辑保持不变；
    static class SubIPParser {
        private String dataClasspath;
        private String country;
        private String location;
        private int recordCount, countryFlag;
        private long rangE, rangB, offSet, startIP, endIP, firstStartIP, lastStartIP, endIPOff;

        public SubIPParser(String classpath) {
            dataClasspath = classpath;
        }

        public SubIPParser seek(String ip) {
            RandomAccessFile fis = null;
            byte[] buff = null;
            long ipn;
            try {
                ipn = ipToLong(ip);
                fis = new RandomAccessFile(getDataPath().getFile(), "r");
                buff = new byte[4];
                fis.seek(0);
                fis.read(buff);
                firstStartIP = this.byteToLong(buff);
                fis.read(buff);
                lastStartIP = this.byteToLong(buff);
                recordCount = (int) ((lastStartIP - firstStartIP) / 7);
                if (recordCount <= 1) {
                    location = country = "未知";
                    return this;
                }
                rangB = 0;
                rangE = recordCount;
                long RecNo;
                do {
                    RecNo = (rangB + rangE) / 2;
                    loadStartIP(RecNo, fis);
                    if (ipn == startIP) {
                        rangB = RecNo;
                        break;
                    }
                    if (ipn > startIP)
                        rangB = RecNo;
                    else
                        rangE = RecNo;
                } while (rangB < rangE - 1);
                loadStartIP(rangB, fis);
                loadEndIP(fis);
                loadCountry(ipn, fis);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                    }
                }
            }
            return this;
        }

        public String getLocation() {
            return this.location;
        }

        public String getCountry() {
            return this.country;
        }

        private long byteToLong(byte[] b) {
            long ret = 0;
            for (int i = 0; i < b.length; i++) {
                long t = 1L;
                for (int j = 0; j < i; j++) {
                    t = t * 256L;
                }
                ret += ((b[i] < 0) ? 256 + b[i] : b[i]) * t;
            }
            return ret;
        }

        private long ipToLong(String ip) {
            String[] arr = ip.split("\\.");
            long ret = 0;
            for (int i = 0; i < arr.length; i++) {
                long l = 1;
                for (int j = 0; j < i; j++)
                    l *= 256;
                try {
                    ret += Long.parseLong(arr[arr.length - i - 1]) * l;
                } catch (Exception e) {
                    ret += 0;
                }
            }
            return ret;
        }

        private URL getDataPath() {
            URL url = null;
            url = Thread.currentThread().getContextClassLoader().getResource(dataClasspath);
            if (url == null) {
                url = IPParser.class.getClassLoader().getResource(dataClasspath);
            }
            return url;
        }

        private String getFlagStr(long OffSet, RandomAccessFile fis) throws IOException {
            int flag = 0;
            byte[] buff = null;
            do {
                fis.seek(OffSet);
                buff = new byte[1];
                fis.read(buff);
                flag = (buff[0] < 0) ? 256 + buff[0] : buff[0];
                if (flag == 1 || flag == 2) {
                    buff = new byte[3];
                    fis.read(buff);
                    if (flag == 2) {
                        countryFlag = 2;
                        endIPOff = OffSet - 4;
                    }
                    OffSet = this.byteToLong(buff);
                } else
                    break;
            } while (true);

            if (OffSet < 12) {
                return "";
            } else {
                fis.seek(OffSet);
                return getText(fis);
            }
        }

        private String getText(RandomAccessFile fis) throws IOException {
            long len = fis.length();
            ByteArrayOutputStream byteout = new ByteArrayOutputStream();
            byte ch = fis.readByte();
            do {
                byteout.write(ch);
                ch = fis.readByte();
            } while (ch != 0 && fis.getFilePointer() < len);
            return byteout.toString("gbk");
        }

        private void loadCountry(long ipn, RandomAccessFile fis) throws IOException {
            if (countryFlag == 1 || countryFlag == 2) {
                country = getFlagStr(endIPOff + 4, fis);
                if (countryFlag == 1) {
                    location = getFlagStr(fis.getFilePointer(), fis);
                    if (ipn >= ipToLong("255.255.255.0") && ipn <= ipToLong("255.255.255.255")) {
                        location = getFlagStr(endIPOff + 21, fis);
                        country = getFlagStr(endIPOff + 12, fis);
                    }
                } else {
                    location = getFlagStr(endIPOff + 8, fis);
                }
            } else {
                country = getFlagStr(endIPOff + 4, fis);
                location = getFlagStr(fis.getFilePointer(), fis);
            }
        }

        private long loadEndIP(RandomAccessFile fis) throws IOException {
            byte[] buff = null;
            fis.seek(endIPOff);
            buff = new byte[4];
            fis.read(buff);
            endIP = this.byteToLong(buff);
            buff = new byte[1];
            fis.read(buff);
            countryFlag = (buff[0] < 0) ? 256 + buff[0] : buff[0];
            return endIP;
        }

        private long loadStartIP(long RecNo, RandomAccessFile fis) throws IOException {
            byte[] buff = null;
            offSet = firstStartIP + RecNo * 7;
            fis.seek(offSet);
            buff = new byte[4];
            fis.read(buff);
            startIP = this.byteToLong(buff);
            buff = new byte[3];
            fis.read(buff);
            endIPOff = this.byteToLong(buff);
            return startIP;
        }
    }

    public static void main(String[] args) throws Exception {
        long initUsedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        long start = System.currentTimeMillis();
// 查询IP地址
        System.out.println(IPParser.getForSeparator("115.239.210.26", ", "));
        long end = System.currentTimeMillis();
        long endUsedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
// 性能测试
        System.out.println("time spent:" + (end - start) + " ms");
        System.out.println("memory consumes:" + (endUsedMemory - initUsedMemory) / 1024 + " kb");
    }

}
