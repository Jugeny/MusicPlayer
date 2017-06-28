package com.example.administrator.mymusicapp;

/**
 * Created by Jugeny on 2017/6/6.
 */

public class AppStringUtil {
    public static  boolean checkUserName(String userName){
        if (userName==null){
            return false;
        }
        if ("".equals(userName)){
            return false;
        }
        if (userName.length()<3||userName.length()>10){
            return false;
        }
        return true;
    }
    public static boolean checkPassword(String password) {
        if (password == null) {
            return false;
        }
        if ("".equals(password)) {
            return false;
        }

        if (password.length() < 3 || password.length() > 10) {
            return false;
        }
        return true;
    }
}
