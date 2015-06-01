package com.yeahmobi.yscheduler.common.fileserver;

/**
 * 通用的文件服务器接口
 *
 * @author Leo Liang
 */
public interface FileServer {

    /**
     * 往特定文件写数据，如果文件不存在，则创建
     *
     * @param nameSpace 命名空间，因为fileServer会负责各种业务场景的文件，nameSpace用于标识业务场景，对应场景的文件存放方式由fileServer决定
     * @param fileName 文件名(不带任何路径)
     * @param data append的数据
     * @param append 是否追加
     */
    public void write(String nameSpace, String fileName, byte[] data, boolean append) throws FileServerException;

    /**
     * 获得下载链接，包括协议类型(HTTP, FTP等)，具体由fileServer决定
     */
    public String getDownloadLink(String nameSpace, String fileName) throws FileServerException;

    /**
     * 文件是否存在
     */
    public boolean exists(String nameSpace, String fileName) throws FileServerException;

    /**
     * 获得文件内容，仅返回<code>byteLimit</code>指定的大小的内容
     */
    public byte[] getContent(String nameSpace, String fileName, int byteLimit) throws FileServerException;

}
