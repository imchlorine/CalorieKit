package com.maclee.calorie.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Steps")
public class Steps {

    @PrimaryKey(autoGenerate = true)
    private int sid;
    @ColumnInfo(name = "steps")
    private int steps;
    @ColumnInfo(name = "time")
    private String addTime;

    @Ignore
    public Steps() {
    }

    public Steps(int sid, int steps, String addTime) {
        this.sid = sid;
        this.steps = steps;
        this.addTime = addTime;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
}
