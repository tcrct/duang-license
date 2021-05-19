package com.duangframework.license.server;

import com.duangframework.license.client.LicenseEntity;

import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

/**
 * @author duang
 */
class SourceLicense {
    /**
     * 申请时间
     */
    public static final String PROPERTY_APPLY_DATE = "applyDate";
    /**
     * 过期时间
     */
    public static final String PROPERTY_EXPIRE_DATE = "expireDate";
    /**
     * 原文
     */
    public static final String PROPERTY_BASE64_CONTENT = "base64Content";
    /**
     * 公钥
     */
    public static final String PROPERTY_PUBLIC_KEY = "publicKey";
    /**
     * 私钥
     */
    public static final String PROPERTY_PRIVATE_KEY = "privateKey";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 格式化后的申请日期
     */
    private final String applyDate;
    /**
     * 格式化后的截止日期
     */
    private final String expireDate;
    /**
     * 公钥
     */
    private final String publicKey;
    /**
     * 私钥
     */
    private final String privateKey;
    /**
     * Base64处理后的原文
     */
    private final String base64Content;

    public SourceLicense(LicenseEntity entity, KeyPair keyPair) {
        Base64.Encoder base64Encoder = Base64.getEncoder();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        this.applyDate = sdf.format(new Date(entity.getApplyTime()));
        this.expireDate = sdf.format(new Date(entity.getExpireTime()));
        this.base64Content = base64Encoder.encodeToString(entity.getData());
        privateKey = base64Encoder.encodeToString(RasUtil.getPrivateKey(keyPair));
        publicKey = base64Encoder.encodeToString(RasUtil.getPublicKey(keyPair));
    }

    public String getPublicKey() {
        return publicKey;
    }


    public String getPrivateKey() {
        return privateKey;
    }


    public String getBase64Content() {
        return base64Content;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public String getExpireDate() {
        return expireDate;
    }
}
