package com.yeahmobi.yscheduler.storage.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.yeahmobi.yscheduler.storage.FileEntry;
import com.yeahmobi.yscheduler.storage.FileKey;
import com.yeahmobi.yscheduler.storage.service.FileService;

/**
 * @author Abel.Cui
 * @date 2015/3/10
 */
@Controller
@RequestMapping(value = { FileOperateController.SCREEN_NAME })
public class FileOperateController {

    public static final String  SCREEN_NAME = "";

    private static final Logger LOGGER      = LoggerFactory.getLogger(FileOperateController.class);

    @Autowired
    private FileService         fileService;

    /**
     * 上传文件
     *
     * @author Abel.Cui
     * @date 2015/3/10
     * @param request
     * @param fileUpload
     * @return JSON字符串,格式为：{"filename":xx.zip,"version":3,"success":true}或{"errmsg":"info...","success":false}
     * @throws Exception
     */
    @RequestMapping(value = "upload")
    public ModelAndView upload(HttpServletRequest request, @RequestParam("fileUpload") MultipartFile fileUpload,
                               @RequestParam("proxyPath") String proxyPath, @RequestParam("callback") String callback)
                                                                                                                      throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Validate.isTrue((fileUpload.getSize() < (this.fileService.getMaxUploadSize() * (1024 * 1024L))),
                            "上传附件不能超过" + this.fileService.getMaxUploadSize() + "M");
            String nameSpace = ServletRequestUtils.getStringParameter(request, "nameSpace");
            String key = ServletRequestUtils.getStringParameter(request, "key");
            Validate.notEmpty(nameSpace, "命名空间不可为空！");
            Validate.notEmpty(key, "文件key不可为空！");
            Validate.notEmpty(proxyPath, "代理页面路径不可为空！");
            Validate.notEmpty(callback, "回调函数不可为空！");
            long version = this.fileService.store(new FileKey(nameSpace, key),
                                                  new FileEntry(fileUpload.getOriginalFilename(),
                                                                fileUpload.getInputStream()));
            map.put("version", version);
            map.put("success", true);
            map.put("filename", fileUpload.getOriginalFilename());
        } catch (Exception e) {
            map.put("errmsg", e.getMessage());
            map.put("success", false);
            LOGGER.error(e.getMessage());
        }
        map.put("callback", callback);
        return new ModelAndView(new RedirectView(proxyPath), map);
    }

    /**
     * 下载
     *
     * @throws IOException
     * @throws Exception
     */
    @RequestMapping(value = "download")
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {

        try {
            String nameSpace = ServletRequestUtils.getStringParameter(request, "nameSpace");
            String key = ServletRequestUtils.getStringParameter(request, "key");
            String version = ServletRequestUtils.getStringParameter(request, "version");
            Validate.notEmpty(nameSpace, "命名空间不可为空！");
            Validate.notEmpty(key, "文件key不可为空！");
            if (StringUtils.isBlank(version)) {
                version = "0";
            }
            FileEntry fileEntry = this.fileService.get(new FileKey(nameSpace, key), Long.valueOf(version));
            downloadFile(request, response, fileEntry);
        } catch (IllegalArgumentException e) {
            response.setStatus(412);// HTTP 错误 412 前提条件失败
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(e.getMessage());
        } catch (FileNotFoundException e) {
            response.setStatus(404);// HTTP 错误 404 找不到该文件
            response.setContentType("text/html; charset=utf-8");
            response.getWriter().write(e.getMessage());
        }
    }

    private void downloadFile(HttpServletRequest request, HttpServletResponse response, FileEntry fileEntry)
                                                                                                            throws UnsupportedEncodingException,
                                                                                                            IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/octet-stream");
        response.setHeader("filename", fileEntry.getFileName());
        response.setHeader("Content-disposition", "attachment; filename="
                                                  + new String(fileEntry.getFileName().getBytes("utf-8"), "ISO8859-1"));
        response.setHeader("Content-Length", String.valueOf(fileEntry.getInputStream().available()));

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        bis = new BufferedInputStream(fileEntry.getInputStream());
        bos = new BufferedOutputStream(response.getOutputStream());
        byte[] buff = new byte[2048];
        int bytesRead;
        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
            bos.write(buff, 0, bytesRead);
        }
        bis.close();
        bos.close();
    }

}
