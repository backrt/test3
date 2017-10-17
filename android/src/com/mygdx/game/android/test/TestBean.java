package com.mygdx.game.android.test;

/**
 * Created by Administrator on 2016/11/19.
 */

public class TestBean {

    private String name;
    private String text;
    private String img;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "name='" + name + '\'' +
                ", text='" + text + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
