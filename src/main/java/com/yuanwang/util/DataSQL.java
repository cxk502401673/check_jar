package com.yuanwang.util;

public class DataSQL {
    //违规项自定义表
    public static  final String CK_OutLineCheckItem="select * from CK_OutLineCheckItem";

    /***
     * 客户端升级处理
     */
    public static  final String Core_CreateMCConfig="{call Core_CreateMCConfig(?,?)}";

    /***
     * 策略缓存表
     */
    public static  final String PoliciesBase="select  id,policyname,policycontent,remark,crccheck,policyobject,status from  PoliciesBase  where status=1 and Disabled=0 ";
    public static  final String CK_EditPolicyBase="{CALL CK_EditPolicyBase(?,?,?,?,?,?,?,?,?,?)}";
    public static  final String PoliciesBase_clear="update PoliciesBase set status=1 and Disabled=1 ";


    //读取任务信息 取最新的两条比较
    public static  final String checkConfigList_time=" select * from  check_config  order by id desc limit 0,2  ";

    /****
     * 服务黑名单
     * 端口黑名单同步
     */
    public static  final String Check_ServiceBlackList="select * from Check_ServiceBlackList"; //服务
    public static  final String Check_PortBlackList="select * from Check_PortBlackList"; //端口
    public static  final String CK_ImportCheckServiceBlackList="{call CK_ImportCheckServiceBlackList(?,?)}";
    public static  final String CK_ImportCheckPortBlackList="{call CK_ImportCheckPortBlackList(?,?)}";
    //明细违规数据处理
    public static  final String Check_ViolationDevice="select * FROM Check_ViolationDevice where checkconfigid=? and id>?";

    public static  final String insert_Check_ViolationDevice="insert into check_violationdevice(CheckConfigId,ReportFlag,UniqueId) values(?,?,?)";
    /***
     * 授权信息处理
     */
    public static  final String TRUNCATE_SystemAuthorizeMap="TRUNCATE TABLE SystemAuthorizeMap";
    public static  final String List_SystemAuthorizeMap="select * from SystemAuthorizeMap";
    public static  final String insertSystemAuthorizeMap="Insert Into SystemAuthorizeMap(AuthorizeKey,AuthorizeValue,updatetime,EncryptionText) Values(?,?,?,?)";
    public static  final String TSMS_CreateConfig="{call TSMS_CreateConfig(?,?,?)}";

    public static  final String TSMS_ListConfigs="{call TSMS_ListConfigs(?)}";
    /****
     * 读取多个单位列表
     * 只取 开启的
     */
    public static  final String groupIds="SELECT GROUP_CONCAT(CONCAT('CheckDetail_',groupId))  as groupIds FROM CK_SystemCascade where IsDisabled=1" ;
    public static  final String CK_ListSystemALL_id="SELECT LoginName,DatabasePassword,DatabaseName,DatabaseIP,Port ,groupId,IsDisabled FROM CK_SystemCascade where id=? ";
    public static  final String CK_ListSystemALL="SELECT LoginName,DatabasePassword,DatabaseName,DatabaseIP,Port ,groupId,IsDisabled FROM CK_SystemCascade where IsDisabled=1 ";
    //通过当前任务查询
    public static  final String CK_ListSystem_Tssk="SELECT LoginName,DatabasePassword,DatabaseName,DatabaseIP,Port ,groupId,IsDisabled FROM CK_SystemCascade where CascadeName=? ";


    //取ID大于多少的ID
    public static  final String where_id= " where id>? ";
    public static  final String and_id= " and id>?  LIMIT 0,80000 ";
    //读取最大ID
    public static  final String max_id= "SELECT max(id) from TABLE_NAME  where checkconfigid=? and id>? LIMIT 0,80000";
    /***
     * 统计已经上报的检查项条数
     */
    public static  final String CK_AnalyserLog= "call CK_AnalyserLog(?,?,?,?)";
    public static  final String CK_ListAnalyserLog= "call CK_ListAnalyserLog(?,?,?)";
    public static  final String TRUNCATE_AnalyserLog= "TRUNCATE TABLE AnalyserLog";

    //删除进程
    public static  final String KILL= " KILL ?";

