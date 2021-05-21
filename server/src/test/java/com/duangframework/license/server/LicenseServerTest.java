
package com.duangframework.license.server;

import com.duangframework.license.client.OshiUtil;

import java.util.Base64;

/**
 * @author duang
 * @version V1.0 , 2020/3/26
 */
public class LicenseServerTest {
    public static void main(String[] args) throws Exception {
        LicenseServer server = new LicenseServer();
        String content = OshiUtil.getSystemInfo();
//        String content = "AMDRyzen51600XSix-CoreProcessor1physicalCPUpackage(s)6physicalCPUcore(s)12logicalCPU(s)Identifier:AuthenticAMDFamily23Model1Stepping1ProcessorID:178BFBFF00800F11Microarchitecture:ZenMicrosoftWindows10.0build190420000_0000_0000_0000_8CE3_8E03_004E_A1CF.WQ2018ABCD1577WD-WCC6Y5DC2KHN";
        byte[] privateKey = Base64.getDecoder().decode("MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJWboem1PyjvCjc9z7KvKnRCIZ0jRzPJhSlkCdoQ1ql9RbeQvJd/fKKxWNFgUmr1i0KvVNQNuPMX98CB7USSNJSSSd8795VvkYLcKl2pOIC+E69C3rAZLpA6E/QEgBK11NCZhS5I6fMIGn6215PAIbqGMXkhp52Cim5OGLYpfigxAgMBAAECgYAOpqkajAddaNtlQYZfh0vvCrLkAppsdeW2gfd9BX0gKAZ1zZTKeU+pVxjNmFM56kXzB9yUz6s3EzfOAGtN9ct0QmnrHoFmUMKXbUe+QkrDvbpbhegEr3smpSYDFyhI1PZib7Ltnvy930DeQPEY5ZuY5w3Omki/qOfYJ1a3qczYiQJBAOcz/YeY2AkAOvI6ky/ive9JQD+DJLgGafrbODcn2pwv7tHDw8wa2GkyVDH+TjvsVOst0bsYEQRMkClzLw9oGVcCQQClp1TNzD0x4SD9CUH5+OabfbgoJwdKgc/q88xE35lMA4UT4wdUbZV5s7GBw2thuc3YpwxQK27AJ/eLXORWxm23AkAKk5BHgbBwSPhpWFCfYin6JkmwHhmx6Wkzto+Nxl7zwiWUpvXwAlJgDZNYbh+6EgeYcpIjkuhEYBRAMSq387UlAkBgzQSQck48TJtzYiqMwbc4m+G2jQAEuRDf8nGmuaciNVhZw6wv2Q2lHa2X77NbWzF/7jYSzx6b6X8NkE3aq3/HAkAx3nyF7dTjRyHl2ByaWC24o4zjysgAau3rZ/fDldTmak3VJ6eo6t049TbP/7EeSqLqLbyae5OUNEuQLifXqhHW");
//        server.createLicense(content.getBytes(), new Date(System.currentTimeMillis() + 3600 * 1000), privateKey);
        server.createLicense(content.getBytes(), null, privateKey);
    }
}
