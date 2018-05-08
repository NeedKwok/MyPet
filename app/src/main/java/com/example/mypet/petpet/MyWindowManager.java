package com.example.mypet.petpet;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.mypet.R;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Ma5ker on 05/06/18.
 */

public class MyWindowManager {
    //悬浮窗实例
    private static FloatWindowView floatWindow;

    //消息框
    private static MsgWindowView msgWindowView;

    //参数
    //悬浮窗
    private static WindowManager.LayoutParams windowParams;
    //消息框
    private static WindowManager.LayoutParams msgWindowParams;
    //控制悬浮窗的显示
    private static WindowManager mWindowManager;

    //消息显示
    private static boolean msgShowFlag;

    private static int screenWidth;
    private static int screenHeight;
    //创建一个悬浮窗。初始位置为屏幕的右部中间位置
    public static void createWindow(Context context) {
         mWindowManager = getWindowManager(context);
        screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        screenHeight = mWindowManager.getDefaultDisplay().getHeight();
        if (floatWindow == null) {
            floatWindow = new FloatWindowView(context);
            if (windowParams == null) {
                windowParams = new WindowManager.LayoutParams();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    windowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                } else {
                    windowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                }
                windowParams.format = PixelFormat.RGBA_8888;
                windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                windowParams.gravity = Gravity.START | Gravity.TOP;
                windowParams.width = FloatWindowView.viewWidth;
                windowParams.height = FloatWindowView.viewHeight;
                windowParams.x = screenWidth;
                windowParams.y = screenHeight / 2;
            }
            floatWindow.setParams(windowParams);
            mWindowManager.addView(floatWindow, windowParams);
        }
    }

    public static void createMsgWindow(Context context,String msg){
        //检查是否有宠物，没有不做任何操作
        if(floatWindow!=null){
            //设置参数
            msgWindowParams = new WindowManager.LayoutParams();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                msgWindowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                msgWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            }
            msgWindowParams.format = PixelFormat.RGBA_8888;
            msgWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            msgWindowParams.gravity = Gravity.START | Gravity.TOP;
            msgWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            msgWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            //设置消息气泡的朝向
            if(FloatWindowView.side==FloatWindowView.RIGHT){
                msgWindowView.setBackground(R.drawable.msg_window_right);
            }else{
                msgWindowView.setBackground(R.drawable.msg_window_left);
            }
            //设置消息
            msgWindowView.setMessage(msg);
            //设置消息框的位置
            //判断当前宠物位置
            //位于屏幕右侧
            if(FloatWindowView.side==FloatWindowView.RIGHT){
                msgWindowParams.x=windowParams.x-msgWindowParams.width;
                msgWindowParams.y=windowParams.y-msgWindowParams.height;
            }
            //位于屏幕左侧
            else{
                msgWindowParams.x=windowParams.x+windowParams.width;
                msgWindowParams.y=windowParams.y+windowParams.height;
            }
            msgWindowView.setLayoutParams(msgWindowParams);
            mWindowManager.addView(msgWindowView,msgWindowParams);
            msgShowFlag=true;
        }
    }


//    移除悬浮窗
    public static void removeWindow(Context context) {
        if (floatWindow != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(floatWindow);
            floatWindow = null;
        }
    }

    //移除消息窗
    public static void removeMsgWindow(Context context){
        if(msgWindowView!=null){
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(msgWindowView);
            msgWindowView=null;
            msgShowFlag=false;
        }
    }


//更新视图
    public static void updateView(Context context){
        if (floatWindow != null) {
            //更新操作
            //GifImageView testView = floatWindow.findViewById(R.id.test);
            //testView.setImageResource(R.drawable.xxpet);
        }
    }


//判断当前是否有悬浮窗
    public static boolean isWindowShowing() {
        return floatWindow != null;
    }

    //创建WindowManager
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

}