    //线程锁列表
    public static  final String INNODB_TRX= " SELECT trx_mysql_thread_id AS Id FROM information_schema.innodb_trx  WHERE trx_started<DATE_ADD(NOW(),INTERVAL -30 SECOND) ";
    //修改检查任务状态
    public static  final String WorkAnalyse_Task= "update WorkAnalyse_Task set IsEnabled=? WHERE TaskName =?";
    ///执行次数
    public static  final String CK_ListSynchroRecord= "{CALL CK_ListSynchroRecord(?)}";
    public static  final String CK_EditSynchroRecord= "{CALL CK_EditSynchroRecord(?)}";

    //生成報告
    public static  final String CK_CreateComCheckReport2= "{call CK_CreateComCheckReport2(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

    //读取设备id
    public static  final String MC_DevicesBasic_id="SELECT   id  from MC_DevicesBasic WHERE UniqueId=? or ( ipnum=TransformIP2Num(?) and mac=? ) LIMIT 0,1  ";

    public static  final String CK_CreateCascadeDatabase= "{CALL CK_CreateCascadeDatabase(?,?,?)}";
    //当前部门列表
    public static  final String CK_ListGroups= "{CALL CK_ListGroups(?)}";
    /******
     * 任务内容包含：
     * 1.给下级部门新建相同的部门信息
     * 2.任务新建
     * 3.策略下发
     * 4.策略执行结果表从下级同步到上级数据库中
     *
     * 管控中心设备入库 Core_CreateDeviceItem
     * 策略执行结果表 Log_PolicyExecuteResult
     */
    //更新检查报告（结束检查之后，有可能会有新的日志上报，需要重现统计）
    public static  final String CK_StatisticFinishCheck="{CALL CK_StatisticFinishCheck(?)}";
    //读取部门信息
    public static  final String Groups="select id,name,parentid from  Groups ";
    //通过部门id进行查询
    public static  final String GroupsByid="select id,name,parentid from  Groups where id=? ";
    //部门批量导入
    public static  final String CK_ImportGroups="{CALL CK_ImportGroups (?,?,?)}";
    //读取任务信息
    public static  final String checkConfigList=" select * from  check_config ORDER BY id DESC LIMIT 0,1  ";
    //新建检查任务
    public static  final String CK_CreateCheckConfig="{CALL CK_CreateCheckConfig(?,?,?,?,?,?,?,?,?,?)}";
    //读取开启的检查策略
    public static  final String Core_GetPolicy="{CALL Core_GetPolicy(?)}";
    //下发检查策略
    public static  final String CK_EditPolicy="{CALL CK_EditPolicy(?,?,?,?,?,?,?,?,?,?)}";
    //读取策略执行结果表
    public static  final String Log_PolicyExecuteResult="select * from  Log_PolicyExecuteResult"+where_id;
    //插入策略执行结果表
    public static  final String CK_ImportPolicyExecuteResult=" {CALL CK_ImportPolicyExecuteResult( "+
            "?,?,?,?,?,?,?,?" +
            " ) }";

    //更新当前注册的设备信息
    public static  final String Core_CreateDeviceItem=" {CALL Core_CreateDeviceItemCascade( "+
            "?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?,?,?" +
            " ) }";

    //结束检查任务
    public static  final String CK_FinishCheck="{CALL CK_FinishCheck()} ";

    /*****读取最新的检查任务ID*******///isOnlyHist=1 只显示浏览器历史 
    //获取数据库配置信息
    public static  final String CK_ListCascadeDatabase="{call CK_ListCascadeDatabase(?)}";
    public static  final String checkConfigId="select max(id) as checkConfigId from check_config";
    /****读取最新的检查任务信息*********/
    public static  final String listCheckConfig="{call CK_ListCheckConfig(?)}";
    /******获取设备信息********/
    //当前正在检查的设备
    public static  final String Check_Devices="select UniqueId,Name,RegisterUser,IP,Mac,DepartmentCode,Phone,GroupId,GroupName,RegisterTime,AVVendor,AVVersion,VirusDbVersion,AVInstalled,SecurityClassification,OriginalMac,IsRegisted,OSDetail,ispoweron,agentversion from Check_Devices where checkConfigId=? and UniqueId!=0 ";
    //历史设备
    public static  final String Devices="select UniqueId,Name,RegisterUser,IP,Mac,DepartmentCode,Phone,GroupId,GroupName,RegisterTime,AVVendor,AVVersion,VirusDbVersion,AVInstalled,SecurityClassification,OriginalMac,IsRegisted,OSDetail,ispoweron,DeviceStatus,lastusetime,lasttriggertime,agentversion from Devices where  UniqueId!=0 ";// where IsRegisted=1
    //插入设备信息
    public static  final String CK_ImportCheckDevices="{call CK_ImportAntiVirus(" +
            "?,?,?,?,?,?,?,?,?,?," +
            "?,?,?,?,?,?,?)}";

