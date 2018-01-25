package com.yuanwang.main;


import com.yuanwang.task.TaskSchedule;
import com.yuanwang.util.JarToolUtil;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;


/**
 * 入口
 */
public class main {
    private static Logger logger  =  Logger.getLogger(main. class );
    public static void main(String[] args) {
        System.out.println("测试代码----");
        try {
           // String path= JarToolUtil.getJarDir()+ File.separator+"conf"+File.separator+"log4j.properties";
            //PropertyConfigurator.configure(path);
           // FileListener.start(MyWebConstant.START_CONTROLL_PATH+ File.separator);
           // logger.info("开启数据同步");
           // TaskSchedule.start();
        } catch (Exception e) {
            logger.error(e);
        } finally {
        }


    }


}
