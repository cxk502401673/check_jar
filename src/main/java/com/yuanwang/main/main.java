package com.yuanwang.main;


import com.yuanwang.task.FileListener;
import com.yuanwang.task.TaskSchedule;
import com.yuanwang.util.*;

import java.io.File;

/**
 * 入口
 */
public class main {

    public static void main(String[] args) {

        try {
           // FileListener.start(MyWebConstant.START_CONTROLL_PATH+ File.separator);
            TaskSchedule.start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }


    }


}
