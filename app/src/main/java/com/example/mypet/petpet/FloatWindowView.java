package com.example.mypet.petpet;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.mypet.R;

import java.lang.reflect.Field;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Ma5ker on 05/06/18.
 */
public class FloatWindowView extends LinearLayout {
    private static final String TAG = "FloatWindowView";

    public static int side=1;
    public static final int LEFT=0;
    public static final int RIGHT=1;

    //宽度
    public static int viewWidth;
    //高度
    public static int viewHeight;
    // 系统状态栏的高度
    private static int statusBarHeight;
    //用于更新悬浮窗的位置
    private WindowManager windowManager;
    //悬浮窗的参数
    public WindowManager.LayoutParams mParams;
    //记录当前手指位置在屏幕上的横坐标值
    private float xInScreen;
    //记录当前手指位置在屏幕上的纵坐标值
    private float yInScreen;
    //记录手指按下时屏幕上的横坐标的值
    private float xDownInScreen;
    //记录手指按下时屏幕上的纵坐标的值
    private float yDownInScreen;
    //记录手指按下时悬浮窗的View上的横坐标的值
    private float xInView;
    //记录手指按下时悬浮窗的View上的纵坐标的值
    private float yInView;
    /**
     *屏幕宽
     */
    private int screenWidth;
    /**
     * 屏幕高
     */
    private int screenHeight;

    /**
     * View的宽
     */
    public static int mVieWidth;
    /**
     * View的高
     */
    public static int mViewHeight;


    public FloatWindowView(Context context){
        super(context);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_window, this);
        View view = findViewById(R.id.float_window_layout);
        View baseView=view.findViewById(R.id.float_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        GifImageView testView = findViewById(R.id.test);
        testView.setImageResource(R.drawable.xxpet);
        int[] size=getScreenSize(context);
        screenHeight=size[1];
        screenWidth=size[0];
        Log.e("屏幕的size","宽度："+screenWidth+"高度："+screenHeight);
        mViewHeight = baseView.getLayoutParams().width;
        mVieWidth = baseView.getLayoutParams().height;
        Log.e("View size","宽度："+mVieWidth+"高度："+mViewHeight);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                xInView = event.getX();
                yInView = event.getY();
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY() - getStatusBarHeight();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                break;
            // 手指移动的时候更新小悬浮窗的位置
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                updateViewPosition();
                break;
            // 如果手指离开时xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，则视为触发了单击事件。
            case MotionEvent.ACTION_UP:
                if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {
                    //点击事件
                }
                //判断位于界面的位置
                toTheSide();
                break;
            default:
                break;
        }
        return true;
    }


    //传参
    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }


    //更新悬浮窗在屏幕中的位置
    private void updateViewPosition() {
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);
        windowManager.updateViewLayout(this, mParams);
    }

    //获取状态栏的高度
    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }

    private void toTheSide(){
        if(xInScreen<screenWidth/2){
            //mParams.x=0;
            side=FloatWindowView.LEFT;
            Log.e("FloatWindowView","Side: left");
        }
        else {
            //mParams.x=screenWidth;
            side=FloatWindowView.RIGHT;
            Log.e("FloatWindowView","Side: right");
        }

        //windowManager.updateViewLayout(this,mParams);
    }

    public static int[] getScreenSize(Context context){
        int[] size=new int[2];
        WindowManager windowManager=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        size[0]=windowManager.getDefaultDisplay().getWidth();
        size[1]=windowManager.getDefaultDisplay().getHeight();
        return size;
    }

}
