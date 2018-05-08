package com.example.mypet.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;

import com.example.mypet.bean.AlarmClockItemInfo;
import com.example.mypet.utils.AudioPlayer;
import com.example.mypet.utils.TimeCalculationUtil;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ClockReceiver extends BroadcastReceiver {
    private static final String TAG = "ClockReceiver";
    private Context mContext;
    @Override
    public void onReceive(final Context context, Intent intent) {
        mContext = context;
        long curTime = System.currentTimeMillis();
        List<AlarmClockItemInfo> alarmClockItemInfoList= DataSupport.findAll(AlarmClockItemInfo.class);
        if(alarmClockItemInfoList == null || alarmClockItemInfoList.size() < 1){
            Log.d(TAG,"闹钟为空");
        }else{
            for(AlarmClockItemInfo itemInfo:alarmClockItemInfoList){
                Log.d(TAG,"curTime:"+new Date(curTime).toString());
                long nextClock = TimeCalculationUtil
                        .calculateNextTimeForStartClock(curTime,itemInfo.getHour(),
                                itemInfo.getMinute(),itemInfo.getRepeat());
                Log.d(TAG,"nextClock:"+new Date(nextClock).toString());
                //可能存在±1S的误差
                if(nextClock - curTime >= -1000&&nextClock - curTime <= 1000){
                    startClock(itemInfo.getRemind(), itemInfo.getMusicUri());

                    new ClockThread().run();
                    /*new Thread() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(5000);//休眠5秒
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            AudioPlayer.getInstance(context).stop();
                            Log.d(TAG,"闹钟关闭");
                        }
                    }.start();*/
                    //暂时不考虑重复闹钟
                    break;
                }
            }
        }
    }

    private class ClockThread extends Thread{
        @Override
        public void run() {
            try {
                Thread.sleep(30000);//响铃30秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            AudioPlayer.getInstance(mContext).stop();
            Log.d(TAG,"闹钟关闭");
        }
    }

    private void startClock(int remind,String uri) {
        switch(remind){
            case 0:/*仅响铃*/
                AudioPlayer.getInstance(mContext)
                        .play(uri,true,false);
                break;
            case 1:/*仅震动*/
                AudioPlayer.getInstance(mContext).vibrate();
                break;
            case 2:
                AudioPlayer.getInstance(mContext)
                        .play(uri,true,true);
                break;
            default:
        }
        Log.d(TAG,"闹钟响起");
    }
}
