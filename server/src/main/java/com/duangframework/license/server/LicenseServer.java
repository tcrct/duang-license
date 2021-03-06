package com.duangframework.license.server;

import com.duangframework.license.client.LicenseEntity;
import com.duangframework.license.client.Md5;
import com.duangframework.license.client.OshiUtil;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

/**
 * @author duang
 */
public class LicenseServer {
    private static final String sourceFile = "duang-license-source.txt";
    private static final String licenseFile = "duang-license.txt";

    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 2) {
            System.out.println("args is invalid");
            return;
        }
        LicenseServer license = new LicenseServer();
        String expire = args[0];
        char type = expire.charAt(expire.length() - 1);
        int value = Integer.valueOf(expire.substring(0, expire.length() - 1));
        Calendar calendar = Calendar.getInstance();
        switch (type) {
            case 'h':
            case 'H':
                calendar.add(Calendar.HOUR, value);
                break;
            case 'd':
            case 'D':
                calendar.add(Calendar.DAY_OF_YEAR, value);
                break;
            case 'y':
            case 'Y':
                calendar.add(Calendar.YEAR, value);
                break;
            default:
                throw new UnsupportedOperationException(expire);
        }
        String data = OshiUtil.getSystemInfo();
        File file = new File(data);
        byte[] bytes;
        if (file.isFile()) {
            System.out.println("sign for file:" + file.getPath());
            bytes = IOUtils.toByteArray(new FileInputStream(file));
        } else {
            System.out.println("sign for string: " + data);
            bytes = data.getBytes();
        }
        if (args.length > 2) {
            license.createLicense(bytes, calendar.getTime(), Base64.getDecoder().decode(args[1]));
        } else {
            license.createLicense(bytes, calendar.getTime());
        }
    }

    public String createLicenseService(byte[] data, Date expireDate) throws Exception {
        byte[] privateKey = Base64.getDecoder().decode(
                "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJWboem1PyjvCjc9z7KvKnRCIZ0jRzPJhSlkCdoQ1ql9RbeQvJd/fKKxWNFgUmr1i0KvVNQNuPMX98CB7USSNJSSSd8795VvkYLcKl2pOIC+E69C3rAZLpA6E/QEgBK11NCZhS5I6fMIGn6215PAIbqGMXkhp52Cim5OGLYpfigxAgMBAAECgYAOpqkajAddaNtlQYZfh0vvCrLkAppsdeW2gfd9BX0gKAZ1zZTKeU+pVxjNmFM56kXzB9yUz6s3EzfOAGtN9ct0QmnrHoFmUMKXbUe+QkrDvbpbhegEr3smpSYDFyhI1PZib7Ltnvy930DeQPEY5ZuY5w3Omki/qOfYJ1a3qczYiQJBAOcz/YeY2AkAOvI6ky/ive9JQD+DJLgGafrbODcn2pwv7tHDw8wa2GkyVDH+TjvsVOst0bsYEQRMkClzLw9oGVcCQQClp1TNzD0x4SD9CUH5+OabfbgoJwdKgc/q88xE35lMA4UT4wdUbZV5s7GBw2thuc3YpwxQK27AJ/eLXORWxm23AkAKk5BHgbBwSPhpWFCfYin6JkmwHhmx6Wkzto+Nxl7zwiWUpvXwAlJgDZNYbh+6EgeYcpIjkuhEYBRAMSq387UlAkBgzQSQck48TJtzYiqMwbc4m+G2jQAEuRDf8nGmuaciNVhZw6wv2Q2lHa2X77NbWzF/7jYSzx6b6X8NkE3aq3/HAkAx3nyF7dTjRyHl2ByaWC24o4zjysgAau3rZ/fDldTmak3VJ6eo6t049TbP/7EeSqLqLbyae5OUNEuQLifXqhHW");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int offset = 0;
        int step = 64;
        if (step > data.length) {
            step = data.length;
        }
        while (offset < data.length) {
            byte[] encryptData = RasUtil.encryptByPrivateKey(data, privateKey, offset, step);
            byteArrayOutputStream.write(encryptData.length);
            byteArrayOutputStream.write(encryptData);
            offset += step;
            step = data.length - offset > step ? step : data.length - offset;
        }

        data = byteArrayOutputStream.toByteArray();
        // ???????????????????????????????????????3??????
        if (expireDate == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, 3);
            expireDate = calendar.getTime();
        }
        //?????????????????????????????????
        KeyPair keyPair = RasUtil.initKey();

        LicenseEntity entity = new LicenseEntity(expireDate.getTime(), RasUtil.getPublicKey(keyPair), Md5.md5(data));
        entity.setData(data);

        LicenseEncode licenseEncode = new LicenseEncode();
        return Base64.getEncoder().encodeToString(licenseEncode.encode(entity, RasUtil.getPrivateKey(keyPair)));
    }

    /**
     * ????????????????????????data????????????
     *
     * @param data license??????
     * @param expireDate ????????????
     * @param privateKey ??????
     */
    public void createLicense(byte[] data, Date expireDate, byte[] privateKey) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int offset = 0;
        int step = 64;
        if (step > data.length) {
            step = data.length;
        }
        while (offset < data.length) {
            byte[] encryptData = RasUtil.encryptByPrivateKey(data, privateKey, offset, step);
            byteArrayOutputStream.write(encryptData.length);
            byteArrayOutputStream.write(encryptData);
            offset += step;
            step = data.length - offset > step ? step : data.length - offset;
        }
        createLicense(byteArrayOutputStream.toByteArray(), expireDate);
    }

    public void createLicense(byte[] data) throws Exception {
        createLicense(data, null);
    }

    /**
     * ??????License
     *
     * @param data       license??????
     * @param expireDate ????????????
     * @return
     * @throws Exception
     */
    public void createLicense(byte[] data, Date expireDate) throws Exception {
        // ???????????????????????????????????????3??????
        if (expireDate == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, 3);
            expireDate = calendar.getTime();
        }
        //?????????????????????????????????
        KeyPair keyPair = RasUtil.initKey();

        LicenseEntity entity = new LicenseEntity(expireDate.getTime(), RasUtil.getPublicKey(keyPair), Md5.md5(data));
        entity.setData(data);
        createLicense(entity, keyPair);

        createSourceLicense(entity, keyPair);
    }

    private void createLicense(LicenseEntity entity, KeyPair keyPair) throws Exception {
        //??????License
        File file = new File(licenseFile);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            LicenseEncode licenseEncode = new LicenseEncode();
            fileOutputStream.write(Base64.getEncoder().encodeToString(licenseEncode.encode(entity, RasUtil.getPrivateKey(keyPair))).getBytes(StandardCharsets.UTF_8));
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
    }

    /**
     * ??????License?????????
     *
     * @param entity
     * @param keyPair
     * @throws IOException
     */
    private void createSourceLicense(LicenseEntity entity, KeyPair keyPair) throws IOException {
        SourceLicense sourceLicense = new SourceLicense(entity, keyPair);
        //??????Source License
        File file = new File(sourceFile);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            Properties properties = new Properties();
            properties.setProperty(SourceLicense.PROPERTY_APPLY_DATE, sourceLicense.getApplyDate());
            properties.setProperty(SourceLicense.PROPERTY_EXPIRE_DATE, sourceLicense.getExpireDate());
            properties.setProperty(SourceLicense.PROPERTY_BASE64_CONTENT, sourceLicense.getBase64Content());
            properties.setProperty(SourceLicense.PROPERTY_PUBLIC_KEY, sourceLicense.getPublicKey());
            properties.put(SourceLicense.PROPERTY_PRIVATE_KEY, sourceLicense.getPrivateKey());
            properties.store(fileWriter, null);
        } finally {
            if (fileWriter != null) {
                fileWriter.close();
            }
        }
        System.out.println("\n##############");
        System.out.println("License data: "+ sourceLicense.getBase64Content());
        System.out.println("Expire Date: "+ sourceLicense.getExpireDate());
    }
}
