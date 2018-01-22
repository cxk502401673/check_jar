package com.yuanwang.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {
	/**
	 *  获取项目信息,通过配置文件方式,配置项目名称，项目版本号
	 * */
	public static String readProInfo(String path,String proKey,String fileName){
		String keyValue = "";
		Properties pro = new Properties();     
        try{
            InputStream in = new FileInputStream(new File(path+File.separator+fileName));
            pro.load(in);     ///加载属性列表
            keyValue = pro.getProperty(proKey);
        }
        catch(Exception e){
        }
        return keyValue;
	}
	

}
