package com.example.mypet.dialog;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class  ItemsAlertDialogUtil  {
    private AlertDialog.Builder builder;
    private String[] items;
    private OnSelectFinishedListener mListener;

    public interface OnSelectFinishedListener{
        void SelectFinished( int which);
    }

    public ItemsAlertDialogUtil(Context context){
        this(context,-1,null);
    }
    public ItemsAlertDialogUtil(Context context, int icon){
        this(context,icon,null);
    }
    public ItemsAlertDialogUtil(Context context, String title){
        this(context,-1,title);
    }
    public ItemsAlertDialogUtil(Context context, int icon, String title){
        builder = new AlertDialog.Builder(context);
        if(icon != -1)
            builder.setIcon(icon);
        if(title != null)
            builder.setTitle(title);
    }

    public ItemsAlertDialogUtil setItems(String[] items) {
        this.items = items;
        return this;
    }
    public ItemsAlertDialogUtil setListener(OnSelectFinishedListener mListener){
        this.mListener = mListener;
        return this;
    }

    public void showDialog(){
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.SelectFinished(which);
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
