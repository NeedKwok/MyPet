package com.example.mypet.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.mypet.bean.AlarmClockItemInfo;
import com.example.mypet.petpet.MyWindowManager;
import com.example.mypet.service.MsgWindowService;
import com.example.mypet.service.PetWindowService;
import com.example.mypet.utils.AudioPlayer;
import com.example.mypet.utils.InfoPrefs;
import com.example.mypet.utils.TimeCalculationUtil;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.List;

import static com.example.mypet.utils.Constants.FALSE;


public class ClockReceiver extends BroadcastReceiver {
    private static final String TAG = "ClockReceiver";
    private Context mContext;
    private static String label;
    @Override
    public void onReceive(final Context context, Intent intent) {
        mContext = context;
        long curTime = System.currentTimeMillis();
        List<AlarmClockItemInfo> alarmClockItemInfoList= DataSupport.findAll(AlarmClockItemInfo.class);

        if(alarmClockItemInfoList == null || alarmClockItemInfoList.size() < 1){
            Log.d(TAG,"闹钟为空");
        }else{
            for(AlarmClockItemInfo itemInfo:alarmClockItemInfoList){
                if(itemInfo.getIsEnable() == 0){
                    continue;
                }
                Log.d(TAG,"curTime:"+new Date(curTime).toString());
                long nextClock = TimeCalculationUtil
                        .calculateNextTimeForStartClock(curTime,itemInfo.getHour(),
                                itemInfo.getMinute(),itemInfo.getRepeat());
                Log.d(TAG,"nextClock:"+new Date(nextClock).toString());
                //可能存在±1S的误差
                if(nextClock - curTime >= -1000&&nextClock - curTime <= 1000){
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    }).start();
                    startClock(itemInfo.getRemind(), itemInfo.getMusicUri());
                    label = itemInfo.getLabel();
                    new ClockThread().start();
                    if(itemInfo.getRepeat() == 0x0){
                        itemInfo.setIsEnable(FALSE);
                        itemInfo.save();
                    }
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
            InfoPrefs.init("pet_info");
            Intent intent = null;
            if(InfoPrefs.getIntData("pet_isopen") == 0) {
                InfoPrefs.setIntData("pet_isopen", 1);
                Intent serviceIntent = new Intent(mContext, PetWindowService.class);
                serviceIntent.putExtra("type",1);
                serviceIntent.putExtra("label",label);
                mContext.startService(serviceIntent);
            }else{
                 intent = new Intent(mContext, MsgWindowService.class);
                intent.putExtra("label",label);
                mContext.startService(intent);
            }


            //MyWindowManager.createMsgWindow(mContext,"闹钟标签："+label);
            try {
                Thread.sleep(5000);//如果不点击则会响铃30S
                for(int i=0;i<30;i++){
                    Thread.sleep(1000);//如果不点击则会响铃30S
                    if(!MyWindowManager.isMsgShowing()){
                        Log.d(TAG,i+"闹钟关闭");
                        AudioPlayer.getInstance(mContext).stop();
                        break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            AudioPlayer.getInstance(mContext).stop();
            if(intent != null)
                mContext.stopService(intent);
            Log.d(TAG,"final---闹钟关闭");
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
