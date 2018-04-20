package com.example.mypet.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ChangeInfoBean implements Parcelable {
    private String title;
    private String infoName;
    private String configName;

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfoName() {
        return infoName;
    }

    public void setInfoName(String infoName) {
        this.infoName = infoName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(infoName);
        dest.writeString(configName);
    }
    public static final Parcelable.Creator<ChangeInfoBean> CREATOR = new Parcelable.Creator<ChangeInfoBean>(){

        @Override
        public ChangeInfoBean createFromParcel(Parcel source) {
            ChangeInfoBean changeInfoBean = new ChangeInfoBean();
            changeInfoBean.title = source.readString();
            changeInfoBean.infoName = source.readString();
            changeInfoBean.configName = source.readString();
            return changeInfoBean;
        }

        @Override
        public ChangeInfoBean[] newArray(int size) {
            return new ChangeInfoBean[size];
        }
    };
}
