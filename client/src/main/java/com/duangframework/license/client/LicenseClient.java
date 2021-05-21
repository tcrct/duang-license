package com.duangframework.license.client;

import java.io.InputStream;
import java.net.URL;
import java.util.Base64;

/**
 * @author duang
 */
public class LicenseClient {

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
                System.out.println("许可证文件["+LICENSE_FILE_NAME+"]不存在，退出验证!");
                return LICENSE_FILE_NOT_EXISTS;
            }
            inputStream = url.openStream();
        } catch (Exception e) {
            System.out.println("读取许可证文件时出错: " + e.getMessage());
            return LICENSE_FILE_EXCEPTION;
        }
        License license = new License();
        LicenseEntity licenseData = license.loadLicense(inputStream,
                Base64.getDecoder().decode(
                        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCVm6HptT8o7wo3Pc+yryp0QiGdI0czyYUpZAnaENapfUW3kLyXf3yisVjRYFJq9YtCr1TUDbjzF/fAge1EkjSUkknfO/eVb5GC3CpdqTiAvhOvQt6wGS6QOhP0BIAStdTQmYUuSOnzCBp+tteTwCG6hjF5IaedgopuThi2KX4oMQIDAQAB"));
        String data = new String(licenseData.getData());
        String systemInfoStr = OshiUtil.getSystemInfo();
        return data.equals(systemInfoStr) ? "200" : "500";
    }
}
