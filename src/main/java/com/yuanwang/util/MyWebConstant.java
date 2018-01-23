package com.yuanwang.util;

import java.io.File;

public class MyWebConstant {
	public static final String PROPERTIES_FILE_NAME="conf.properties";
	public static final String MYSQLDRIVER = "com.mysql.jdbc.Driver";
	/**
	 * jar包目录
	 */
	public static final String START_CONTROLL_PATH=JarToolUtil.getJarDir()+ File.separator+"conf";
	public static final String START_CONTROLL_FILE="check";
	public static final String START_TASK_SHELL="start_jar.sh";
	public static final String STOP_TASK_SHELL="stop_jar.sh";

	// 定时任务
	public static final String HOUR="hour";
	public static final String MINUTE="minute";
	public static final String SECOND="second";



	public static final String URL="url";
	public static final String USERNAME="username";
	public static final String PASSWORD="password";
	public static final String IP="ip";
	public static final String PORT="port";
	public static final String DATANASENAME="databaeName";

	public static final String LOWUSERNAME="low_username";
	public static final String LOWPASSWORD="low_password";
	public static final String LOWIP="low_ip";
	public static final String LOWPORT="low_port";
	public static final String LOWDATANASENAME="low_databaeName";


	//数据库类型
	public static final String ORACLE="1";
	public static final String SQLSERVER="2";
	public static final String MYSQL="3";
	public static final String DBTYPE="type";
	public static final String LOWDBTYPE="low_type";
}
