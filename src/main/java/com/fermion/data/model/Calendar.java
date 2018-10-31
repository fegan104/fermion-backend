package com.fermion.data.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

/**
 * Created by @author frankegan on 10/30/18.
 */
public class Calendar {
    private String id;
    private Map<LocalDate, List<Timeslot>> timeslots;
    private int startHour;
    private int endHour;
    private int duration;

    public Calendar(String id, List<Timeslot> timeslots, int startHour, int endHour, int duration) {
        this.id = id;
        this.timeslots = timeslots.stream().collect(groupingBy(Timeslot::getDay));
        this.startHour = startHour;
        this.endHour = endHour;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public Map<LocalDate, List<Timeslot>> getTimeslots() {
        return timeslots;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public int getDuration() {
        return duration;
    }
}
