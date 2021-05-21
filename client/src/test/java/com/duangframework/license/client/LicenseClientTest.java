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
        InputStream inputStream = LicenseClientTest.class.getClassLoader().getResourceAsStream("duang-license.txt");
        License license = new License();
        LicenseEntity licenseData = license.loadLicense(inputStream,
                Base64.getDecoder().decode(
                        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCVm6HptT8o7wo3Pc+yryp0QiGdI0czyYUpZAnaENapfUW3kLyXf3yisVjRYFJq9YtCr1TUDbjzF/fAge1EkjSUkknfO/eVb5GC3CpdqTiAvhOvQt6wGS6QOhP0BIAStdTQmYUuSOnzCBp+tteTwCG6hjF5IaedgopuThi2KX4oMQIDAQAB"));
        String data = new String(licenseData.getData());

        System.out.println(data);
    }
}
