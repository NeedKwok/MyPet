package com.example.mypet.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.example.mypet.utils.Constants;
import com.example.mypet.utils.InfoPrefs;

/**
 * 用法 constructor.fun1.fun2.……
 */
public class AlertDialogUtil {
    private AlertDialog.Builder builder;
    private String[] items;
    private TextView textView;
    private String configName, infoItemName;
    //-1代表不要图片
    public AlertDialogUtil(Context context,int icon,String title){
        builder = new AlertDialog.Builder(context);
        if(icon != -1)
            builder.setIcon(icon);
        if(title != null)
            builder.setTitle(title);
    }
//如果不想用Items,自己再写一个别的，在show里面要写判断
    public AlertDialogUtil setItems(final int constant){
        switch(constant){
            case Constants.GENDER:
                items = Constants.GENDER_ITEMS;
                break;
            case Constants.PHOTO:
                items = Constants.PHOTO_ITEMS;
                break;
        }
        return this;
    }

    public AlertDialogUtil setTextView(TextView tv){
        textView = tv;
        return this;
    }
    public AlertDialogUtil setSettings(String configName,String infoItemName){
        this.configName = configName;
        this.infoItemName = infoItemName;
        return this;
    }

    public void showDialog(){
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(configName != null){
                    InfoPrefs.init(configName);
                    InfoPrefs.setData(infoItemName,items[which]);
                    if(textView != null)
                        textView.setText(InfoPrefs.getData(infoItemName));
                }
               // else if()

            }
        });
        builder.show();
    }
/*
    //    设置一个下拉的列表选择项
                builder.setItems(cities, new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            Toast.makeText(MainActivity.this, "选择的城市为：" + cities[which], Toast.LENGTH_SHORT).show();
        }
    });
                builder.show();*/
}
