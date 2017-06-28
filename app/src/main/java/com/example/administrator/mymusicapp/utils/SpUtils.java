package com.example.administrator.mymusicapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences工具类
 * 调用该工具类可以很方便的存储
 * 1、Boolean
 * 2、String
 * 等类型的数据
 * Created by Jugeny on 2017/6/7.
 */

public class SpUtils {
    //配置文件的名字
    static String SP_NAME="config";

    /**
     * 使用sp存储一个布尔类型的数据
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context,String key,boolean value){
        SharedPreferences sp=context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor et=sp.edit();
        et.putBoolean(key,value);
        et.commit();
    }

    /**
     * 获取布尔类型的数据
     * @param context
     * @param key 取指定key的值
     * @return 取到的值，默认值为True
     */
    public static boolean getBoolean(Context context,String key){
        SharedPreferences sp=context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        boolean value=sp.getBoolean(key,true);
        return value;
    }
}
