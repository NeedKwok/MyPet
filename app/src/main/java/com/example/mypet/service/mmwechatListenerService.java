package com.example.mypet.service;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.util.List;

/**
 * Created by Ma5ker on 05/04/18.
 */
public class mmwechatListenerService extends AccessibilityService {
    public static boolean isRunning = false;//是否运行
    //微信包名
    //private String mmPge = "com.tencent.mm";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (!isRunning) {
            stopSelf();
        } else {
            //获取事件类型
            int eventType = event.getEventType();
            switch (eventType) {
                case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                    //判断事件的触发包名是不是微信，此处由于AndroidManifest.xml中写明了监听的com.tencent.mm，故不用
                    //if (event.getPackageName()==mmPge){
                        Log.e("检测到微信消息","转入消息处理");
                        handleNotification(event);
                    //}
            }
        }
    }

    //处理事件
    private void handleNotification(AccessibilityEvent event) {
        List<CharSequence> texts = event.getText();
        if (!texts.isEmpty()) {
            for (CharSequence text : texts) {
                String content = text.toString();
                Log.e("mmWechat", "微信新消息:  " + content);
            }
        }
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }
}
