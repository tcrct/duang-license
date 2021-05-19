package com.duangframework.license.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;
import java.util.Base64;

/**
 * @author duang
 */
public class LicenseClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(LicenseClient.class);
    private static final String LICENSE_FILE_NAME = "duang-license.txt";
    public static final String LICENSE_FILE_NOT_EXISTS = "404";
    public static final String LICENSE_FILE_EXCEPTION= "500";


    public static String check() {
//        String webRootPath = PathKit.getWebRootPath();
//        String licenseFilePath = webRootPath + (webRootPath.endsWith("/") ? LICENSE_FILE_NAME : "/"+LICENSE_FILE_NAME);
        InputStream inputStream = null;
        try {
            URL url = LicenseClient.class.getClassLoader().getResource(LICENSE_FILE_NAME);
            if (null == url) {
                LOGGER.warn("许可证文件[{}]不存在，退出验证!", LICENSE_FILE_NAME);
                return LICENSE_FILE_NOT_EXISTS;
            }
            inputStream = url.openStream();
        } catch (Exception e) {
            LOGGER.warn("读取许可证文件时出错: " + e.getMessage(), e);
            return LICENSE_FILE_EXCEPTION;
        }
        License license = new License();
        LicenseEntity licenseData = license.loadLicense(inputStream,
                Base64.getDecoder().decode(
                        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCiYHbUgm37S4qoMVti7GT3K7uGFVp4dWFCzIyLR+TNKxAQOH8kZmFQVlqRubS0Zhg7huDnHUtSq0QfUfGpHjULlqZAh3BOPT02Kz5WSKPq81EMcTsvhDq4E4GFmPQBnFQe512HipnLxT99Hhl4ZsapdgiPzRKUQ5ALNdJqKAUT0wIDAQAB"));
        String data = new String(licenseData.getData());
        String systemInfoStr = OshiUtil.getSystemInfo();
        return data.equals(systemInfoStr) ? "200" : "500";
    }
}
