package com.duangframework.license.client;

import oshi.SystemInfo;
import oshi.hardware.*;
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
        String serverInfo =  sb.toString().replace(" ", "").replace("\n","");
//        System.out.println(serverInfo);
        // 要将进程ID去掉，因为这个每次断电再开机，会发生变化的
        String processorID = "ProcessorID:"+processor.getProcessorIdentifier().getProcessorID();
        serverInfo = serverInfo.replace(processorID, "");
        return  serverInfo;
    }

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n#######请复制以下字符串，并发送给厂商管理员，生成系统许可证#######\n\n");
        sb.append( OshiUtil.getSystemInfo() );
        sb.append("\n\n###########################\n");
        System.out.println(sb);
    }
}
