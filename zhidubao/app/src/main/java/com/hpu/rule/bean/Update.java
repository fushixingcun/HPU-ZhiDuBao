package com.hpu.rule.bean;

import cn.bmob.v3.BmobObject;

public class Update extends BmobObject {
    private String path;
    private String versionName;
    private boolean word_update;

    public boolean isWord_update() {
        return word_update;
    }

    public void setWord_update(boolean word_update) {
        this.word_update = word_update;
    }

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
