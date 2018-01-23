package com.yuanwang.util;
import com.yuanwang.bean.PTPResult;
import com.yuanwang.bean.YW_PACKET;
import lombok.Data;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
@Data
public class PTPUtil {
    /**
     * 点对点连接IP
     */
    private String 	ip="";
    /**
     * 点对点连接端口
     */
    private int 	port=22088;
    /**
     * 请求是TCP还是UDP
     */
    private boolean isTCP = true;
    /**
     * 点对点命令
     */
    private String 	cmd_str="";
    /**
     * 返回结果不可超过最大限制
     */

    private long 	result_maxsize=1024*1024*200;
    private int 	timeout=0;
    /**
     * 返回结果保存全路径
     */

    private String 	savepath="";

    public PTPUtil(){

    }

    public PTPUtil(String ip){
        this.ip=ip;
    }

    public PTPUtil(String ip,int port){
        this.ip=ip;
        if(port>0){
            this.port=port;
        }
    }

    public PTPUtil(String ip,int port,String cmd_str){
        this.ip=ip;
        if(port>0){
            this.port=port;
        }
        this.cmd_str=cmd_str;
    }

    public PTPUtil(String ip,int port,String cmd_str,boolean isTCP){
        this.ip=ip;
        if(port>0){
            this.port=port;
        }
        this.cmd_str=cmd_str;
        this.isTCP=isTCP;
    }



    private byte[] read(InputStream is,int readlen) throws IOException{
        byte[] result = new byte[readlen];
        int read_off=0;
        while(read_off<readlen){
            int n = is.read(result, read_off, readlen-read_off);
            if(n==-1){
                break;
            }
            read_off+=n;
        }
        return result;
    }

