package com.example.mypet.utils;

public class RingUtil {

    public static String formatTitle(String title){
        /*if(title.length()>=8){
            return title.substring(0,7)+"……";
        }*/
        return title;
    }

    public static String formatDuration(long duration){
        long second = duration/1000;
        String res ;
        if(second/60 >59){
            res = "大于1小时";
        }else if(second/60 > 0){
            if(second%60 > 0)
                res = second/60+"分" + second%60 + "秒";
            else
                res = second/60+"分";
        }else {
            if(second > 0)
                res = second + "秒";
            else
                res = "1秒";
        }
        return res;
    }


}

