package com.fermion.data.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by @author frankegan on 10/30/18.
 */
public class Meeting {
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate day;
    private String guest;
    private String location;

    public Meeting(LocalTime startTime, LocalTime endTime, LocalDate day, String guest, String location) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
        this.guest = guest;
        this.location = location;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
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
