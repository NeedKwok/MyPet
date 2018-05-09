package com.example.mypet.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.mypet.petpet.MyWindowManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ma5ker on 05/06/18.
 */
public class PetWindowService extends Service {
    private static final String TAG = "PetWindowService";
    //用于在线程中创建或移除悬浮窗
    private Handler handler = new Handler();
    //定时器，定时进行检测当前应该创建还是移除悬浮窗。
    private Timer timer;

    private int type;
    private String label;

    @Nullable
    @Override

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        type = intent.getIntExtra("type",0);
        label = intent.getStringExtra("label");
        if(label == null){
            label = "闹钟";
        }
        // 开启定时器，每隔5秒刷新一次
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), 0, 5000);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    class RefreshTask extends TimerTask {
        @Override
        public void run() {
            Log.e(TAG, "PetWindowService RefreshTask run");
            //boolean flag1 = isHome();
            boolean flag = MyWindowManager.isWindowShowing();
            // 当前界面没有悬浮窗显示，则创建悬浮窗。
            if (!flag) {
                Log.e(TAG, "Now no floatwindow");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyWindowManager.createWindow(getApplicationContext(),type,label);
                    }
                });
            }
            // 当前界面不是桌面，且有悬浮窗显示，则移除悬浮窗。
//            else if (!flag1 && flag2) {
//                Log.e("PetWindowService", "Now is not home and have floatwindow");
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        MyWindowManager.removeWindow(getApplicationContext());
//                        if (MyWindowManager.msgShowFlag) {
//                            MyWindowManager.removeMsgWindow(getApplicationContext());
//                        }
//                    }
//                });
//            }
            //有悬浮窗，则执行更新操作。
            else{
                Log.e(TAG, "Now have floatwindow,update it");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MyWindowManager.updateView(getApplicationContext());
                    }
                });
            }
        }
    }

//    /**
//     * 判断当前界面是否是桌面
//     */
//    private boolean isHome() {
//        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
//        return getHomes().contains(rti.get(0).topActivity.getPackageName());
//    }

    //获得属于桌面的应用的应用包名，返回包含所有包名的字符串列表
//    private List<String> getHomes() {
//        List<String> names = new ArrayList<String>();
//        PackageManager packageManager = this.getPackageManager();
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
//                PackageManager.MATCH_DEFAULT_ONLY);
//        for (ResolveInfo ri : resolveInfo) {
//            names.add(ri.activityInfo.packageName);
//        }
//        return names;
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Service", "Service destroyed");
        // Service被终止的同时也停止定时器继续运行
        timer.cancel();
        if(MyWindowManager.isWindowShowing()){
            MyWindowManager.removeWindow(getApplicationContext());
        }
        if(MyWindowManager.isMsgShowing()){
            MyWindowManager.removeMsgWindow(getApplicationContext());
        }
        timer = null;
    }

}
