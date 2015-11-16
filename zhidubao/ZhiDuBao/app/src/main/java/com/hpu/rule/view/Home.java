package com.hpu.rule.view;

/**
 * 作为主界面listview的适配器
 * name代表主界面的文字，imageId代表图片
 * Created by hjs on 2015/11/7.
 */
public class Home {
    private String name;
    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    private int imageId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Home(String name,int imageId){
         this.name=name;
         this.imageId=imageId;
    }

}
