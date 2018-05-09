package com.example.mypet.activity;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
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
import com.example.mypet.utils.FormatUtil;
import com.example.mypet.utils.PickerUtil;
import com.example.mypet.utils.RingUtil;
import com.example.mypet.utils.TimeCalculationUtil;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.Calendar;


import static android.media.RingtoneManager.TYPE_ALARM;

public class EditAlarmClockActivity extends AppCompatActivity implements
        View.OnClickListener,CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "EditAlarmClockActivity";

    private static final int REQUEST_CHANGE_ALARM_CLOCK_LABEL = 10;
    private static final int REQUEST_CHANGE_ALARM_CLOCK_RING = 11;
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

    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm_clock);

        initAll();

        Toolbar toolbar = findViewById(R.id.toolbar_edit_alarm_clock);
        toolbar.setTitle("编辑闹钟");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null ){
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeAsUpIndicator();
        }
    }


    private void initAll() {
        Intent intent = getIntent();
        position = intent.getIntExtra("position",0);
        item = DataSupport.limit(1).offset(position).find(AlarmClockItemInfo.class).get(0);
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

        int curHour = item.getHour();
        int curMinute = item.getMinute();
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

        int repeat = item.getRepeat();
        for(int i=0;i<7;i++){
            if(repeat>0x10000000){
                toggleButtons[i].setChecked(true);
            }
            repeat = repeat<<4;
        }

        relativeLayout_alarm_clock_repeat.setOnClickListener(this);
        toggleButton_alarm_clock_repeat_monday.setOnCheckedChangeListener(this);
        toggleButton_alarm_clock_repeat_tuesday.setOnCheckedChangeListener(this);
        toggleButton_alarm_clock_repeat_wednesday.setOnCheckedChangeListener(this);
        toggleButton_alarm_clock_repeat_thursday.setOnCheckedChangeListener(this);
        toggleButton_alarm_clock_repeat_friday.setOnCheckedChangeListener(this);
        toggleButton_alarm_clock_repeat_saturday.setOnCheckedChangeListener(this);
        toggleButton_alarm_clock_repeat_sunday.setOnCheckedChangeListener(this);

        textView_alarm_clock_repeat = findViewById(R.id.alarm_clock_repeat_TV);

        changeRepeatText();
    }

    private void initLabel(){
        RelativeLayout relativeLayout_alarm_clock_label = findViewById(R.id.alarm_clock_label);
        textView_alarm_clock_label = findViewById(R.id.alarm_clock_label_TV);
        refreshLabel();
        relativeLayout_alarm_clock_label.setOnClickListener(this);
    }

    private void refreshLabel() {
        if(item.getLabel() == null)
            textView_alarm_clock_label.setText(Constants.DEFAULT_LABEL);
        else {//最多只显示8个字    在setText中不要写太多字符串操作
            textView_alarm_clock_label.setText(FormatUtil.lessThan8Words(item.getLabel()));
        }
    }

    private void initRemind(){
        RelativeLayout relativeLayout_alarm_clock_remind = findViewById(R.id.alarm_clock_remind);
        textView_alarm_clock_remind = findViewById(R.id.alarm_clock_remind_TV);
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
       /* InfoPrefs.init("settings");
        if(InfoPrefs.getIntData("first_use_alarm_clock") == 0){
            //第一次使用闹钟

        }*/
        RelativeLayout relativeLayout_alarm_clock_music = findViewById(R.id.alarm_clock_music);
        textView_alarm_clock_music = findViewById(R.id.alarm_clock_music_TV);
        refreshMusic();
        relativeLayout_alarm_clock_music.setOnClickListener(this);

    }

    private void refreshMusic() {
        String music = item.getMusicName();
        textView_alarm_clock_music.setText(RingUtil.formatTitle(music));
    }

    private void initSnooze() {
        RelativeLayout relativeLayout_alarm_clock_snooze = findViewById(R.id.alarm_clock_snooze);
        textView_alarm_clock_snooze = findViewById(R.id.alarm_clock_snooze_TV);
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
    public boolean onOptionsItemSelected(MenuItem item1) {
        switch(item1.getItemId()){
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.confirm:
                item.save();
                String nextClock = timeCalculation();
                Toast.makeText(EditAlarmClockActivity.this,nextClock,Toast.LENGTH_LONG).show();
                setResult(RESULT_OK);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item1);
    }

    private String  timeCalculation() {
        long now = System.currentTimeMillis();
        long nextClock = TimeCalculationUtil.calculateNextTime(item.getHour(),item.getMinute(),item.getRepeat());

        // 距离下次响铃间隔毫秒数
        long ms = nextClock - now;

        // 单位秒
        int ss = 1000;
        // 单位分
        int mm = ss * 60;
        // 单位小时
        int hh = mm * 60;
        // 单位天
        int dd = hh * 24;
        // 不计算秒，故响铃间隔加一分钟
        ms += mm;
        // 剩余天数
        long remainDay = ms / dd;
        // 剩余小时
        long remainHour = (ms - remainDay * dd) / hh;
        // 剩余分钟
        long remainMinute = (ms - remainDay * dd - remainHour * hh) / mm;

        return TimeCalculationUtil.nextClockToastText(remainDay,remainHour,remainMinute);
    }


    /**
     * 点击事件
     *
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.alarm_clock_repeat:
                new ItemsAlertDialogUtil(EditAlarmClockActivity.this,"快捷选择").setItems(Constants.DAY_ITEMS)
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
                Intent intent = new Intent(EditAlarmClockActivity.this,ChangeInfoActivity.class);
                intent.putExtra("data",bean);
                startActivityForResult(intent,REQUEST_CHANGE_ALARM_CLOCK_LABEL);
                break;

            case R.id.alarm_clock_remind:
                new ItemsAlertDialogUtil(EditAlarmClockActivity.this).setItems(Constants.REMINDING)
                        .setListener(new ItemsAlertDialogUtil.OnSelectFinishedListener() {
                            @Override
                            public void SelectFinished(int which) {
                                item.setRemind(which);
                                refreshRemind();
                            }
                        }).showDialog();
                break;

            case R.id.alarm_clock_music:
                String title = item.getMusicName();
                String uri = item.getMusicUri();
                int from = item.getMusicFrom();
                Intent intent1 = new Intent(EditAlarmClockActivity.this,SystemRingSelectActivity.class);
                intent1.putExtra("ring_title",title);
                intent1.putExtra("ring_uri",uri);
                intent1.putExtra("ring_from",from);
                startActivityForResult(intent1,REQUEST_CHANGE_ALARM_CLOCK_RING);
                break;

            case R.id.alarm_clock_snooze:
                new ItemsAlertDialogUtil(EditAlarmClockActivity.this,"小睡间隔").setItems(Constants.SNOOZE)
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
                case REQUEST_CHANGE_ALARM_CLOCK_RING:
                    item.setMusicUri(data.getStringExtra("ring_uri"));
                    item.setMusicName(data.getStringExtra("ring_title"));
                    item.setMusicFrom(data.getIntExtra("ring_from",0));
                    refreshMusic();
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

    private void changeRepeatText() {
        String text = FormatUtil.getRepeatText(item.getRepeat());
        textView_alarm_clock_repeat.setText(text);
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
