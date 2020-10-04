package com.project.bteam.home.model;

import com.project.bteam.R;

import java.io.Serializable;

/**
 * 날씨 정보 DTO
 */
public class Weather implements Serializable {

    private int background, icon;
    private String hour, day, temperature, current, city;

    public Weather(String hour, String day, String temperature, String current, String city) {
        this.hour = hour;
        this.temperature = temperature;
        this.current = current;
        this.city = city;
        setBackground();
        setIcon();
        setDayToString(day);
    }

    private void setBackground() {
        int hour_x = Integer.parseInt(hour);
        if (hour_x >= 20 || hour_x <= 6) {
            background = R.drawable.gradation;
        } else {
            background = R.drawable.ic_weather_clear;
        }
    }

    private void setIcon() {
        if (current.equals("맑음")) {
            icon = R.drawable.ic_sunny;
        } else if (current.equals("흐림")) {
            icon = R.drawable.ic_cloudy;
        } else if (current.equals("비") || current.equals("소나기")) {
            icon = R.drawable.ic_rainy;
        } else if (current.equals("구름 많음")) {
            icon = R.drawable.ic_many_cloud;
        }
    }

    private void setDayToString(String day) {
        int days = Integer.parseInt(day);
        if (days == 0) {
            this.day = "오늘";
        } else if (days == 1) {
            this.day = "내일";
        } else if (days == 2) {
            this.day = "모레";
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
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
