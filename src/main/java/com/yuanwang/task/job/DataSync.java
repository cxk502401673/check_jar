package com.yuanwang.task.job;


import java.sql.Connection;
import java.util.Map;

/**
 * 数据同步任务
 */
public class DataSync implements Runnable {
    private Integer checkConfigId;
    private Map<String,Object> config;
    private Connection conn;
    private String ip;
    private String taskName;
    private String port;
    private String userName;
    private String password;
    private String databaseName;
    private String groupId;
    private String isEnabled;

    public DataSync(Integer checkConfigId, Map<String, Object> config, Connection conn, String ip, String taskName, String port, String userName, String password, String databaseName, String groupId, String isEnabled) {
        this.checkConfigId = checkConfigId;
        this.config = config;
        this.conn = conn;
        this.ip = ip;
        this.taskName = taskName;
        this.port = port;
        this.userName = userName;
        this.password = password;
        this.databaseName = databaseName;
        this.groupId = groupId;
        this.isEnabled = isEnabled;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5000);
        } catch ( Exception e) {
            e.printStackTrace();
        }

    }
}
