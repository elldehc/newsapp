package com.java.liqibin.util;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTime {
    private LocalDate date;
    private LocalTime time;

    public DateTime() {
        this.date = null;
        this.time = null;
    }

    public DateTime(LocalDate date, LocalTime time) {
        this.date = date;
        this.time = time;
    }

    public static DateTime now() {
        return new DateTime(LocalDate.now(), LocalTime.now(ZoneId.of("Asia/Shanghai")));
    }

    public DateTime setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public DateTime setTime(LocalTime time) {
        this.time = time;
        return this;
    }

    public LocalTime getTime() {
        return this.time;
    }

    @NonNull
    @Override
    public String toString() {
        return date.toString() + " " + time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public String toQueryString(){
        return date.toString() + "%20" + time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
