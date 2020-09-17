package com.bteam.project.alarm.model;

import java.io.Serializable;

public class Alarm implements Serializable {

    // 알람 상태..
    private boolean turnedOn;

    // 이미 울린 알람의 개수 (알람 반복에 필요)
    private int alreadyRangAlarms;

    private long wakeUpTime, arrivalTime;
    private int wakeUpHour, wakeUpMinute, interval, repeat,
                duration, arrivalHour, arrivalMinute;
    private float latitude, longitude;
    private boolean isVibrate, isRing, isRead;
    private String ringtoneName, ringtoneUri, memo, destination;

    public boolean isTurnedOn() {
        return turnedOn;
    }

    public void setTurnedOn(boolean turnedOn) {
        this.turnedOn = turnedOn;
    }

    public int getAlreadyRangAlarms() {
        return alreadyRangAlarms;
    }

    public void setAlreadyRangAlarms(int alreadyRangAlarms) {
        this.alreadyRangAlarms = alreadyRangAlarms;
    }

    public long getWakeUpTime() {
        return wakeUpTime;
    }

    public void setWakeUpTime(long wakeUpTime) {
        this.wakeUpTime = wakeUpTime;
    }

    public int getWakeUpHour() {
        return wakeUpHour;
    }

    public void setWakeUpHour(int wakeUpHour) {
        this.wakeUpHour = wakeUpHour;
    }

    public int getWakeUpMinute() {
        return wakeUpMinute;
    }

    public void setWakeUpMinute(int wakeUpMinute) {
        this.wakeUpMinute = wakeUpMinute;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getArrivalHour() {
        return arrivalHour;
    }

    public void setArrivalHour(int arrivalHour) {
        this.arrivalHour = arrivalHour;
    }

    public int getArrivalMinute() {
        return arrivalMinute;
    }

    public void setArrivalMinute(int arrivalMinute) {
        this.arrivalMinute = arrivalMinute;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public boolean isVibrate() {
        return isVibrate;
    }

    public void setVibrate(boolean vibrate) {
        isVibrate = vibrate;
    }

    public boolean isRing() {
        return isRing;
    }

    public void setRing(boolean ring) {
        isRing = ring;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getRingtoneName() {
        return ringtoneName;
    }

    public void setRingtoneName(String ringtoneName) {
        this.ringtoneName = ringtoneName;
    }

    public String getRingtoneUri() {
        return ringtoneUri;
    }

    public void setRingtoneUri(String ringtoneUri) {
        this.ringtoneUri = ringtoneUri;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
