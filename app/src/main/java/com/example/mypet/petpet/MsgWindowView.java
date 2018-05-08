package com.example.mypet.petpet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mypet.R;

import java.lang.reflect.Field;

/**
 * Created by Ma5ker on 05/08/18.
 */
public class MsgWindowView extends RelativeLayout {
    private static final String TAG = "MsgWindowView";

    //消息文本框和整个的layout
    private TextView msg_text;
    private RelativeLayout msg_window;

    //消息缓冲区
    public static String textBuffer;
    //是否有消息的标志位
    public static boolean hasBuffer;

    //当前手指在屏幕上的横坐标值
    private float xInScreen;
    //当前手指在屏幕上的纵坐标值
    private float yInScreen;
    //记录手指按下时在屏幕上的横坐标值
    private float xDownInScreen;
    //记录手指按下时在屏幕上的纵坐标值
    private float yDownInScreen;
    //系统状态栏高度
    private int statusBarHeight;


    /**
     * view的宽
     */
    public static int viewWidth;

    /**
     * view的高
     */
    public static int viewHeight;

    //保存上下文
    private Context context;
    public MsgWindowView(Context context){
        super(context);
        this.context=context;
        View view= LayoutInflater.from(context).inflate(R.layout.msg_window,this);
        msg_text=view.findViewById(R.id.msg_textview);
        msg_window=view.findViewById(R.id.msg_window);
        statusBarHeight=getStatusBarHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                xDownInScreen=event.getRawX();
                yDownInScreen=event.getRawY()-statusBarHeight;
                xInScreen=xDownInScreen;
                yInScreen=yDownInScreen;
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen=event.getRawX();
                yInScreen=event.getRawY()-statusBarHeight;
                break;
            case MotionEvent.ACTION_UP:
                //单击时关闭PetMessageWindow
                if(xInScreen==xDownInScreen&&yInScreen==yDownInScreen){
                    //TODO do what you need to do
                }
                break;
            default:
                break;
        }
        return true;
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

    public void setMessage(String msg){
        msg_text.setText(msg);
        textBuffer=msg;
        hasBuffer=true;
    }

    //设置消息气泡的方向
    public void setBackground(int resId){
        msg_window.setBackgroundResource(resId);
    }

}
