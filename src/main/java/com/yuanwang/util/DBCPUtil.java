package com.yuanwang.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;


import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class DBCPUtil {
    private static Logger logger  =  Logger.getLogger(DBCPUtil. class );
    private static Properties properties = new Properties();
    //private static Properties lowproperties = new Properties();
    private static DataSource dataSource;
    //private static DataSource lowDataSource;
    private static String ip;
    private static String port;
    private static String databaseName;

//    private static String low_ip;
//    private static String low_port;
//    private static String low_databaseName;

    //加载DBCP配置文件
    static{
        try{
            InputStream in = new FileInputStream(new File(MyWebConstant.START_CONTROLL_PATH + File.separator + MyWebConstant.PROPERTIES_FILE_NAME));
            properties.load(in);
        }catch(IOException e){
            logger.error(e);
           
        }

        try{
            ip=properties.getProperty(MyWebConstant.IP);
            port=properties.getProperty(MyWebConstant.PORT);
            databaseName=properties.getProperty(MyWebConstant.DATANASENAME);

            String type=properties.getProperty(MyWebConstant.DBTYPE);
            setDriver(type,properties);
            String url=getUrl(type);
            if(!StringUtils.isBlank(url)){
                properties.setProperty("url",url);
                dataSource = BasicDataSourceFactory.createDataSource(properties);
            }else{
                logger.error("上级数据库信息有误；port:"+port+";ip:"+ip+";databaseName:"+databaseName);
                throw new Exception("上级数据库信息有误；port:"+port+";ip:"+ip+";databaseName:"+databaseName);
            }
            }catch(Exception e){
                logger.error(e);
               
            }


//        try {
//            //本地连接信息
//            low_ip=properties.getProperty(MyWebConstant.LOWIP);
//            low_port=properties.getProperty(MyWebConstant.LOWPORT);
//            low_databaseName=properties.getProperty(MyWebConstant.LOWDATANASENAME);
//
//            String low_type=properties.getProperty(MyWebConstant.LOWDBTYPE);
//            setDriver(low_type,lowproperties);
//            String low_url=getLowUrl(low_type);
//            if(!StringUtils.isBlank(low_url)){
//                lowproperties.setProperty("url",low_url);
//                lowDataSource = BasicDataSourceFactory.createDataSource(lowproperties);
//            }else{
//                logger.error("下级数据库信息有误；port:"+low_port+";ip:"+low_ip+";databaseName:"+low_databaseName);
//                throw new Exception("下级数据库信息有误；port:"+low_port+";ip:"+low_ip+";databaseName:"+low_databaseName);
//            }
//
//            String low_userName=properties.getProperty(MyWebConstant.LOWUSERNAME);
//            String  low_password=properties.getProperty(MyWebConstant.LOWPASSWORD);
//            lowproperties.setProperty("username",low_userName);
//            lowproperties.setProperty("password",low_password);
//            lowDataSource=BasicDataSourceFactory.createDataSource(lowproperties);
//        } catch (Exception e) {
//            logger.error(e);
//
//        }

    }

    /**
     * 获取上级连接  改连接不会自动提交事物 需要手动commit
     * 设定setAutoCommit(false)没有在catch中进行Connection的rollBack操作，操作的表就会被锁住，造成数据库死锁
     * @return
     */
    public static Connection getConnection(){
        Connection connection = null;
        try{
            connection = dataSource.getConnection();
        }catch(SQLException e){
           
        }
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
           
            logger.error(e);
        }
        return connection;
    }

//    /**
//     * 获取下级连接
//     * @return
//     */
//    public static Connection getLowConnection(){
//        Connection connection = null;
//        try{
//            connection = lowDataSource.getConnection();
//        }catch(SQLException e){
//
//        }
//        try {
//            connection.setAutoCommit(false);
//        } catch (SQLException e) {
//            logger.error(e);
//
//        }
//        return connection;
//    }





    /**
     *  获取上级数据库url
     * @param dbType 1 :oracle ;2:sqlserver ;3:mysql
     * @return
     */
    public static String getUrl(String dbType){
        String url=null;
        if(StringUtils.isBlank(ip)||StringUtils.isBlank(port)||StringUtils.isBlank(databaseName)){
            return null;
        }else{
            if (MyWebConstant.ORACLE.equals(dbType)) {
                // Oracle
                url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + databaseName;
            } else if (MyWebConstant.SQLSERVER.equals(dbType)) {
                // sqlServer
                url = "jdbc:sqlserver://" + ip + ":" + port + ";databaseName=" + databaseName;
            } else if (MyWebConstant.MYSQL.equals(dbType)) {
                // mysql
                url = "jdbc:mysql://" + ip + ":" + port + "/" + databaseName;
            }
        }

        return url;
    }


    /**
     * 设置数据库驱动
     * @param dbType
     * @param prop
     */
    private static void setDriver(String dbType, Properties prop){
        if (MyWebConstant.ORACLE.equals(dbType)) {
            prop.setProperty("driverClassName","oracle.jdbc.driver.OracleDriver");
        } else if (MyWebConstant.SQLSERVER.equals(dbType)) {
            prop.setProperty("driverClassName","com.microsoft.jdbc.sqlserver.SQLServerDriver");
        } else if (MyWebConstant.MYSQL.equals(dbType)) {
            prop.setProperty("driverClassName","com.mysql.jdbc.Driver");
        }
    }
}
