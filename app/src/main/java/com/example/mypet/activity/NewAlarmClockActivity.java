package com.example.mypet.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.mypet.R;
import com.example.mypet.bean.AlarmClockItemInfo;
import com.example.mypet.bean.ChangeInfoBean;
import com.example.mypet.dialog.ItemsAlertDialogUtil;
import com.example.mypet.utils.Constants;
import com.example.mypet.utils.PickerUtil;

import java.util.Calendar;

public class NewAlarmClockActivity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {
    private static final int REQUEST_CHANGE_ALARM_CLOCK_LABEL = 10;
    TextView textView_alarm_clock_repeat;
    TextView textView_alarm_clock_label;
    TextView textView_alarm_clock_remind;
    TextView textView_alarm_clock_music;
    TextView textView_alarm_clock_snooze;
    AlarmClockItemInfo item;

    ToggleButton toggleButton_alarm_clock_repeat_monday;
    ToggleButton toggleButton_alarm_clock_repeat_tuesday;
    ToggleButton toggleButton_alarm_clock_repeat_wednesday;
    ToggleButton toggleButton_alarm_clock_repeat_thursday;
    ToggleButton toggleButton_alarm_clock_repeat_friday;
    ToggleButton toggleButton_alarm_clock_repeat_saturday;
    ToggleButton toggleButton_alarm_clock_repeat_sunday;
    ToggleButton[] toggleButtons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarmclock);

        initAll();

        Toolbar toolbar = findViewById(R.id.toolbar_new_alarm_clock);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null ){
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeAsUpIndicator();
        }
    }


    private void initAll() {
        item = new AlarmClockItemInfo();
        initTimePicker();
        initRepeat();
        initLabel();
        initRemind();
        initMusic();
        initSnooze();
    }
    private void initTimePicker() {
        TimePicker timePicker = findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        PickerUtil.deleteDivider(timePicker);

        Calendar calendar = Calendar.getInstance();
        int curHour = calendar.get(Calendar.HOUR_OF_DAY);
        int curMinute = calendar.get(Calendar.MINUTE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(curHour);
            timePicker.setMinute(curMinute);
        }else{
            timePicker.setCurrentHour(curHour);
            timePicker.setCurrentMinute(curMinute);
        }
        item.setHour(curHour);
        item.setMinute(curMinute);
        //InfoPrefs.init("alarm_clock_set");
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                item.setHour(hourOfDay);
                item.setMinute(minute);
                //InfoPrefs.setIntData("hour",hourOfDay);
                //InfoPrefs.setIntData("minute",minute);
            }
        });

    }
    private void initRepeat(){
        RelativeLayout relativeLayout_alarm_clock_repeat = findViewById(R.id.alarm_clock_repeat);
        toggleButtons = new ToggleButton[7];
        toggleButtons[0]=toggleButton_alarm_clock_repeat_monday = findViewById(R.id.monday_tb);
        toggleButtons[1]=toggleButton_alarm_clock_repeat_tuesday = findViewById(R.id.tuesday_tb);
        toggleButtons[2]=toggleButton_alarm_clock_repeat_wednesday = findViewById(R.id.wednesday_tb);
        toggleButtons[3]=toggleButton_alarm_clock_repeat_thursday = findViewById(R.id.thursday_tb);
        toggleButtons[4]=toggleButton_alarm_clock_repeat_friday = findViewById(R.id.friday_tb);
        toggleButtons[5]=toggleButton_alarm_clock_repeat_saturday = findViewById(R.id.saturday_tb);
        toggleButtons[6]=toggleButton_alarm_clock_repeat_sunday = findViewById(R.id.sunday_tb);

        relativeLayout_alarm_clock_repeat.setOnClickListener(this);
        toggleButton_alarm_clock_repeat_monday.setOnCheckedChangeListener(this);
        toggleButton_alarm_clock_repeat_tuesday.setOnCheckedChangeListener(this);
        toggleButton_alarm_clock_repeat_wednesday.setOnCheckedChangeListener(this);
        toggleButton_alarm_clock_repeat_thursday.setOnCheckedChangeListener(this);
        toggleButton_alarm_clock_repeat_friday.setOnCheckedChangeListener(this);
        toggleButton_alarm_clock_repeat_saturday.setOnCheckedChangeListener(this);
        toggleButton_alarm_clock_repeat_sunday.setOnCheckedChangeListener(this);
        //设置仅一次
        textView_alarm_clock_repeat = findViewById(R.id.alarm_clock_repeat_TV);
        textView_alarm_clock_repeat.setText(Constants.ONLY_ONCE);

        item.setRepeat(0x00000000);

    }
    private void initLabel(){
        RelativeLayout relativeLayout_alarm_clock_label = findViewById(R.id.alarm_clock_label);
        textView_alarm_clock_label = findViewById(R.id.alarm_clock_label_TV);
        item.setLabel(Constants.DEFAULT_LABEL);
        refreshLabel();
        relativeLayout_alarm_clock_label.setOnClickListener(this);
    }

    private void refreshLabel() {
        if(item.getLabel() == null)
            textView_alarm_clock_label.setText(Constants.DEFAULT_LABEL);
        else {//最多只显示8个字    在setText中不要写太多字符串操作
            String text;
            if(item.getLabel().length() > 8) {
                text = item.getLabel().substring(0, 8) + "……";
                textView_alarm_clock_label.setText(text);
            } else
                textView_alarm_clock_label.setText(item.getLabel());
            //Log.e("result","-"+item.getLabel().length());
        }
    }

    private void initRemind(){
        RelativeLayout relativeLayout_alarm_clock_remind = findViewById(R.id.alarm_clock_remind);
        textView_alarm_clock_remind = findViewById(R.id.alarm_clock_remind_TV);
        item.setRemind(Constants.CODE_RING);
        refreshRemind();
        relativeLayout_alarm_clock_remind.setOnClickListener(this);
    }

    private void refreshRemind() {
        switch(item.getRemind()){
            case Constants.CODE_RING:
                textView_alarm_clock_remind.setText(Constants.RING);
                break;
            case Constants.CODE_SHOCK:
                textView_alarm_clock_remind.setText(Constants.SHOCK);
                break;
            case Constants.CODE_RING_AND_SHOCK:
                textView_alarm_clock_remind.setText(Constants.RING_AND_SHOCK);
                break;
            default:
        }
    }

    private void initMusic(){

    }

    private void initSnooze() {
        RelativeLayout relativeLayout_alarm_clock_snooze = findViewById(R.id.alarm_clock_snooze);
        textView_alarm_clock_snooze = findViewById(R.id.alarm_clock_snooze_TV);
        item.setSnooze(Constants.CODE_NO_SNOOZE);
        refreshSnooze();
        relativeLayout_alarm_clock_snooze.setOnClickListener(this);
    }

    private void refreshSnooze() {
        switch (item.getSnooze()){
            case Constants.CODE_NO_SNOOZE:
                textView_alarm_clock_snooze.setText(Constants.NO_SNOOZE);
                break;
            case Constants.CODE_ONE_MINUTE:
                textView_alarm_clock_snooze.setText(Constants.ONE_MINUTE);
                break;
            case Constants.CODE_THREE_MINUTES:
                textView_alarm_clock_snooze.setText(Constants.THREE_MINUTES);
                break;
            case Constants.CODE_FIVE_MINUTES:
                textView_alarm_clock_snooze.setText(Constants.FIVE_MINUTES);
                break;
            case Constants.CODE_TEN_MINUTES:
                textView_alarm_clock_snooze.setText(Constants.TEN_MINUTES);
                break;
            case Constants.CODE_QUARTER_HOUR:
                textView_alarm_clock_snooze.setText(Constants.QUARTER_HOUR);
                break;
            case Constants.CODE_HALF_HOUR:
                textView_alarm_clock_snooze.setText(Constants.HALF_HOUR);
                break;
            default:
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.confirm:

                Toast.makeText(NewAlarmClockActivity.this,"you clicked confirm!",Toast.LENGTH_LONG).show();
                /**
                 * 做两件事 1是存数据litepal,2是toast显示响铃时间
                 */

                //Toast.makeText(ChangeInfoActivity.this,sp.getString(settings.getInfoName(),"nothing!"),Toast.LENGTH_LONG).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 点击事件
     *
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.alarm_clock_repeat:
                new ItemsAlertDialogUtil(NewAlarmClockActivity.this,"快捷选择").setItems(Constants.DAY_ITEMS)
                        .setListener(new ItemsAlertDialogUtil.OnSelectFinishedListener() {
                            @Override
                            public void SelectFinished(int which) {
                                switch(which){
                                    case Constants.CODE_WEEKDAY: //WEEKDAY
                                        toggleButtonChange(0,5);
                                        break;
                                    case Constants.CODE_WEEKEND: //WEEKEND
                                        toggleButtonChange(5,7);
                                        break;
                                    case Constants.CODE_EVERYDAY: //EVERYDAY
                                        toggleButtonChange(0,7);
                                        break;
                                    case Constants.CODE_ONLY_ONCE: //ONLY_ONCE
                                        toggleButtonChange(0,0);
                                        break;
                                }
                            }
                        }).showDialog();
                break;
            case R.id.alarm_clock_label:
                ChangeInfoBean bean = new ChangeInfoBean();
                bean.setTitle("修改标签");
                bean.setInfo(item.getLabel());
                Intent intent = new Intent(NewAlarmClockActivity.this,ChangeInfoActivity.class);
                intent.putExtra("data",bean);
                startActivityForResult(intent,REQUEST_CHANGE_ALARM_CLOCK_LABEL);
                break;
            case R.id.alarm_clock_remind:
                new ItemsAlertDialogUtil(NewAlarmClockActivity.this).setItems(Constants.REMINDING)
                        .setListener(new ItemsAlertDialogUtil.OnSelectFinishedListener() {
                            @Override
                            public void SelectFinished(int which) {
                                item.setRemind(which);
                                refreshRemind();
                            }
                        }).showDialog();
                break;
            case R.id.alarm_clock_snooze:
                new ItemsAlertDialogUtil(NewAlarmClockActivity.this,"小睡间隔").setItems(Constants.SNOOZE)
                        .setListener(new ItemsAlertDialogUtil.OnSelectFinishedListener() {
                            @Override
                            public void SelectFinished(int which) {
                                switch(which){
                                    case 0:
                                        item.setSnooze(Constants.CODE_NO_SNOOZE);
                                        break;
                                    case 1:
                                        item.setSnooze(Constants.CODE_ONE_MINUTE);
                                        break;
                                    case 2:
                                        item.setSnooze(Constants.CODE_THREE_MINUTES);
                                        break;
                                    case 3:
                                        item.setSnooze(Constants.CODE_FIVE_MINUTES);
                                        break;
                                    case 4:
                                        item.setSnooze(Constants.CODE_TEN_MINUTES);
                                        break;
                                    case 5:
                                        item.setSnooze(Constants.CODE_QUARTER_HOUR);
                                        break;
                                    case 6:
                                        item.setSnooze(Constants.CODE_HALF_HOUR);
                                        break;
                                   default:
                                }
                                refreshSnooze();
                            }
                        }).showDialog();
            default:
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch(requestCode){
                case REQUEST_CHANGE_ALARM_CLOCK_LABEL:
                    String returnData = data.getStringExtra("data_return");
                    item.setLabel(returnData);
                    refreshLabel();
                    break;
                default:
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        int repeat = item.getRepeat();
        int change = 0;
        switch(id){
            case R.id.monday_tb:
                change = 0x10000000;
                break;
            case R.id.tuesday_tb:
                change = 0x01000000;
                break;
            case R.id.wednesday_tb:
                change = 0x00100000;
                break;
            case R.id.thursday_tb:
                change = 0x00010000;
                break;
            case R.id.friday_tb:
                change = 0x00001000;
                break;
            case R.id.saturday_tb:
                change = 0x00000100;
                break;
            case R.id.sunday_tb:
                change = 0x00000010;
                break;
        }
        if(isChecked){
            item.setRepeat(repeat + change + 1);
        }else{
            item.setRepeat(repeat - change - 1);
        }
        changeRepeatText();
    }
    /**  弃用的方法
    private void changeRepeatSelect() {
        int repeat = item.getRepeat();
        if(repeat == 0x0){
            toggleButton_alarm_clock_repeat_monday.setChecked(false);
            toggleButton_alarm_clock_repeat_tuesday.setChecked(false);
            toggleButton_alarm_clock_repeat_wednesday.setChecked(false);
            toggleButton_alarm_clock_repeat_thursday.setChecked(false);
            toggleButton_alarm_clock_repeat_friday.setChecked(false);
            toggleButton_alarm_clock_repeat_saturday.setChecked(false);
            toggleButton_alarm_clock_repeat_sunday.setChecked(false);
        }
        else if(repeat == 0x11111117){
            toggleButton_alarm_clock_repeat_monday.setChecked(true);
            toggleButton_alarm_clock_repeat_tuesday.setChecked(true);
            toggleButton_alarm_clock_repeat_wednesday.setChecked(true);
            toggleButton_alarm_clock_repeat_thursday.setChecked(true);
            toggleButton_alarm_clock_repeat_friday.setChecked(true);
            toggleButton_alarm_clock_repeat_saturday.setChecked(true);
            toggleButton_alarm_clock_repeat_sunday.setChecked(true);
        }
        else if(repeat == 0x11111005){
            toggleButton_alarm_clock_repeat_monday.setChecked(true);
            toggleButton_alarm_clock_repeat_tuesday.setChecked(true);
            toggleButton_alarm_clock_repeat_wednesday.setChecked(true);
            toggleButton_alarm_clock_repeat_thursday.setChecked(true);
            toggleButton_alarm_clock_repeat_friday.setChecked(true);
            toggleButton_alarm_clock_repeat_saturday.setChecked(false);
            toggleButton_alarm_clock_repeat_sunday.setChecked(false);
        }
        else if(repeat == 0x00000112){
            toggleButton_alarm_clock_repeat_monday.setChecked(false);
            toggleButton_alarm_clock_repeat_tuesday.setChecked(false);
            toggleButton_alarm_clock_repeat_wednesday.setChecked(false);
            toggleButton_alarm_clock_repeat_thursday.setChecked(false);
            toggleButton_alarm_clock_repeat_friday.setChecked(false);
            toggleButton_alarm_clock_repeat_saturday.setChecked(true);
            toggleButton_alarm_clock_repeat_sunday.setChecked(true);
        }
    }*/

    private void changeRepeatText() {
        int repeat = item.getRepeat();
        //Log.e("result",""+repeat);
        if(repeat == 0x0)
            textView_alarm_clock_repeat.setText(Constants.ONLY_ONCE);
        else if(repeat == 0x11111117)
            textView_alarm_clock_repeat.setText(Constants.EVERYDAY);
        else if(repeat == 0x11111005)
            textView_alarm_clock_repeat.setText(Constants.WEEKDAY);
        else if(repeat == 0x00000112)
            textView_alarm_clock_repeat.setText(Constants.WEEKEND);
        else{
            String text = "";
            if((repeat & 0x10000000) > 0)
                text = text + "、一";
            if((repeat & 0x01000000) > 0)
                text = text + "、二";
            if((repeat & 0x00100000) > 0)
                text = text + "、三";
            if((repeat & 0x00010000) > 0)
                text = text + "、四";
            if((repeat & 0x00001000) > 0)
                text = text + "、五";
            if((repeat & 0x00000100) > 0)
                text = text + "、六";
            if((repeat & 0x00000010) > 0)
                text = text + "、日";
            text = text.replaceFirst("、","周");
            textView_alarm_clock_repeat.setText(text);
        }
    }
    // [start,end) = true ,else = false
    private void toggleButtonChange(int start,int end){
        if(toggleButtons == null||toggleButtons.length<1){
            return ;
        }
        for (int i = 0;i<toggleButtons.length;i++){
            if(i>=start && i<end){
                toggleButtons[i].setChecked(true);
            }else{
                toggleButtons[i].setChecked(false);
            }
        }
    }
}
