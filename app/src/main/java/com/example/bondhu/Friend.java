package com.example.bondhu;

import java.util.ArrayList;

public class Friend {
    String name, currentStatus,id;
    ArrayList<String> totalStatus = new ArrayList<>();
    ArrayList<String> totalFriends = new ArrayList<>();

    public Friend(String name, String currentStatus, String id, ArrayList<String> totalStatus) {
        this.name = name;
        this.currentStatus = currentStatus;
        this.id = id;
        this.totalStatus = totalStatus;
    }

    public String getName() {
        return name;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public String getId() {
        return id;
    }

    public ArrayList<String> getTotalStatus() {
        return totalStatus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTotalStatus(ArrayList<String> totalStatus) {
        this.totalStatus = totalStatus;
    }

    public ArrayList<String> getTotalFriends() {
        return totalFriends;
    }

    public void setTotalFriends(ArrayList<String> totalFriends) {
        this.totalFriends = totalFriends;
    }
}
