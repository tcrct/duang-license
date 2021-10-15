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
    public static void main(String[] args) throws Exception{
//        InputStream inputStream = LicenseClientTest.class.getClassLoader().getResourceAsStream("license_revert.txt");
        InputStream inputStream = LicenseClientTest.class.getClassLoader().getResourceAsStream("duang-license.txt");
        License license = new License();
        LicenseEntity licenseData = license.loadLicense(inputStream,
                Base64.getDecoder().decode(
                        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCVm6HptT8o7wo3Pc+yryp0QiGdI0czyYUpZAnaENapfUW3kLyXf3yisVjRYFJq9YtCr1TUDbjzF/fAge1EkjSUkknfO/eVb5GC3CpdqTiAvhOvQt6wGS6QOhP0BIAStdTQmYUuSOnzCBp+tteTwCG6hjF5IaedgopuThi2KX4oMQIDAQAB"));
        String data = new String(licenseData.getData());
//        String systemInfoStr = OshiUtil.getSystemInfo();
//        System.out.println(systemInfoStr);
//        String content= "Intel(R)Xeon(R)Platinum8163CPU@2.50GHz1physicalCPUpackage(s)2physicalCPUcore(s)4logicalCPU(s)Identifier:Intel64Family6Model85Stepping4ProcessorID:0F81FBFF00500654Microarchitecture:Skylake(Server)GNU/LinuxCentOSLinux7.6.1810(Core)build3.10.0-957.21.3.el7.x86_64unknownunknown00:16:3e:0e:0d:cb";
//        String content= "Intel(R)Xeon(R)Platinum8269CYCPU@2.50GHz1physicalCPUpackage(s)2physicalCPUcore(s)4logicalCPU(s)Identifier:Intel64Family6Model85Stepping7ProcessorID:57060500FFFB8B0FMicroarchitecture:CascadeLakeGNU/LinuxCentOSLinux7.6.1810(Core)build3.10.0-957.21.3.el7.x86_64unknownunknown00:16:3e:19:47:b6";
//        String content= "IntelXeonProcessor(Skylake,IBRS)4physicalCPUpackage(s)4physicalCPUcore(s)4logicalCPU(s)Identifier:Intel64Family6Model85Stepping4ProcessorID:0781FBFF00500654Microarchitecture:Skylake(Server)GNU/LinuxCentOSLinux7.5.1804(Core)build3.10.0-862.el7.x86_64QM00001unknown52:54:00:b3:98:9d";
        //String content = "Intel(R)Xeon(R)Gold6258RCPU@2.70GHz2physicalCPUpackage(s)8physicalCPUcore(s)8logicalCPU(s)Identifier:Intel64Family6Model85Stepping7ProcessorID:57060500FFFB8B0FMicroarchitecture:CascadeLakeGNU/LinuxCentOSLinux7.1.1503(Core)build3.10.0-229.el7.x86_64QM00002unknownunknownunknownfa:b0:ee:7c:2f:00";
        //String content = "AMDRyzen51600XSix-CoreProcessor1physicalCPUpackage(s)6physicalCPUcore(s)12logicalCPU(s)Identifier:AuthenticAMDFamily23Model1Stepping1ProcessorID:178BFBFF00800F11Microarchitecture:ZenMicrosoftWindows10.0build190430000_0000_0000_0000_8CE3_8E03_004E_A1CF.WQ2018ABCD1577WD-WCC6Y5DC2KHN";
        //String content="Intel(R)Core(TM)i5-7500CPU@3.40GHz1physicalCPUpackage(s)4physicalCPUcore(s)4logicalCPU(s)Identifier:Intel64Family6Model158Stepping9ProcessorID:BFEBFBFF000906E9Microarchitecture:KabyLakeMicrosoftWindows6.3build9600WD-WMC6M0H014PXWD-WMC6M0E2HS16YX70202101250296";
        String content="Intel(R)Xeon(R)Silver4210CPU@2.20GHz2physicalCPUpackage(s)20physicalCPUcore(s)40logicalCPU(s)Identifier:Intel64Family6Model85Stepping7ProcessorID:57060500FFFBEBBFMicroarchitecture:CascadeLakeGNU/LinuxCentOSLinux7.9.2009(Core)build3.10.0-1160.36.2.el7.x86_642WXYCPLC00116546HA026b07b250ddb997002882ca830588cc7dunknownunknownunknown";
        System.out.println(data.equals(content) ? "200" : "500");
        System.out.println(data);
    }
}
