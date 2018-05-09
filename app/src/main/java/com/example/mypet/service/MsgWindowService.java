package com.example.mypet.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.mypet.petpet.MyWindowManager;

public class MsgWindowService extends Service {
    public MsgWindowService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String label = intent.getStringExtra("label");
        MyWindowManager.createMsgWindow(getApplicationContext(),"闹钟标签："+label);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyWindowManager.removeWindow(getApplicationContext());
    }
}
