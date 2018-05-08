package com.example.mypet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

public class MusicInfo implements Parcelable {
    //0 系统铃声 1本地音乐
    private int from;
    private int isSelected;
    private String musicName;
    private String duration;
    private String musicUri;

    public String getMusicUri() { return musicUri; }

    public void setMusicUri(String musicUri) { this.musicUri = musicUri; }

    public int getFrom() { return from; }

    public void setFrom(int from) { this.from = from; }

    public String getMusicName() { return musicName; }

    public void setMusicName(String musicName) { this.musicName = musicName; }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(from);
        dest.writeInt(isSelected);
        dest.writeString(musicName);
        dest.writeString(duration);
        dest.writeString(musicUri);
    }
    public static final Parcelable.Creator<MusicInfo> CREATOR = new Parcelable.Creator<MusicInfo>(){

        @Override
        public MusicInfo createFromParcel(Parcel source) {
            MusicInfo musicInfo = new MusicInfo();
            musicInfo.from = source.readInt();
            musicInfo.isSelected = source.readInt();
            musicInfo.musicName= source.readString();
            musicInfo.duration = source.readString();
            musicInfo.musicUri = source.readString();
            return musicInfo;
        }

        @Override
        public MusicInfo[] newArray(int size) {
            return new MusicInfo[size];
        }
    };
}
