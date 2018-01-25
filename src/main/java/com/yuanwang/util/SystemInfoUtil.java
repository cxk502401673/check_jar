package com.yuanwang.util;

import java.util.Properties;

public class SystemInfoUtil {
    /**
     * 返回到Java虚拟机的可用的处理器数量
     * @return
     */
    public static int cupCore(){
        return Runtime.getRuntime().availableProcessors();

    }


//      System.out.print("虚拟机内存总量:");
//        System.out.println(Runtime.getRuntime().totalMemory());//totalMemory()获取java虚拟机中的内存总量
//        System.out.print("虚拟机空闲内存量:");
//        System.out.println(Runtime.getRuntime().freeMemory());//freeMemory()获取java虚拟机中的空闲内存量
//        System.out.print("虚拟机使用最大内存量:");
//        System.out.println(Runtime.getRuntime().maxMemory());//maxMemory()获取java虚拟机试图使用的最大内存量
}
