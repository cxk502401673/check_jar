package com.yuanwang.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config extends Properties {
    public static Config config = null;

    public Config() {




        try {
            InputStream in = new FileInputStream(new File(MyWebConstant.START_CONTROLL_PATH + File.separator + MyWebConstant.PROPERTIES_FILE_NAME));
            this.load(in);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static Config getInstance() {
        if (config != null) {
            return config;
        } else {
            newInstance();
            return config;
        }


    }

    private static void newInstance() {
        config = new Config();
    }


    public static String getString(String key,String v) {
        String value=Config.getInstance().getProperty(key);
        return value==null?v:value;
    }
}
