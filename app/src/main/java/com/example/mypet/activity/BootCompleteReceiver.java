package com.example.mypet.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.mypet.activity.MainActivity;

public class BootCompleteReceiver extends BroadcastReceiver {
    private static final String TAG = "BootCompleteReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent it = new Intent(context, MainActivity.class);
        //Log.e(TAG,"get boot");
        Toast.makeText(context,"get boot",Toast.LENGTH_LONG).show();
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }
}
