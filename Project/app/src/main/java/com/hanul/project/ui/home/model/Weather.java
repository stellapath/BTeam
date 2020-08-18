package com.hanul.project.ui.home.model;

import com.hanul.project.R;

public class Weather {

    private int background, icon;
    private String temperature, current, city;

    public Weather(int background, int icon, String temperature, String current, String city) {
        this.background = background;
        this.icon = icon;
        this.temperature = temperature;
        this.current = current;
        this.city = city;
    }

    public Weather(String temperature, String current, String city) {
        this.temperature = temperature;
        this.current = current;
        this.city = city;
        if (current.equals("맑음")) {
            background = R.drawable.ic_weather_clear;
            icon = R.drawable.ic_day;
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
