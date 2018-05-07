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

    //参数
    private static WindowManager.LayoutParams windowParams;
//控制悬浮窗的显示
    private static WindowManager mWindowManager;


    private static int screenWidth;
    private static int screenHeight;
    //创建一个悬浮窗。初始位置为屏幕的右部中间位置
    public static void createWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        screenWidth = windowManager.getDefaultDisplay().getWidth();
        screenHeight = windowManager.getDefaultDisplay().getHeight();
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
            windowManager.addView(floatWindow, windowParams);
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
