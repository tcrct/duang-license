/*
 * Copyright (c) 2020-2020, org.smartboot. All rights reserved.
 * project name: smart-license
 * file name: LicenseTest.java
 * Date: 2020-03-26
 * Author: sandao (zhengjunweimail@163.com)
 */

package com.duangframework.license.server;

import java.util.Base64;

/**
 * @author 三刀
 * @version V1.0 , 2020/3/26
 */
public class LicenseServerTest {
    public static void main(String[] args) throws Exception {
        LicenseServer server = new LicenseServer();
        String content = OshiUtil.getSystemInfo();
        byte[] privateKey = Base64.getDecoder().decode("MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKJgdtSCbftLiqgxW2LsZPcru4YVWnh1YULMjItH5M0rEBA4fyRmYVBWWpG5tLRmGDuG4OcdS1KrRB9R8akeNQuWpkCHcE49PTYrPlZIo+rzUQxxOy+EOrgTgYWY9AGcVB7nXYeKmcvFP30eGXhmxql2CI/NEpRDkAs10mooBRPTAgMBAAECgYBFgU88VfxoBgT7S+1XYeMyHkkt8vP//ha89jvu3r5kyFse5mAeH6jmL6CjIj/kvJMA4BJjO5njcCY/1d7AyopflZ+p17+U9Cv3BajI6he5AMJvsdn/rwBg7Q48iRANpfPknzPYQe8r5uZwdd6ADrlkEpBRXMg6C9mreJpSTBe6KQJBANkcGE2gwb1TJs5T0EH7G3vNSMNPHqqEXfT8SfMGfC1x6I5BeN80q3jHRUgmjUD24wFmi4GZZ7tX2O9rm4ztn10CQQC/doFoCX1of7DAmLwdTaUcUZjOBQT4C5FAhVijLPcE6+qrMveqeoJHieZzcp0O20/Sy9S0OHT2DsER1LwYabzvAkEAlF9y7HpUbQKKnq8AwFbPrYfZlkBYAMh9PZO9JezkqFoUSEPOSxw/o0brvic6mY8gDDD8XhFspseeZeDwaRtrsQJBAIYunV62V+empc80u5Gl2vNP1FF+fw7/vFqFmasyViRi2mIFJEGb/jX41UNPrfa9iOHo7Dcp4f6YNIM0nmZVpI0CQCYV6Rkw6z+/ymN4rSncW7u68dB/EaCceBkOp7i3+5LWDZMDUkcOR8tfqugNzeg03CUhj4azXYwokv+QtvLJhAY=");
//        server.createLicense(content.getBytes(), new Date(System.currentTimeMillis() + 3600 * 1000), privateKey);
        server.createLicense(content.getBytes(), null, privateKey);
    }
}
