package com.duangframework.license.server;

import com.duangframework.license.client.LicenseEntity;
import com.duangframework.license.client.Md5;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Properties;

/**
 * 还原License
 * 根据源文件还原出License文件，防止用户丢失后重新生成
 *
 *@author duang
 */
public class LicenseRevert {
    public static void main(String[] args) throws IOException, ParseException {
        if (args.length == 0) {
            System.err.println("file path is null");
            return;
        }
        File file = new File(args[0]);
        if (file.isFile()) {
            createLicense(new FileInputStream(file));
        } else {
            System.err.println("file " + file.getAbsolutePath() + " is not exists");
        }
    }

    private static void createLicense(InputStream inputStream) throws IOException, ParseException {
        Properties properties = new Properties();
        properties.load(inputStream);
        String expireDate = properties.getProperty(SourceLicense.PROPERTY_EXPIRE_DATE);
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] content = decoder.decode(properties.getProperty(SourceLicense.PROPERTY_BASE64_CONTENT));
        byte[] publicKey = decoder.decode(properties.getProperty(SourceLicense.PROPERTY_PUBLIC_KEY));
        byte[] privateKey = decoder.decode(properties.getProperty(SourceLicense.PROPERTY_PRIVATE_KEY));
        SimpleDateFormat sdf = new SimpleDateFormat(SourceLicense.DATE_FORMAT);
        LicenseEntity licenseEntity = new LicenseEntity(sdf.parse(expireDate).getTime(), publicKey, Md5.md5(content));
        licenseEntity.setData(content);

        //生成License
        File file = new File("license_revert.txt");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            LicenseEncode licenseEncode = new LicenseEncode();
            fileOutputStream.write(Base64.getEncoder().encodeToString(licenseEncode.encode(licenseEntity, privateKey)).getBytes(StandardCharsets.UTF_8));
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }
}
