package com.duangframework.license.server;

import com.duangframework.license.client.LicenseEntity;
import com.duangframework.license.client.Md5;
import com.duangframework.license.client.OshiUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(LicenseServer.class);
    private final String sourceFile = "source.txt";
    private final String licenseFile = "license.txt";

    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 2) {
            LOGGER.info("args is invalid");
            return;
        }
        String key = args[0];
        if (!"1b88ab6d".equals(key)) {
            LOGGER.info("sucerity key is invalid");
            return;
        }
        LicenseServer license = new LicenseServer();
        String expire = args[1];
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
            LOGGER.info("sign for file:{}", file.getPath());
            bytes = IOUtils.toByteArray(new FileInputStream(file));
        } else {
            LOGGER.info("sign for string:{}", data);
            bytes = data.getBytes();
        }
        if (args.length > 2) {
            license.createLicense(bytes, calendar.getTime(), Base64.getDecoder().decode(args[2]));
        } else {
            license.createLicense(bytes, calendar.getTime());
        }
    }

    /**
     * 采用非对称加密对data作预处理
     *
     * @param data license内容
     * @param expireDate 过期时间
     * @param privateKey 私钥
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
     * 生成License
     *
     * @param data       license内容
     * @param expireDate 过期时间
     * @return
     * @throws Exception
     */
    public void createLicense(byte[] data, Date expireDate) throws Exception {
        // 如果为空，则默认过期时间为1年后
        if (expireDate == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.YEAR, 1);
            expireDate = calendar.getTime();
        }
        //初始化密钥，生成密钥对
        KeyPair keyPair = RasUtil.initKey();

        LicenseEntity entity = new LicenseEntity(expireDate.getTime(), RasUtil.getPublicKey(keyPair), Md5.md5(data));
        entity.setData(data);
        createLicense(entity, keyPair);

        createSourceLicense(entity, keyPair);
    }

    private void createLicense(LicenseEntity entity, KeyPair keyPair) throws Exception {
        //生成License
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
     * 生成License源文件
     *
     * @param entity
     * @param keyPair
     * @throws IOException
     */
    private void createSourceLicense(LicenseEntity entity, KeyPair keyPair) throws IOException {
        SourceLicense sourceLicense = new SourceLicense(entity, keyPair);
        //生成Source License
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
        LOGGER.info("License data :{}", sourceLicense.getBase64Content());
        LOGGER.info("Expire Date:{}", sourceLicense.getExpireDate());
    }
}
