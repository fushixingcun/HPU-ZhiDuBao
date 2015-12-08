package com.hpu.rule.bean;

import android.content.Context;

import cn.bmob.v3.BmobInstallation;

public class MyBmobInstallation extends BmobInstallation {
    private String device;

    public MyBmobInstallation(Context context) {
        super(context);
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
