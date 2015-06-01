package com.yeahmobi.yscheduler.storage.service;

import java.io.FileNotFoundException;

import com.yeahmobi.yscheduler.storage.FileEntry;
import com.yeahmobi.yscheduler.storage.FileKey;

/**
 * 文件存取服务器接口
 *
 * @author Abel.Cui
 * @date 2015/3/10
 */
public interface FileService {

    /**
     * 功能： 文件存储
     *
     * @param fileKey
     * @param fileEntry
     * @return 返回存储文件的时间戳
     * @throws Exception
     */
    public long store(FileKey fileKey, FileEntry fileEntry) throws Exception;

    /**
     * 功能： 根据key和version获取文件流
     *
     * @param fileKey
     * @param version
     * @return 包装了文件名和文件流的FileEntry对象
     * @throws FileNotFoundException
     */
    public FileEntry get(FileKey fileKey, long version) throws FileNotFoundException;

    public long getMaxUploadSize();
}
