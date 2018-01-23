package com.yuanwang.util;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellUtils {
    private static Logger logger  =  Logger.getLogger(ShellUtils. class );



    /**
     *
     * @param command shell脚本路径
      */
    public static String exec(String command){
        InputStreamReader stdISR = null;
        InputStreamReader errISR = null;
        Process process = null;
        String r="";
        try {
            process = Runtime.getRuntime().exec(command);
            int exitValue = process.waitFor();

            String line = null;

            stdISR = new InputStreamReader(process.getInputStream());
            BufferedReader stdBR = new BufferedReader(stdISR);
            while ((line = stdBR.readLine()) != null) {
                System.out.println("STD line:" + line);
                r+=line;
            }

            errISR = new InputStreamReader(process.getErrorStream());
            BufferedReader errBR = new BufferedReader(errISR);
            while ((line = errBR.readLine()) != null) {
                System.out.println("ERR line:" + line);
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (stdISR != null) {
                    stdISR.close();
                }
                if (errISR != null) {
                    errISR.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (IOException e) {
                System.out.println("正式执行命令：" + command + "有IO异常");
            }
        }
    return r;
    }


}
