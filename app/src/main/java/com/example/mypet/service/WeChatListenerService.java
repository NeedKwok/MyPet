package com.example.mypet.service;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.example.mypet.petpet.MyWindowManager;

import java.util.List;

/**
 * Created by Ma5ker on 05/04/18.
 */
public class WeChatListenerService extends AccessibilityService {
    private static final String TAG = "WeChatListenerService";
    public static boolean isRunning = false;//是否运行

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (!isRunning) {
            stopSelf();
        } else {
            //获取事件类型
            int eventType = event.getEventType();
            switch (eventType) {
                case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                    Log.e(TAG,"转入消息处理");
                    handleNotification(event);
            }
        }
    }

    //处理事件
    private void handleNotification(AccessibilityEvent event) {
        List<CharSequence> texts = event.getText();
        if (!texts.isEmpty()) {
            for (CharSequence text : texts) {
                String content = text.toString();
                //创建消息框
                MyWindowManager.createMsgWindow(getApplicationContext(),content);
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
