package com.fermion.data.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

/**
 * Created by @author frankegan on 10/30/18.
 */
public class Timeslot {
    private String id;
    private LocalDate localDate;
    private LocalTime startTime;
    private LocalTime endTime;

    public Timeslot(LocalDate localDate, LocalTime startTime, LocalTime endTime) {
        this(UUID.randomUUID().toString(), localDate, startTime, endTime);
    }

    public Timeslot(String id, LocalDate localDate, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.localDate = localDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public LocalDate getDay() {
        return localDate;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}