    /****日志查询接口***************/
    //103 涉密文件检查
    // IsDelFile 1,已删除文件，0 普通文件检查
    public static  final String Log_ConfidentialSpecial="select * from Log_ConfidentialSpecial where checkConfigId=? and IsDelFile=?"+and_id;//"{call CK_ListConfidential_KeyWords(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    //邮件文件检查
    public static  final String Log_ConfidentialEmail="select * from Log_ConfidentialEmail where checkConfigId=? "+and_id;//"{call CK_ListConfidential_KeyWords(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
    // 507 安全审计配置
    public static  final String Log_AuditStrategy="select * from Log_AuditStrategy WHERE  checkConfigId=? "+and_id;

    //506= '账户安全配置'
    public static  final String Log_StrategySecurity="select * from Log_StrategySecurity WHERE  checkConfigId=? "+and_id;

    //203= '网络应用软件'
    public static  final String Log_TerminalSoftware="select * from Log_TerminalSoftware WHERE  checkConfigId=? "+and_id;
    //502= '开放端口';
    public static  final String Log_NetworkConnectionAudit="select * from Log_NetworkConnectionAudit WHERE  checkConfigId=? "+and_id;
    // 201= '服务检查';
    public static  final String Log_TerminalService="select * from Log_TerminalService WHERE  checkConfigId=? "+and_id;

    //  505= '弱口令';
    public static  final String Log_SystemAccount="select * from Log_SystemAccount WHERE  checkConfigId=? "+and_id;

    //  512= '已安装补丁';
    public static  final String CK_InstalledPatch="select * from CK_InstalledPatch WHERE  checkConfigId=? "+and_id;

    //  515 = '虚拟机安装';
    public static  final String Log_VmwareChk="select * from Log_VmwareChk WHERE  checkConfigId=? "+and_id;

    //  204= '三合一产品';
    public static  final String Log_SanHeYi="select * from Log_SanHeYi WHERE  checkConfigId=? "+and_id;

    //  504= '未安装补丁';
    public static  final String Log_unInstalled_Patch="select * from Log_unInstalled_Patch WHERE  checkConfigId=? "+and_id;
    //  503= '共享列表';
    public static  final String Log_ShareInfo="select * from Log_ShareInfo WHERE  checkConfigId=? "+and_id;

    // 514 = '无线通信设备';// 2017-9-19
    public static  final String Log_HardDev="select * from Log_HardDev WHERE (iswireless=1 or  iswireless=2) and checkConfigId=? "+and_id;

    // 516= 多操作系统
    public static  final String Log_OSInstall="select * from Log_OSInstall WHERE  checkConfigId=? "+and_id;

    // 517= 硬盘信息
    public static  final String Log_HardDiskInfo="select * from Log_HardDiskInfo WHERE  checkConfigId=? "+and_id;
    // 5171= 硬盘信息--分区信息
    public static  final String Log_HardDevicePart="select * from Log_HardDevicePart WHERE  checkConfigId=? "+and_id;

    // 5172= 硬盘信息--2.8.1.43磁盘加密分区日志上报（517）
    public static  final String Log_DiskLocker="select * from Log_DiskLocker WHERE  checkConfigId=? "+and_id;

    // 5173= 硬盘信息--2.8.1.44从盘使用记录日志上报（517）
    public static  final String Log_ExecDiskDrive="select * from Log_ExecDiskDrive WHERE  checkConfigId=? "+and_id;

