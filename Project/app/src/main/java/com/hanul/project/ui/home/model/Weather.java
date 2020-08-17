package com.hanul.project.ui.home.model;

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
