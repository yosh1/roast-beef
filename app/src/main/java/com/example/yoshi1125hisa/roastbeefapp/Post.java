package com.example.yoshi1125hisa.roastbeefapp;


import java.io.Serializable;

public class Post implements Serializable{

    private String telNum;
    public Post(){

    }

    public Post(String telNum) {
        this.telNum = telNum;

    }


    public String getTelNum() { return  telNum; }
    public void setTelNum(String telNum){ this.telNum = telNum; }

}
