package com.hpu.rule.bease;

import cn.bmob.v3.BmobObject;

/**用于用户的反馈的bean
 * Created by hjs on 2015/11/7.
 */
public class Feedback extends BmobObject {
    //反馈内容
    private String content;
    //联系方式
    private String contacts;
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getContacts() {
        return contacts;
    }
    public void setContacts(String contacts) {
        this.contacts = contacts;
    }
}
