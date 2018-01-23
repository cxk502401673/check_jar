package com.yuanwang.util;

import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SQLDetailDao {
   private static Logger logger  =  Logger.getLogger(SQLDetailDao. class );
    /*******
     * 返回单个值
     ************/
    public static Object getForObject(Connection conn, String sql, Object... objects) {
        Object object = null;
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            for (int i = 0; i < objects.length; i++) {
                statement.setObject(i + 1, objects[i]);
            }
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount(); //获取返回的列数
            while (resultSet.next()) {
                if (columnCount == 1) {
                    String name = resultSetMetaData.getColumnName(1).toLowerCase();//获取返回的列名
                    Object value = resultSet.getObject(1);
                    object = value;
                }
            }
            statement.close();
            resultSet.close();
        } catch (Exception e) {
            object = null;
            logger.error(e);
        }
        return object;
    }

    /*******
     * 返回list集合
     **********/
    public static List<Map<String, Object>> getList(Connection conn, String sql, Object... objects) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            for (int i = 0; i < objects.length; i++) {
                statement.setObject(i + 1, objects[i]);
            }
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount(); //获取返回的列数
            while (resultSet.next()) {
                Map<String, Object> objMap = new LinkedHashMap<String, Object>();
                for (int i = 0; i < columnCount; i++) {
                    String name = resultSetMetaData.getColumnName(i + 1).toLowerCase();//获取返回的列名
                    Object value = resultSet.getObject(i + 1);
                    objMap.put(name, value);
                }
                list.add(objMap);
            }
            statement.close();
            resultSet.close();
        } catch (Exception e) {
            logger.error(e);
        }
        return list;
    }

    /*********
     * 返回map一个对象
     *****/
    public static Map<String, Object> getMap(Connection conn, String sql, Object... objects) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            for (int i = 0; i < objects.length; i++) {
                statement.setObject(i + 1, objects[i]);
            }
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount(); //获取返回的列数
            while (resultSet.next()) {
                for (int i = 0; i < columnCount; i++) {
                    String name = resultSetMetaData.getColumnName(i + 1).toLowerCase();//获取返回的列名
                    Object value = resultSet.getObject(i + 1);
                    map.put(name, value);
                }
            }
            statement.close();
            resultSet.close();
        } catch (Exception e) {
            logger.error(e);
        }
        return map;
    }

    /********
     * 数据更新
     ****/
    public static int sqlUpdate(Connection conn, String sql, Object... objects) {
        int row = 0;
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            for (int i = 0; i < objects.length; i++) {
                statement.setObject(i + 1, objects[i]);
            }

            row = statement.executeUpdate();
            statement.close();
        } catch (Exception e) {
            logger.error(e);
        }
        return row;
    }

    //批量插入
    public static void sqlUpdate2(Connection conn, String sql, Object... objects) {
        int row[] = null;
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            conn.setAutoCommit(false);//1,首先把Auto commit设置为false,不让它自动提交
            for (int i = 0; i < objects.length; i++) {
                statement.setObject(i + 1, objects[i]);

            }
            statement.addBatch();
            row = statement.executeBatch();
            conn.setAutoCommit(true);//1,首先把Auto commit设置为false,不让它自动提交
            //row=statement.executeUpdate();
            conn.commit();
            statement.close();

        } catch (Exception e) {
            logger.error(e);
        }
        //return row;
    }

    /********
     * 获取所有的检查项
     *
     * @param ip
     * @param port
     * @param username
     * @param password
     * @param databaseName
     * @param type
     * @return
     */
    public static Connection getConnection(String ip, String port, String username, String password, String databaseName, int type) {
        boolean ishost=PTP_CHECK.isHostConnectable(ip, Integer.parseInt(port));
        if(ishost){
            try {
                String className = "";
                // 数据库驱动
                if (type == 1) {// Oracle
                    className = "oracle.jdbc.OracleDriver";
                } else if (type == 2) {// sqlServer
                    className = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                } else if (type == 3) {// mysql
                    className = "com.mysql.jdbc.Driver";
                }
                Class.forName(className);
                try {
                    Connection conn;
                    String url = "";
                    String userName = "";
                    // 数据库访问链接
                    if (type == 1) {// Oracle
                        url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + databaseName;
                        userName = username;
                    } else if (type == 2) {// sqlServer
                        url = "jdbc:sqlserver://" + ip + ":" + port + ";databaseName=" + databaseName;
                        userName = username;
                    } else if (type == 3) {// mysql
                        url = "jdbc:mysql://" + ip + ":" + port + "/" + databaseName;
                        userName = username;
                    }
                    // 数据库连接超时控制
                    DriverManager.setLoginTimeout(5);// 秒
                    conn = DriverManager.getConnection(url, userName, password);
                    return conn;
                } catch (Exception e) {
                    logger.error(e);
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
        }else{
            logger.error( "getConnection-->" +ip+":"+port+"端口连接失败！");
            return null;
        }

    }

    /***
     * 获取数据库连接
     *
     * @param className
     * @param url
     * @param userName
     * @param passWord
     * @return
     */
    public Connection getConnection(String className, String url, String userName, String passWord) {
        logger.info("getConnection"+ className + "===" + url + "===" + userName + "===" + passWord);

        try {
            try {
                Class.forName(className);
                // 数据库连接超时控制
                DriverManager.setLoginTimeout(2);// 秒
                Connection conn = DriverManager.getConnection(url, userName, passWord);
                return conn;
            } catch (Exception e) {
                logger.error("getConnection-->" + e.getMessage());
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
