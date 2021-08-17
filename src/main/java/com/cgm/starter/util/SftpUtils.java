package com.cgm.starter.util;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.Properties;

/**
 * SFTP连接工具类
 *
 * @author cgm
 */
@Slf4j
public class SftpUtils {

    private ChannelSftp sftp;

    private Session session;

    /**
     * SFTP 登录用户名
     */
    private String userName;

    /**
     * SFTP 登录密码
     */
    private String password;

    /**
     * SFTP 服务器地址IP地址
     */
    private String host;

    /**
     * SFTP 端口
     */
    private int port;

    /**
     * 构造基于密码认证的SFTP对象
     *
     * @param userName SFTP 登录用户名
     * @param password SFTP 登录密码
     * @param host     SFTP 服务器地址IP地址
     * @param port     SFTP 端口
     */
    public SftpUtils(String userName, String password, String host, int port) {
        this.userName = userName;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    public SftpUtils() {
    }

    /**
     * 连接SFTP服务器
     */
    public void login() {
        try {
            JSch jsch = new JSch();
            log.info("SFTP connect by host:{} username:{}", host, userName);

            session = jsch.getSession(userName, host, port);
            log.info("Session created.");
            if (password != null) {
                session.setPassword(password);
            }
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");

            session.setConfig(config);
            session.connect();
            log.info("Session connected.");

            Channel channel = session.openChannel("SFTP");
            channel.connect();
            log.info("Channel connected.");

            sftp = (ChannelSftp) channel;
            log.info("SFTP server host:{} port:{} connected.", host, port);
        } catch (JSchException e) {
            log.error("Cannot connect to specified SFTP server : {}:{} \n", host, port, e);
        }
    }

    /**
     * 关闭连接 server
     */
    public void disConnect() {
        if (sftp != null && sftp.isConnected()) {
            sftp.disconnect();
            log.info("SFTP disconnected.");
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
            log.info("Session disconnected.");
        }
    }

    /**
     * 根据路径创建文件夹
     *
     * @param dir 文件夹名称
     */
    public void mkdirDir(String dir) {
        String slash = "/";
        if (!StringUtils.hasText(dir)) {
            return;
        }
        if (dir.endsWith(slash)) {
            dir = dir.substring(0, dir.length() - 1);
        }

        // 当前要创建的文件夹
        StringBuilder currentDir = new StringBuilder();
        // 剩余文件夹
        String lastDir = dir;
        // 剩余文件夹的分隔位置
        int splitIndex = 1;
        while (splitIndex > 0) {
            splitIndex = lastDir.indexOf(slash, 1);
            currentDir.append(splitIndex > 0 ? lastDir.substring(0, splitIndex) : lastDir);
            lastDir = splitIndex > 0 ? lastDir.substring(splitIndex) : "";
            try {
                if (checkDirExists(currentDir.toString())) {
                    log.info("目录已存在-{}", currentDir.toString());
                } else {
                    log.warn("创建目录-{}", currentDir.toString());
                    sftp.mkdir(currentDir.toString());
                }
            } catch (SftpException e) {
                log.error("创建目录{}失败-异常信息:{}", currentDir.toString(), e);
            }
        }
    }

    /**
     * 将输入流的数据上传到SFTP作为文件（多层目录）
     *
     * @param directory    上传到该目录(多层目录)
     * @param sftpFileName sftp端文件名
     * @param input        输入流
     * @throws SftpException sftp异常
     */
    public void uploadMore(String directory, String sftpFileName, InputStream input) throws SftpException {
        try {
            sftp.cd(directory);
        } catch (SftpException e) {
            // 目录不存在，则创建文件夹
            mkdirDir(directory);
        }
        // 上传文件
        sftp.put(input, sftpFileName);
    }

    /**
     * 将输入流的数据上传到SFTP作为文件
     *
     * @param directory    上传到该目录(单层目录)
     * @param sftpFileName sftp端文件名
     * @param input        输入流
     * @throws SftpException sftp异常
     */
    public void upload(String directory, String sftpFileName, InputStream input) throws SftpException {
        try {
            sftp.cd(directory);
        } catch (SftpException e) {
            log.warn("Directory does not exist.");
            sftp.mkdir(directory);
            sftp.cd(directory);
        }
        sftp.put(input, sftpFileName);
        log.info("File:{} uploaded.", sftpFileName);
    }

    /**
     * 上传单个文件
     *
     * @param directory  上传到SFTP目录
     * @param uploadFile 要上传的文件,包括路径
     * @throws FileNotFoundException 文件未找到异常
     * @throws SftpException         sftp异常
     */
    public void upload(String directory, String uploadFile) throws FileNotFoundException, SftpException {
        File file = new File(uploadFile);
        upload(directory, file.getName(), new FileInputStream(file));
    }

    /**
     * 将byte[]上传到SFTP，作为文件。注意:从String生成byte[]是，要指定字符集。
     *
     * @param directory    上传到SFTP目录
     * @param sftpFileName 文件在SFTP端的命名
     * @param byteArr      要上传的字节数组
     * @throws SftpException SFTP异常
     */
    public void upload(String directory, String sftpFileName, byte[] byteArr) throws SftpException {
        upload(directory, sftpFileName, new ByteArrayInputStream(byteArr));
    }

    /**
     * 将字符串按照指定的字符编码上传到SFTP
     *
     * @param directory    上传到SFTP目录
     * @param sftpFileName 文件在SFTP端的命名
     * @param dataStr      待上传的数据
     * @param charsetName  SFTP上的文件，按该字符编码保存
     * @throws UnsupportedEncodingException 编码不支持异常
     * @throws SftpException                SFTP异常
     */
    public void upload(String directory, String sftpFileName, String dataStr, String charsetName) throws UnsupportedEncodingException, SftpException {
        upload(directory, sftpFileName, new ByteArrayInputStream(dataStr.getBytes(charsetName)));
    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件
     * @param saveFile     存在本地的路径
     * @throws SftpException         SFTP异常
     * @throws FileNotFoundException 文件未找到异常
     */
    public void download(String directory, String downloadFile, String saveFile) throws SftpException, FileNotFoundException {
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        File file = new File(saveFile);
        sftp.get(downloadFile, new FileOutputStream(file));
        log.info("File:{} downloaded.", downloadFile);
    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件名
     * @return 字节数组
     * @throws SftpException SFTP异常
     * @throws IOException   IO异常
     */
    public byte[] download(String directory, String downloadFile) throws SftpException, IOException {
        if (directory != null && !"".equals(directory)) {
            sftp.cd(directory);
        }
        InputStream is = sftp.get(downloadFile);
        byte[] fileData = IOUtils.toByteArray(is);
        log.info("File:{} downloaded.", downloadFile);
        return fileData;
    }

    /**
     * 删除文件
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件
     * @throws SftpException sftp异常
     */
    public void delete(String directory, String deleteFile) throws SftpException {
        sftp.cd(directory);
        sftp.rm(deleteFile);
    }

    /**
     * 检查当前文件夹有无指定的文件夹
     *
     * @param dir 指定文件夹
     * @return 有无指定的文件夹
     */
    private boolean checkDirExists(String dir) {
        try {
            sftp.cd(dir);
            sftp.cd("..");
            return true;
        } catch (SftpException e) {
            return false;
        }
    }
}