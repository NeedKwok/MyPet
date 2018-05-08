package com.example.mypet.bean;

import org.litepal.crud.DataSupport;

public class AlarmClockItemInfo extends DataSupport{
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
    //铃声名
    private String musicName;
    //铃声存储位置
    private String musicUri;
    //铃声来自 0系统铃声 1本地音乐
    private int musicFrom;
    //标签
    private String label;

    public String getMusicUri() {
        return musicUri;
    }

    public void setMusicUri(String musicUri) {
        this.musicUri = musicUri;
    }

    public int getMusicFrom() {
        return musicFrom;
    }

    public void setMusicFrom(int musicFrom) {
        this.musicFrom = musicFrom;
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

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
