package com.example.administrator.mymusicapp.utils;

import okhttp3.Request;

/**
 * Created by Jugeny on 2017/6/9.
 */

public class HttpUtils {
    public static Request.Builder getBuilder()
    {
        Request.Builder builder=new Request.Builder()
                .addHeader("X-LC-Id", "kCFRDdr9tqej8FRLoqopkuXl-gzGzoHsz")
                .addHeader("X-LC-Key", "bmEeEjcgvKIq0FRaPl8jV2Um")
                .addHeader("Content-Type", "application/json");
        return builder;
    }

    /**
     * 获取一个Get请求
     * @param url
     * @return
     */
    public static Request requestGET(String url){
        Request request=getBuilder().url(url).get().build();
        return request;
    }
}