    // 301=USB设备
    public static  final String Log_DetailUsbRecord="select * from Log_DetailUsbRecord WHERE  checkConfigId=? "+and_id;

    // 3001=USB深度检查
    public static  final String Log_DetailUsbHis="select * from Log_DetailUsbHis WHERE  checkConfigId=? "+and_id;

    //  401= '互联网连接状态';
    public static  final String Log_InternetLink="select * from Log_InternetLink WHERE  checkConfigId=? "+and_id;

    //  403= 网络接口
    public static  final String Log_NetConn="select * from Log_NetConn WHERE  checkConfigId=? "+and_id;

    //  2.8.1.13浏览器网站访问记录上报（302）
    public static  final String Log_HistoryRecord="select * from Log_HistoryRecord WHERE  checkConfigId=? "+and_id;

    //  2.8.1.18上网痕迹--上网记录深度检查上报（302）
    public static  final String Log_UrlDeepFind="select * from Log_UrlDeepFind WHERE  checkConfigId=? "+and_id;

    //30202,浏览器Cookie@";
    public static  final String Log_CookieRecord="select * from Log_CookieRecord WHERE  checkConfigId=? "+and_id;
    //30203,浏览器地址栏@";
    public static  final String Log_IeAddress="select * from Log_IeAddress WHERE  checkConfigId=? "+and_id;

    //30204,浏览器收藏夹@";
    public static  final String Log_Favorite="select * from Log_Favorite WHERE  checkConfigId=? "+and_id;
    //30205,浏览器缓存@";
    public static  final String Log_CacheRecord="select * from Log_CacheRecord WHERE  checkConfigId=? "+and_id;
    //3021,下载软件@";
    public static  final String Log_DownloadTool="select * from Log_DownloadTool WHERE  checkConfigId=? "+and_id;
    //3022,网盘痕迹@";
    public static  final String Log_SkyDrive="select * from Log_SkyDrive WHERE  checkConfigId=? "+and_id;

    //3023,聊天软件@";
    public static  final String Log_ChatSoft="select * from Log_ChatSoft WHERE  checkConfigId=? "+and_id;

    //3024,邮件客户端@";;
    public static  final String Log_EmailAgent="select * from Log_EmailAgent WHERE  checkConfigId=? "+and_id;

    /****日志入库接口***************/
    //103  涉密文件检查 包含邮件文件
    public static  final String Core_CreateLog_Confidential=
            "{ call Core_CreateLog_ConfidentialExtend_analyser (" +
                    "?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?," +

                    "?,?,?,?,?," +
                    "?,?,?) }";

    // 507 安全审计配置
    public static  final String Core_CreateLog_AuditStrategy=
            "{ call Core_CreateLog_AuditStrategy_analyser (" +
                    "?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?) }";

    //506= '账户安全配置'
    public static  final String Core_CreateLog_StrategySecurity=
            "{ call Core_CreateLog_StrategySecurity_analyser (" +
                    "?,?,?,?,?) }";

    //203= '网络应用软件'
    public static  final String Core_CreateLog_TerminalSoftware=
            "{ call Core_CreateLog_TerminalSoftware_analyser (" +
                    "?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?," +//GroupId,GroupName,IP,IPNum,Mac,`User`,DepartmentCode,Phone,DevicesName,SecurityClassification,CheckSum
                    "?,?) }";

    //502= '开放端口';
    public static  final String Core_CreateLogNetworkConnectionAudit=
            "{ call Core_CreateLogNetworkConnectionAudit_analyser (" +
                    "?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?" +//GroupId,GroupName,IP,IPNum,Mac,`User`,DepartmentCode,Phone,DevicesName,SecurityClassification,CheckSum
                    ") }";

    // 201= '服务检查';
    public static  final String Core_CreateLog_TerminalService=
            "{ call Core_CreateLog_TerminalService_analyser (" +
                    "?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,"+//GroupId,GroupName,IP,IPNum,Mac,`User`,DepartmentCode,Phone,DevicesName,SecurityClassification,CheckSum
                    "?,?,?,?,?,?,?,?,?" +
                    ") }";

