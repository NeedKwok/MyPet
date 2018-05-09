package com.example.mypet.petpet;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.example.mypet.R;
import com.example.mypet.utils.Constants;
import com.example.mypet.utils.InfoPrefs;

import java.util.Random;

/**
 * Created by Ma5ker on 05/06/18.
 */

public class MyWindowManager {
    private static final String TAG = "MyWindowManager";
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
    //宠物信息
    private static int pet_theme;
    private static String pet_name;

    //消息显示
    //private static boolean msgShowFlag ;

    private static int screenWidth;
    private static int screenHeight;

//    public static boolean isMsgShow(){
//        return msgShowFlag;
//    }
    //创建一个悬浮窗。初始位置为屏幕的右部中间位置
    public static void createWindow(Context context) {
        //获取宠物信息
        InfoPrefs.init("pet_info");
        pet_name = InfoPrefs.getData("pet_name");
        pet_theme = InfoPrefs.getIntData("pet_theme");
        mWindowManager = getWindowManager(context);
        screenWidth = mWindowManager.getDefaultDisplay().getWidth();
        screenHeight = mWindowManager.getDefaultDisplay().getHeight();
        if (floatWindow == null) {
            floatWindow = new FloatWindowView(context);
            //设置初始主题
            floatWindow.setGifView(Constants.resId[pet_theme][0]);
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
            MyWindowManager.createMsgWindow(context, "我是你的宠物"+pet_name+".");
        }
    }

    public static void createMsgWindow(Context context, String msg) {
        //检查是否有宠物，没有不做任何操作
        if (floatWindow != null) {
            msgWindowView = new MsgWindowView(context);
            //设置消息
            msgWindowView.setMessage(msg);
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
            if (FloatWindowView.side == FloatWindowView.RIGHT) {
                msgWindowView.setBackground(R.drawable.msg_window_right);
            } else {
                msgWindowView.setBackground(R.drawable.msg_window_left);
            }
            int widthMs = View.MeasureSpec.makeMeasureSpec((1 << 30) - 1, View.MeasureSpec.AT_MOST);
            int heightMs = View.MeasureSpec.makeMeasureSpec((1 << 30) - 1, View.MeasureSpec.AT_MOST);
            msgWindowView.setLayoutParams(msgWindowParams);
            msgWindowView.measure(widthMs, heightMs);
            //获取填充消息后的view的长宽
            int mw = msgWindowView.getMeasuredWidth();
            int mh = msgWindowView.getMeasuredHeight();
            //设置消息框的位置
            if (FloatWindowView.side == FloatWindowView.RIGHT) {
                msgWindowParams.x = windowParams.x - FloatWindowView.mVieWidth - mw;
            } else {
                msgWindowParams.x = windowParams.x + FloatWindowView.mVieWidth;
            }
            msgWindowParams.y = windowParams.y - FloatWindowView.mViewHeight + mh;
            msgWindowView.setLayoutParams(msgWindowParams);
            mWindowManager.addView(msgWindowView, msgWindowParams);
            //msgShowFlag = true;
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
    public static void removeMsgWindow(Context context) {
        if (msgWindowView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(msgWindowView);
            msgWindowView = null;
            //msgShowFlag = false;
        }
    }


    //更新视图
    public static void updateView(Context context) {
        InfoPrefs.init("pet_info");
        if (floatWindow != null) {
            //获取主题数组
            pet_theme = InfoPrefs.getIntData("pet_theme");
            int[] resId = Constants.resId[pet_theme];
            int index = (int) (resId.length * Math.random());
            floatWindow.setGifView(resId[index]);
        }
    }


    //判断当前是否有悬浮窗
    public static boolean isWindowShowing() {
        return floatWindow != null;
    }

    public static boolean isMsgShowing() {
        return msgWindowView != null;
    }

    //创建WindowManager
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

}
