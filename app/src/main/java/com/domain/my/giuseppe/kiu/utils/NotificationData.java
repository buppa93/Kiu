package com.domain.my.giuseppe.kiu.utils;

import java.io.Serializable;

/**
 * Created by PaoloPepe on 14/01/2017.
 */


public class NotificationData implements Serializable {

    private String title;
    private String body;
    private String sound;
    private int accepted; //0 if not accepted 1 else

    public NotificationData(String title, String body, String sound, int accepted) {
        this.title = title;
        this.body = body;
        this.sound = sound;
        this.accepted = accepted;
    }

    public NotificationData(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public int getAccepted() {return this.accepted;}

    public void setAccepted(int accepted) {this.accepted = accepted;}
}
