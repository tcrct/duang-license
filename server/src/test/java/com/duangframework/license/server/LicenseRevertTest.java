/*
 * Copyright (c) 2020-2020, org.smartboot. All rights reserved.
 * project name: smart-license
 * file name: LicenseRevertTest.java
 * Date: 2020-04-14
 * Author: sandao (zhengjunweimail@163.com)
 */

package com.duangframework.license.server;

import java.io.IOException;
import java.text.ParseException;

/**
 * 根据源文件还原出License文件，防止用户丢失后重新生成
 * @author duang
 */
public class LicenseRevertTest {
    public static void main(String[] args) throws IOException, ParseException {
        String[] file = new String[]{"source.txt"};
        LicenseRevert.main(file);
    }
}
