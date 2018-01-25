package com.yuanwang.util;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SQLDetailDao {
     private static Logger logger  =  Logger.getLogger(SQLDetailDao. class );
     private static BasicDataSource dataSource=new BasicDataSource();

    /*******
     * 返回单个值
     ************/
    public static Object getForObject(Connection conn, String sql, Object... objects) {
        Object object = null;
        PreparedStatement statement=null;
        ResultSet resultSet=null;
        try {
            statement= conn.prepareStatement(sql);
            for (int i = 0; i < objects.length; i++) {
                statement.setObject(i + 1, objects[i]);
            }
            resultSet = statement.executeQuery();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount(); //获取返回的列数
            while (resultSet.next()) {
                if (columnCount == 1) {
                    String name = resultSetMetaData.getColumnName(1).toLowerCase();//获取返回的列名
                    Object value = resultSet.getObject(1);
                    object = value;
                }
            }

        } catch (Exception e) {
            object = null;
            logger.error(e);
            if(conn!=null){
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    logger.error(e1);

                }
            }
        }finally {
            try {
                statement.close();
                resultSet.close();
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return object;
    }

    /*******
     * 返回list集合
     **********/
    public static List<Map<String, Object>> getList(Connection conn, String sql, Object... objects) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        PreparedStatement statement=null;
        ResultSet resultSet=null;
        try {
             statement = conn.prepareStatement(sql);
            for (int i = 0; i < objects.length; i++) {
                statement.setObject(i + 1, objects[i]);
            }
              resultSet= statement.executeQuery();
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

        } catch (Exception e) {
            logger.error(e);
            if(conn!=null){
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    logger.error(e1);

                }
            }
        }finally {
            try {
                statement.close();
                resultSet.close();
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return list;
    }

    /*********
     * 返回map一个对象
     *****/
    public static Map<String, Object> getMap(Connection conn, String sql, Object... objects) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        try {
             statement = conn.prepareStatement(sql);
            for (int i = 0; i < objects.length; i++) {
                statement.setObject(i + 1, objects[i]);
            }
            resultSet = statement.executeQuery();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            //获取返回的列数
            int columnCount = resultSetMetaData.getColumnCount();
            while (resultSet.next()) {
                for (int i = 0; i < columnCount; i++) {
                    //获取返回的列名
                    String name = resultSetMetaData.getColumnName(i + 1).toLowerCase();
                    Object value = resultSet.getObject(i + 1);
                    map.put(name, value);
                }
            }

        } catch (Exception e) {
            logger.error(e);

            if(conn!=null){
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    logger.error(e1);

                }
            }
        }finally {
            try {
                resultSet.close();
                statement.close();
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return map;
    }

    /********
     * 数据更新
     ****/
    public static int sqlUpdate(Connection conn, String sql, Object... objects) {
        int row = 0;
        PreparedStatement statement=null;
        try {
             statement = conn.prepareStatement(sql);
            for (int i = 0; i < objects.length; i++) {
                statement.setObject(i + 1, objects[i]);
            }
            row = statement.executeUpdate();

        } catch (Exception e) {
            logger.error(e);
            if(conn!=null){
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }finally {
            try {
                statement.close();
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
                if(conn!=null){
                    try {
                        conn.rollback();
                    } catch (SQLException e1) {
                        logger.error(e1);

                    }
                }
            }
        }
        return row;
    }


    /**
     *  获取下级数据库连接 关闭事物自动提交
     * @param ip
     * @param port
     * @param username
     * @param password
     * @param databaseName
     * @param type
     * @return
     * @throws Exception
     */
    public static Connection getConnection(String ip, String port, String username, String password, String databaseName, int type) throws Exception{

        boolean ishost=PTP_CHECK.isHostConnectable(ip, Integer.parseInt(port));
        if(ishost){
            if(dataSource==null){
                String className = "";
                // 数据库驱动
                if (type == 1) {
                    className = "oracle.jdbc.OracleDriver";
                } else if (type == 2) {
                    className = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                } else if (type == 3) {
                    className = "com.mysql.jdbc.Driver";
                }
                Class.forName(className);
                String url = "";
                String userName = "";
                // 数据库访问链接
                if (type == 1) {
                    url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + databaseName;
                    userName = username;
                } else if (type == 2) {
                    url = "jdbc:sqlserver://" + ip + ":" + port + ";databaseName=" + databaseName;
                    userName = username;
                } else if (type == 3) {
                    url = "jdbc:mysql://" + ip + ":" + port + "/" + databaseName;
                    userName = username;
                }
                dataSource=new BasicDataSource();
                //数据库驱动
                dataSource.setDriverClassName(className);
                dataSource.setUsername(username);
                dataSource.setPassword(password);
                //"jdbc:mysql://localhost:3306/yecheck?useUnicode=true&characterEncoding=utf-8&useSSL=false"
                //连接url
                dataSource.setUrl(url);
                // 初始的连接数；
                dataSource.setInitialSize(10);
                //最大连接数
                dataSource.setMaxTotal(100);
                // 设置最大空闲连接
                dataSource.setMaxIdle(80);
                // 设置最大等待时间
                dataSource.setMaxWaitMillis(6000);
                // 设置最小空闲连接
                dataSource.setMinIdle(10);

               Connection conn=dataSource.getConnection();
                conn.setAutoCommit(false);
                return conn;
            }else{
                Connection conn=dataSource.getConnection();
                conn.setAutoCommit(false);
                return conn;
            }
        }else{
            logger.error( "getConnection-->" +ip+":"+port+"端口连接失败！");
            return null;
        }

    }


}
