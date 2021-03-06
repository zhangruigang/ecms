
package com.qslion.framework.controller;

import com.google.common.base.Charsets;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import com.qslion.framework.util.JxlsUtils;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controller 基类
 *
 * @author Gray.Z
 * @date 2018/4/21 13:43.
 */
public class BaseController<T> {

    protected final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * 错误消息
     */
    protected static final String ERROR_MESSAGE = "common.message.error";

    /**
     * 成功消息
     */
    protected static final String SUCCESS_MESSAGE = "common.message.success";


    /**
     * 导出EXCEL
     *
     * @param dataModel 数据
     * @param fileName 导出文件名称
     * @param templatePath 模板路径
     */
    protected void exportToExcel(HttpServletRequest request, HttpServletResponse response, Map<String, Object> dataModel,
        String fileName,
        String templatePath) {
        try (OutputStream os = response.getOutputStream()) {
            setResponseParam(response, getLocalFileName(fileName, request.getHeader(HttpHeaders.USER_AGENT)));
            InputStream is = getClass().getClassLoader().getResourceAsStream(templatePath);
            JxlsUtils.exportExcel(is, os, dataModel);
        } catch (FileNotFoundException e) {
            logger.error("export excel happened file not fund exception", e);
        } catch (IOException e) {
            logger.error("export excel happened IO exception", e);
        } catch (Exception e) {
            logger.error("export excel happened exception", e);
        }
    }

    private void setResponseParam(HttpServletResponse response, String fileName) {
        response.setCharacterEncoding(Charsets.UTF_8.toString());
        //MediaType.APPLICATION_JSON_UTF8_VALUE
        response.setContentType(MediaType.MICROSOFT_EXCEL.toString());
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
    }

    private String getLocalFileName(String fileName, String userAgent) throws UnsupportedEncodingException {
        if (userAgent.toUpperCase().contains("MSIE") || userAgent.contains("Trident/7.0")) {
            fileName = URLEncoder.encode(fileName, Charsets.UTF_8.toString());
        } else if (userAgent.toUpperCase().contains("MOZILLA") ||
            userAgent.toUpperCase().contains("CHROME")) {
            fileName = new String(fileName.getBytes(), Charsets.ISO_8859_1.toString());
        } else {
            fileName = URLEncoder.encode(fileName, Charsets.UTF_8.toString());

        }
        return fileName;
    }
}
