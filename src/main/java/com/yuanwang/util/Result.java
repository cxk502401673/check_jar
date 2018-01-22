package com.yuanwang.util;

import java.io.Serializable;

import lombok.Data;

/**
 * 所有ajax 调用接口返回公共类
 * 
 * @description：操作结果集
 */
@Data
public class Result implements Serializable {

	private static final long serialVersionUID = 5576237395711742681L;
	
	public static final int SUCCESS = 1;
    public static final int FAILURE = -1;

    private boolean success = false;

    private String msg = "";

    private Object obj = null;

}