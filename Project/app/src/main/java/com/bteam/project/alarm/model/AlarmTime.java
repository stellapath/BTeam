package com.bteam.project.alarm.model;

import java.io.Serializable;

public class AlarmTime implements Serializable {

    private int hour, minute;

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
