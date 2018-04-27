package com.example.mypet.utils;

import android.content.Context;
import org.litepal.LitePalApplication;

public class MyPetApplication extends LitePalApplication {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
