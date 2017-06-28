package com.example.administrator.mymusicapp.utils;

import android.content.Context;

/**
 * Created by Jugeny on 2017/6/7.
 */

public class AppConfigUtils {
    /**这个App在运行过程中，应该只存在一个AppConfigUtils对象
     * app配置信息的类
     * 调用该类的方法可以方便获取到
     * 1、是否第一次使用应用
     * 2、是否登录
     */
    static String GUIDE="guide";
    private AppConfigUtils(){}
    private static AppConfigUtils mConfigUtils;
    public static final AppConfigUtils getInstance(){
        if (mConfigUtils==null){
            mConfigUtils=new AppConfigUtils();
        }
        return mConfigUtils;
    }

    /**
     * 获取是否第一次使用
     * @param context 上下文
     * @return 是否第一次使用，true是    false否
     */
    public boolean getGuide(Context context){
        return SpUtils.getBoolean(context,GUIDE);
    }

    /**
     * 设置是否第一次使用
     * @param context 上下文
     * @param guide  是否第一次使用
     */
    public void setGUIDE(Context context,boolean guide){
        SpUtils.putBoolean(context,GUIDE,guide);
    }

}
