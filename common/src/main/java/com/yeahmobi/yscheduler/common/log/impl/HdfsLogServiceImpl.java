package com.yeahmobi.yscheduler.common.log.impl;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.yeahmobi.yscheduler.common.log.LogFileService;

public class HdfsLogServiceImpl implements LogFileService {

    private static final String LOG_PATH = "/log";

    private Configuration       configuration;
    private String              baseDir;          // 尾部包括'/'

    private FileSystem          fs;

    public void init() throws IOException {
        this.fs = FileSystem.get(this.configuration);
        this.baseDir = this.configuration.get("yscheduler.baseDir", "/yscheduler/attemptLog/");
    }

    public void close() throws IOException {
        this.fs.close();
    }

    public FSDataOutputStream getOutputStream(long attemptId) throws IOException {
        Path path = new Path(this.baseDir + attemptId + LOG_PATH);
        return this.fs.create(path, true);
    }

    public FSDataInputStream getInputStream(long attemptId) throws IOException {
        Path path = new Path(this.baseDir + attemptId + LOG_PATH);
        return this.fs.open(path);
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    // public static void main(String[] args) throws IOException {
    // ApplicationContext context = new ClassPathXmlApplicationContext("/applicationContext.xml");
    // HdfsLogServiceImpl logService = context.getBean(HdfsLogServiceImpl.class);
    //
    // long attemptId = 2;
    // FSDataOutputStream outputStream = logService.getOutputStream(attemptId);
    //
    // outputStream.write(1);
    // outputStream.write(2);
    // outputStream.write(3);
    // outputStream.hflush();
    //
    // outputStream.close();
    //
    // FSDataInputStream inputStream = logService.getInputStream(attemptId);
    // System.out.println(IOUtils.toString(inputStream));
    //
    // }
}
