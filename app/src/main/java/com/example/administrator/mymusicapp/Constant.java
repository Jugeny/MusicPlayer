package com.example.administrator.mymusicapp;

/**
 * Created by Jugeny on 2017/6/9.
 */

public class Constant {
    /**
     * 存储app所有的接口地址
     */
    public static class URL {
        //首页数据接口
        public  final static String HOME = "https://leancloud.cn:443/1.1/classes/Home";
        //大小写转换  Ctrl + shift + u
        public final static String NEWPLAYLIST = "https://leancloud.cn:443/1.1/classes/NewPlayList";

        public final static String PLAYLIST="https://leancloud.cn:443/1.1/classes/PlayList";

    }
    public static class Action{
        public final static String ACTION_PLAY="com.example.administrator.mymusicapp.action_play";
        //通知 开始播放音乐的通知，在MusicService服务里面，如果条用了play方法就会发送该播放
        public final static String PLAY="com.example.administrator.mymusicapp.play";
        //通知—暂停播放音乐的通知
        public final static String PAUSE="com.example.administrator.mymusicapp.pause";
    }
}
