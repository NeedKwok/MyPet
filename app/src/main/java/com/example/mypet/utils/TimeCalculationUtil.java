package com.example.mypet.utils;

import android.app.AlarmManager;
import android.util.Log;
import java.util.Calendar;

public class TimeCalculationUtil {
    private static final String TAG = "TimeCalculationUtil";

    public static long calculateNextTimeForStartClock(Long curTime,int hour, int minute, int repeat) {
        // 当前系统时间
        long now = curTime;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // 下次响铃时间
        long nextTime = calendar.getTimeInMillis();
        Log.e(TAG,"now:"+now);
        Log.e(TAG,"next:"+nextTime);
        // 当单次响铃时
        if (repeat == 0x0) {
            // 当设置时间大于系统时间时
            if (nextTime - now >= -1000) {
                return nextTime;
            } else {
                // 设置的时间加一天
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                nextTime = calendar.getTimeInMillis();
                return nextTime;
            }
        }
        else {
            nextTime = 0;
            // 临时比较用响铃时间
            long tempTime;
            int temp = 0x10000000;
            for(int i = 0 ;i<7; repeat=repeat<<4,i++){
                if(repeat/temp >= 1){
                    int week = (i+1)%7+1;
                    // 设置重复的周
                    calendar.set(Calendar.DAY_OF_WEEK, week);
                    tempTime = calendar.getTimeInMillis();
                    // 当设置时间小于等于当前系统时间时
                    if (tempTime - now < 1000) {
                        // 设置时间加7天
                        tempTime += AlarmManager.INTERVAL_DAY * 7;
                    }

                    if (nextTime == 0) {
                        nextTime = tempTime;
                    } else {
                        // 比较取得最小时间为下次响铃时间
                        nextTime = Math.min(tempTime, nextTime);
                    }
                }
            }
            return nextTime;
        }
    }

    /**
     * 计算下一次响铃时间,用于toast
     * 周日 是 1，周一 是2
     * @param hour
     * @param minute
     * @param repeat
     * @return
     */

    public static long calculateNextTime(int hour, int minute, int repeat) {
        // 当前系统时间
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        //Log.e("calculateNextTime",""+calendar.get(Calendar.DAY_OF_WEEK));
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        // 下次响铃时间
        long nextTime = calendar.getTimeInMillis();
        // 当单次响铃时
        if (repeat == 0x0) {
            // 当设置时间大于系统时间时
            if (nextTime > now) {
                return nextTime;
            } else {
                // 设置的时间加一天
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                nextTime = calendar.getTimeInMillis();
                return nextTime;
            }
        }
        else {
            nextTime = 0;
            // 临时比较用响铃时间
            long tempTime;
            int temp = 0x10000000;
            for(int i = 0 ;i<7; repeat=repeat<<4,i++){
                if(repeat/temp >= 1){
                    int week = (i+1)%7+1;
                    // 设置重复的周
                    calendar.set(Calendar.DAY_OF_WEEK, week);
                    tempTime = calendar.getTimeInMillis();
                    // 当设置时间小于等于当前系统时间时
                    if (tempTime <= now) {
                        // 设置时间加7天
                        tempTime += AlarmManager.INTERVAL_DAY * 7;
                    }

                    if (nextTime == 0) {
                        nextTime = tempTime;
                    } else {
                        // 比较取得最小时间为下次响铃时间
                        nextTime = Math.min(tempTime, nextTime);
                    }
                }
            }
            return nextTime;
        }
    }

    public static String nextClockToastText(long remainDay,long remainHour,long remainMinute) {
        String res = "闹钟在";
        if(remainDay > 0){
            res += remainDay + "天";
        }
        if(remainHour > 0){
            res += remainHour + "小时";
        }
        if(remainMinute > 0){
            res += remainMinute + "分钟";
        }
        return res + "后响起";
    }

}
