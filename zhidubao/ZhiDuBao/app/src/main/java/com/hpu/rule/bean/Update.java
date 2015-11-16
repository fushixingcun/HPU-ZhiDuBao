package com.hpu.rule.bean;

import cn.bmob.v3.BmobObject;

public class Update extends BmobObject {
    private String path;
    private String versionName;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
