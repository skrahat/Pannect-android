package com.example.bondhu;

public class Status {

    String liveStatus;
    String user;
    String currentStatus;

    public Status(String liveStatus, String user, String currentStatus) {
        this.liveStatus = liveStatus;
        this.user = user;
        this.currentStatus = currentStatus;
    }

    public String getLiveStatus() {
        return liveStatus;
    }

    public String getUser() {
        return user;
    }
    public String getcurrentStatus() {
        return currentStatus;
    }
}
