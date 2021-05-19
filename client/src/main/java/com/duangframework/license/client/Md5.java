package com.duangframework.license.client;

import java.security.MessageDigest;

/**
 * @author duang
 */
public final class Md5 {
    private Md5() {

    }

    public static String md5(byte[] data) {
        final int m = 2;
        final int n = 4;
        final int a = 0xf;
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(data);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * m];
            int k = 0;
            byte byte0 = 0;
            for (int i = 0; i < j; i++) {
                byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> n & a];
                str[k++] = hexDigits[byte0 & a];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}