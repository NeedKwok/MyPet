package com.example.mypet.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ChangeInfoBean implements Parcelable {
    private String title;
    private String info;
    //private String configName;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(info);
    }
    public static final Parcelable.Creator<ChangeInfoBean> CREATOR = new Parcelable.Creator<ChangeInfoBean>(){

        @Override
        public ChangeInfoBean createFromParcel(Parcel source) {
            ChangeInfoBean changeInfoBean = new ChangeInfoBean();
            changeInfoBean.title = source.readString();
            changeInfoBean.info = source.readString();
            return changeInfoBean;
        }

        @Override
        public ChangeInfoBean[] newArray(int size) {
            return new ChangeInfoBean[size];
        }
    };
}
