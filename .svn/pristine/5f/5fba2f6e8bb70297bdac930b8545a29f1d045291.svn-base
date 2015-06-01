package com.yeahmobi.yscheduler.common.fileserver.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.yeahmobi.yscheduler.common.fileserver.FileServer;
import com.yeahmobi.yscheduler.common.fileserver.FileServerException;

/**
 * Only support unix
 *
 * @author Leo Liang
 */
public class LocalFileBasedFileServer implements FileServer {

    private File   baseDir;
    private String downloadPathBase;

    public void setDownloadPathBase(String downloadPathBase) {
        this.downloadPathBase = downloadPathBase;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = new File(baseDir);
    }

    protected String getFilePath(String nameSpace, String fileName) {
        StringBuilder sb = new StringBuilder(nameSpace);
        sb.append(IOUtils.DIR_SEPARATOR_UNIX);
        String fileNameWithoutExt = StringUtils.substringBefore(fileName, ".");
        for (char c : fileNameWithoutExt.toCharArray()) {
            sb.append(c).append(IOUtils.DIR_SEPARATOR_UNIX);
        }
        sb.append(fileName);
        return sb.toString();
    }

    public void write(String nameSpace, String fileName, byte[] data, boolean append) throws FileServerException {
        try {
            FileUtils.writeByteArrayToFile(getFile(nameSpace, fileName), data, append);
        } catch (IOException e) {
            throw new FileServerException(String.format("Append file failed(nameSpace=%s, fileName=%s).", nameSpace,
                                                        fileName), e);
        }
    }

    private File getFile(String nameSpace, String fileName) {
        return new File(this.baseDir, getFilePath(nameSpace, fileName));
    }

    /**
     * downloadLink 不含 contextPath，contextPath 在 controller 中拼加
     */
    public String getDownloadLink(String nameSpace, String fileName) throws FileServerException {
        return this.downloadPathBase + "/" + getFilePath(nameSpace, fileName);
    }

    public boolean exists(String nameSpace, String fileName) throws FileServerException {
        return getFile(nameSpace, fileName).exists();
    }

    public byte[] getContent(String nameSpace, String fileName, int byteLimit) throws FileServerException {
        File file = getFile(nameSpace, fileName);
        if (file.length() < byteLimit) {
            try {
                return FileUtils.readFileToByteArray(file);
            } catch (IOException e) {
                throw new FileServerException(String.format("Get file content failed(nameSpace=%s, fileName=%s).",
                                                            nameSpace, fileName), e);
            }
        } else {

            FileInputStream fi = null;

            try {
                fi = new FileInputStream(file);
                byte[] data = new byte[byteLimit];
                IOUtils.read(fi, data, 0, byteLimit);
                return data;
            } catch (IOException e) {
                throw new FileServerException(String.format("Get file content failed(nameSpace=%s, fileName=%s).",
                                                            nameSpace, fileName), e);
            } finally {
                IOUtils.closeQuietly(fi);
            }
        }
    }

    /**
     * 此方法是Local实现的独有，通过下载连接，获取File对象（downloadLink 不含 contextPath）
     */
    public File getFileFromDownloadLink(String downloadLink) {
        String prefix = this.downloadPathBase + "/";
        if (StringUtils.startsWith(downloadLink, prefix)) {
            String path = StringUtils.substring(downloadLink, prefix.length());
            return new File(this.baseDir, path);
        } else {
            throw new IllegalArgumentException("Invalid download link:" + downloadLink);
        }
    }

    public static void main(String[] args) throws FileServerException {
        String fileName = "abc";
        String nameSpace = "log";

        LocalFileBasedFileServer fs = new LocalFileBasedFileServer();
        fs.setBaseDir("/b/c/d");
        fs.setDownloadPathBase("http://localhost:8080");

        System.out.println(fs.getFilePath(nameSpace, fileName));
        System.out.println(fs.getDownloadLink(nameSpace, fileName));
    }

}
