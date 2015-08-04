package com.example.friday;

/**
 * Created by zhengyuanjie on 15/6/17.
 */
public class Comment {
    private String name,time,cmt;
    private int id,good;
    private boolean clicked;

    public Comment(){
        this.clicked = false;
    }

    public Comment(String name,String time,String cmt, int id, int good){
        this.name = name;
        this.time = time;
        this.cmt = cmt;
        this.id = id;
        this.good = good;
    }

    public boolean getClicked(){
        return clicked;
    }
    public String getName(){
        return name;
    }
    public String getTime(){
        return time;
    }
    public String getCmt(){
        return cmt;
    }
    public int getID(){
        return id;
    }
    public int getGood(){
        return good;
    }
    public void setClicked(boolean clicked){
        this.clicked = clicked;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setTime(String time){
        this.time = time;
    }
    public void setCmt(String cmt){
        this.cmt = cmt;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setGood(int good){
        this.good = good;
    }
}
