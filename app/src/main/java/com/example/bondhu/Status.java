package com.example.bondhu;

public class Status {

    String liveStatus;
    String user;

    public Status(String liveStatus, String user) {
        this.liveStatus = liveStatus;
        this.user = user;
    }

    public String getLiveStatus() {
        return liveStatus;
    }

    public String getUser() {
        return user;
    }
}
