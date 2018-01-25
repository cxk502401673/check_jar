package com.yuanwang.task.job;

import com.yuanwang.util.*;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author cxk
 *
 */
public class JobServer implements Runnable {
    private static Logger logger = Logger.getLogger(JobServer.class);

    private ExecutorService executorService = null;

    @Override
    public void run() {
        try {
            logger.info("我是定时执行的任务");
            //采用newFixedThreadPool
            if(executorService==null){

                String numString=Config.getString(MyWebConstant.THREADPOOlNUM,String.valueOf(SystemInfoUtil.cupCore() * 2));
                Integer number=Integer.valueOf(numString);
                ExecutorService executorService = Executors.newFixedThreadPool(number==0?SystemInfoUtil.cupCore() * 2:number);
            }


            doWork(executorService);

        } catch (Exception e) {

            logger.error(e);
        } finally {


        }

    }

    private void doWork(ExecutorService executorService) throws Exception {
        List<Map<String, Object>> listSystemALL = SQLDetailDao.getList(DBCPUtil.getConnection(), DataSQL.CK_ListSystemALL);
        //获取当前系统的检查任务ID
        Object maxCheckConfigId = SQLDetailDao.getForObject(DBCPUtil.getConnection(), DataSQL.checkConfigId);
        //获取检查项信息
        Map<String, Object> configInfo = com.yuanwang.util.SQLDetailDao.getMap(DBCPUtil.getConnection(), DataSQL.listCheckConfig, maxCheckConfigId);
        if (maxCheckConfigId != null && configInfo != null && listSystemALL.size() > 0) {
            for (Map<String, Object> systemMap : listSystemALL) {
                String ip = String.valueOf(systemMap.get("databaseip"));
                String port = String.valueOf(systemMap.get("port"));
                String username = String.valueOf(systemMap.get("loginname"));
                String password = String.valueOf(systemMap.get("databasepassword"));
                String databaseName = String.valueOf(systemMap.get("databasename"));
                String groupId = String.valueOf(systemMap.get("groupid"));
                String isEnabled = String.valueOf(systemMap.get("isdisabled"));
                executorService.submit(new DataSync(maxCheckConfigId, configInfo,
                         ip, ip, port, username, password, databaseName, groupId, isEnabled));


            }

        }
    }

}
