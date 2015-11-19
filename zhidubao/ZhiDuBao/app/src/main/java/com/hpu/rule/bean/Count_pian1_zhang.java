package com.hpu.rule.bean;

import cn.bmob.v3.BmobObject;

public class Count_pian1_zhang extends BmobObject {
    private String zhang_name;
    private String content;

    public Count_pian1_zhang(String zhang_name, String content) {
        this.zhang_name = zhang_name;
        this.content = content;
    }

    public String getZhang_name() {
        return zhang_name;
    }

    public void setZhang_name(String zhang_name) {
        this.zhang_name = zhang_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
