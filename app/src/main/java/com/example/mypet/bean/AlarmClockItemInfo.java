package com.example.mypet.bean;

import org.litepal.crud.DataSupport;

public class AlarmClockItemInfo extends DataSupport{
    //设置时间
    private String curSetTime;
    //最近修改
    private String latestChangeTime;
    //时
    private int hour;
    //分
    private int minute;
    //星期 16进制8位 ，第1位为1是周一，以此类推，最后一位为共选中几天
    private int repeat;
    //贪睡间隔 单位分钟，0为不贪睡
    private int snooze;
    //提醒方式 0响铃 1震动 2 = 0 + 1
    private int remind;
    //铃声
    private String music;
    //标签
    private String label;

    public String getCurSetTime() {
        return curSetTime;
    }

    public void setCurSetTime(String curSetTime) {
        this.curSetTime = curSetTime;
    }

    public String getLatestChangeTime() {
        return latestChangeTime;
    }

    public void setLatestChangeTime(String latestChangeTime) {
        this.latestChangeTime = latestChangeTime;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public int getSnooze() {
        return snooze;
    }

    public void setSnooze(int snooze) {
        this.snooze = snooze;
    }

    public int getRemind() {
        return remind;
    }

    public void setRemind(int remind) {
        this.remind = remind;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