    // 505= '弱口令';
    public static  final String Core_CreateLog_SystemAccount=
            "{ call Core_CreateLog_SystemAccount_analyser (" +
                    "?,?,?,?,?,?,?,?,?,?," +
                    "?,?" +
                    ") }";
    //  512= '已安装补丁';
    public static  final String CK_CreateInstalledPatch=
            "{ call CK_CreateInstalledPatch_analyser (" +
                    "?,?,?,?,?,?" +
                    ") }";

    //  515 = '虚拟机安装';
    public static  final String Core_CreateLog_VmwareChk=
            "{ call Core_CreateLog_VmwareChk_analyser (" +
                    "?,?,?,?,?,?,?,?" +
                    ") }";
    //  204= '三合一产品';	
    public static  final String Core_CreateLog_SanHeYi=
            "{ call Core_CreateLog_SanHeYi_analyser (" +
                    "?,?,?,?,?,?,?,?" +
                    ") }";

    //  504= '未安装补丁';
    public static  final String Core_CreateLog_unInstalled_Patch=
            "{ call Core_CreateLog_unInstalled_Patch_analyser (" +
                    "?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?" +//GroupId,GroupName,IP,IPNum,Mac,`User`,DepartmentCode,Phone,DevicesName,SecurityClassification,CheckSum
                    ") }";
    //  503= '共享列表';
    public static  final String Core_CreateLog_ShareInfo=
            "{ call Core_CreateLog_ShareInfo_analyser (" +
                    "?,?,?,?,?,?,?,?,?" +
                    ") }";
    //   514 = '无线通信设备';// 2017-9-19;
    public static  final String Core_CreateLog_HardDev=
            "{ call Core_CreateLog_HardDev_analyser (" +
                    "?,?,?,?,?,?,?" +
                    ") }";
    //  516 = 多操作系统
    public static  final String Core_CreateLog_OSInstall=
            "{ call Core_CreateLog_OSInstall_analyser (" +
                    "?,?,?,?,?,?,?,?,?,?," +
                    "?,?" +
                    ") }";
    //  517 =硬盘信息
    public static  final String Core_CreateLog_HardDiskInfo=
            "{ call Core_CreateLog_HardDiskInfo_analyser (" +
                    "?,?,?,?,?,?,?,?,?,?," +
                    "?" +
                    ") }";

    //  5171 =硬盘信息--分区信息
    public static  final String Core_CreateLog_HardDevicePart =
            "{ call Core_CreateLog_HardDevicePart_analyser  (" +
                    "?,?,?,?,?,?,?,?,?" +
                    ") }";

    //  5172 =硬盘信息--磁盘加密分区日志上报（517）
    public static  final String Core_CreateLog_DiskLocker =
            "{ call Core_CreateLog_DiskLocker_analyser  (" +
                    "?,?,?,?,?" +
                    ") }";

    //  5173 =硬盘信息--从盘使用记录
    public static  final String Core_CreateLog_ExecDiskDrive =
            "{ call Core_CreateLog_ExecDiskDrive_analyser  (" +
                    "?,?,?,?,?" +
                    ") }";

