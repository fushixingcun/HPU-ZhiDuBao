package com.hpu.rule.bean;

import cn.bmob.v3.BmobObject;

public class Collect extends BmobObject{
    private String url;
    private String pian_name;

    public Collect(String url, String pian_name) {
        this.url = url;
        this.pian_name = pian_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPian_name() {
        return pian_name;
    }

    public void setPian_name(String pian_name) {
        this.pian_name = pian_name;
    }
}
