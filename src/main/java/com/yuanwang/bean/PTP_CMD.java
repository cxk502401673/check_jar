package com.yuanwang.bean;

public class PTP_CMD {
    public  final String Module="cmd=module";//ywsmpagent.exe 加载的 dll(通过此配置可以获取到安装目录)
    /*** 点对对命令,通用检查系统功能命令****/
    public  final String Onlyid="cmd=onlyid";//获取唯一设备ID
    public  final String Policy="cmd=policy";//客户端策略
    public  final String Prclist="cmd=prclist";//进程列表
    public  final String Svclist="cmd=svclist";//客户端服务列表
    public  final String Getfile="cmd=getfile\r\np1=";//获取指定文件
    public  final String Debug="cmd=debug\r\np1=";//下发调试号
    public  final String Software="cmd=software";//已安装软件列表
    public  final String Uninstall="cmd=uninstall";//卸载客户端
    public  final String BMCK_Uplog_32="C:\\Windows\\System32\\BMCK\\uplog.db3.sed";//BMCK
    public  final String BMCK_Uplog_64="C:\\Windows\\SysWOW64\\BMCK\\uplog.db3.sed";//BMCK
    public  final String BMCK="C:\\BMCK.log";//BMCK
    /*** 其他命令 ***/
    public  final String Stopprotect="cmd=stopprotect";//停保护
    public  final String Setdenyall="cmd=setdenyall";//取消一切断网
    public  final String Shutdown="cmd=shutdown";//关机
    /**** 断网 ****/
    public  final String Killnet="cmd=killnet\r\np1=";//断网 + 提示信息
    public  final String Unkillnet="cmd=unkillnet\r\np1=";//恢复网络，取消断网 + 提示信息
    public  final String Unlockaction="cmd=unlockaction\r\np1=";//解除持续断网 + 提示信息

    public  final String Exit="cmd=exit";//结束客户端进程
    public  final String Getdir="cmd=getdir";//获取客户端安装目录下的文件（组件版本）

    //提醒
    public  final String Message="cmd=message\r\np1=";//提醒

    //cmd
    public  final String Execcmd="cmd=execcmd\r\np1=";//执行本地命令
    /***
     * 获取文件
     * cmd=getfile\r\np1=c:\\bmck.log
     *
     * 1.设备ID
     * cmd=onlyid
     *
     * 2.当前策略
     * cmd=policy
     *
     * 3.进程列表
     * cmd=prclist
     *
     * 4.服务列表
     * cmd=svclist
     *
     * 5.客户端上报日志
     * C:\\Windows\\System32\\BMCK\\uplog.db3.sed
     * C:\\Windows\\SysWOW64\\BMCK\\uplog.db3.sed
     * cmd=getfile\r\np1=C:\\Windows\\SysWOW64\\BMCK\\uplog.db3.sed
     *
     * 6.下发调试号 ，多个,号分割
     * cmd=debug\r\np1=3
     *
     * 7.已安装软件列表
     * cmd=software
     *
     * 8.卸载客户端
     * cmd=uninstall
     *
     * 获取本地安全策略
     * cmd=security_config
     */
}
