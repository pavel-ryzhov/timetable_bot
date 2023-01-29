package org.example.entities;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Time implements Comparable<Time>{
    private int hour;
    private int minute;

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

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

    public String getAsString(){
        return formatTime(hour) + "." + formatTime(minute);
    }

    public int countDistance(Time time){
        return this.hour * 60 + this.minute - time.hour * 60 - time.minute;
    }
    public int countDistance(Time time, int dayDifference){
        return this.hour * 60 + this.minute - time.hour * 60 - time.minute + dayDifference * 24 * 60;
    }

    private static String formatTime(int a){
        return a > 9 ? String.valueOf(a) : "0" + a;
    }

    public static boolean between(Time time, Time firstBorder, Time secondBorder){
        var max = max(firstBorder, secondBorder);
        var min = min(firstBorder, secondBorder);
        return !(time.compareTo(min) > 0 && time.compareTo(max) < 0);
    }

    private static Time max(Time t1, Time t2){
        return t1.compareTo(t2) < 0 ? t2 : t1;
    }
    private static Time min(Time t1, Time t2){
        return t1.compareTo(t2) > 0 ? t2 : t1;
    }

    public static Time current(){
        var calendar = new GregorianCalendar();
        return new Time(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    @Override
    public int compareTo(@NotNull Time time) {
        return this.hour == time.hour ? Integer.compare(this.minute, time.minute) : Integer.compare(this.hour, time.hour);
    }

    @Override
    public String toString() {
        return "Time{" +
                "hour=" + hour +
                ", minute=" + minute +
                '}';
    }
}
