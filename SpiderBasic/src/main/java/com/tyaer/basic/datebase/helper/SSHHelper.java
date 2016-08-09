package com.tyaer.basic.datebase.helper;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.StreamGobbler;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.sshtools.j2ssh.SftpClient;
import com.sshtools.j2ssh.SshClient;
import com.sshtools.j2ssh.authentication.AuthenticationProtocolState;
import com.sshtools.j2ssh.authentication.PasswordAuthenticationClient;
import com.sshtools.j2ssh.sftp.FileAttributes;
import com.sshtools.j2ssh.sftp.SftpFile;
import com.sshtools.j2ssh.sftp.SftpFileInputStream;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.List;

/**
 * Created by Twin on 2016/6/21.
 */
public class SSHHelper {

    /**
     * 远程 执行命令并返回结果调用过程 是同步的（执行完才会返回）
     *
     * @param host    主机名
     * @param user    用户名
     * @param psw     密码
     * @param port    端口
     * @param command 命令
     * @return
     */
    public String exec(String host, String user, String psw, int port, String command) {
        String result = "";
        Session session = null;
        ChannelExec openChannel = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host, port);
            session = jsch.getSession(user, host);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword(psw);
            session.connect();
            openChannel = (ChannelExec) session.openChannel("exec");
            openChannel.setCommand(command);
            int exitStatus = openChannel.getExitStatus();
            System.out.println(exitStatus);
            openChannel.connect();
            InputStream in = openChannel.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String buf = null;
            while ((buf = reader.readLine()) != null) {
                result += new String(buf.getBytes("gbk"), "UTF-8") + "    <br>\r\n";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSchException e) {
            e.printStackTrace();
        } finally {
            if (openChannel != null && !openChannel.isClosed()) {
                openChannel.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
        return result;
    }

    public static String exec(String host, String user, String psw, String command) {
        String result = "";
        Session session = null;
        ChannelExec openChannel = null;
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(user, host);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword(psw);
            session.connect();
            openChannel = (ChannelExec) session.openChannel("exec");
            openChannel.setCommand(command);
            int exitStatus = openChannel.getExitStatus();
            System.out.println(exitStatus);
            openChannel.connect();
            InputStream in = openChannel.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String buf = null;
            while ((buf = reader.readLine()) != null) {
                result += new String(buf.getBytes("gbk"), "UTF-8") + "    <br>\r\n";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSchException e) {
            e.printStackTrace();
        } finally {
            if (openChannel != null && !openChannel.isClosed()) {
                openChannel.disconnect();
            }
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
        System.out.println(result);
        return result;
    }

    public boolean connect(String host, int port, String user, String psw) {
        Session session = null;
        JSch jsch = new JSch();
        try {
            if (port == 0) {
                session = jsch.getSession(user, host);
            } else {
                session = jsch.getSession(user, host, port);
            }
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword(psw);
            session.connect();
        } catch (JSchException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 验证服务器是否能正常访问
     *
     * @param host
     * @param port
     * @param user
     * @param psw
     * @return
     */
    public boolean connectVerification(String host, int port, String user, String psw) {
        Session session = null;
        JSch jsch = new JSch();
        try {
            session = jsch.getSession(user, host, port);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword(psw);
            session.connect();
        } catch (JSchException e) {
            return false;
        } finally {
            if (session != null) {
                session.disconnect();
            }
        }
        return true;
    }

    public void copyFile(String host, String user, String psw, String Url) {
        SshClient client = new SshClient();
        try {
            client.connect(host);
            //设置用户名和密码
            PasswordAuthenticationClient pwd = new PasswordAuthenticationClient();
            pwd.setUsername(user);
            pwd.setPassword(psw);
            int result = client.authenticate(pwd);
            if (result == AuthenticationProtocolState.COMPLETE) {//如果连接完成
                System.out.println("===============" + result);
                List<SftpFile> list = client.openSftpClient().ls(Url);
                for (SftpFile f : list) {
                    System.out.println(f.getFilename());
                    String fileUrl = f.getAbsolutePath();
                    if (f.getFilename().equals("aliases")) {
                        OutputStream os = new FileOutputStream("d:/mail/" + f.getFilename());
                        client.openSftpClient().get("/etc/mail/aliases", os);
                        //以行为单位读取文件start
                        File file = new File("d:/mail/aliases");
                        BufferedReader reader = null;
                        try {
                            System.out.println("以行为单位读取文件内容，一次读一整行：");
                            reader = new BufferedReader(new FileReader(file));
                            String tempString = null;
                            int line = 1;//行号
                            //一次读入一行，直到读入null为文件结束
                            while ((tempString = reader.readLine()) != null) {
                                //显示行号
                                System.out.println("line " + line + ": " + tempString);
                                line++;
                            }
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (reader != null) {
                                try {
                                    reader.close();
                                } catch (IOException e1) {
                                }
                            }
                        }
                        //以行为单位读取文件end
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFile(String host, String user, String psw, String Url) throws IOException {
        SshClient client = new SshClient();
        try {
            client.connect(host);
            //设置用户名和密码
            PasswordAuthenticationClient pwd = new PasswordAuthenticationClient();
            pwd.setUsername(user);
            pwd.setPassword(psw);
            int result = client.authenticate(pwd);
            if (result == AuthenticationProtocolState.COMPLETE) {//如果连接完成
                SftpClient sftpClient = client.openSftpClient();
                System.out.println("=============== " + result);
                List<SftpFile> list = client.openSftpClient().ls(Url);
                for (SftpFile f : list) {
                    if (f.getFilename().equals("str")) {
                        System.out.println(f.getAbsolutePath());
                        FileAttributes fileAttributes = client.openSftpClient().get(f.getAbsolutePath());
                        SftpFileInputStream in = new SftpFileInputStream(f);
                        String txt = IOUtils.toString(in, Charsets.toCharset("utf-8"));
                        System.out.println(txt);
                        if (fileAttributes.isFile()) {
                            OutputStream os = new FileOutputStream("F:/mail/" + f.getFilename());
                            client.openSftpClient().get(f.getAbsolutePath(), os);
                        }
                        System.out.println(getFileLinesNum(f.getAbsolutePath()));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.disconnect();
        }
    }

    public void readServerFile(String host, String user, String psw, String Url) throws IOException {
        SshClient client = new SshClient();
        try {
            client.connect(host);
            //设置用户名和密码
            PasswordAuthenticationClient pwd = new PasswordAuthenticationClient();
            pwd.setUsername(user);
            pwd.setPassword(psw);
            int result = client.authenticate(pwd);
            if (result == AuthenticationProtocolState.COMPLETE) {//如果连接完成
                SftpClient sftpClient = client.openSftpClient();
                System.out.println("=============== " + result);
                List<SftpFile> list = client.openSftpClient().ls(Url);
                for (SftpFile f : list) {
                    if (f.getFilename().equals("str")) {
                        FileAttributes fileAttributes = client.openSftpClient().get(f.getAbsolutePath());
                        System.out.println(f.getAbsolutePath());
                        System.out.println(f.isOpen());
                        System.out.println(f.toString());
                        System.out.println(f.canRead());
                        SftpFileInputStream in = new SftpFileInputStream(f);
                        String txt = IOUtils.toString(in, Charsets.toCharset("utf-8"));
                        System.out.println(txt);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            client.disconnect();
        }
    }

    public int getFileLinesNum(String url) {
        int lines = 0;
        File test = new File(url);
        long fileLength = test.length();
        LineNumberReader rf = null;
        try {
            rf = new LineNumberReader(new FileReader(test));
            if (rf != null) {
                rf.skip(fileLength);
                lines = rf.getLineNumber();
                rf.close();
            }
        } catch (IOException e) {
            if (rf != null) {
                try {
                    rf.close();
                } catch (IOException ee) {
                }
            }
        }
        return lines;
    }

    public String executeRemoteCommand(String hostname, String username, String password, String command) {
        StringBuilder sb = new StringBuilder();
        //指明连接主机的IP地址
        Connection conn = new Connection(hostname);
        ch.ethz.ssh2.Session ssh = null;
        try {
            //连接到主机
            conn.connect();
            //使用用户名和密码校验
            boolean isconn = conn.authenticateWithPassword(username, password);
            if (!isconn) {
                System.out.println("连接失败！");
            } else {
                System.out.println("连接成功！");
                ssh = conn.openSession();
                //使用多个命令用分号隔开
//              ssh.execCommand("pwd;cd /tmp;mkdir hb;ls;ps -ef|grep weblogic");
                ssh.execCommand(command);
                //只允许使用一行命令，即ssh对象只能使用一次execCommand这个方法，多次使用则会出现异常
//              ssh.execCommand("mkdir hb");
                //将屏幕上的文字全部打印出来
                InputStream is = new StreamGobbler(ssh.getStdout());
                BufferedReader brs = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = brs.readLine()) != null) {
                    sb.append(line + "\n");
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            //连接的Session和Connection对象都需要关闭
            ssh.close();
            conn.close();
        }
        return sb.toString();
    }

    public int countDataTotalNum() {
        SSHHelper helper = new SSHHelper();
        String username = "root";
        String password = "111111";
        String command = "wc -l program/TrafficAnalysis/bin/logs/vehicleData/vehicleData.log";
        String s1 = helper.executeRemoteCommand("43.122.101.77", username, password, command);
        int num77 = Integer.valueOf(s1.split(" ")[0]);
        System.out.println(num77);
        String s2 = helper.executeRemoteCommand("43.122.101.78", username, password, command);
        int num78 = Integer.valueOf(s2.split(" ")[0]);
        System.out.println(num78);
        String s = helper.executeRemoteCommand("43.122.101.80", username, password, command);
//        String hostname = "43.122.101.77";
//        String s=helper.executeRemoteCommand("43.122.101.78",username,password,command);
//        System.out.println(s);
        return 0;
    }

    public static void main(String args[]) throws IOException {
        SSHHelper helper = new SSHHelper();
//        helper.countDataTotalNum();
        boolean connect = helper.connectVerification("58.60.169.154", 5522, "pi", "raspberry");
        System.out.println(connect);
    }
}
