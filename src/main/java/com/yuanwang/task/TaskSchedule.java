package com.yuanwang.task;

import com.yuanwang.task.job.JobServer;
import com.yuanwang.util.Config;
import com.yuanwang.util.MyWebConstant;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaskSchedule {

    /**
     * 以固定延迟时间进行执行
     * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
     */
    public static void executeFixedDelay(Long period) {
        //开启1个定期执行任务线程
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleWithFixedDelay(
                new JobServer(),
                0,
                period,
                TimeUnit.MILLISECONDS);
    }

    /**
     * 启动定时同步数据
     */
    public static void start(){
        Integer hour=Integer.valueOf(Config.getString(MyWebConstant.HOUR,"0"));
        Integer minute= Integer.valueOf(Config.getString(MyWebConstant.MINUTE,"0"));
        Integer second=Integer.valueOf(Config.getString(MyWebConstant.SECOND,"0"));
        Long hour_l=hour*60L*60L*1000L;
        Long minute_l=minute*60L*1000L;
        Long second_l=second*1000L;
        Long l=hour_l+ minute_l+second_l;
        if(l==0L){
            return;
        }
        executeFixedDelay(hour_l+ minute_l+second_l);
    }
}
