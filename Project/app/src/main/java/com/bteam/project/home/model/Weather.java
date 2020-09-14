package com.bteam.project.home.model;

import android.content.Context;

import com.bteam.project.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 날씨 정보 DTO
 */
public class Weather implements Serializable {

    private int background, icon;
    private String hour, temperature, current, city;

    public Weather() {}

    public Weather(String hour, String temperature, String current, String city) {
        this.hour = hour;
        this.temperature = temperature;
        this.current = current;
        this.city = city;
        setBackground();
        setIcon();
    }

    public void setBackground() {
        long currentTimeMillis = System.currentTimeMillis();
        Date date = new Date(currentTimeMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        int hour = Integer.parseInt(sdf.format(date));
        if (hour >= 20 || hour <= 6) {
            background = R.drawable.gradation;
        } else {
            background = R.drawable.ic_weather_clear;
        }
    }

    public void setIcon() {
        if (current.equals("맑음")) {
            icon = R.drawable.ic_sunny;
        } else if (current.equals("흐림")) {
            icon = R.drawable.ic_cloudy;
        } else if (current.equals("비")) {
            icon = R.drawable.ic_rainy;
        } else if (current.equals("구름 많음")) {
            icon = R.drawable.ic_many_cloud;
        }
    }

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
