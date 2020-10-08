package com.bteam.project.alarm.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 시간을 계산해주는 클래스
 */
public class TimeCalculator {

    public int getHour(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public int getMinute(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.get(Calendar.MINUTE);
    }

    public long getMillis(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    public String toString(long millis, int type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        Date date = calendar.getTime();
        SimpleDateFormat sdf = null;
        if (type == 0) {
            sdf = new SimpleDateFormat("a hh:mm");
        } else if (type == 1) {
            sdf = new SimpleDateFormat("yyyy/MM/dd kk:mm:ss");
        }
        return sdf.format(date);
    }

    public long isBefore(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        // 현재 시간보다 이전에 설정되었을 경우
        if (calendar.before(Calendar.getInstance())) {
            // 현재 날짜에 맞춘 뒤
            Calendar newCalendar = Calendar.getInstance();
            // 전에 설정된 알람 시간으로 설정하고
            newCalendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
            newCalendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
            newCalendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
            // 그 설정된 시간이 현재시간 이전일 경우
            if (newCalendar.before(Calendar.getInstance())) {
                // 하루를 더함
                newCalendar.add(Calendar.DATE, 1);
            }
            return newCalendar.getTimeInMillis();
        } else {
            return calendar.getTimeInMillis();
        }
    }

    public String getTimerString(long millis) {
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = millis / daysInMilli;
        millis = millis % daysInMilli;

        long elapsedHours = millis / hoursInMilli;
        millis = millis % hoursInMilli;

        long elapsedMinutes = millis / minutesInMilli;
        millis = millis % minutesInMilli;

        long elapsedSeconds = millis / secondsInMilli;

        if (elapsedDays == 0 && elapsedHours == 0 && elapsedMinutes == 0) {
            return elapsedSeconds + "초";
        }

        if (elapsedDays == 0 && elapsedHours == 0) {
            return elapsedMinutes + "분 " + elapsedSeconds + "초";
        }

        if (elapsedDays == 0) {
            return elapsedHours + "시간 " + elapsedMinutes + "분 " + elapsedSeconds + "초";
        }

        return elapsedDays + "일 " + elapsedHours + "시간 " + elapsedMinutes + "분 " + elapsedSeconds + "초";
    }

}
