package com.yuanwang.bean;

import lombok.Data;


public class YW_PACKET {
    private long HeadSize;	//数据包头大小
    private long Flag;		//标记
    private long Ver;		//版本
    private long DataLen;	//数据大小
    private long CmdId;
    private int  OnlyId;
    private long PwdCheck;
    private long Resv;
    public static long YW_FLAG1=0x87654321;
    public static long YW_FLAG2=0x11121314;
    public YW_PACKET(){
        this.setHeadSize(32);
        this.setFlag(YW_FLAG2);
        this.setVer(1);
        this.setOnlyId(1);
        this.setPwdCheck(0);
        this.setCmdId(15);
        this.setResv(0);
    }
    public YW_PACKET(long cmdid,long datalen){
        this.setHeadSize(32);
        this.setFlag(YW_FLAG2);
        this.setVer(1);
        this.setOnlyId(1);
        this.setPwdCheck(0);
        this.setResv(0);
        this.setCmdId(cmdid);
        this.setDataLen(datalen);
    }
    public long getHeadSize() {
        return HeadSize;
    }

    public void setHeadSize(long headSize) {
        HeadSize = headSize & 0x00000000ffffffff;
    }

    public long getFlag() {
        return Flag;
    }

    public void setFlag(long flag) {
        Flag = flag & 0x00000000ffffffff;
    }

    public long getVer() {
        return Ver;
    }

    public void setVer(long ver) {
        Ver = ver & 0x00000000ffffffff;
    }

    public long getDataLen() {
        return DataLen;
    }

    public void setDataLen(long dataLen) {
        DataLen = dataLen & 0x00000000ffffffff;
    }

    public long getCmdId() {
        return CmdId;
    }

    public void setCmdId(long cmdId) {
        CmdId = cmdId & 0x00000000ffffffff;
    }

    public int getOnlyId() {
        return OnlyId;
    }

    public void setOnlyId(int onlyId) {
        OnlyId = onlyId & 0x0000ffff;
    }

    public long getPwdCheck() {
        return PwdCheck;
    }

    public void setPwdCheck(long pwdCheck) {
        PwdCheck = pwdCheck & 0x00000000ffffffff;
    }

    public long getResv() {
        return Resv;
    }

    public void setResv(long resv) {
        Resv = resv & 0x00000000ffffffff;
    }

    private byte[] longToBytes(long l){
        byte[] b=new byte[4];
        b[3]=(byte)(l>>>24);
        b[2]=(byte)(l>>>16);
        b[1]=(byte)(l>>>8);
        b[0]=(byte)(l);
        return b;
    }

    private byte[] intToBytes(int i){
        byte[] b=new byte[4];
        b[3]=(byte)(i>>>24);
        b[2]=(byte)(i>>>16);
        b[1]=(byte)(i>>>8);
        b[0]=(byte)(i);
        return b;
    }

    public byte[] ToByres(){
        byte[] b=new byte[32];
        System.arraycopy(this.longToBytes(this.HeadSize), 0, b, 0, 4);
        System.arraycopy(this.longToBytes(this.Flag), 0, b, 4, 4);
        System.arraycopy(this.longToBytes(this.Ver), 0, b, 8, 4);
        System.arraycopy(this.longToBytes(this.DataLen), 0, b, 12, 4);
        System.arraycopy(this.longToBytes(this.CmdId), 0, b, 16, 4);
        System.arraycopy(this.intToBytes(this.OnlyId), 0, b, 20, 4);
        System.arraycopy(this.longToBytes(this.PwdCheck), 0, b, 24, 4);
        System.arraycopy(this.longToBytes(this.Resv), 0, b, 28, 4);
        return b;
    }

    private long byteToLong(byte b){
        return b& 0xFF;
    }

    private int byteToInt(byte b){
        return b& 0xFF;
    }

    public int bytesToInt(byte b0,byte b1 ,byte b2,byte b3){
        return this.byteToInt(b0)+(this.byteToInt(b1)<<8)+(this.byteToInt(b2)<<16)+(this.byteToInt(b3)<<24);
    }
    public long bytesToLong(byte b0,byte b1 ,byte b2,byte b3){
        return this.byteToLong(b0)+(this.byteToLong(b1)<<8)+(this.byteToLong(b2)<<16)+(this.byteToLong(b3)<<24);
    }

    public void BytesToYWPACKET(byte[] b){
        this.setHeadSize(this.bytesToLong(b[0], b[1], b[2], b[3]));
        this.setFlag((int)this.bytesToLong(b[4], b[5], b[6], b[7]));
        this.setVer(this.bytesToLong(b[8], b[9], b[10], b[11]));
        this.setDataLen(this.bytesToLong(b[12], b[13], b[14], b[15]));
        this.setCmdId(this.bytesToLong(b[16], b[17], b[18], b[19]));
        this.setOnlyId(this.bytesToInt(b[20], b[21], b[22], b[23]));
        this.setPwdCheck(this.bytesToLong(b[24], b[25], b[26], b[27]));
        this.setResv(this.bytesToLong(b[28], b[29], b[30], b[31]));
    }
}
