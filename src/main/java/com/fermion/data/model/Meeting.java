package com.fermion.data.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by @author frankegan on 10/30/18.
 */
public class Meeting {
    private String calendarId;
    private LocalTime startTime;
    private LocalDate day;
    private String guest;
    private String location;

    public Meeting(String calendarId, LocalTime startTime, LocalDate day, String guest, String location) {
        this.calendarId = calendarId;
        this.startTime = startTime;
        this.day = day;
        this.guest = guest;
        this.location = location;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalDate getDay() {
        return day;
    }

    public String getGuest() {
        return guest;
    }

    public String getLocation() {
        return location;
    }
}
