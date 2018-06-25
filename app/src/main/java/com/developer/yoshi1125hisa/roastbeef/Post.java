package com.developer.yoshi1125hisa.roastbeef;

import java.io.Serializable;

public class Post implements Serializable{
    private Boolean cooking;
    private String telNum;
    private String willEndAt;

    public Post(String telNum) {
        this.telNum = telNum;
        this.cooking = cooking;
        this.willEndAt = willEndAt;

    }

    public String getTelNum() {
        return  telNum;
    }

    public void setTelNum(String telNum){
        this.telNum = telNum;
    }

    public Boolean getCooking() {
        return cooking;
    }

    public void setCooking(Boolean cook) {
        this.cooking = cook;
}

    public String getWillEndAt() {
        return willEndAt;
    }

    public void setWillEndAt(String willEndAt){
        this.willEndAt = willEndAt;
    }
}


