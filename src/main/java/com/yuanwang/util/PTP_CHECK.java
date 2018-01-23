package com.yuanwang.util;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.yuanwang.bean.PTPResult;
import com.yuanwang.bean.PTP_CMD;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


import javax.servlet.http.HttpServletRequest;
public class PTP_CHECK {
    private static Logger logger  =  Logger.getLogger(PTP_CHECK. class );
    /****
     * ptp 点对点调试客户端信息
     * @param args
     */
    private String savePath="";
    private int ostype=0;//操作系统类型 1 32位系统，2 64位系统
    private String debug_numbers="";//调试号，多个用","分割
    private String ip="";//执行IP
    private int port=22089;//调试端口
    private int regist_type=0;//执行动作
    private String  CMD_STR="";//点对点命令
    //构造方法
    public PTP_CHECK(String ip,int regist_type,String savePath,int ostype){
        this.ip=ip;
        this.regist_type=regist_type;
        this.savePath=savePath;
        this.ostype=ostype;
    }
    public PTP_CHECK(String ip,int regist_type,String savePath){
        this.ip=ip;
        this.regist_type=regist_type;
        this.savePath=savePath;
    }
    public PTP_CHECK(String ip,int regist_type){
        this.ip=ip;
        this.regist_type=regist_type;
    }
    /**
     * 执行方法
     */
    public void run(){
        boolean ishost=PTP_CHECK.isHostConnectable(ip, port);
        PTP_CMD ptp_CMD=new PTP_CMD();//完整命令

        if(regist_type==1){//客户端Uniqueid
            CMD_STR=ptp_CMD.Onlyid;
        }
        else if(regist_type==2){//客户端策略
            CMD_STR=ptp_CMD.Policy;
        }
        else if(regist_type==3){//客户端进程列表
            CMD_STR=ptp_CMD.Prclist;
        }else if(regist_type==4){//客户端服务列表
            CMD_STR=ptp_CMD.Svclist;
        }else if(regist_type==5){//客户端上报日志
            String BMCK_Uplog_Path=getBMCKPath(ishost,ptp_CMD.Module);
            if("连接失败".equals(BMCK_Uplog_Path)){
                ishost=false;
            }
            CMD_STR=ptp_CMD.Getfile+BMCK_Uplog_Path+"uplog.db3.sed";
        }
        else if(regist_type==7){//已安装软件列表
            CMD_STR=ptp_CMD.Software;
        }
        else if(regist_type==10){//客户端日志
            CMD_STR=ptp_CMD.Getfile+ptp_CMD.BMCK;
        }

        if(ishost){
            PTPUtil ptp=new PTPUtil(ip, port,CMD_STR);
            ptp.setSavepath(savePath);//输出内容保存到本地文件中
            PTPResult r=ptp.SendCmd();//执行命令
            if("".equals(r.getErrMsg().toString().trim())){
                String ErrorInfo=ip+"，"+port+""+r.getErrMsg();
                PTP_CHECK.ptpErrLog(ErrorInfo, savePath);
            }
        }else{
            String ErrorInfo=ip+"，"+port+"连接失败！";
            PTP_CHECK.ptpErrLog(ErrorInfo, savePath);
        }
    }
    /***
     * 读取bmck具体目录
     */
    public  String getBMCKPath(boolean ishost,String cmd_STR) {
        String path=savePath;
        String lines = "";
        if(ishost){
            //m0=C:\Windows\SysWOW64\BMCK\BMCKAgent.exe
            PTPUtil ptp=new PTPUtil(ip,port,cmd_STR);
            ptp.setSavepath(savePath);//输出内容保存到本地文件中
            PTPResult r=ptp.SendCmd();//执行命令
            if("".equals(r.getErrMsg().toString().trim())){
                String ErrorInfo=ip+"，"+port+""+r.getErrMsg();
                PTP_CHECK.ptpErrLog(ErrorInfo, savePath);
            }
            //读取文件中的内容
            try {
                InputStream inputStream = new FileInputStream(path);
                BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                String line = null;
                int row=0;
                while ((line = bf.readLine()) != null) {
                    row++;
                    if(row>1) {
                        break;
                    }
                    lines += line + "\n";
                }
                inputStream.close(); // 关闭流
                bf.close();
            } catch (Exception e) {
                logger.error(e);
            }
            int index=lines.indexOf("BMCK\\BMCKAgent.exe");
            if(index!=-1){
                lines=lines.substring(3,index)+"BMCK\\";
            }else{
                lines="";
            }
        }else{
            lines="连接失败";
        }
        return lines;
    }
    /***
     * 6.下发调试号 ，多个,号分割
     * 8.卸载客户端
     * cmd=debug\r\np1=3
     * 不需要生成日志
     */
    public  String run_debug() {
        /***
         * 6.下发调试号 ，多个,号分割
         * 8.卸载客户端
         * cmd=debug\r\np1=3
         */
        int port=22089;
        PTP_CMD ptp_CMD=new PTP_CMD();//完整命令
        if(regist_type==6){
            CMD_STR=ptp_CMD.Debug+debug_numbers;
        }
        if(regist_type==8){
            CMD_STR=ptp_CMD.Uninstall;
        }
        if(regist_type==9){
            CMD_STR=ptp_CMD.Shutdown;//重启客户端
        }
        boolean ishost=isHostConnectable(ip, port);
        if(ishost){
            PTPUtil ptp=new PTPUtil(ip, port,CMD_STR);
            PTPResult r=ptp.SendCmd();
            if(!"".equals(r.getErrMsg().trim())){
                return "操作失败！"+r.getErrMsg();
            }
            return "操作成功！";
        }else{
            return ip+","+port+"连接失败！";
        }
    }
    /***
     * 生成错误日志
     */
    public static void ptpErrLog(String ErrorInfo,String logRootPath){
        String content=ErrorInfo;
        File fileName = new File(logRootPath);

        RandomAccessFile mm = null;
        FileOutputStream o = null;
        try {
            //如果文件不存在就新建一个文件
            if (!fileName.exists()) {
                fileName.createNewFile();
            }
            o = new FileOutputStream(fileName,true);
            o.write(content.getBytes("GBK"));
            o.close();
        } catch (IOException e) {
            logger.error(e);
        } finally {
            if (mm != null) {
                try {
                    mm.close();
                } catch (IOException e) {
                    logger.error(e);
                }

            }
        }
    }
    /*******
     * 判断是否可以连接通
     * @param host
     * @param port
     * @return
     */
    public static boolean isHostConnectable(String host, int port) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port),500);
        } catch (IOException e) {
            logger.error(e);
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
        return true;
    }

    public String getCMD_STR() {
        return CMD_STR;
    }
    public void setCMD_STR(String cMD_STR) {
        CMD_STR = cMD_STR;
    }
    public int getRegist_type() {
        return regist_type;
    }
    public void setRegist_type(int regist_type) {
        this.regist_type = regist_type;
    }
    public String getDebug_numbers() {
        return debug_numbers;
    }
    public void setDebug_numbers(String debug_numbers) {
        this.debug_numbers = debug_numbers;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getSavePath() {
        return savePath;
    }
    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }
    public int getOstype() {
        return ostype;
    }
    public void setOstype(int ostype) {
        this.ostype = ostype;
    }
    public static boolean message(String fromIP, String toIP, String UniqueId, HttpServletRequest req) {
        int port=22089;
        Integer port1=req.getLocalPort();
        String name=req.getContextPath();
        String cmd="cmd=message\r\n" +
                "p1=" +
                "个人报告：\"http://"+fromIP+":"+port1+name+"/toClientReportPage.do?uniqueId="+UniqueId+"\"";
        boolean ishost=PTP_CHECK.isHostConnectable(toIP, port);
        if(ishost){
            String savePath="c:\\uplog.log";

            PTPUtil ptp=new PTPUtil(toIP, port,cmd);
            ptp.setSavepath(savePath);
            PTPResult r=ptp.SendCmd();
            if(StringUtils.isBlank(r.getResult())){
                return true;
            }else{
                return false;
            }

        }else{
            System.out.println(toIP+"，"+port+"连接失败！");
            return false;

        }
    }

    public static boolean cmdFIle(String fromIP,String toIP,String UniqueId,HttpServletRequest  req) {
        int port=22089;
        Integer port1=req.getLocalPort();
        String name=req.getContextPath();
        String info="个人报告：\"http://"+fromIP+":"+port1+name+"/toClientReportPage.do?uniqueId="+UniqueId+"\"";
        //String info=FIleLine.getLines("c:/a.html").replace("\\", "\\\\");
        String cmd="cmd=execcmd\r\n" +
                "p1=cmd /c echo "+info+" > c:/ywcheck.txt";
        boolean ishost=PTP_CHECK.isHostConnectable(toIP, port);
        if(ishost){
            String savePath="c:\\uplog.log";
            PTPUtil ptp=new PTPUtil(toIP, port,cmd);
            ptp.setSavepath(savePath);	//输出内容保存到本地文件中
            PTPResult r=ptp.SendCmd();						//执行命令
            if(StringUtils.isBlank(r.getResult())){
                return true;
            }else{
                return false;
            }

        }else{
            System.out.println(toIP+"，"+port+"连接失败！");
            return false;

        }
    }
}
