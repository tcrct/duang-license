package com.duangframework.license.server;

import java.io.FileOutputStream;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

/**
 * 创建公私钥对
 * @author duang
 */
public class KeyPairCreator {
    public static void main(String[] args) throws Exception {
        KeyPair keyPair = RasUtil.initKey();
        String publicKey = "publicKey: " + Base64.getEncoder().encodeToString(RasUtil.getPublicKey(keyPair));
        String privateKey = "privateKey: " + Base64.getEncoder().encodeToString(RasUtil.getPrivateKey(keyPair));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("--------- " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " ---------");
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append(publicKey);
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append(privateKey);
        stringBuilder.append(System.lineSeparator());
        stringBuilder.append("--------- END ---------");
        stringBuilder.append(System.lineSeparator());
        System.out.println(stringBuilder);
        FileOutputStream fileOutputStream = new FileOutputStream("keystore", true);
        fileOutputStream.write(stringBuilder.toString().getBytes());
        fileOutputStream.close();
    }
}
