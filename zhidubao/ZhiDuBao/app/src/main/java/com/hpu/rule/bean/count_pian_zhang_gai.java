package com.hpu.rule.bean;

import cn.bmob.v3.BmobObject;

public class count_pian_zhang_gai extends BmobObject {
    private String zhang_name;
    private String content;

    private String pian_name;

    public String getPian_name() {
        return pian_name;
    }

    public void setPian_name(String pian_name) {
        this.pian_name = pian_name;
    }


    public count_pian_zhang_gai(String zhang_name, String content, String pian_name) {
        this.zhang_name = zhang_name;
        this.content = content;
        this.pian_name = pian_name;
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
