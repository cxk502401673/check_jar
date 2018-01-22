package com.yuanwang.task.job;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JobServer implements Runnable{
    @Override
    public void run() {
        try {
            System.out.println("我是定时执行的任务");
            ExecutorService executorService = Executors.newCachedThreadPool();
//            for (int i = 1; i < 10000; i++) {
//                executorService.submit(new DataSync());
//            }


    } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
