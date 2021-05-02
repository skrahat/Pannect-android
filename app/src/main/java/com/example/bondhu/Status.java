package com.example.bondhu;

public class Status {

    String liveStatus;
    String user;
    String currentStatus;
    String time;

    public Status(String liveStatus, String user, String currentStatus) {
        this.liveStatus = liveStatus;
        this.user = user;
        this.currentStatus = currentStatus;
        this.time = time;
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
    public String gettime() {
        return time;
    }
}