    //  301= USB设备
    public static  final String Core_CreateLog_DetailUsbRecord=
            "{ call Core_CreateLog_DetailUsbRecord_analyser (" +
                    "?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?,?," +//GroupId,GroupName,IP,IPNum,Mac,`User`,DepartmentCode,Phone,DevicesName,SecurityClassification,CheckSum
                    "?,?" +
                    ") }";
    //  3001= USB深度检查
    public static  final String Core_CreateLog_DetailUsbHis=
            "{ call Core_CreateLog_DetailUsbHis_analyser (" +
                    "?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?,?," +//GroupId,GroupName,IP,IPNum,Mac,`User`,DepartmentCode,Phone,DevicesName,SecurityClassification,CheckSum
                    "?" +
                    ") }";
    //  401= '互联网连接状态';
    public static  final String Core_CreateLog_InternetLink=
            "{ call Core_CreateLog_InternetLink_analyser (" +
                    "?,?,?,?,?" +
                    ") }";
    //  403= '互联网连接状态';
    public static  final String Core_CreateLog_NetConn=
            "{ call Core_CreateLog_NetConn_analyser (" +
                    "?,?,?,?,?,?" +
                    ") }";
    //2.8.1.13上网痕迹--浏览器网站访问记录上报（302）
    public static  final String Core_CreateLog_HistoryRecord=
            "{ call Core_CreateLog_HistoryRecord_analyser (" +
                    "?,?,?,?,?,?,?,?,?,?, " +
                    "?,?,?,?,?,?,?,?,?,?,?" +//GroupId,GroupName,IP,IPNum,Mac,`User`,DepartmentCode,Phone,DevicesName,SecurityClassification,CheckSum
                    ") }";
    //2.8.1.18上网痕迹--上网记录深度检查上报（302）
    public static  final String Core_CreateLog_UrlDeepFind=
            "{ call Core_CreateLog_UrlDeepFind_analyser (" +
                    "?,?,?,?,?,?,?,?,?, " +
                    "?,?,?,?,?,?,?,?,?,?,?" +//GroupId,GroupName,IP,IPNum,Mac,`User`,DepartmentCode,Phone,DevicesName,SecurityClassification,CheckSum
                    ") }";
    //230202,浏览器Cookie@";
    public static  final String Core_CreateLog_CookieRecord=
            "{ call Core_CreateLog_CookieRecord_analyser (" +
                    "?,?,?,?,?,?,?,?,?,? ," +
                    "?,?,?,?,?,?,?,?,?,?,?" +//GroupId,GroupName,IP,IPNum,Mac,`User`,DepartmentCode,Phone,DevicesName,SecurityClassification,CheckSum
                    ") }";
    //30203,浏览器地址栏@";
    public static  final String Core_CreateLog_IeAddress=
            "{ call Core_CreateLog_IeAddress_analyser (" +
                    "?,?,?,?,?,?,?,? ," +
                    "?,?,?,?,?,?,?,?,?,?,?" +//GroupId,GroupName,IP,IPNum,Mac,`User`,DepartmentCode,Phone,DevicesName,SecurityClassification,CheckSum
                    ") }";
    //30204,浏览器收藏夹@";
    public static  final String Core_CreateLog_Favorite=
            "{ call Core_CreateLog_Favorite_analyser (" +
                    "?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?," +//GroupId,GroupName,IP,IPNum,Mac,`User`,DepartmentCode,Phone,DevicesName,SecurityClassification,CheckSum
                    "? " +
                    ") }";
    //30205,浏览器缓存@";;
    public static  final String Core_CreateLog_CacheRecord=
            "{ call Core_CreateLog_CacheRecord_analyser (" +
                    "?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?" +//GroupId,GroupName,IP,IPNum,Mac,`User`,DepartmentCode,Phone,DevicesName,SecurityClassification,CheckSum
                    ") }";
    //3021,下载软件@";
    public static  final String Core_CreateLog_DownloadTool=
            "{ call Core_CreateLog_DownloadTool_analyser (" +
                    "?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?," +//GroupId,GroupName,IP,IPNum,Mac,`User`,DepartmentCode,Phone,DevicesName,SecurityClassification,CheckSum
                    "?,?" +
                    ") }";
    //3022,网盘痕迹@";
    public static  final String Core_CreateLog_SkyDrive=
            "{ call Core_CreateLog_SkyDrive_analyser (" +
                    "?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?," +//GroupId,GroupName,IP,IPNum,Mac,`User`,DepartmentCode,Phone,DevicesName,SecurityClassification,CheckSum
                    "?" +
                    ") }";
    //3023,聊天软件@";
    public static  final String Core_CreateLog_ChatSoft=
            "{ call Core_CreateLog_ChatSoft_analyser (" +
                    "?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?" +//GroupId,GroupName,IP,IPNum,Mac,`User`,DepartmentCode,Phone,DevicesName,SecurityClassification,CheckSum

                    ") }";
    //3024,邮件客户端@";
    public static  final String Core_CreateLog_EmailAgent=
            "{ call Core_CreateLog_EmailAgent_analyser (" +
                    "?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?," +//GroupId,GroupName,IP,IPNum,Mac,`User`,DepartmentCode,Phone,DevicesName,SecurityClassification,CheckSum
                    "?" +
                    ") }";
}
