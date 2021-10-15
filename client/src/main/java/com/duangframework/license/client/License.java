package com.duangframework.license.client;

import org.apache.commons.io.IOUtils;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 读取License并解析其内容
 *
 * @author duang
 */
public class License {
    /**
     * 忽略
     */
    public static final RuntimeExpireStrategy EXPIRE_STRATEGY_IGNORE = new RuntimeExpireStrategy() {
        @Override
        public void expire() {
            System.err.println("invalid license");
        }
    };

    /**
     * 异常
     */
    public static final RuntimeExpireStrategy EXPIRE_STRATEGY_THROWS = new RuntimeExpireStrategy() {
        @Override
        public void expire() {
            throw new LicenseException("invalid license");
        }
    };

    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 过期策略
     */
    private RuntimeExpireStrategy expireStrategy = EXPIRE_STRATEGY_THROWS;

    /**
     * 使用公钥进行解密
     *
     * @param data
     * @param publicKey
     * @return
     */
    private byte[] decryptByPublicKey(byte[] data, byte[] publicKey) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(encodedKeySpec);
            Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new LicenseException("decrypt exception", e);
        }
    }


    /**
     * 加载本地License
     *
     * @param file license文件
     */
    public LicenseEntity loadLicense(File file) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            return loadLicense(fileInputStream);
        } catch (Exception e) {
            throw new LicenseException("load license exception", e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                    fileInputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 加载License
     *
     * @param inputStream 待解析的License文件流
     * @param publicKey   RAS公钥
     */
    public LicenseEntity loadLicense(InputStream inputStream, byte[] publicKey) {
        try {
//            System.out.println(IOUtils.toString(inputStream, Charset.defaultCharset()));
            LicenseEntity licenseEntity = loadLicense(inputStream);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(licenseEntity.getData());
            while (byteArrayInputStream.available() > 0) {
                byte[] block = new byte[byteArrayInputStream.read()];
                byteArrayInputStream.read(block);
                byteArrayOutputStream.write(decryptByPublicKey(block, publicKey));
            }
            licenseEntity.setData(byteArrayOutputStream.toByteArray());

            return licenseEntity;
        } catch (LicenseException e) {
            throw e;
        } catch (Exception e) {
            throw new LicenseException("load license exception", e);
        }
    }

    /**
     * 加载License
     *
     * @param inputStream
     */
    public LicenseEntity loadLicense(InputStream inputStream) {
        try {
            LicenseEntity licenseEntity = decode(inputStream);
            monitorExpireThread(licenseEntity);
            return licenseEntity;
        } catch (LicenseException e) {
            throw e;
        } catch (Exception e) {
            throw new LicenseException("load license exception", e);
        }
    }

    /**
     * 启动License过期监控
     *
     * @param licenseEntity
     */
    private void monitorExpireThread(final LicenseEntity licenseEntity) {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(licenseEntity.getExpireTime() - System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            expireStrategy.expire();
        }, "licenseMonitor");
        thread.setDaemon(true);
        thread.start();
    }

    private LicenseEntity decode(InputStream fileInputStream) throws IOException, ClassNotFoundException {
        byte[] bytes = Base64.getDecoder().decode(IOUtils.toString(fileInputStream, StandardCharsets.UTF_8));
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        byte[] magicBytes = new byte[LicenseEntity.MAGIC_NUM.length];
        inputStream.read(magicBytes);
        checkBytes(magicBytes, LicenseEntity.MAGIC_NUM);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        LicenseEntity entity = (LicenseEntity) objectInputStream.readObject();

        checkSplitBytes(objectInputStream, entity);
        long applyTime = objectInputStream.readLong();
        if (applyTime > System.currentTimeMillis()) {
            throw new LicenseException("invalid license");
        }
        long expireTime = objectInputStream.readLong();
        if (expireTime != entity.getExpireTime()) {
            throw new LicenseException("invalid license");
        }
        if (expireTime < System.currentTimeMillis()) {
            throw new LicenseException("license expire");
        }
        checkSplitBytes(objectInputStream, entity);

        byte[] publicKey = new byte[objectInputStream.read()];
        objectInputStream.read(publicKey);
        checkBytes(publicKey, entity.getPublicKeys());
        checkSplitBytes(objectInputStream, entity);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int size = 0;
        while ((size = objectInputStream.read()) != 0) {
            byte[] part = new byte[size];
            objectInputStream.read(part);
            byte[] decodeData = decryptByPublicKey(part, entity.getPublicKeys());
            byteArrayOutputStream.write(decodeData);
            checkSplitBytes(objectInputStream, entity);
            if (objectInputStream.readLong() != expireTime % decodeData.length) {
                throw new LicenseException("invalid license");
            }

        }

        byte[] data = byteArrayOutputStream.toByteArray();
        if (!Md5.md5(data).equals(entity.getMd5())) {
            throw new LicenseException("invalid license");
        }
        entity.setData(data);
        return entity;
    }

    private void checkSplitBytes(InputStream objectInputStream, LicenseEntity protocol) throws IOException {
        byte[] splitBytes = new byte[protocol.getSplitFlag().length];
        objectInputStream.read(splitBytes);
        for (int i = 0; i < splitBytes.length; i++) {
            if (splitBytes[i] != protocol.getSplitFlag()[i]) {
                throw new LicenseException("invalid license");
            }
        }
    }

    private void checkBytes(byte[] b1, byte[] b2) {
        if (b1.length != b2.length) {
            throw new LicenseException("invalid license");
        }
        for (int i = 0; i < b1.length; i++) {
            if (b1[i] != b2[i]) {
                throw new LicenseException("invalid license");
            }
        }
    }

    public void setExpireStrategy(RuntimeExpireStrategy expireStrategy) {
        this.expireStrategy = expireStrategy;
    }
}
