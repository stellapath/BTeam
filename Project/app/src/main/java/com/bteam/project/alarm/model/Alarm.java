package com.bteam.project.alarm.model;

import java.io.Serializable;

public class Alarm implements Serializable {

    private int hour, minute, interval, repeat;
    private String memo;
    private float latitude, longitude;

}
