package com.yuanwang.task.job;

import com.yuanwang.util.DBCPUtil;
import com.yuanwang.util.InsertTable;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JobServer implements Runnable{
    private static Logger logger  =  Logger.getLogger(JobServer. class );
    Connection conn;
    ResultSet rs;
    Statement st;
    @Override
    public void run() {
        try {
            logger.info("我是定时执行的任务");
            ExecutorService executorService = Executors.newCachedThreadPool();

             conn= DBCPUtil.getLowConnection();
             st = conn.createStatement();
            String sql = "select * from wx_account ";
             rs= st.executeQuery(sql);
            //5）处理结果
            while(rs.next()){
                System.out.print(rs.getObject(1)+"\t");
                System.out.print(rs.getObject(2)+"\t");
                System.out.print(rs.getObject(3)+"\t");
                System.out.print(rs.getObject(4)+"\t");
                System.out.print(rs.getObject(5)+"\t");
                System.out.print(rs.getObject(6)+"\t"); //共输出要显示的字段名信息
            }
    } catch (Exception e) {
            
            logger.error(e);
        }finally {
            try {
                rs.close();
                st.close();
                conn.close();
            } catch (SQLException e) {
                logger.error(e );
                
            }

        }

    }
}
