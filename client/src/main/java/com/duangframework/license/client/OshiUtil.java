package com.duangframework.license.client;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.OperatingSystem;

import java.util.List;

/**
 * @author duang
 */
public class OshiUtil {
    public static String getSystemInfo() {
        StringBuilder sb = new StringBuilder();
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        // CPU
        CentralProcessor processor = hardware.getProcessor();
        processor.getProcessorIdentifier();
        sb.append(processor.toString());
        OperatingSystem os = systemInfo.getOperatingSystem();
        sb.append(os);
        //硬盘信息
        List<HWDiskStore> diskStoreList = hardware.getDiskStores();
        for (HWDiskStore hwDiskStore : diskStoreList) {
            sb.append(hwDiskStore.getSerial());
        }
        // 网卡
        List<NetworkIF> networkIFList = hardware.getNetworkIFs();
        for (NetworkIF networkIF : networkIFList) {
            // 只取eth0
            if ("eth0".equals(networkIF.getName())) {
                sb.append(networkIF.getMacaddr());
            }
        }
        // 分区信息
//        FileSystem fileSystem = os.getFileSystem();
//        List<OSFileStore> osFileStoreList = fileSystem.getFileStores();
//        for (OSFileStore fileStore : osFileStoreList) {
//            if (!"CDFS".equalsIgnoreCase(fileStore.getType())) {
//                sb.append(fileStore.getUUID());
//            }
//        }
        return sb.toString().replace(" ", "").replace("\n","");
    }

    public static void main(String[] args) {
        System.out.println( OshiUtil.getSystemInfo() );
    }
}
