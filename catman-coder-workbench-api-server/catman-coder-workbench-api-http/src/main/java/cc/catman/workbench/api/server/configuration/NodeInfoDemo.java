package cc.catman.workbench.api.server.configuration;

import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Duration;

@Component
public class NodeInfoDemo  {

    @Resource
    private ConfigurableEnvironment environment;

    @PostConstruct
    public void e(){
        // os.version -> 14.0
        // os.name -> Mac OS X
        // os.arch -> x86_64
        //user.name -> catman
        // java.version -> 1.8.0_131
        // java.vm.specification.version -> 1.8
        // user.dir -> /Users/catman/Workbench/catman-coder-workbench/catman-coder-workbench-api-server/catman-coder-workbench-api-http

        System.out.println("系统版本:"+System.getProperty("os.version"));
        System.out.println("系统名称:"+System.getProperty("os.name"));
        System.out.println("系统架构:"+System.getProperty("os.arch"));
        System.out.println("用户名:"+System.getProperty("user.name"));
        System.out.println("java版本:"+System.getProperty("java.version"));
        System.out.println("java虚拟机规范版本:"+System.getProperty("java.vm.specification.version"));
        System.out.println("当前工作目录:"+System.getProperty("user.dir"));




        // 获取cpu和内存信息
        System.out.println(environment.getProperty("spring.application.name"));
    }


    public static void main(String[] args) {
        // 创建 SystemInfo 对象
        SystemInfo si = new SystemInfo();
        OperatingSystem operatingSystem = si.getOperatingSystem();
        HardwareAbstractionLayer hardware = si.getHardware();
        CentralProcessor processor = hardware.getProcessor();
        // 获取操作系统信息
        System.out.println("操作系统:"+operatingSystem);
        System.out.println("系统位数:"+operatingSystem.getBitness());
        System.out.println("系统描述:"+operatingSystem.getFamily());
        System.out.println("系统制造商:"+operatingSystem.getManufacturer());
        System.out.println("系统版本:"+operatingSystem.getVersionInfo());
        // 获取cpu信息
        System.out.println("cpu数量:"+processor.getPhysicalProcessorCount());
        OSProcess currentProcess = operatingSystem.getCurrentProcess();
        System.out.println( "当前程序cpu用量:"+currentProcess.getProcessCpuLoadCumulative());
        // 获取cpu温度
        System.out.println("cpu温度:"+hardware.getSensors().getCpuTemperature());
       // 获取内存信息
        System.out.println("内存总量:"+hardware.getMemory().getTotal()/1024/1024/1024+"G");
        System.out.println("可用内存:"+hardware.getMemory().getAvailable()/1024/1024/1024+"G");
        // 获取gpu信息
        System.out.println("显卡数量:"+hardware.getGraphicsCards().size());
        hardware.getGraphicsCards().forEach(graphicsCard -> {
            System.out.println("显卡名称:"+graphicsCard.getName());
        });
        // 获取磁盘信息
        System.out.println("磁盘数量:"+hardware.getDiskStores().size());
        // 获取网络信息
        System.out.println("网络数量:"+hardware.getNetworkIFs().size());
        // 获取进程信息
        System.out.println("进程数量:"+operatingSystem.getProcessCount());
        // 获取文件系统信息
        System.out.println("文件系统数量:"+operatingSystem.getFileSystem().getFileStores().size());
        // 获取服务信息
        System.out.println("服务数量:"+operatingSystem.getServices().size());

        System.out.println("系统版本:"+System.getProperty("os.version"));
        System.out.println("系统名称:"+System.getProperty("os.name"));
        System.out.println("系统架构:"+System.getProperty("os.arch"));
        System.out.println("用户名:"+System.getProperty("user.name"));
        System.out.println("java版本:"+System.getProperty("java.version"));
        System.out.println("java虚拟机规范版本:"+System.getProperty("java.vm.specification.version"));
        System.out.println("当前工作目录:"+System.getProperty("user.dir"));
    }
}
