package com.yuanwang.task.job;


import com.yuanwang.util.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 数据同步任务
 */
public class DataSync implements Runnable {
    private static Logger logger  =  Logger.getLogger(DataSync. class );
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

    //上级数据库连接
    private Connection sConnection;

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

        } catch ( Exception e) {
            e.printStackTrace();
        }

    }

    /*****
     * 对集合进行排序
     * @param list
     */
    public  static void Collections_sort(List<Map<String,Object>> list){
        Collections.sort(list,new Comparator<Map<String,Object>>() {
            @Override
            public int compare(Map<String, Object> o1,
                               Map<String, Object> o2) {
                int id=Integer.parseInt(o1.get("id").toString())-Integer.parseInt(o2.get("id").toString());
                return id;
            }
        });
    }

    private void doWork() throws Exception {

        List<Map<String,Object>> ListSystemALL=SQLDetailDao.getList(getsConnection(MyWebConstant.MYSQLDRIVER), DataSQL.CK_ListSystemALL);

        Object checkConfigId_0=SQLDetailDao.getForObject(getsConnection(MyWebConstant.MYSQLDRIVER), DataSQL.checkConfigId);//获取当前系统的检查任务ID
        //获取检查项信息
        Map<String, Object>  config_0= com.yuanwang.util.SQLDetailDao.getMap(getsConnection(MyWebConstant.MYSQLDRIVER), DataSQL.listCheckConfig,checkConfigId_0);
        if(checkConfigId_0!=null && config_0!=null && ListSystemALL.size()>0){
            for (Map<String,Object> systemMap:ListSystemALL) {
                String ip=String.valueOf(systemMap.get("databaseip"));
                String port=String.valueOf(systemMap.get("port"));
                String username=String.valueOf(systemMap.get("loginname"));
                String password=String.valueOf(systemMap.get("databasepassword"));
                String databaseName=String.valueOf(systemMap.get("databasename"));
                String groupId=String.valueOf(systemMap.get("groupid"));
                String isEnabled=String.valueOf(systemMap.get("isdisabled"));
                taskName(
                        checkConfigId_0,config_0,
                        conn, ip, ip, port, username, password, databaseName, groupId, isEnabled);

               
            }

        }
    }

    private Connection getsConnection(String DBDRIVER ) throws  Exception {
        if (sConnection!=null){
            return sConnection;
        }else{
            Class.forName(MyWebConstant.MYSQLDRIVER);

            String ip= Config.getString(MyWebConstant.IP,"localhost");
            String port= Config.getString(MyWebConstant.PORT,"3306");
            String userName= Config.getString(MyWebConstant.USERNAME,"root");
            String password= Config.getString(MyWebConstant.PASSWORD,"1");
            String database= Config.getString(MyWebConstant.DATANASENAME,"ywcheck");
            String type= Config.getString(MyWebConstant.DBTYPE,"3");

            String url= DBCPUtil.getUrl(type) ;
            sConnection = DriverManager.getConnection(url, userName, password);
            return sConnection;
        }

    }

    public void taskName(
            Object checkConfigId_0,Map<String, Object> config_0,
            Connection conn,String taskName,
            String ip0,String port,String username,String password,String databaseName,String groupId,String isEnabled)throws Exception{

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
        logger.info("开始时间："+sdf.format(new Date())+" 任务名称："+taskName);

        /********
         * 第一步：获取下级系统的数据库信息
         */

        if(checkConfigId_0!=null && config_0!=null){
            //Map<String, Object>  getdatabase=SQLDetailDao.getMap(conn, DataSQL.CK_ListSystem_Tssk,taskName);
			/*String ip0=getdatabase.get("databaseip")+"";
			String port=getdatabase.get("port")+"";
			String username=getdatabase.get("loginname")+"";
			String password=getdatabase.get("databasepassword")+"";
			String databaseName=getdatabase.get("databasename")+"";
			String groupId=getdatabase.get("groupid")+"";*/

            String depthcheckstr_0=config_0.get("depthcheckstr")+"";//获取深度检查
            String checkitems_0=config_0.get("checkitems")+"";//获取检查项id
            String checkstate_0=config_0.get("checkstate")+"";//检查状态
            String organizename_0=config_0.get("organizename")+"";//任务名称
            /**===========================插入和读取日志更新条数=====================================**/
            //SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"tablName");
            //Map<String, Object>  CK_ListAnalyserLog=SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"tablName");
            /*******
             * 第二步：获取下级系统的任务信息
             * 如果没有检查任务不做同步
             * 只取最后的一个 不区分是否结束检查
             */
            Connection conn_1=SQLDetailDao.getConnection(ip0, port, username, password, databaseName, 3);
            Object checkConfigId_1=SQLDetailDao.getForObject(conn_1, DataSQL.checkConfigId);//获取当前系统的检查任务ID

            //获取检查项信息
            Map<String, Object>  config_1= com.yuanwang.util.SQLDetailDao.getMap(conn_1, DataSQL.listCheckConfig,checkConfigId_1);
            if(conn_1!=null && config_1!=null){
                String depthcheckstr_1=config_1.get("depthcheckstr")+"";//获取深度检查
                String checkitems_1=config_1.get("checkitems")+"";//获取检查项id
                String checkstate_1=config_1.get("checkstate")+"";//检查状态
                String organizename_1=config_1.get("organizename")+"";//任务名称
                /***
                 * 判断下级系统的任务是否结束
                 * 如果没有结束检查，读取设备表里面已注册的的设备信息  Devices
                 * 如果已经结束检查，读取历史设备表中最后面的一个检查任务的设备信息 Check_Devices
                 * 如果设备表中注册的设备信息就是不做日志插入动作
                 */
                List<Map<String,Object>> deviceList=new ArrayList<Map<String,Object>>();
                if(checkstate_1.equals("4")){//已经结束检查
                    deviceList=SQLDetailDao.getList(conn_1, DataSQL.Check_Devices,checkConfigId_1);
                }else if(checkstate_1.equals("2")){//正在检查
                    deviceList=SQLDetailDao.getList(conn_1, DataSQL.Devices);
                }
                logger.info(  "checkstate_1"+"**检查状态***"+checkstate_1);
                InsertTable insertTable=new InsertTable(String.valueOf(checkConfigId_0));

                //執行次數統計
                Map<String, Object>  ListSynchroRecord= com.yuanwang.util.SQLDetailDao.getMap(conn, DataSQL.CK_ListSynchroRecord,checkConfigId_0);
                int startSynchro=0;
                int endSynchro=0;
                if(ListSynchroRecord.get("startsynchro")!=null&&ListSynchroRecord.get("endsynchro")!=null){
                    startSynchro=Integer.parseInt(ListSynchroRecord.get("startsynchro")+"");//检查中同步次数
                    endSynchro=Integer.parseInt(ListSynchroRecord.get("endsynchro")+"");//结束检查同步次数
                }
                logger.info( "**开启检查同步次数**"+startSynchro);
                logger.info("**结束检查同步次数**"+endSynchro);
                /************
                 * 判断检查项是否相同，如果不同不做同步处理
                 */
                if(depthcheckstr_0.equals(depthcheckstr_1)&&checkitems_0.equals(checkitems_1) && checkstate_0.equals(checkstate_1)&&
                        !groupId.equals("")&&
                        organizename_0.equals(organizename_1)){

                }else{
                    if("".equals(groupId)){
                        logger.error(groupId+"**配置下级单位失败**");
                    }
                    else{
                        logger.info("depthcheckstr_0+checkitems_0"+"**判断执行查任务不同不做同步**");
                    }
                }
                if(checkstate_0.equals("4")&& endSynchro==1){
                    logger.warn("ListSynchroRecord"+(startSynchro+endSynchro)+"**数据同步已经完成不需要再同步**");
                }
                /******设备列表个数大于0，否则不做插入日志动作*******/
                if(deviceList.size()>0 &&
                        depthcheckstr_0.equals(depthcheckstr_1)&&
                        checkitems_0.equals(checkitems_1) &&
                        checkstate_0.equals(checkstate_1) &&
                        !groupId.equals("") &&
                        endSynchro==0 &&
                        checkstate_0.equals("2")&&
                        organizename_0.equals(organizename_1)){

                    if(checkstate_1.equals("4")){//已经结束检查
                        //设备插入
                        insertTable.CK_ImportCheckDevices(conn, DataSQL.CK_ImportCheckDevices,deviceList);
                        logger.info(  "CK_ImportCheckDevices"+"**设备更新***"+deviceList.size());

                    }
                    if(checkstate_1.equals("2")){///正在检查
                        insertTable.Core_CreateDeviceItem(conn, DataSQL.Core_CreateDeviceItem,deviceList,groupId);
                        logger. info(  "Core_CreateDeviceItem"+"**设备更新***"+deviceList.size());

                        /*******读取策略执行结果表********/
                        Map<String, Object>  CK_ListAnalyserLog=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_PolicyExecuteResult");
                        Object num=CK_ListAnalyserLog.get("num");
                        if(num==null){//表示没有插入过数据
                            num=0;
                        }
                        List<Map<String,Object>> Log_PolicyExecuteResult=SQLDetailDao.getList(conn_1, DataSQL.Log_PolicyExecuteResult,num);
                        if(Log_PolicyExecuteResult!=null &&Log_PolicyExecuteResult.size()>0){
                            Collections_sort(Log_PolicyExecuteResult);
                            //执行插入数据
                            insertTable.CK_ImportPolicyExecuteResult(conn, DataSQL.CK_ImportPolicyExecuteResult,Log_PolicyExecuteResult);
                            logger. info(  "策略执行结果 上报***"+Log_PolicyExecuteResult.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=Log_PolicyExecuteResult.get(Log_PolicyExecuteResult.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_PolicyExecuteResult",id);

                        }else{
                            logger. info(  "策略执行结果上报,没有需要更新的数据***"+Log_PolicyExecuteResult.size());
                        }

                    }


                    if(checkitems_0.indexOf("103")!=-1&&checkitems_1.indexOf("103")!=-1){
                        //涉密文件检查
                        //IsDelFile 1,已删除文件，0 普通文件检查
                        /*******读取策略执行结果表********/
                        Map<String, Object>  CK_ListAnalyserLog103=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_ConfidentialSpecial_0");
                        Object num103=CK_ListAnalyserLog103.get("num");
                        if(num103==null){//表示没有插入过数据
                            num103=0;
                        }
                        List<Map<String,Object>> list103=SQLDetailDao.getList(conn_1, DataSQL.Log_ConfidentialSpecial, checkConfigId_1,0,num103);
                        //执行插入数据
                        if(list103!=null &&list103.size()>0){
                            Collections_sort(list103);
                            insertTable.InsertTable103(conn, DataSQL.Core_CreateLog_Confidential,list103);
                            logger. info(  "InsertTable103"+"**涉密文件检查***"+list103.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list103.get(list103.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_ConfidentialSpecial_0",id);

                        }else{
                            logger. info(  "InsertTable103"+"**涉密文件检查,没有需要更新的数据***"+list103.size());
                        }


                        //邮件文件检查
                        Map<String, Object>  CK_ListAnalyserLog1030=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_ConfidentialEmail");
                        Object num1030=CK_ListAnalyserLog1030.get("num");
                        if(num1030==null){//表示没有插入过数据
                            num1030=0;
                        }
                        List<Map<String,Object>> list1030=SQLDetailDao.getList(conn_1, DataSQL.Log_ConfidentialEmail, checkConfigId_1,num1030);
                        if(list1030!=null &&list1030.size()>0){
                            Collections_sort(list1030);
                            insertTable.InsertTable1030(conn,   DataSQL.Core_CreateLog_Confidential,list1030);
                            logger. info(  "InsertTable1030"+"**1030 邮件文件检查***"+list1030.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list1030.get(list1030.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_ConfidentialEmail",id);

                        }else{
                            logger. info(  "InsertTable1030"+"**1030 邮件文件检查,没有需要更新的数据***"+list1030.size());
                        }

                    }
                    /*********邮件文件*********/

                    if(depthcheckstr_0.indexOf("isKeyWordDepth")!=-1 && depthcheckstr_1.indexOf("isKeyWordDepth")!=-1){
                        //涉密文件检查
                        //IsDelFile 1,已删除文件，0 普通文件检查
                        Map<String, Object>  CK_ListAnalyserLog1003=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_ConfidentialSpecial_1");
                        Object num1003=CK_ListAnalyserLog1003.get("num");
                        if(num1003==null){//表示没有插入过数据
                            num1003=0;
                        }

                        List<Map<String,Object>> list1003=SQLDetailDao.getList(conn_1, DataSQL.Log_ConfidentialSpecial, checkConfigId_1,1,num1003);
                        //执行插入数据
                        if(list1003!=null &&list1003.size()>0){
                            Collections_sort(list1003);
                            insertTable.InsertTable103( conn, DataSQL.Core_CreateLog_Confidential,list1003);
                            logger. info(  "InsertTable1003"+"**1003 已删除文件检查***"+list1003.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list1003.get(list1003.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_ConfidentialSpecial_1",id);

                        }else{
                            logger. info(  "InsertTable1003"+"**1003 已删除文件检查,没有需要更新的数据***"+list1003.size());
                        }
                    }
                    //507 安全审计配置
                    if(checkitems_0.indexOf("507")!=-1&&checkitems_1.indexOf("507")!=-1){
                        Map<String, Object>  CK_ListAnalyserLog507=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_AuditStrategy");
                        Object num507=CK_ListAnalyserLog507.get("num");
                        if(num507==null){//表示没有插入过数据
                            num507=0;
                        }
                        //安全审计配置
                        List<Map<String,Object>> list507=SQLDetailDao.getList(conn_1, DataSQL.Log_AuditStrategy, checkConfigId_1,num507);
                        //执行插入数据
                        if(list507!=null &&list507.size()>0){
                            Collections_sort(list507);
                            insertTable.InsertTable507(conn,  DataSQL.Core_CreateLog_AuditStrategy,list507);
                            logger. info(  "InsertTable507"+"**507 安全审计配置***"+list507.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list507.get(list507.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_AuditStrategy",id);
                        }else{
                            logger. info(  "InsertTable507"+"**507 安全审计配置,没有需要更新的数据***"+list507.size());
                        }

                    }
                    //506= '账户安全配置'
                    if(checkitems_0.indexOf("506")!=-1&&checkitems_1.indexOf("506")!=-1){
                        Map<String, Object>  CK_ListAnalyserLog506=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_StrategySecurity");
                        Object num506=CK_ListAnalyserLog506.get("num");
                        if(num506==null){//表示没有插入过数据
                            num506=0;
                        }
                        //506= '账户安全配置'
                        List<Map<String,Object>> list506=SQLDetailDao.getList(conn_1, DataSQL.Log_StrategySecurity, checkConfigId_1,num506);
                        if(list506!=null &&list506.size()>0){
                            Collections_sort(list506);
                            //执行插入数据
                            insertTable.InsertTable506(conn,DataSQL.Core_CreateLog_StrategySecurity,list506);
                            logger. info(  "InsertTable506"+"**506 账户安全配置***"+list506.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list506.get(list506.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_StrategySecurity",id);
                        }else{
                            logger. info(  "InsertTable506"+"**506 账户安全配置,没有需要更新的数据***"+list506.size());
                        }
                    }
                    //203= '网络应用软件'
                    if(checkitems_0.indexOf("203")!=-1&&checkitems_1.indexOf("203")!=-1){
                        Map<String, Object>  CK_ListAnalyserLog203=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_TerminalSoftware");
                        Object num203=CK_ListAnalyserLog203.get("num");
                        if(num203==null){//表示没有插入过数据
                            num203=0;
                        }
                        //203= '网络应用软件'
                        List<Map<String,Object>> list203=SQLDetailDao.getList(conn_1, DataSQL.Log_TerminalSoftware, checkConfigId_1,num203);
                        //执行插入数据
                        if(list203!=null &&list203.size()>0){
                            Collections_sort(list203);
                            insertTable.InsertTable203(conn,  DataSQL.Core_CreateLog_TerminalSoftware,list203);
                            logger. info(  "InsertTable203"+"**203 网络应用软件***"+list203.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list203.get(list203.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_TerminalSoftware",id);
                        }else{
                            logger. info(  "InsertTable203"+"**203 网络应用软件,没有需要更新的数据***"+list203.size());
                        }
                    }
                    //502= '开放端口';
                    if(checkitems_0.indexOf("502")!=-1&&checkitems_1.indexOf("502")!=-1){
                        Map<String, Object>  CK_ListAnalyserLog502=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_NetworkConnectionAudit");
                        Object num502=CK_ListAnalyserLog502.get("num");
                        if(num502==null){//表示没有插入过数据
                            num502=0;
                        }
                        //502= '开放端口';
                        List<Map<String,Object>> list502=SQLDetailDao.getList(conn_1, DataSQL.Log_NetworkConnectionAudit, checkConfigId_1,num502);
                        //执行插入数据
                        if(list502!=null &&list502.size()>0){
                            Collections_sort(list502);
                            insertTable.InsertTable502(conn, DataSQL.Core_CreateLogNetworkConnectionAudit,list502);
                            logger. info(  "InsertTable502"+"**502  开放端口 ***"+list502.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list502.get(list502.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_NetworkConnectionAudit",id);
                        }else{
                            logger. info(  "InsertTable502"+"**502 开放端口,没有需要更新的数据***"+list502.size());
                        }
                    }
                    // 201= '服务检查';
                    if(checkitems_0.indexOf("201")!=-1&&checkitems_1.indexOf("201")!=-1){
                        Map<String, Object>  CK_ListAnalyserLog201=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_TerminalService");
                        Object num201=CK_ListAnalyserLog201.get("num");
                        if(num201==null){//表示没有插入过数据
                            num201=0;
                        }
                        // 201= '服务检查';
                        List<Map<String,Object>> list201=SQLDetailDao.getList(conn_1, DataSQL.Log_TerminalService, checkConfigId_1,num201);
                        if(list201!=null &&list201.size()>0){
                            Collections_sort(list201);
                            //执行插入数据
                            insertTable.InsertTable201( conn, DataSQL.Core_CreateLog_TerminalService,list201);
                            logger. info(  "InsertTable201"+"**201  服务检查 ***"+list201.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list201.get(list201.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_TerminalService",id);
                        }else{
                            logger. info(  "InsertTable201"+"**201  服务检查,没有需要更新的数据 ***"+list201.size());
                        }
                    }
                    //  505= '弱口令';
                    if(checkitems_0.indexOf("505")!=-1&&checkitems_1.indexOf("505")!=-1){
                        Map<String, Object>  CK_ListAnalyserLog505=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_SystemAccount");
                        Object num505=CK_ListAnalyserLog505.get("num");
                        if(num505==null){//表示没有插入过数据
                            num505=0;
                        }
                        // 505= '弱口令';
                        List<Map<String,Object>> list505=SQLDetailDao.getList(conn_1, DataSQL.Log_SystemAccount, checkConfigId_1,num505);
                        if(list505!=null &&list505.size()>0){
                            Collections_sort(list505);
                            //执行插入数据
                            insertTable.InsertTable505( conn, DataSQL.Core_CreateLog_SystemAccount,list505);
                            logger. info(  "InsertTable505"+"** 505 弱口令 ****"+list505.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list505.get(list505.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_SystemAccount",id);
                        }else{
                            logger. info(  "InsertTable505"+"** 505 弱口令 ,没有需要更新的数据****"+list505.size());
                        }
                    }
                    // 512= '已安装补丁';
                    if(checkitems_0.indexOf("512")!=-1&&checkitems_1.indexOf("512")!=-1){
                        Map<String, Object>  CK_ListAnalyserLog512=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"CK_InstalledPatch");
                        Object num512=CK_ListAnalyserLog512.get("num");
                        if(num512==null){//表示没有插入过数据
                            num512=0;
                        }
                        // 512= '已安装补丁';
                        List<Map<String,Object>> list512=SQLDetailDao.getList(conn_1, DataSQL.CK_InstalledPatch, checkConfigId_1,num512);
                        if(list512!=null &&list512.size()>0){
                            Collections_sort(list512);
                            //执行插入数据
                            insertTable.InsertTable512( conn, DataSQL.CK_CreateInstalledPatch,list512);
                            logger. info(  "InsertTable512"+"** 512 已安装补丁 ****"+list512.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list512.get(list512.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"CK_InstalledPatch",id);

                        }else{
                            logger. info(  "InsertTable512"+"** 512 已安装补丁 ,没有需要更新的数据 ****"+list512.size());
                        }
                    }

                    // 204= '三合一产品';
                    if(checkitems_0.indexOf("204")!=-1&&checkitems_1.indexOf("204")!=-1){
                        Map<String, Object>  CK_ListAnalyserLog204=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_SanHeYi");
                        Object num204=CK_ListAnalyserLog204.get("num");
                        if(num204==null){//表示没有插入过数据
                            num204=0;
                        }
                        //  204= '三合一产品';
                        List<Map<String,Object>> list204=SQLDetailDao.getList(conn_1, DataSQL.Log_SanHeYi, checkConfigId_1,num204);
                        if(list204!=null &&list204.size()>0){
                            Collections_sort(list204);
                            //执行插入数据
                            insertTable.InsertTable204( conn, DataSQL.Core_CreateLog_SanHeYi,list204);
                            logger. info(  "InsertTable204"+"**  204 三合一产品 ****"+list204.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list204.get(list204.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_SanHeYi",id);
                        }else{

                            logger. info(  "InsertTable204"+"**  204 三合一产品,没有需要更新的数据 ****"+list204.size());
                        }

                    }

                    // 504= '未安装补丁';
                    if(checkitems_0.indexOf("504")!=-1&&checkitems_1.indexOf("504")!=-1){
                        Map<String, Object>  CK_ListAnalyserLog504=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_unInstalled_Patch");
                        Object num504=CK_ListAnalyserLog504.get("num");
                        if(num504==null){//表示没有插入过数据
                            num504=0;
                        }
                        //  504= '未安装补丁';
                        List<Map<String,Object>> list504=SQLDetailDao.getList(conn_1, DataSQL.Log_unInstalled_Patch, checkConfigId_1,num504);
                        if(list504!=null &&list504.size()>0){
                            Collections_sort(list504);
                            //执行插入数据
                            insertTable.InsertTable504( conn, DataSQL.Core_CreateLog_unInstalled_Patch,list504);
                            logger. info(  "InsertTable504"+"**  504 未安装补丁 ****"+list504.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list504.get(list504.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_unInstalled_Patch",id);
                        }else{
                            logger. info(  "InsertTable504"+"**  504 未安装补丁,没有需要更新的数据 ****"+list504.size());
                        }

                    }
                    // 503= '共享列表';
                    if(checkitems_0.indexOf("503")!=-1&&checkitems_1.indexOf("503")!=-1){
                        Map<String, Object>  CK_ListAnalyserLog503=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_ShareInfo");
                        Object num503=CK_ListAnalyserLog503.get("num");
                        if(num503==null){//表示没有插入过数据
                            num503=0;
                        }
                        // 503= '共享列表';
                        List<Map<String,Object>> list503=SQLDetailDao.getList(conn_1, DataSQL.Log_ShareInfo, checkConfigId_1,num503);
                        if(list503!=null &&list503.size()>0){
                            Collections_sort(list503);
                            //执行插入数据
                            insertTable.InsertTable503( conn, DataSQL.Core_CreateLog_ShareInfo,list503);
                            logger. info(  "InsertTable503"+"**  503 共享列表 ***"+list503.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list503.get(list503.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_ShareInfo",id);
                        }else{
                            logger. info(  "InsertTable503"+"**  503 共享列表,没有需要更新的数据 ***"+list503.size());
                        }
                    }

                    //  514 = '硬件信息';// 2017-9-19
                    if(checkitems_0.indexOf("514")!=-1&&checkitems_1.indexOf("514")!=-1){
                        Map<String, Object>  CK_ListAnalyserLog514=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_HardDev");
                        Object num514=CK_ListAnalyserLog514.get("num");
                        if(num514==null){//表示没有插入过数据
                            num514=0;
                        }
                        //514 = '硬件信息';
                        List<Map<String,Object>> list514=SQLDetailDao.getList(conn_1, DataSQL.Log_HardDev, checkConfigId_1,num514);
                        if(list514!=null &&list514.size()>0){
                            Collections_sort(list514);
                            //执行插入数据
                            insertTable.InsertTable514( conn, DataSQL.Core_CreateLog_HardDev,list514);
                            logger. info(  "InsertTable514"+"**  514 硬件信息 ****"+list514.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list514.get(list514.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_HardDev",id);
                        }else{
                            logger. info(  "InsertTable514"+"**  514 硬件信息,没有需要更新的数据 ****"+list514.size());
                        }
                    }

                    //  516= 多操作系统
                    if(checkitems_0.indexOf("516")!=-1&&checkitems_1.indexOf("516")!=-1){
                        Map<String, Object>  CK_ListAnalyserLog516=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_OSInstall");
                        Object num516=CK_ListAnalyserLog516.get("num");
                        if(num516==null){//表示没有插入过数据
                            num516=0;
                        }
                        //516= 多操作系统
                        List<Map<String,Object>> list516=SQLDetailDao.getList(conn_1, DataSQL.Log_OSInstall, checkConfigId_1,num516);
                        if(list516!=null &&list516.size()>0){
                            Collections_sort(list516);
                            //执行插入数据
                            insertTable.InsertTable516( conn, DataSQL.Core_CreateLog_OSInstall,list516);
                            logger. info(  "InsertTable516"+"**  516 多操作系统 ****"+list516.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list516.get(list516.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_OSInstall",id);
                        }else{
                            logger. info(  "InsertTable516"+"**  516 多操作系统 ,没有需要更新的数据 ****"+list516.size());
                        }
                    }
                    //  517= 硬盘信息
                    if(checkitems_0.indexOf("517")!=-1&&checkitems_1.indexOf("517")!=-1){
                        // 517= 硬盘信息
                        Map<String, Object>  CK_ListAnalyserLog517=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_HardDiskInfo");
                        Object num517=CK_ListAnalyserLog517.get("num");
                        if(num517==null){//表示没有插入过数据
                            num517=0;
                        }

                        List<Map<String,Object>> list517=SQLDetailDao.getList(conn_1, DataSQL.Log_HardDiskInfo, checkConfigId_1,num517);
                        if(list517!=null &&list517.size()>0){
                            Collections_sort(list517);
                            //执行插入数据
                            insertTable.InsertTable517( conn, DataSQL.Core_CreateLog_HardDiskInfo,list517);
                            logger. info(  "InsertTable517"+"**  517 硬盘信息 ****"+list517.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list517.get(list517.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_HardDiskInfo",id);
                        }else{
                            logger. info(  "InsertTable517"+"**  517 硬盘信息 ,没有需要更新的数据 ****"+list517.size());
                        }

                        // 5171= 硬盘信息 --分区信息
                        Map<String, Object>  CK_ListAnalyserLog5171=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_HardDevicePart");
                        Object num5171=CK_ListAnalyserLog5171.get("num");
                        if(num5171==null){//表示没有插入过数据
                            num5171=0;
                        }

                        List<Map<String,Object>> list5171=SQLDetailDao.getList(conn_1, DataSQL.Log_HardDevicePart, checkConfigId_1,num5171);
                        if(list5171!=null &&list5171.size()>0){
                            Collections_sort(list5171);
                            //执行插入数据
                            insertTable.InsertTable5171( conn, DataSQL.Core_CreateLog_HardDevicePart,list5171);
                            logger. info(  "InsertTable5171"+"**  5171 硬盘信息-分区信息 ****"+list5171.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list5171.get(list5171.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_HardDevicePart",id);
                        }else{
                            logger. info(  "InsertTable5171"+"**  5171 硬盘信息-分区信息,没有需要更新的数据  ****"+list5171.size());
                        }

                        // 5172= 硬盘信息 --加密磁盘
                        Map<String, Object>  CK_ListAnalyserLog5172=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_DiskLocker");
                        Object num5172=CK_ListAnalyserLog5172.get("num");
                        if(num5172==null){//表示没有插入过数据
                            num5172=0;
                        }

                        List<Map<String,Object>> list5172=SQLDetailDao.getList(conn_1, DataSQL.Log_DiskLocker, checkConfigId_1,num5172);
                        if(list5172!=null &&list5172.size()>0){
                            Collections_sort(list5172);
                            //执行插入数据
                            insertTable.InsertTable5172( conn, DataSQL.Core_CreateLog_DiskLocker,list5172);
                            logger. info(  "InsertTable5172"+"**  5172 硬盘信息-加密磁盘 ****"+list5172.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list5172.get(list5172.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_DiskLocker",id);
                        }else{

                            logger. info(  "InsertTable5172"+"**  5172 硬盘信息-加密磁盘,没有需要更新的数据  ****"+list5172.size());
                        }


                        // 5173= 硬盘信息 -从盘
                        Map<String, Object>  CK_ListAnalyserLog5173=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_ExecDiskDrive");
                        Object num5173=CK_ListAnalyserLog5173.get("num");
                        if(num5173==null){//表示没有插入过数据
                            num5173=0;
                        }
                        List<Map<String,Object>> list5173=SQLDetailDao.getList(conn_1, DataSQL.Log_ExecDiskDrive, checkConfigId_1,num5173);
                        if(list5173!=null &&list5173.size()>0){
                            Collections_sort(list5173);
                            //执行插入数据
                            insertTable.InsertTable5173( conn, DataSQL.Core_CreateLog_ExecDiskDrive,list5173);
                            logger. info(  "InsertTable5173"+"**  5173 硬盘信息-从盘信息 ****"+list5173.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list5173.get(list5173.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_ExecDiskDrive",id);
                        }else{
                            logger. info(  "InsertTable5173"+"**  5173 硬盘信息-从盘信息,没有需要更新的数据  ****"+list5173.size());
                        }


                    }

                    // 515 = '虚拟机安装';
                    if(checkitems_0.indexOf("515")!=-1&&checkitems_1.indexOf("515")!=-1){
                        //515 = '虚拟机安装';
                        Map<String, Object>  CK_ListAnalyserLog515=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_VmwareChk");
                        Object num515=CK_ListAnalyserLog515.get("num");
                        if(num515==null){//表示没有插入过数据
                            num515=0;
                        }
                        List<Map<String,Object>> list515=SQLDetailDao.getList(conn_1, DataSQL.Log_VmwareChk, checkConfigId_1,num515);
                        //执行插入数据
                        if(list515!=null &&list515.size()>0){
                            Collections_sort(list515);
                            insertTable.InsertTable515( conn, DataSQL.Core_CreateLog_VmwareChk,list515);
                            logger. info(  "InsertTable515"+"**  515 虚拟机安装 ****"+list515.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list515.get(list515.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_VmwareChk",id);
                        }else{
                            logger. info(  "InsertTable515"+"**  515 虚拟机安装,没有需要更新的数据  ****"+list515.size());
                        }


                    }
                    //  301= USB设备
                    if(checkitems_0.indexOf("301")!=-1&&checkitems_1.indexOf("301")!=-1){
                        // 301=USB设备
                        Map<String, Object>  CK_ListAnalyserLog301=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_DetailUsbRecord");
                        Object num301=CK_ListAnalyserLog301.get("num");
                        if(num301==null){//表示没有插入过数据
                            num301=0;
                        }
                        List<Map<String,Object>> list301=SQLDetailDao.getList(conn_1, DataSQL.Log_DetailUsbRecord, checkConfigId_1,num301);
                        if(list301!=null &&list301.size()>0){
                            Collections_sort(list301);
                            //执行插入数据
                            insertTable.InsertTable301( conn, DataSQL.Core_CreateLog_DetailUsbRecord,list301);
                            logger. info(  "InsertTable301"+"**  301 USB设备 ****"+list301.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list301.get(list301.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_DetailUsbRecord",id);
                        }else{
                            logger. info(  "InsertTable301"+"**  301 USB设备 ,没有需要更新的数据 ****"+list301.size());
                        }
                    }
                    //3001 USB深度检查
                    if(depthcheckstr_0.indexOf("isUsbDepth")!=-1 && depthcheckstr_1.indexOf("isUsbDepth")!=-1){
                        Map<String, Object>  CK_ListAnalyserLog3001=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_DetailUsbHis");
                        Object num3001=CK_ListAnalyserLog3001.get("num");
                        if(num3001==null){//表示没有插入过数据
                            num3001=0;
                        }
                        List<Map<String,Object>> list3001=SQLDetailDao.getList(conn_1, DataSQL.Log_DetailUsbHis, checkConfigId_1,num3001);
                        if(list3001!=null &&list3001.size()>0){
                            Collections_sort(list3001);
                            //执行插入数据
                            insertTable.InsertTable3001( conn, DataSQL.Core_CreateLog_DetailUsbHis,list3001);
                            logger. info(  "InsertTable3001"+"**3001 USB深度检查***"+list3001.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list3001.get(list3001.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_DetailUsbHis",id);
                        }else{
                            logger. info(  "InsertTable3001"+"**3001 USB深度检查,没有需要更新的数据***"+list3001.size());
                        }
                    }
                    // 401= '互联网连接状态';
                    if(checkitems_0.indexOf("401")!=-1&&checkitems_1.indexOf("401")!=-1){
                        // 301=USB设备
                        Map<String, Object>  CK_ListAnalyserLog401=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_InternetLink");
                        Object num401=CK_ListAnalyserLog401.get("num");
                        if(num401==null){//表示没有插入过数据
                            num401=0;
                        }
                        List<Map<String,Object>> list401=SQLDetailDao.getList(conn_1, DataSQL.Log_InternetLink, checkConfigId_1,num401);
                        if(list401!=null &&list401.size()>0){
                            Collections_sort(list401);
                            //执行插入数据
                            insertTable.InsertTable401( conn, DataSQL.Core_CreateLog_InternetLink,list401);
                            logger. info(  "InsertTable401"+"**  401 互联网连接状态 ****"+list401.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list401.get(list401.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_InternetLink",id);
                        }else{
                            logger. info(  "InsertTable401"+"**  401 互联网连接状态,没有需要更新的数据 ****"+list401.size());
                        }
                    }
                    //  403 = '网络接口';
                    if(checkitems_0.indexOf("403")!=-1&&checkitems_1.indexOf("403")!=-1){
                        // 301=USB设备
                        Map<String, Object>  CK_ListAnalyserLog403=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_NetConn");
                        Object num403=CK_ListAnalyserLog403.get("num");
                        if(num403==null){//表示没有插入过数据
                            num403=0;
                        }
                        List<Map<String,Object>> list403=SQLDetailDao.getList(conn_1, DataSQL.Log_NetConn, checkConfigId_1,num403);
                        if(list403!=null &&list403.size()>0){
                            Collections_sort(list403);
                            //执行插入数据
                            insertTable.InsertTable403( conn, DataSQL.Core_CreateLog_NetConn,list403);
                            logger. info(  "InsertTable403"+"**  403 网络接口****"+list403.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list403.get(list403.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_NetConn",id);;
                        }else{
                            logger. info(  "InsertTable403"+"**  403 网络接口,没有需要更新的数据****"+list403.size());
                        }
                    }
                    //  2.8.1.13上网痕迹--浏览器网站访问记录上报（302）;
                    if(checkitems_0.indexOf("302")!=-1&&checkitems_1.indexOf("302")!=-1){
                        // 2.8.1.13上网痕迹--浏览器网站访问记录上报（302）
                        Map<String, Object>  CK_ListAnalyserLog30201=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_HistoryRecord");
                        Object num30201=CK_ListAnalyserLog30201.get("num");
                        if(num30201==null){//表示没有插入过数据
                            num30201=0;
                        }
                        List<Map<String,Object>> list30201=SQLDetailDao.getList(conn_1, DataSQL.Log_HistoryRecord, checkConfigId_1,num30201);
                        if(list30201!=null &&list30201.size()>0){
                            Collections_sort(list30201);
                            //执行插入数据
                            insertTable.InsertTable30201( conn, DataSQL.Core_CreateLog_HistoryRecord,list30201);
                            logger. info(  "InsertTable30201"+"**  30201 浏览器网站访问记录上报****"+list30201.size());
                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list30201.get(list30201.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_HistoryRecord",id);;
                        }else{
                            logger. info(  "InsertTable30201"+"**  30201 浏览器网站访问记录上报,没有需要更新的数据****"+list30201.size());
                        }

                        //=========================================================================================================
                        // 30202,浏览器Cookie@
                        Map<String, Object>  CK_ListAnalyserLog30202=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_CookieRecord");
                        Object num30202=CK_ListAnalyserLog30202.get("num");
                        if(num30202==null){//表示没有插入过数据
                            num30202=0;
                        }
                        List<Map<String,Object>> list30202=SQLDetailDao.getList(conn_1, DataSQL.Log_CookieRecord, checkConfigId_1,num30202);
                        if(list30202!=null &&list30202.size()>0){
                            Collections_sort(list30202);
                            //执行插入数据
                            insertTable.InsertTable30202( conn, DataSQL.Core_CreateLog_CookieRecord,list30202);
                            logger. info(  "InsertTablelist30202"+"**   30202,浏览器Cookie****"+list30202.size());
                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list30202.get(list30202.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_CookieRecord",id);;
                        }else{
                            logger. info(  "InsertTablelist30202"+"**   30202,浏览器Cookie,没有需要更新的数据****"+list30202.size());
                        }

                        // 30203,浏览器地址栏@
                        Map<String, Object>  CK_ListAnalyserLog30203=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_IeAddress");
                        Object num30203=CK_ListAnalyserLog30203.get("num");
                        if(num30203==null){//表示没有插入过数据
                            num30203=0;
                        }
                        List<Map<String,Object>> list30203=SQLDetailDao.getList(conn_1, DataSQL.Log_IeAddress, checkConfigId_1,num30203);
                        if(list30203!=null &&list30203.size()>0){
                            Collections_sort(list30203);
                            //执行插入数据
                            insertTable.InsertTable30203( conn, DataSQL.Core_CreateLog_IeAddress,list30203);
                            logger. info(  "InsertTablelist30203"+"**  30203,浏览器地址栏****"+list30203.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list30203.get(list30203.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_IeAddress",id);;
                        }else{
                            logger. info(  "InsertTablelist30203"+"**  30203,浏览器地址栏,没有需要更新的数据****"+list30203.size());
                        }

                        // 30204,浏览器收藏夹@
                        Map<String, Object>  CK_ListAnalyserLog30204=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_Favorite");
                        Object num30204=CK_ListAnalyserLog30204.get("num");
                        if(num30204==null){//表示没有插入过数据
                            num30204=0;
                        }
                        List<Map<String,Object>> list30204=SQLDetailDao.getList(conn_1, DataSQL.Log_Favorite, checkConfigId_1,num30204);
                        if(list30204!=null &&list30204.size()>0){
                            Collections_sort(list30204);
                            //执行插入数据
                            insertTable.InsertTable30204( conn, DataSQL.Core_CreateLog_Favorite,list30204);
                            logger. info(  "InsertTablelist30204"+"**  30204,浏览器收藏夹****"+list30204.size());
                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list30204.get(list30204.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_Favorite",id);;
                        }else{
                            logger. info(  "InsertTablelist30204"+"**  30204,浏览器收藏夹,没有需要更新的数据****"+list30204.size());
                        }

                        // 30205,浏览器缓存
                        Map<String, Object>  CK_ListAnalyserLog30205=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_CacheRecord");
                        Object num30205=CK_ListAnalyserLog30205.get("num");
                        if(num30205==null){//表示没有插入过数据
                            num30205=0;
                        }
                        List<Map<String,Object>> list30205=SQLDetailDao.getList(conn_1, DataSQL.Log_CacheRecord, checkConfigId_1,num30205);
                        if(list30205!=null &&list30205.size()>0){
                            Collections_sort(list30205);
                            //执行插入数据
                            insertTable.InsertTable30205( conn, DataSQL.Core_CreateLog_CacheRecord,list30205);
                            logger. info(  "InsertTablelist30205"+"** 30205,浏览器缓存****"+list30205.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list30205.get(list30205.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_CacheRecord",id);;
                        }else{
                            logger. info(  "InsertTablelist30205"+"** 30205,浏览器缓存,没有需要更新的数据****"+list30205.size());
                        }

                        //3021,下载软件
                        Map<String, Object>  CK_ListAnalyserLog3021=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_DownloadTool");
                        Object num3021=CK_ListAnalyserLog3021.get("num");
                        if(num3021==null){//表示没有插入过数据
                            num3021=0;
                        }
                        List<Map<String,Object>> list3021=SQLDetailDao.getList(conn_1, DataSQL.Log_DownloadTool, checkConfigId_1,num3021);
                        if(list3021!=null &&list3021.size()>0){
                            Collections_sort(list3021);
                            //执行插入数据
                            insertTable.InsertTable3021( conn, DataSQL.Core_CreateLog_DownloadTool,list3021);
                            logger. info(  "InsertTablelist3021"+"** 3021,下载软件****"+list3021.size());
                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list3021.get(list3021.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_DownloadTool",id);;
                        }else{
                            logger. info(  "InsertTablelist3021"+"** 3021,下载软件,没有需要更新的数据****"+list3021.size());
                        }

                        //3022,网盘痕迹
                        Map<String, Object>  CK_ListAnalyserLog3022=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_SkyDrive");
                        Object num3022=CK_ListAnalyserLog3022.get("num");
                        if(num3022==null){//表示没有插入过数据
                            num3022=0;
                        }
                        List<Map<String,Object>> list3022=SQLDetailDao.getList(conn_1, DataSQL.Log_SkyDrive, checkConfigId_1,num3022);
                        if(list3022!=null &&list3022.size()>0){
                            Collections_sort(list3022);
                            //执行插入数据
                            insertTable.InsertTable3022( conn, DataSQL.Core_CreateLog_SkyDrive,list3022);
                            logger. info(  "InsertTablelist3022"+"** 3022,网盘痕迹****"+list3022.size());
                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list3022.get(list3022.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_SkyDrive",id);;
                        }else{
                            logger. info(  "InsertTablelist3022"+"** 3022,网盘痕迹,没有需要更新的数据****"+list3022.size());
                        }

                        //3023,聊天软件
                        Map<String, Object>  CK_ListAnalyserLog3023=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_ChatSoft");
                        Object num3023=CK_ListAnalyserLog3023.get("num");
                        if(num3023==null){//表示没有插入过数据
                            num3023=0;
                        }
                        List<Map<String,Object>> list3023=SQLDetailDao.getList(conn_1, DataSQL.Log_ChatSoft, checkConfigId_1,num3023);
                        if(list3023!=null &&list3023.size()>0){
                            Collections_sort(list3023);
                            //执行插入数据
                            insertTable.InsertTable3023( conn, DataSQL.Core_CreateLog_ChatSoft,list3023);
                            logger. info(  "InsertTablelist3023"+"** 3023,聊天软件****"+list3023.size());
                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list3023.get(list3023.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_ChatSoft",id);;
                        }else{

                            logger. info(  "InsertTablelist3023"+"** 3023,聊天软件,没有需要更新的数据****"+list3023.size());
                        }

                        //3024,邮件客户端
                        Map<String, Object>  CK_ListAnalyserLog3024=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_EmailAgent");
                        Object num3024=CK_ListAnalyserLog3024.get("num");
                        if(num3024==null){//表示没有插入过数据
                            num3024=0;
                        }
                        List<Map<String,Object>> list3024=SQLDetailDao.getList(conn_1, DataSQL.Log_EmailAgent, checkConfigId_1,num3024);
                        if(list3024!=null &&list3024.size()>0){
                            Collections_sort(list3024);
                            //执行插入数据
                            insertTable.InsertTable3024( conn, DataSQL.Core_CreateLog_EmailAgent,list3024);
                            logger. info(  "InsertTablelist3024"+"** 3024,邮件客户端****"+list3024.size());
                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list3024.get(list3024.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_EmailAgent",id);;
                        }else{
                            logger. info(  "InsertTablelist3024"+"** 3024,邮件客户端,没有需要更新的数据****"+list3024.size());
                        }

                    }
                    //2.8.1.18上网痕迹--上网记录深度检查上报（302）
                    if(depthcheckstr_0.indexOf("isUrlDepth")!=-1 && depthcheckstr_1.indexOf("isUrlDepth")!=-1){
                        Map<String, Object>  CK_ListAnalyserLog3002=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Log_UrlDeepFind");
                        Object num3002=CK_ListAnalyserLog3002.get("num");
                        if(num3002==null){//表示没有插入过数据
                            num3002=0;
                        }
                        List<Map<String,Object>> list3002=SQLDetailDao.getList(conn_1, DataSQL.Log_UrlDeepFind, checkConfigId_1,num3002);
                        if(list3002!=null &&list3002.size()>0){
                            Collections_sort(list3002);
                            //执行插入数据
                            insertTable.InsertTable3002( conn, DataSQL.Core_CreateLog_UrlDeepFind,list3002);
                            logger. info(  "InsertTable3002"+"**3002 上网记录深度检查上报***"+list3002.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=list3002.get(list3002.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Log_UrlDeepFind",id);;
                        }else{
                            logger. info(  "InsertTable3002"+"**3002 上网记录深度检查上报,没有需要更新的数据***"+list3002.size());
                        }
                    }
                    /****
                     * 处理违规数据
                     */
                    if(checkstate_0.equals("2")){
                        Map<String, Object>  Check_ViolationDevice=SQLDetailDao.getMap(conn, DataSQL.CK_ListAnalyserLog,checkConfigId_0,groupId,"Check_ViolationDevice");
                        Object num=Check_ViolationDevice.get("num");
                        if(num==null){//表示没有插入过数据
                            num=0;
                        }
                        List<Map<String,Object>> listViolationDevice=SQLDetailDao.getList(conn_1, DataSQL.Check_ViolationDevice, checkConfigId_1,num);
                        if(listViolationDevice!=null &&listViolationDevice.size()>0){
                            Collections_sort(listViolationDevice);
                            //执行插入数据
                            insertTable.Check_ViolationDevice( conn, DataSQL.insert_Check_ViolationDevice,listViolationDevice);
                            logger. info(  "listViolationDevice"+"*违规设备信息入库***"+listViolationDevice.size());

                            /**===========================插入和读取日志更新条数=====================================**/
                            Object id=listViolationDevice.get(listViolationDevice.size()-1).get("id");
                            SQLDetailDao.sqlUpdate(conn, DataSQL.CK_AnalyserLog,checkConfigId_0,groupId,"Check_ViolationDevice",id);;
                        }else{
                            logger. info(  "listViolationDevice"+"**违规设备信息入库,没有需要更新的数据***"+listViolationDevice.size());
                        }

                    }

                    /****更新检查报告（结束检查之后，有可能会有新的日志上报，需要重现统计）
                     * CK_StatisticFinishCheck
                     */
                    if(checkstate_0.equals("4")){
                        //int flag=insertTable.CK_StatisticFinishCheck( conn, DataSQL.CK_StatisticFinishCheck,checkConfigId_0);
                        //logger. info(  "CK_StatisticFinishCheck"+"**3结束检查之后，有可能会有新的日志上报，需要重现统计***"+flag);
                    }
                    /**********插入同步次数**********/
                    int flag=insertTable.CK_EditSynchroRecord( conn, DataSQL.CK_EditSynchroRecord,checkConfigId_0);
                    logger. info(  "CK_EditSynchroRecord"+"**插入执行次数***"+flag);

                }
            }


            /******关掉连接********/
            conn_1.close();
        }else{
            logger. info(  "没有需要同步的检查任务，任务名称："+taskName);
        }

        //System.out.println("结束时间："+sdf.format(new Date()));
        logger. info(  "结束时间："+sdf.format(new Date())+" 任务名称："+taskName);

    }
}
