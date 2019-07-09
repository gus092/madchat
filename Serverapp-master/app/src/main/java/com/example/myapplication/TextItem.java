package com.example.myapplication;

import java.io.Serializable;

public class TextItem implements Serializable
{
    private String user_Text, user_Name,user_Photo;


    public TextItem(String name, String text, String bitmap){
        user_Name = name;
        user_Text = text;
        user_Photo = bitmap;
        //Photo = photo;
    }

    public String getUser_Name(){
        return user_Name;
    }

    public String getUser_Text(){ return user_Text; }

    public String getUser_Photo(){return user_Photo;}

    public void setUser_Name(String name){
        this.user_Name = name;
    }

    public void setUser_Text(String text){
        this.user_Text = text;
    }

}
