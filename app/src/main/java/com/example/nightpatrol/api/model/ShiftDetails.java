package com.example.nightpatrol.api.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShiftDetails {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ShiftLeader getShiftLeader() {
        return shiftLeader;
    }

    public void setShiftLeader(ShiftLeader shiftLeader) {
        this.shiftLeader = shiftLeader;
    }

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("shiftLeader")
    @Expose
    ShiftLeader shiftLeader;
}
