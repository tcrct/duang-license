/*
 * Copyright (c) 2020-2020, org.smartboot. All rights reserved.
 * project name: smart-license
 * file name: LicenseTest.java
 * Date: 2020-03-22
 * Author: sandao (zhengjunweimail@163.com)
 */

package com.duangframework.license.client;

import java.io.InputStream;
import java.util.Base64;

/**
 * @author daung
 */
public class LicenseClientTest {
    public static void main(String[] args) {
//        InputStream inputStream = LicenseClientTest.class.getClassLoader().getResourceAsStream("license_revert.txt");
        InputStream inputStream = LicenseClientTest.class.getClassLoader().getResourceAsStream("license.txt");
        License license = new License();
        LicenseEntity licenseData = license.loadLicense(inputStream,
                Base64.getDecoder().decode(
                        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCiYHbUgm37S4qoMVti7GT3K7uGFVp4dWFCzIyLR+TNKxAQOH8kZmFQVlqRubS0Zhg7huDnHUtSq0QfUfGpHjULlqZAh3BOPT02Kz5WSKPq81EMcTsvhDq4E4GFmPQBnFQe512HipnLxT99Hhl4ZsapdgiPzRKUQ5ALNdJqKAUT0wIDAQAB"));
        String data = new String(licenseData.getData());

        System.out.println(data);
    }
}
