package com.fermion.data.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by @author frankegan on 10/30/18.
 */
public class Timeslot {
    private String id;
    private Meeting meeting;
    private LocalDate localDate;
    private LocalTime startTime;
    private LocalTime endTime;

    public Timeslot(String id, Meeting meeting, LocalDate localDate, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.meeting = meeting;
        this.localDate = localDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public LocalDate getDay() {
        return localDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}
