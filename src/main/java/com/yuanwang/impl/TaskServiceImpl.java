package com.yuanwang.impl;

import com.yuanwang.service.TaskService;
import com.yuanwang.util.JarToolUtil;
import com.yuanwang.util.MyWebConstant;
import com.yuanwang.util.ShellUtils;

import java.io.File;

public class TaskServiceImpl implements TaskService {
    @Override
    public void stop() {
        ShellUtils.exec(JarToolUtil.getJarDir()+ File.separator+ MyWebConstant.STOP_TASK_SHELL);
    }

    @Override
    public void start() {
        ShellUtils.exec(JarToolUtil.getJarDir()+ File.separator+ MyWebConstant.START_TASK_SHELL);
    }
}
