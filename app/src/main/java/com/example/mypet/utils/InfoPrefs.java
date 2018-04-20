package com.example.mypet.utils;

import android.content.SharedPreferences;

/**
 * 本地用户信息存储类
 *
 * @author 汪俊 是个大坏蛋
 * @version 1.0
 * @date 2016-10-23
 * edit by yc 添加stuID和用户身份常量
 */
public class InfoPrefs {

    private static SPUtil sp;

    public static void init(String configName){
        if(sp == null||!sp.getConfigName().equals(configName)){
            sp = new SPUtil(configName);
        }
    }

    public static void setData(String key,String value){
        sp.setString(key,value);
    }
    public static String getData(String key){
        return sp.getString(key,"");
    }

}
