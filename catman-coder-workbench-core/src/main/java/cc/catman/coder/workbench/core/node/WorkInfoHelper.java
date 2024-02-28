package cc.catman.coder.workbench.core.node;


import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.OperatingSystem;

import java.util.List;

/**
 * 获取工作节点信息的工具类
 * 其主要用于获取物理节点上的各种信息,比如,cpu,内存,磁盘等
 * 这些信息被分为两类:
 * - 一类是固定信息,比如cpu型号,内存大小,磁盘大小等
 * - 一类是动态信息,比如cpu使用率,内存使用率,磁盘使用率等
 * 除了用于获取信息之外,该工具类还提供一些常见的计算方法,比如计算cpu使用率,内存使用率等
 * 该工具类的实现,应该是线程安全的
 */
public final class WorkInfoHelper {
    public static SystemInfo systemInfo=new SystemInfo();

    public static WorkSystemInfo getWorkInfo(){
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        CentralProcessor processor = hardware.getProcessor();
        GlobalMemory memory = hardware.getMemory();
        List<NetworkIF> networkIFs = hardware.getNetworkIFs();

        WorkSystemInfo workSystemInfo = WorkSystemInfo.builder()
                .systemName(operatingSystem.getFamily())
                .systemVersion(operatingSystem.getVersionInfo().getVersion())
                .cpuName(processor.getProcessorIdentifier().getName())
                .cpuModel(processor.getProcessorIdentifier().getModel())
                .cpuPhysicalCount(processor.getPhysicalProcessorCount())
                .cpuLogicalCount(processor.getLogicalProcessorCount())
                .cpuFrequency(processor.getProcessorIdentifier().getVendorFreq())
                .memoryTotal(memory.getTotal())
                .diskTotal(hardware.getDiskStores().stream().mapToLong(HWDiskStore::getSize).sum())
                .gpuTotal(hardware.getGraphicsCards().size())
                .macAddress(networkIFs.stream().map(NetworkIF::getMacaddr).findFirst().orElse(null))
                .ipAddresses(networkIFs.stream().map(NetworkIF::getIPv4addr).findFirst().orElse(null))
                .build();

        // 当前一定是java进程,所以填充java信息
        workSystemInfo.setJavaVersion(System.getProperty("java.version"));
        workSystemInfo.setJavaVendor(System.getProperty("java.vendor"));
        workSystemInfo.setJavaWorkSpace(System.getProperty("java.home"));
        workSystemInfo.setDynamicInfo(getDynamicInfo());
        return workSystemInfo;
    }

    public static WorkDynamicInfo getDynamicInfo(){
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        CentralProcessor processor = hardware.getProcessor();
        GlobalMemory memory = hardware.getMemory();
        List<NetworkIF> networkIFs = hardware.getNetworkIFs();

        // 获取cpu使用信息
        return WorkDynamicInfo.builder().build();
    }

    public static void main(String[] args) {
        System.out.println(getWorkInfo());
    }
}
