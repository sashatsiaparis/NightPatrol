package com.example.nightpatrol;

public class ShiftData {

    private String date;
    private String van;
    private String time;
    private String teamID;



    public ShiftData(String date, String van, String time, String teamID) {
        this.date = date;
        this.time = time;
        this.van = van;
        this.teamID = teamID;

    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVan() {
        return van;
    }

    public void setVan(String van) {
        this.van = van;
    }

    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }
}