    //写文件
    private String savepath(InputStream is ,String path){
        File file=new File(path);
        if (file.exists()){
            boolean ok=file.delete();
            if(!ok){
                return "文件已经存在，删除失败";
            }
        }
        try {
            if(!file.getParentFile().exists()){
                boolean ok=file.getParentFile().mkdirs();
                if(!ok){
                    return "创建目录"+file.getParentFile()+"失败";
                }
            }
            boolean ok=file.createNewFile();
            if(!ok){
                return "创建文件"+path+"失败";
            }
        } catch (IOException e2) {
            return "创建文件"+path+"失败："+e2.getMessage();
        }
        FileOutputStream fos=null;
        try {
            fos = new FileOutputStream(path);
            byte[] b=new byte[1024];
            while(true){
                int readlen=is.read(b);
                if (readlen==-1){
                    break;
                }
                if (readlen==1024){
                    fos.write(b);
                }else{
                    byte[] tmp = new byte[readlen];
                    System.arraycopy(b, 0, tmp, 0, readlen);
                    fos.write(tmp);
                }
            }
            fos.close();
        } catch (Exception e) {
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e1) {
                    return "文件写入失败后，关闭文件失败："+e1.getMessage();
                }
            }
            return "写文件失败："+e.getMessage();
        }
        return "";
    }

    public PTPResult SendCmd(){
        if(this.cmd_str.equals("cmd=ver")){
            this.isTCP=false;
        }
        if(this.isTCP){
            return SendCmd_TCP();
        }else{
            return SendCmd_UDP();
        }
    }

    private PTPResult SendCmd_UDP(){
        PTPResult ptp_result=new PTPResult();
        try {
            //创建发送包头
            YW_PACKET ywpacket = new YW_PACKET(15,this.cmd_str.getBytes("GBK").length);

            //生成包头加内内容的byte[]
            byte[] send_str =new byte[(int)ywpacket.getHeadSize()+this.cmd_str.getBytes("GBK").length];
            System.arraycopy(ywpacket.ToByres(), 0, send_str, 0, (int)ywpacket.getHeadSize());
            System.arraycopy(this.cmd_str.getBytes("GBK"), 0, send_str, (int)ywpacket.getHeadSize(), this.cmd_str.getBytes("GBK").length);

            //发送
            DatagramPacket dataGramPacket = new DatagramPacket(send_str, send_str.length, InetAddress.getByName(this.ip), this.port);
            DatagramSocket socket = new DatagramSocket();  //创建套接字
            if(this.timeout!=0){
                socket.setSoTimeout(this.timeout);
            }
            socket.send(dataGramPacket);  //通过套接字发送数据

            //接收返回值
            byte[] backbuf = new byte[32];
            DatagramPacket backPacket = new DatagramPacket(backbuf, backbuf.length);
            socket.receive(backPacket);  //接收返回数据
            YW_PACKET result_packet=new YW_PACKET();
            result_packet.BytesToYWPACKET(backbuf);

            //有时个有返回包头有时间没有返回包头,通过把第一次返回值转成包，看包头的datalen
            if(result_packet.getDataLen()<50){
                backbuf = new byte[(int)result_packet.getDataLen()];
                DatagramPacket backbuf12 = new DatagramPacket(backbuf, backbuf.length);
                socket.receive(backbuf12);  //接收返回数据
                ptp_result.setResult(new String(backbuf,"GBK"));
            }else{
                ptp_result.setResult(new String(backbuf,"GBK").trim());
            }
            if(this.savepath!=""){
                String result=this.savepath(new ByteArrayInputStream(backbuf) ,this.savepath);
                if (result!=""){
                    ptp_result.setErrMsg(result);
                }
            }
            socket.close();
        } catch (Exception e) {
            ptp_result.setErrMsg("UDP 命令"+this.cmd_str+"出错:"+e.getMessage());
        }
        return ptp_result;
    }

    private PTPResult SendCmd_TCP(){
        PTPResult ptp_result=new PTPResult();
        OutputStream os=null;
        InputStream is=null;
        Socket socket=null;
        try {
            if(this.ip==""){
                ptp_result.setErrMsg("IP地址不可为空");
                return ptp_result;
            }
            //1.创建Socket，指定服务器地址和端口
            socket=new Socket();
            if(this.timeout!=0){
                socket.connect(new InetSocketAddress(this.ip, this.port),this.timeout);
                socket.setSoTimeout(this.timeout);
            }else{
                socket.connect(new InetSocketAddress(this.ip, this.port));
            }
            //2.获取输出流，向服务器端发送信息
            os=socket.getOutputStream();//字节输出流
            byte[] cmd_str =this.cmd_str.getBytes("GBK");
            YW_PACKET ywpacket = new YW_PACKET(15,cmd_str.length);
            //发送包头

            os.write(ywpacket.ToByres());
            //发送包内容
            os.write(cmd_str);
            socket.shutdownOutput();
            //关闭输出流
            //3.获取输入流，并读取服务器端的响应信息

            is=socket.getInputStream();
            //读取返回结果的包头大小
            byte[] headsize_byte=this.read(is, 4);
            int headsize = ywpacket.bytesToInt(headsize_byte[0], headsize_byte[1], headsize_byte[2], headsize_byte[3]);

            //读取返回结果的包头
            if (headsize>100||headsize<32){
                ptp_result.setErrMsg("返回包头不正确");
            }else{
                byte[] Head=new byte[headsize];
                byte[] headbody=this.read(is, headsize-4);
                System.arraycopy(headsize_byte, 0, Head, 0, 4);
                System.arraycopy(headbody, 0, Head, 4, headsize-4);
                YW_PACKET ywpacket_result=new YW_PACKET();
                ywpacket_result.BytesToYWPACKET(Head);
                if(ywpacket_result.getFlag()!=YW_PACKET.YW_FLAG1&&ywpacket_result.getFlag()!=YW_PACKET.YW_FLAG2){
                    ptp_result.setErrMsg("返回包头不正确");
                    os.close();
                    is.close();
                    socket.close();
                    return ptp_result;
                }
                if(ywpacket_result.getDataLen()>this.result_maxsize){
                    ptp_result.setErrMsg("返回结果集超出最大限制");
                    os.close();
                    is.close();
                    socket.close();
                    return ptp_result;
                }
                if(this.savepath==""){
                    byte[] body=this.read(is, (int)ywpacket_result.getDataLen());
                    ptp_result.setResult(new String(body,"GBK"));
                }else{
                    String errMsg=savepath( is ,this.savepath);
                    if(!errMsg.equals("")){
                        ptp_result.setErrMsg(errMsg);
                    }
                }
            }
            os.close();
            is.close();
            socket.close();
        } catch (Exception e) {
            ptp_result.setErrMsg(e.getMessage());
        } finally{
            if(os!=null){
                try {
                    os.close();
                } catch (IOException e) {
                    ptp_result.setErrMsg("OutputStream关闭失败："+e.getMessage());
                }
            }
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    ptp_result.setErrMsg("InputStream关闭失败："+e.getMessage());
                }
            }
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    ptp_result.setErrMsg("socket关闭失败："+e.getMessage());
                }
            }
        }
        return ptp_result;
    }

    public PTPResult SendCmd(String ip,String CMD_STR,int timeout){
        this.ip=ip;
        this.cmd_str=CMD_STR;
        this.timeout=timeout;
        this.savepath="";
        return this.SendCmd();
    }

    public PTPResult SendCmd(String ip,String CMD_STR,int timeout,String savepath){
        this.ip=ip;
        this.cmd_str=CMD_STR;
        this.timeout=timeout;
        this.savepath=savepath;
        return this.SendCmd();
    }
}
