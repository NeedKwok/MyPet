package com.example.mypet.utils;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.lang.reflect.Field;

import static com.example.mypet.R.color.colorPrimary;

/**
目前还需要修改冒号
 */


public class PickerUtil {
    private static final String TAG = "PickerUtil";

    @SuppressLint("ResourceAsColor")
    public static void deleteDivider(TimePicker timePicker){
        Resources systemResources = Resources.getSystem();
        int hourNumberPickerId = systemResources.getIdentifier("hour", "id", "android");
        int minuteNumberPickerId = systemResources.getIdentifier("minute", "id", "android");
        int dividerId = systemResources.getIdentifier("divider","id","android");
        NumberPicker hourNumberPicker = timePicker.findViewById(hourNumberPickerId);
        NumberPicker minuteNumberPicker = timePicker.findViewById(minuteNumberPickerId);
        TextView dividerTextView = timePicker.findViewById(dividerId);
        setNumberPickerDivider(hourNumberPicker);
        setNumberPickerDivider(minuteNumberPicker);
        if(dividerTextView != null){
            dividerTextView.setText(":");
            dividerTextView.setTextSize(20);
            dividerTextView.setTextColor(colorPrimary);
        }
    }

    private static void setNumberPickerDivider(NumberPicker numberPicker) {
        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            try{
                Field dividerField = numberPicker.getClass().getDeclaredField("mSelectionDivider");
                dividerField.setAccessible(true);
                ColorDrawable colorDrawable = new ColorDrawable(
                        ContextCompat.getColor(MyPetApplication.getContext(), android.R.color.transparent));
                dividerField.set(numberPicker,colorDrawable);
                numberPicker.invalidate();
            }
            catch(NoSuchFieldException | IllegalAccessException | IllegalArgumentException e){
                Log.e(TAG,e.toString());
            }
        }
    }
}
