package com.yuanwang.util;

import com.yuanwang.task.job.DataSync;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class InsertTable {
    private static Logger logger = Logger.getLogger(InsertTable.class);
    String checkConfigId = "";//任务ID


    //同步执行次数
    public int CK_EditSynchroRecord(Connection conn, String sql, Object... object) {
        return SQLDetailDao.sqlUpdate(conn, sql, object);
    }

    //策略执行结果
    public int CK_ImportPolicyExecuteResult(Connection conn, String sql, List<Map<String, Object>> list) {
        int row = 0;//
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);
            for (Map<String, Object> map : list) {
                Object endtime = map.get("endtime");
                Object id = map.get("id");
                Object uniqueid = map.get("uniqueid");
                Object flag = map.get("flag");
                Object starttime = map.get("starttime");
                Object policytype = map.get("policytype");
                Object crccheck = map.get("crccheck");
                Object protectnum = map.get("protectnum");
                Object policyid = map.get("policyid");

                Object[] objects = {
                        uniqueid, policyid, policytype, crccheck, starttime, endtime, flag, protectnum};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] rows = statement.executeBatch();
            logger.info("CK_ImportPolicyExecuteResult-->" + Arrays.toString(rows));
            conn.commit();
        } catch (Exception e) {
            logger.error(sql + "-->" + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    logger.error(e1);

                }
            }
        } finally {
            try {

                statement.close();
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //设备入库--MC_DevicesBasic、MC_DevicesDynamic
    public int Core_CreateDeviceItem(Connection conn, String sql, List<Map<String, Object>> list, Object groupId) {
        //UniqueId,Name,RegisterUser,IP,Mac,DepartmentCode,Phone,GroupId,GroupName
        DataSQL dataSQL = new DataSQL();
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);
            for (Map<String, Object> map : list) {
                Object departmentcode = map.get("departmentcode");
                Object phone = map.get("phone");
                Object originalmac = map.get("originalmac");
                Object registertime = map.get("registertime");
                Object avinstalled = map.get("avinstalled");
                Object avvendor = map.get("avvendor");
                Object mac = map.get("mac");
                Object groupname = map.get("groupname");
                Object avversion = map.get("avversion");
                Object ip = map.get("ip");
                Object registeruser = map.get("registeruser");
                Object uniqueid = map.get("uniqueid");
                Object isregisted = map.get("isregisted");
                Object securityclassification = map.get("securityclassification");
                Object name = map.get("name");
                //Object groupid =map.get("groupid");
                Object virusdbversion = map.get("virusdbversion");
                Object osdetail = map.get("osdetail");

                Object devicestatus = map.get("devicestatus");


                Object lastusetime = map.get("lastusetime");

                Object lasttriggertime = map.get("lasttriggertime");
                Object agentversion = map.get("agentversion");
                //ispoweron=0 是离线
                Object ispoweron = map.get("ispoweron");

                //插入到本地库
                Object checkconfigid = this.checkConfigId;
                //处理设备id，判断是否存在
                Object id = SQLDetailDao.getForObject(conn, DataSQL.MC_DevicesBasic_id, uniqueid, ip, mac);//获取当前系统的检查任务ID

                Object[] objects = {
                        id, uniqueid, name, registeruser, ip, mac, registeruser, groupname, groupId, groupname,
                        null, null, null, null, null, osdetail, null, null, null, null,
                        null, null, agentversion, null, null, null, null, null, null, devicestatus,
                        registertime, null, null, null, lastusetime, lasttriggertime, null, null, null, null,
                        null, avinstalled, avvendor, avversion, virusdbversion, null, null, securityclassification, originalmac};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] rows = statement.executeBatch();
            logger.info("Core_CreateDeviceItem-->" + Arrays.toString(rows));
            conn.commit();

        } catch (Exception e) {
            logger.info(sql + "-->" + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    logger.error(e1);

                }
            }
        } finally {
            try {

                statement.close();
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //级联开关控制
    public int CK_CreateCascadeDatabase(Connection conn, String sql, Object... object) {
        return SQLDetailDao.sqlUpdate(conn, sql, object);
    }

    //结束检查任务
    public int CK_FinishCheck(Connection conn, String sql) {
        return SQLDetailDao.sqlUpdate(conn, sql);
    }

    //生成報告
    public int CK_CreateComCheckReport2(Connection conn, String sql, Object... object) {
        return SQLDetailDao.sqlUpdate(conn, sql, object);
    }

    //策略下发
    public int CK_EditPolicy(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object policycontent = map.get("policycontent");
                Object remark = map.get("remark");
                Object status = map.get("status");
                Object policyname = map.get("policyname");
                Object id = map.get("id");
                Object policyobject = map.get("policyobject");
                Object crccheck = map.get("crccheck");

                Object editFlag = "1";//编辑标记:0为更新策略,1为只改变启用状态
                Object userId = "1";//操作用户
                Object operatorIp = "0.0.0.0";//操作IP

                Object[] objects = {
                        id, editFlag, status, policyname, policycontent, crccheck, remark, userId, operatorIp, policyobject};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] rows = statement.executeBatch();
            logger.info("CK_EditPolicy-->" + Arrays.toString(rows));

            statement.close();
        } catch (Exception e) {
            logger.info(sql + "-->" + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    logger.error(e1);

                }
            }
        } finally {
            try {

                statement.close();
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }


    //更新下级部门信息
    public int CK_ImportGroups(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);
            for (Map<String, Object> map : list) {
                Object id = map.get("id");
                Object name = map.get("name");
                Object parentid = "0";//map.get("parentid");

                Object[] objects = {
                        id, name, parentid};

                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] rows = statement.executeBatch();
            logger.info("CK_ImportGroups-->" + Arrays.toString(rows));
            conn.commit();

        } catch (Exception e) {
            logger.info(sql + "-->" + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    logger.error(e1);

                }
            }

        } finally {
            try {
                statement.close();
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }

        return 0;
    }

    /******
     * 服务数据下发
     * @param conn
     * @param sql
     * @param list
     * @return
     */
    public int CK_ImportCheckServiceBlackList(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);
            for (Map<String, Object> map : list) {
                String servicename = map.get("servicename") + "";
                String description = map.get("description") + "";

                Object[] objects = {
                        servicename, description};

                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] rows = statement.executeBatch();
            logger.info("CK_ImportCheckServiceBlackList-->" + Arrays.toString(rows));
            conn.commit();

        } catch (Exception e) {
            logger.info(sql + "-->" + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    logger.error(e1);

                }
            }
        } finally {
            try {

                statement.close();
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    /******
     * 端口数据下发
     * @param conn

     * @param sql
     * @param list
     * @return
     */
    public int CK_ImportCheckPortBlackList(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);
            for (Map<String, Object> map : list) {
                String id = map.get("id") + "";
                String port = map.get("port") + "";
                String description = map.get("description") + "";
                String updatetime = map.get("updatetime") + "";

                Object[] objects = {
                        port, description};

                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] rows = statement.executeBatch();
            logger.info("CK_ImportCheckPortBlackList-->" + Arrays.toString(rows));
            conn.commit();

        } catch (Exception e) {
            logger.info(sql + "-->" + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    logger.error(e1);

                }
            }
        } finally {
            try {

                statement.close();
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //设备入库
    public int CK_ImportCheckDevices(Connection conn, String sql, List<Map<String, Object>> list) {
        //UniqueId,Name,RegisterUser,IP,Mac,DepartmentCode,Phone,GroupId,GroupName
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object UniqueId = map.get("uniqueid");
                Object Name = map.get("name");
                Object RegisterUser = map.get("registeruser");
                Object ip = map.get("ip");
                Object Mac = map.get("mac");
                Object DepartmentCode = map.get("departmentcode");
                Object Phone = map.get("phone");
                Object GroupId = map.get("groupid");
                Object GroupName = map.get("groupname");
                //杀毒软件信息
                Object avinstalled = map.get("avinstalled");
                Object avvendor = map.get("avvendor");
                Object avversion = map.get("avversion");
                Object virusdbversion = map.get("virusdbversion");

                Object originalmac = map.get("originalmac");
                Object securityclassification = map.get("securityclassification");
                //插入到本地库
                Object checkconfigid = this.checkConfigId;

                Object[] objects = {
                        checkconfigid, UniqueId, Name, RegisterUser, ip, Mac, DepartmentCode, Phone, GroupId, GroupName, 1,
                        avvendor, avversion, virusdbversion, avinstalled, securityclassification, originalmac};

                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] rows = statement.executeBatch();
            logger.info("CK_ImportCheckDevices-->" + Arrays.toString(rows));

            conn.commit();

        } catch (Exception e) {
            logger.info(sql + "-->" + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    logger.error(e1);

                }
            }

        } finally {
            try {
                statement.close();
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    /********==================================================执行日志插入===================================================******/
    //3024,邮件客户端@";
    public int InsertTable3024(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object checkconfigid = map.get("checkconfigid");
                Object updatetime = map.get("updatetime");
                Object fromuser = map.get("fromuser");
                Object subject = map.get("subject");
                Object remarks = map.get("remarks");
                Object touser = map.get("touser");
                Object emailagentname = map.get("emailagentname");
                Object id = map.get("id");
                Object uniqueid = map.get("uniqueid");
                Object sourcepath = map.get("sourcepath");
                Object usetime = map.get("usetime");
                Object sourcetype = map.get("sourcetype");
                Object policyid = map.get("policyid");

                Object groupid = map.get("groupid");
                Object groupname = map.get("groupname");
                Object ip = map.get("ip");
                Object mac = map.get("mac");
                Object user = map.get("user");
                Object departmentcode = map.get("departmentcode");
                Object phone = map.get("phone");
                Object securityclassification = map.get("securityclassification");
                Object devicesname = map.get("devicesname");
                Object ipnum = map.get("ipnum");
                Object checksum = map.get("checksum");

                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, policyid, emailagentname, subject, fromuser, touser, usetime, remarks, checkconfigid, sourcetype, sourcepath,
                        groupid, groupname, ip, mac, user, departmentcode, phone, securityclassification, devicesname, ipnum, checksum
                };
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }


            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //3023,聊天软件@";
    public int InsertTable3023(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object id = map.get("id");
                Object chatsoftname = map.get("chatsoftname");
                Object uniqueid = map.get("uniqueid");
                Object checkconfigid = map.get("checkconfigid");
                Object updatetime = map.get("updatetime");
                Object sourcepath = map.get("sourcepath");
                Object usetime = map.get("usetime");
                Object account = map.get("account");
                Object remarks = map.get("remarks");
                Object sourcetype = map.get("sourcetype");
                Object serveraddress = map.get("serveraddress");
                Object policyid = map.get("policyid");

                Object groupid = map.get("groupid");
                Object groupname = map.get("groupname");
                Object ip = map.get("ip");
                Object mac = map.get("mac");
                Object user = map.get("user");
                Object departmentcode = map.get("departmentcode");
                Object phone = map.get("phone");
                Object securityclassification = map.get("securityclassification");
                Object devicesname = map.get("devicesname");
                Object ipnum = map.get("ipnum");
                Object checksum = map.get("checksum");

                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, policyid, chatsoftname, serveraddress, account, usetime, remarks, checkconfigid, sourcetype, sourcepath,
                        groupid, groupname, ip, mac, user, departmentcode, phone, securityclassification, devicesname, ipnum, checksum};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }


            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //3022,网盘痕迹@";
    public int InsertTable3022(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object operationtime = map.get("operationtime");
                Object operationtype = map.get("operationtype");
                Object checkconfigid = map.get("checkconfigid");
                Object updatetime = map.get("updatetime");
                Object skydrivename = map.get("skydrivename");
                Object localpath = map.get("localpath");
                Object remarks = map.get("remarks");
                Object drivepath = map.get("drivepath");
                Object id = map.get("id");
                Object uniqueid = map.get("uniqueid");
                Object sourcepath = map.get("sourcepath");
                Object sourcetype = map.get("sourcetype");
                Object policyid = map.get("policyid");

                Object groupid = map.get("groupid");
                Object groupname = map.get("groupname");
                Object ip = map.get("ip");
                Object mac = map.get("mac");
                Object user = map.get("user");
                Object departmentcode = map.get("departmentcode");
                Object phone = map.get("phone");
                Object securityclassification = map.get("securityclassification");
                Object devicesname = map.get("devicesname");
                Object ipnum = map.get("ipnum");
                Object checksum = map.get("checksum");

                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, policyid, skydrivename, drivepath, localpath, operationtype, operationtime, remarks, checkconfigid, sourcetype,
                        sourcepath,
                        groupid, groupname, ip, mac, user, departmentcode, phone, securityclassification, devicesname, ipnum, checksum};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }


            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //3021,下载软件@";
    public int InsertTable3021(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object checkconfigid = map.get("checkconfigid");
                Object updatetime = map.get("updatetime");
                Object originalurl = map.get("originalurl");
                Object toolname = map.get("toolname");
                Object remarks = map.get("remarks");
                Object id = map.get("id");
                Object savepath = map.get("savepath");
                Object uniqueid = map.get("uniqueid");
                Object filesize = map.get("filesize");
                Object downloadtime = map.get("downloadtime");
                Object savename = map.get("savename");
                Object sourcepath = map.get("sourcepath");
                Object sourcetype = map.get("sourcetype");
                Object policyid = map.get("policyid");

                Object groupid = map.get("groupid");
                Object groupname = map.get("groupname");
                Object ip = map.get("ip");
                Object mac = map.get("mac");
                Object user = map.get("user");
                Object departmentcode = map.get("departmentcode");
                Object phone = map.get("phone");
                Object securityclassification = map.get("securityclassification");
                Object devicesname = map.get("devicesname");
                Object ipnum = map.get("ipnum");
                Object checksum = map.get("checksum");

                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, policyid, toolname, originalurl, savename, savepath, filesize, downloadtime, remarks, checkconfigid,
                        sourcetype, sourcepath,
                        groupid, groupname, ip, mac, user, departmentcode, phone, securityclassification, devicesname, ipnum, checksum};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }


            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //30205,浏览器缓存@";
    public int InsertTable30205(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object browsertype = map.get("browsertype");
                Object id = map.get("id");
                Object uniqueid = map.get("uniqueid");
                Object checkconfigid = map.get("checkconfigid");
                Object updatetime = map.get("updatetime");
                Object accesstime = map.get("accesstime");
                Object sourcepath = map.get("sourcepath");
                Object outline = map.get("outline");
                Object sourcetype = map.get("sourcetype");
                Object url = map.get("url");
                Object policyid = map.get("policyid");

                Object groupid = map.get("groupid");
                Object groupname = map.get("groupname");
                Object ip = map.get("ip");
                Object mac = map.get("mac");
                Object user = map.get("user");
                Object departmentcode = map.get("departmentcode");
                Object phone = map.get("phone");
                Object securityclassification = map.get("securityclassification");
                Object devicesname = map.get("devicesname");
                Object ipnum = map.get("ipnum");
                Object checksum = map.get("checksum");

                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, policyid, url, accesstime, checkconfigid, sourcetype, sourcepath, browsertype, outline,
                        groupid, groupname, ip, mac, user, departmentcode, phone, securityclassification, devicesname, ipnum, checksum};

                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //30204,浏览器收藏夹@";
    public int InsertTable30204(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object browsertype = map.get("browsertype");
                Object checkconfigid = map.get("checkconfigid");
                Object updatetime = map.get("updatetime");
                Object accesstime = map.get("accesstime");
                Object accessuser = map.get("accessuser");
                Object outline = map.get("outline");
                Object url = map.get("url");
                Object id = map.get("id");
                Object uniqueid = map.get("uniqueid");
                Object sourcepath = map.get("sourcepath");
                Object sourcetype = map.get("sourcetype");
                Object tagname = map.get("tagname");
                Object policyid = map.get("policyid");


                Object groupid = map.get("groupid");
                Object groupname = map.get("groupname");
                Object ip = map.get("ip");
                Object mac = map.get("mac");
                Object user = map.get("user");
                Object departmentcode = map.get("departmentcode");
                Object phone = map.get("phone");
                Object securityclassification = map.get("securityclassification");
                Object devicesname = map.get("devicesname");
                Object ipnum = map.get("ipnum");
                Object checksum = map.get("checksum");

                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, policyid, tagname, url, accessuser, accesstime, checkconfigid, sourcetype, sourcepath, browsertype,
                        outline,
                        groupid, groupname, ip, mac, user, departmentcode, phone, securityclassification, devicesname, ipnum, checksum};

                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //30203,浏览器地址栏@";
    public int InsertTable30203(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object browsertype = map.get("browsertype");
                Object id = map.get("id");
                Object uniqueid = map.get("uniqueid");
                Object checkconfigid = map.get("checkconfigid");
                Object updatetime = map.get("updatetime");
                Object sourcepath = map.get("sourcepath");
                Object outline = map.get("outline");
                Object sourcetype = map.get("sourcetype");
                Object url = map.get("url");
                Object policyid = map.get("policyid");

                Object groupid = map.get("groupid");
                Object groupname = map.get("groupname");
                Object ip = map.get("ip");
                Object mac = map.get("mac");
                Object user = map.get("user");
                Object departmentcode = map.get("departmentcode");
                Object phone = map.get("phone");
                Object securityclassification = map.get("securityclassification");
                Object devicesname = map.get("devicesname");
                Object ipnum = map.get("ipnum");
                Object checksum = map.get("checksum");

                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, policyid, url, checkconfigid, sourcetype, sourcepath, browsertype, outline,
                        groupid, groupname, ip, mac, user, departmentcode, phone, securityclassification, devicesname, ipnum, checksum};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //30202,浏览器Cookie@";
    public int InsertTable30202(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object browsertype = map.get("browsertype");
                Object id = map.get("id");
                Object uniqueid = map.get("uniqueid");
                Object checkconfigid = map.get("checkconfigid");
                Object updatetime = map.get("updatetime");
                Object accesstime = map.get("accesstime");
                Object accessuser = map.get("accessuser");
                Object sourcepath = map.get("sourcepath");
                Object outline = map.get("outline");
                Object sourcetype = map.get("sourcetype");
                Object url = map.get("url");
                Object policyid = map.get("policyid");

                Object groupid = map.get("groupid");
                Object groupname = map.get("groupname");
                Object ip = map.get("ip");
                Object mac = map.get("mac");
                Object user = map.get("user");
                Object departmentcode = map.get("departmentcode");
                Object phone = map.get("phone");
                Object securityclassification = map.get("securityclassification");
                Object devicesname = map.get("devicesname");
                Object ipnum = map.get("ipnum");
                Object checksum = map.get("checksum");

                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, policyid, url, accessuser, accesstime, checkconfigid, sourcetype, sourcepath, browsertype, outline,
                        groupid, groupname, ip, mac, user, departmentcode, phone, securityclassification, devicesname, ipnum, checksum};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //2.8.1.18上网痕迹--上网记录深度检查上报（302）
    public int InsertTable3002(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object browsertype = map.get("browsertype");
                Object checkconfigid = map.get("checkconfigid");
                Object updatetime = map.get("updatetime");
                Object accesstime = map.get("accesstime");
                Object outline = map.get("outline");
                Object url = map.get("url");
                Object id = map.get("id");
                Object shorturl = map.get("shorturl");
                Object uniqueid = map.get("uniqueid");
                Object urltype = map.get("urltype");
                Object sourcepath = map.get("sourcepath");
                Object sourcetype = map.get("sourcetype");
                Object policyid = map.get("policyid");

                Object groupid = map.get("groupid");
                Object groupname = map.get("groupname");
                Object ip = map.get("ip");
                Object mac = map.get("mac");
                Object user = map.get("user");
                Object departmentcode = map.get("departmentcode");
                Object phone = map.get("phone");
                Object securityclassification = map.get("securityclassification");
                Object devicesname = map.get("devicesname");
                Object ipnum = map.get("ipnum");
                Object checksum = map.get("checksum");


                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, policyid, url, accesstime, checkconfigid, sourcetype, sourcepath, browsertype, outline,
                        groupid, groupname, ip, mac, user, departmentcode, phone, securityclassification, devicesname, ipnum, checksum};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    // 2.8.1.13上网痕迹--浏览器网站访问记录上报（302）
    public int InsertTable30201(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object browsertype = map.get("browsertype");
                Object visittime = map.get("visittime");
                Object checkconfigid = map.get("checkconfigid");
                Object updatetime = map.get("updatetime");
                Object outline = map.get("outline");
                Object url = map.get("url");
                Object shorturl = map.get("shorturl");
                Object id = map.get("id");
                Object uniqueid = map.get("uniqueid");
                Object title = map.get("title");
                Object urltype = map.get("urltype");
                Object sourcepath = map.get("sourcepath");
                Object sourcetype = map.get("sourcetype");
                Object policyid = map.get("policyid");

                Object groupid = map.get("groupid");
                Object groupname = map.get("groupname");
                Object ip = map.get("ip");
                Object mac = map.get("mac");
                Object user = map.get("user");
                Object departmentcode = map.get("departmentcode");
                Object phone = map.get("phone");
                Object securityclassification = map.get("securityclassification");
                Object devicesname = map.get("devicesname");
                Object ipnum = map.get("ipnum");
                Object checksum = map.get("checksum");

                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, policyid, url, title, visittime, checkconfigid, sourcetype, sourcepath, browsertype, outline,
                        groupid, groupname, ip, mac, user, departmentcode, phone, securityclassification, devicesname, ipnum, checksum};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //    403 = '网络接口';
    public int InsertTable403(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object id = map.get("id");
                Object uniqueid = map.get("uniqueid");
                Object checkconfigid = map.get("checkconfigid");
                Object cardname = map.get("cardname");
                Object updatetime = map.get("updatetime");
                Object connstatus = map.get("connstatus");
                Object connname = map.get("connname");
                Object policyid = map.get("policyid");

                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, policyid, connname, cardname, connstatus, checkconfigid};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //   401= '互联网连接状态';
    public int InsertTable401(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object id = map.get("id");
                Object uniqueid = map.get("uniqueid");
                Object checkconfigid = map.get("checkconfigid");
                Object updatetime = map.get("updatetime");
                Object directinternet = map.get("directinternet");
                Object agentlink = map.get("agentlink");
                Object policyid = map.get("policyid");

                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, policyid, directinternet, agentlink, checkconfigid};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //  3001=USB深度检查
    public int InsertTable3001(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object checkconfigid = map.get("checkconfigid");
                Object updatetime = map.get("updatetime");
                Object pid = map.get("pid");
                Object id = map.get("id");
                Object isillegal = map.get("isillegal");
                Object uniqueid = map.get("uniqueid");
                Object iscrossused = map.get("iscrossused");
                Object iswireless = map.get("iswireless");
                Object sourcepath = map.get("sourcepath");
                Object usbdevicedesc = map.get("usbdevicedesc");
                Object lastwrite = map.get("lastwrite");
                Object usbdevicetype = map.get("usbdevicetype");
                Object ismisplug = map.get("ismisplug");
                Object sourcetype = map.get("sourcetype");
                Object vid = map.get("vid");
                Object serial = map.get("serial");
                Object isillegalused = map.get("isillegalused");
                Object policyid = map.get("policyid");

                Object groupid = map.get("groupid");
                Object groupname = map.get("groupname");
                Object ip = map.get("ip");
                Object mac = map.get("mac");
                Object user = map.get("user");
                Object departmentcode = map.get("departmentcode");
                Object phone = map.get("phone");
                Object securityclassification = map.get("securityclassification");
                Object devicesname = map.get("devicesname");
                Object ipnum = map.get("ipnum");
                Object checksum = map.get("checksum");
                Object usbchecksum = map.get("usbchecksum");

                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, policyid, vid, pid, serial, lastwrite, usbdevicetype, usbdevicedesc, checkconfigid,
                        sourcetype, sourcepath,
                        groupid, groupname, ip, mac, user, departmentcode, phone, securityclassification, devicesname, ipnum, checksum, usbchecksum};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //  301=USB设备
    public int InsertTable301(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object checkconfigid = map.get("checkconfigid");
                Object updatetime = map.get("updatetime");
                Object pid = map.get("pid");
                Object firsttime = map.get("firsttime");
                Object id = map.get("id");
                Object isillegal = map.get("isillegal");
                Object uniqueid = map.get("uniqueid");
                Object iscrossused = map.get("iscrossused");
                Object iswireless = map.get("iswireless");
                Object lasttime = map.get("lasttime");
                Object sourcepath = map.get("sourcepath");
                Object usbdevicedesc = map.get("usbdevicedesc");
                Object usbdevicetype = map.get("usbdevicetype");
                Object ismisplug = map.get("ismisplug");
                Object sourcetype = map.get("sourcetype");
                Object vid = map.get("vid");
                Object serial = map.get("serial");
                Object isillegalused = map.get("isillegalused");
                Object policyid = map.get("policyid");

                Object groupid = map.get("groupid");
                Object groupname = map.get("groupname");
                Object ip = map.get("ip");
                Object mac = map.get("mac");
                Object user = map.get("user");
                Object departmentcode = map.get("departmentcode");
                Object phone = map.get("phone");
                Object securityclassification = map.get("securityclassification");
                Object devicesname = map.get("devicesname");
                Object ipnum = map.get("ipnum");
                Object checksum = map.get("checksum");
                Object usbchecksum = map.get("usbchecksum");

                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, policyid, vid, pid, serial, firsttime, lasttime, usbdevicetype, usbdevicedesc, checkconfigid,
                        sourcetype, sourcepath,
                        groupid, groupname, ip, mac, user, departmentcode, phone, securityclassification, devicesname, ipnum, checksum, usbchecksum};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //  517= 硬盘信息';;
    public int InsertTable517(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object diskmodel = map.get("diskmodel");
                Object checkconfigid = map.get("checkconfigid");
                Object updatetime = map.get("updatetime");
                Object boottimes = map.get("boottimes");
                Object diskvendor = map.get("diskvendor");
                Object isvirtualdisk = map.get("isvirtualdisk");
                Object id = map.get("id");
                Object uniqueid = map.get("uniqueid");
                Object partitioninfo = map.get("partitioninfo");
                Object disksize = map.get("disksize");
                Object disksn = map.get("disksn");
                Object poweronhours = map.get("poweronhours");
                Object policyid = map.get("policyid");

                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, policyid, disksn, diskvendor, diskmodel, disksize, partitioninfo, boottimes, poweronhours, isvirtualdisk,
                        checkconfigid};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //  5171= 硬盘信息 --分区信息';;
    public int InsertTable5171(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object diskname = map.get("diskname");
                Object id = map.get("id");
                Object uniqueid = map.get("uniqueid");
                Object checkconfigid = map.get("checkconfigid");
                Object disknum = map.get("disknum");
                Object diskturesize = map.get("diskturesize");
                Object islocked = map.get("islocked");
                Object updatetime = map.get("updatetime");
                Object ishide = map.get("ishide");
                Object disksize = map.get("disksize");
                Object vollabel = map.get("vollabel");

                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, diskname, vollabel, diskturesize, disksize, ishide, islocked, disknum, checkconfigid};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //  5172= 硬盘信息 --加密磁盘信息';;
    public int InsertTable5172(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object softtype = map.get("softtype");
                Object id = map.get("id");
                Object uniqueid = map.get("uniqueid");
                Object checkconfigid = map.get("checkconfigid");
                Object updatetime = map.get("updatetime");
                Object partdisk = map.get("partdisk");
                Object policyid = map.get("policyid");


                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, policyid, partdisk, softtype, checkconfigid};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //  5173= 硬盘信息 --从盘信息';;
    public int InsertTable5173(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object id = map.get("id");
                Object harddiskmodel = map.get("harddiskmodel");
                Object uniqueid = map.get("uniqueid");
                Object checkconfigid = map.get("checkconfigid");
                Object updatetime = map.get("updatetime");
                Object manufacturer = map.get("manufacturer");
                Object lastdate = map.get("lastdate");

                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, manufacturer, harddiskmodel, lastdate, checkconfigid};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //  516= '多操作系统;;
    public int InsertTable516(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object person = map.get("person");
                Object checkconfigid = map.get("checkconfigid");
                Object osdesc = map.get("osdesc");
                Object installeddate = map.get("installeddate");
                Object updatetime = map.get("updatetime");
                Object version = map.get("version");
                Object ospath = map.get("ospath");
                Object id = map.get("id");
                Object uniqueid = map.get("uniqueid");
                Object osname = map.get("osname");
                Object organization = map.get("organization");
                Object osghost = map.get("osghost");
                Object isclone = map.get("isclone");
                Object serial = map.get("serial");
                Object policyid = map.get("policyid");


                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, policyid, osname, version, serial, ospath, installeddate, organization, person, checkconfigid,
                        osghost, osdesc};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //  514= '硬件信息;;
    public int InsertTable514(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object harddevname = map.get("harddevname");
                Object id = map.get("id");
                Object uniqueid = map.get("uniqueid");
                Object checkconfigid = map.get("checkconfigid");
                Object harddevtype = map.get("harddevtype");
                Object iswireless = map.get("iswireless");
                Object updatetime = map.get("updatetime");
                Object harddevdesc = map.get("harddevdesc");
                Object policyid = map.get("policyid");


                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, policyid, harddevtype, harddevname, harddevdesc, iswireless, checkconfigid};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //  503= '共享列表';;
    public int InsertTable503(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object sharetype = map.get("sharetype");
                Object uniqueid = map.get("uniqueid");
                Object checkconfigid = map.get("checkconfigid");
                Object discoverytime = map.get("discoverytime");
                Object updatetime = map.get("updatetime");
                Object sharelinknum = map.get("sharelinknum");
                Object sharename = map.get("sharename");
                Object protectnum = map.get("protectnum");
                Object sharedescription = map.get("sharedescription");
                Object sharepath = map.get("sharepath");

                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, sharename, sharepath, sharetype, sharelinknum, sharedescription, discoverytime, protectnum, checkconfigid};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    // 504= '未安装补丁';;
    public int InsertTable504(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object id = map.get("id");
                Object uniqueid = map.get("uniqueid");
                Object checkconfigid = map.get("checkconfigid");
                Object level = map.get("level");
                Object updatetime = map.get("updatetime");
                Object pubdate = map.get("pubdate");
                Object leakdesc = map.get("leakdesc");
                Object leakname = map.get("leakname");
                Object size = map.get("size");
                Object leakid = map.get("leakid");
                Object policyid = map.get("policyid");

                Object groupid = map.get("groupid");
                Object groupname = map.get("groupname");
                Object ip = map.get("ip");
                Object mac = map.get("mac");
                Object user = map.get("user");
                Object departmentcode = map.get("departmentcode");
                Object phone = map.get("phone");
                Object securityclassification = map.get("securityclassification");
                Object devicesname = map.get("devicesname");
                Object ipnum = map.get("ipnum");
                Object checksum = map.get("checksum");

                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, policyid, leakname, leakid, leakdesc, level, size, pubdate, checkconfigid,
                        groupid, groupname, ip, mac, user, departmentcode, phone, securityclassification, devicesname, ipnum, checksum};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //204= '三合一产品';
    public int InsertTable204(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                String process = map.get("process") + "";
                Object id = map.get("id");
                Object uniqueid = map.get("uniqueid");
                Object checkconfigid = map.get("checkconfigid");
                Object updatetime = map.get("updatetime");
                Object installtime = map.get("installtime");
                Object company = map.get("company");
                String isactivated = map.get("isactivated") + "";
                Object binstall = map.get("binstall");
                Object fullpath = map.get("fullpath");
                Object policyid = map.get("policyid");
                if ("1".equals(isactivated)) {
                    process += "(已激活)";
                } else {
                    process += "(未激活)";
                }

                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, policyid, process, company, checkconfigid, fullpath, binstall, installtime};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    // 515 = '虚拟机安装';//虚拟机安装  修改成 虚拟机软件  2015-03-02
    // 5151= '虚拟机文件';//虚拟机安装文件夹 下添加  虚拟操作系统
    public int InsertTable515(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object id = map.get("id");
                Object uniqueid = map.get("uniqueid");
                Object checkconfigid = map.get("checkconfigid");
                Object updatetime = map.get("updatetime");
                Object installeddate = map.get("installeddate");
                Object softpath = map.get("softpath");
                Object company = map.get("company");
                Object softname = map.get("softname");
                Object version = map.get("version");
                Object policyid = map.get("policyid");

                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, policyid, softname, version, company, softpath, installeddate, checkconfigid};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //512= '已安装补丁';
    public int InsertTable512(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object id = map.get("id");
                Object patchdesc = map.get("patchdesc");
                Object uniqueid = map.get("uniqueid");
                Object checkconfigid = map.get("checkconfigid");
                Object updatetime = map.get("updatetime");
                Object installedby = map.get("installedby");
                Object installeddate = map.get("installeddate");
                Object patchid = map.get("patchid");

                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, patchid, patchdesc, installeddate, installedby, checkconfigid};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    // 505= '弱口令';
    public int InsertTable505(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object usergroup = map.get("usergroup");
                Object checkconfigid = map.get("checkconfigid");
                Object updatetime = map.get("updatetime");
                Object weakpassword = map.get("weakpassword");
                Object isforbidden = map.get("isforbidden");
                Object isovertime = map.get("isovertime");
                Object isweekpassword = map.get("isweekpassword");
                Object id = map.get("id");
                Object uniqueid = map.get("uniqueid");
                Object username = map.get("username");
                Object islocked = map.get("islocked");
                Object changepwdtime = map.get("changepwdtime");
                Object lastlogintime = map.get("lastlogintime");
                Object policyid = map.get("policyid");

                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, policyid, username, usergroup, isforbidden, islocked, isovertime, isweekpassword, lastlogintime, checkconfigid,
                        weakpassword, changepwdtime};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    // 201= '服务检查';
    public int InsertTable201(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object phone = map.get("phone");
                Object discoverytime = map.get("discoverytime");
                Object updatetime = map.get("updatetime");
                Object servicename = map.get("servicename");
                Object mac = map.get("mac");
                Object groupname = map.get("groupname");
                Object id = map.get("id");
                Object signinfo = map.get("signinfo");
                Object username = map.get("username");
                Object starttype = map.get("starttype");
                Object description = map.get("description");
                Object path = map.get("path");
                Object groupid = map.get("groupid");
                Object ipnum = map.get("ipnum");
                Object startstatus = map.get("startstatus");
                Object departmentcode = map.get("departmentcode");
                Object checkconfigid = map.get("checkconfigid");
                Object ip = map.get("ip");
                Object isillegal = map.get("isillegal");
                Object processname = map.get("processname");
                Object uniqueid = map.get("uniqueid");
                Object flag = map.get("flag");
                Object user = map.get("user");
                Object displayname = map.get("displayname");
                Object policyid = map.get("policyid");
                Object processid = map.get("processid");

//				Object groupid=map.get("groupid");
//				Object groupname=map.get("groupname");
//				Object ip=map.get("ip");
//				Object mac=map.get("mac");
//				Object user=map.get("user");
//				Object departmentcode=map.get("departmentcode");
//				Object phone=map.get("phone");
                Object securityclassification = map.get("securityclassification");
                Object devicesname = map.get("devicesname");
                //Object ipnum=map.get("ipnum");
                Object checksum = map.get("checksum");

                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, policyid, servicename, description, path, username, starttype, startstatus, ip, mac,
                        user, departmentcode, phone, discoverytime, displayname, checkconfigid, signinfo, processid, processname,
                        groupid, groupname, securityclassification, devicesname, ipnum, checksum};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] row = statement.executeBatch();
          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //502= '开放端口';
    public int InsertTable502(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object departmentcode = map.get("departmentcode");
                Object checkconfigid = map.get("checkconfigid");
                Object phone = map.get("phone");
                Object protocol = map.get("protocol");
                Object sourceip = map.get("sourceip");
                Object updatetime = map.get("updatetime");
                Object affairlevel = map.get("affairlevel");
                Object mac = map.get("mac");
                Object localport = map.get("localport");
                Object affairresult = map.get("affairresult");
                Object ip = map.get("ip");
                Object isillegal = map.get("isillegal");
                Object id = map.get("id");
                Object uniqueid = map.get("uniqueid");
                Object processname = map.get("processname");
                Object connectstate = map.get("connectstate");
                Object targetip = map.get("targetip");
                Object ipnum = map.get("ipnum");
                Object user = map.get("user");
                Object processid = map.get("processid");
                //GroupId,GroupName,IP,IPNum,Mac,`User`,DepartmentCode,Phone,DevicesName,SecurityClassification,CheckSum

                Object groupid = map.get("groupid");
                Object groupname = map.get("groupname");
                //Object ip=map.get("ip");
                //Object mac=map.get("mac");
                //Object user=map.get("user");
                //Object departmentcode=map.get("departmentcode");
                //Object phone=map.get("phone");
                Object securityclassification = map.get("securityclassification");
                Object devicesname = map.get("devicesname");
                //Object ipnum=map.get("ipnum");
                Object checksum = map.get("checksum");

                checkconfigid = this.checkConfigId;
                Object[] objects = {uniqueid, processid, processname, protocol, sourceip, targetip, connectstate, localport, checkconfigid,
                        groupid, groupname, ip, mac, user, departmentcode, phone, securityclassification, devicesname, ipnum, checksum};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }


            int[] row = statement.executeBatch();

          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //203= '网络应用软件'
    public int InsertTable203(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object departmentcode = map.get("departmentcode");
                Object checkconfigid = map.get("checkconfigid");
                Object phone = map.get("phone");
                Object discoverytime = map.get("discoverytime");
                Object updatetime = map.get("updatetime");
                Object installtime = map.get("installtime");
                Object softwarename = map.get("softwarename");
                Object groupname = map.get("groupname");
                Object mac = map.get("mac");
                Object version = map.get("version");
                Object ip = map.get("ip");
                Object id = map.get("id");
                Object uniqueid = map.get("uniqueid");
                Object flag = map.get("flag");
                Object isnetapp = map.get("isnetapp");
                Object company = map.get("company");
                Object path = map.get("path");
                Object groupid = map.get("groupid");
                Object user = map.get("user");
                Object policyid = map.get("policyid");


                Object actionType = "0";
                Object softType = "";

				/*Object groupid=map.get("groupid");
				Object groupname=map.get("groupname");
				Object ip=map.get("ip");
				Object mac=map.get("mac");
				Object user=map.get("user");
				Object departmentcode=map.get("departmentcode");
				Object phone=map.get("phone");*/
                Object securityclassification = map.get("securityclassification");
                Object devicesname = map.get("devicesname");
                Object ipnum = map.get("ipnum");
                Object checksum = map.get("checksum");

                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, policyid, softwarename, path, company, version, installtime, discoverytime, actionType, softType,
                        checkconfigid, isnetapp,
                        groupid, groupname, ip, mac, user, departmentcode, phone, securityclassification, devicesname, ipnum, checksum
                };
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }


            int[] row = statement.executeBatch();

          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //506= '账户安全配置'
    public int InsertTable506(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object departmentcode = map.get("departmentcode");
                Object checkconfigid = map.get("checkconfigid");
                Object phone = map.get("phone");
                Object discoverytime = map.get("discoverytime");
                Object updatetime = map.get("updatetime");
                Object groupname = map.get("groupname");
                Object mac = map.get("mac");
                Object strategycomment = map.get("strategycomment");
                Object ip = map.get("ip");
                Object id = map.get("id");
                Object uniqueid = map.get("uniqueid");
                Object flag = map.get("flag");
                Object ipnum = map.get("ipnum");
                Object groupid = map.get("groupid");
                Object user = map.get("user");
                Object policyid = map.get("policyid");

                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, policyid, strategycomment, discoverytime, checkconfigid};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }


            int[] row = statement.executeBatch();

          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }


    //507 安全审计配置
    public int InsertTable507(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object poweruse = map.get("poweruse");
                Object checkconfigid = map.get("checkconfigid");
                Object updatetime = map.get("updatetime");
                Object objectaccess = map.get("objectaccess");
                Object loginevent = map.get("loginevent");
                Object strategychange = map.get("strategychange");
                Object dirservice = map.get("dirservice");
                Object strategyname = map.get("strategyname");
                Object isillegal = map.get("isillegal");
                Object id = map.get("id");
                Object uniqueid = map.get("uniqueid");
                Object procefollow = map.get("procefollow");
                Object safetyset = map.get("safetyset");
                Object accountmanage = map.get("accountmanage");
                Object systemevent = map.get("systemevent");
                Object policyid = map.get("policyid");
                Object accountlogin = map.get("accountlogin");


                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        uniqueid, policyid, strategyname, strategychange, loginevent, objectaccess, procefollow, dirservice, poweruse, systemevent,
                        accountlogin, accountmanage, safetyset, checkconfigid};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }


            int[] row = statement.executeBatch();

          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }


    /********执行日志插入******/
    //103 涉密文件检查
    // IsDelFile 1,已删除文件，0 普通文件检查
    public int InsertTable103(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {
                Object isclosed = map.get("isclosed");
                Object filepath = map.get("filepath");
                Object phone = map.get("phone");
                Object discoverytime = map.get("discoverytime");
                Object desc = map.get("desc");
                Object updatetime = map.get("updatetime");
                Object similaritydegree = map.get("similaritydegree");
                Object mac = map.get("mac");
                Object length1 = map.get("length1");
                Object protectnum = map.get("protectnum");
                Object length2 = map.get("length2");
                Object length3 = map.get("length3");
                Object mode = map.get("mode");
                Object isdelfile = map.get("isdelfile");
                Object id = map.get("id");
                Object file = map.get("file");
                Object clienteventid = map.get("clienteventid");
                Object isconfirmed = map.get("isconfirmed");
                Object filetype = map.get("filetype");
                Object keywordfilename = map.get("keywordfilename");
                Object contextfilename = map.get("contextfilename");
                Object option = map.get("option");
                Object departmentcode = map.get("departmentcode");
                Object filenamecheckresult = map.get("filenamecheckresult");
                Object checkconfigid = map.get("checkconfigid");
                Object infortype = map.get("infortype");
                Object filemodifytime = map.get("filemodifytime");
                Object keyword = map.get("keyword");
                Object keywordhitheadpart = map.get("keywordhitheadpart");
                Object ip = map.get("ip");
                Object content = map.get("content");
                Object filecreatetime = map.get("filecreatetime");
                Object uniqueid = map.get("uniqueid");
                Object filesize = map.get("filesize");
                Object flag = map.get("flag");
                Object similarity = map.get("similarity");
                Object user = map.get("user");
                Object keywordhittimes = map.get("keywordhittimes");
                Object policyid = map.get("policyid");
                int isKnowledgeLearn = 0;// 是否是知识库学习：0表示不是1表示是
                int KnowledgeLearnType = 0;//学习的类型：0表示分数1表示关键字
                Object SourceFeature = "";//知识库来源特征内容,学习的具体内容,具体的内容根据学习的类型而定
                Object FeatureContent = "";//--知识特征内容
                Object FileCharsSum = "";//文档总字符数
                Object FileHeadCharsSum = "";//
                int LearnResult = 0;//识库学习的结果：0表示该文件不符合要求，1表示该文件符合要求
                int isCheck = 1;//--是否检查类：1 检查类；0 监管类
                Object KnowledgeUniqueId = "";//学习到的特征的唯一标识
                Object KnowledgeMatchSequence = "";//个知识库检查匹配后的字段匹配相关字段
                Object ContentGuId = "";//文件内容ID（该参数对检查系统无效）
                Object EmailAttachmentFileName = "";//--邮件附件名称
                Object EmailSubfileTitle = "";//--邮件标题
                Object EmailSendAddr = "";//-发送地址
                Object EmailRecvAddr = "";//--接收地址
                Object EmailSendTime = "";//

                //GroupId,GroupName,IP,IPNum,Mac,`User`,DepartmentCode,Phone,DevicesName,SecurityClassification,CheckSum
                Object groupId = map.get("groupid");
                Object groupName = map.get("groupname");
                Object securityClassification = map.get("securityclassification");
                Object devicesname = map.get("devicesname");
                Object ipnum = map.get("ipnum");
                Object checksum = map.get("checksum");


                Object guessfileext = map.get("guessfileext");//文件后缀修改
                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        clienteventid, isclosed, uniqueid, policyid, file, desc, filepath, filesize, discoverytime, ip,
                        mac, user, departmentcode, phone, flag, mode, similarity, keyword, infortype, length1,
                        length2, length3, option, content, protectnum, isKnowledgeLearn, KnowledgeLearnType, SourceFeature, FeatureContent, FileCharsSum,
                        FileHeadCharsSum, LearnResult, KnowledgeUniqueId, KnowledgeMatchSequence, isdelfile, checkconfigid, ContentGuId, isCheck, filecreatetime, filemodifytime, EmailAttachmentFileName,
                        EmailSubfileTitle, EmailSendAddr, EmailRecvAddr, EmailSendTime, keywordhitheadpart, keywordhittimes, filenamecheckresult, keywordfilename, contextfilename,
                        similaritydegree, guessfileext,
                        groupId, groupName, securityClassification, devicesname, ipnum, checksum};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }


            //statement.addBatch();
            int[] row = statement.executeBatch();

          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    //103 涉密文件检查 --邮件文件
    public int InsertTable1030(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);


            for (Map<String, Object> map : list) {

                Object isclosed = map.get("isclosed");
                Object filepath = map.get("filepath");
                Object phone = map.get("phone");
                Object discoverytime = map.get("discoverytime");
                Object desc = map.get("desc");
                Object updatetime = map.get("updatetime");
                Object similaritydegree = map.get("similaritydegree");
                Object mac = map.get("mac");
                Object length1 = map.get("length1");
                Object protectnum = map.get("protectnum");
                Object length2 = map.get("length2");
                Object length3 = map.get("length3");
                Object mode = map.get("mode");
                Object isdelfile = map.get("isdelfile");
                Object id = map.get("id");
                Object emailattachmentfilename = map.get("emailattachmentfilename");
                Object emailsendtime = map.get("emailsendtime");
                Object file = map.get("file");
                Object clienteventid = map.get("clienteventid");
                Object isconfirmed = map.get("isconfirmed");
                Object filetype = map.get("filetype");
                Object keywordfilename = map.get("keywordfilename");
                Object emailsubfiletitle = map.get("emailsubfiletitle");
                Object contextfilename = map.get("contextfilename");
                Object option = map.get("option");
                Object departmentcode = map.get("departmentcode");
                Object filenamecheckresult = map.get("filenamecheckresult");
                Object checkconfigid = checkConfigId;//map.get("checkconfigid");
                Object infortype = map.get("infortype");
                Object emailsendaddr = map.get("emailsendaddr");
                Object filemodifytime = map.get("filemodifytime");
                Object emailrecvaddr = map.get("emailrecvaddr");
                Object keyword = map.get("keyword");
                Object keywordhitheadpart = map.get("keywordhitheadpart");
                Object ip = map.get("ip");
                Object content = map.get("content");
                Object filecreatetime = map.get("filecreatetime");
                Object uniqueid = map.get("uniqueid");
                Object filesize = map.get("filesize");
                Object flag = map.get("flag");
                Object similarity = map.get("similarity");
                Object user = map.get("user");
                Object keywordhittimes = map.get("keywordhittimes");
                Object policyid = map.get("policyid");


                int isKnowledgeLearn = 0;// 是否是知识库学习：0表示不是1表示是
                int KnowledgeLearnType = 0;//学习的类型：0表示分数1表示关键字
                Object SourceFeature = "";//知识库来源特征内容,学习的具体内容,具体的内容根据学习的类型而定
                Object FeatureContent = "";//--知识特征内容
                Object FileCharsSum = "";//文档总字符数
                Object FileHeadCharsSum = "";//
                int LearnResult = 0;//识库学习的结果：0表示该文件不符合要求，1表示该文件符合要求
                int isCheck = 1;//--是否检查类：1 检查类；0 监管类
                Object KnowledgeUniqueId = "";//学习到的特征的唯一标识
                Object KnowledgeMatchSequence = "";//个知识库检查匹配后的字段匹配相关字段
                Object ContentGuId = "";//文件内容ID（该参数对检查系统无效）

                Object guessfileext = map.get("guessfileext");//文件后缀修改

                //GroupId,GroupName,IP,IPNum,Mac,`User`,DepartmentCode,Phone,DevicesName,SecurityClassification,CheckSum
                Object groupId = map.get("groupid");
                Object groupName = map.get("groupname");
                Object securityClassification = map.get("securityclassification");
                Object devicesname = map.get("devicesname");
                Object ipnum = map.get("ipnum");
                Object checksum = map.get("checksum");

                checkconfigid = this.checkConfigId;
                //System.out.println(emailsubfiletitle+"++"+emailsendaddr+"==="+emailrecvaddr);
                Object[] objects = {
                        clienteventid, isclosed, uniqueid, policyid, file, desc, filepath, filesize, discoverytime, ip,
                        mac, user, departmentcode, phone, flag, mode, similarity, keyword, infortype, length1,
                        length2, length3, option, content, protectnum, isKnowledgeLearn, KnowledgeLearnType, SourceFeature, FeatureContent, FileCharsSum,
                        FileHeadCharsSum, LearnResult, KnowledgeUniqueId, KnowledgeMatchSequence, isdelfile, checkconfigid, ContentGuId, isCheck, filecreatetime, filemodifytime, emailattachmentfilename,
                        emailsubfiletitle, emailsendaddr, emailrecvaddr, emailsendtime, keywordhitheadpart, keywordhittimes, filenamecheckresult, keywordfilename, contextfilename,
                        similaritydegree, guessfileext,
                        groupId, groupName, securityClassification, devicesname, ipnum, checksum};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }


            //statement.addBatch();
            int[] row = statement.executeBatch();

          
            conn.commit();
         
        } catch (Exception e) {
           
            logger.info(sql + "-->" + e.getMessage());
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
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    /***
     * 违规设备信息入库
     * @param conn

     * @param sql
     * @param list
     * @return
     */
    public int Check_ViolationDevice(Connection conn, String sql, List<Map<String, Object>> list) {
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(sql);
            for (Map<String, Object> map : list) {
                String checkconfigid = map.get("checkconfigid") + "";
                String reportflag = map.get("reportflag") + "";
                String uniqueid = map.get("uniqueid") + "";
                String id = map.get("id") + "";

                checkconfigid = this.checkConfigId;
                Object[] objects = {
                        checkconfigid, reportflag, uniqueid};
                for (int i = 0; i < objects.length; i++) {
                    statement.setObject(i + 1, objects[i]);
                }
                statement.addBatch();
            }

            int[] row = statement.executeBatch();
            conn.commit();

        } catch (Exception e) {
            logger.info(sql + "-->" + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    logger.error(e1);

                }
            }
        } finally {
            try {
                statement.close();
                conn.close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return 0;
    }

    public InsertTable(String checkConfigId) {
        super();
        this.checkConfigId = checkConfigId;
    }

    public Object getCheckConfigId() {
        return checkConfigId;
    }

    public void setCheckConfigId(String checkConfigId) {
        this.checkConfigId = checkConfigId;
    }
}
