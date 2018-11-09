package com.fermion.data.model;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.stream.Collectors.groupingBy;

/**
 * Created by @author frankegan on 10/30/18.
 */
public class Calendar {
    private String id;
    private Map<LocalDate, List<Timeslot>> timeslots;
    private LocalDate startDay;
    private LocalDate endDay;
    private LocalTime startHour;
    private LocalTime endHour;
    private int duration;

    //Constructor used when creating a calendar from scratch
    public Calendar(
            LocalDate startDate,
            LocalDate endDate,
            int startHour,
            int endHour,
            int duration
    ) {
        this.id = UUID.randomUUID().toString();
        this.startHour = LocalTime.of(startHour, 0);
        this.endHour = LocalTime.of(endHour, 0);
        this.duration = duration;
        this.startDay = startDate;
        this.endDay = endDate;
        this.timeslots = fillInTimeslots().stream().collect(groupingBy(Timeslot::getDay));
    }

    //Constructor used when loading a calendar from RDS
    public Calendar(
    	    String id,
    	    String ownerName,
    	    String calName,
    	    int startHr,
    	    int endHr,
    	    int duration
    ) {
        this.id = id;
        this.startHour = LocalTime.of(startHr, 0);
        this.endHour = LocalTime.of(endHr, 0);
        this.duration = duration;
        this.startDay = null; //TODO
        this.endDay = null; //TODO 
        this.timeslots= null; //need to fill in timeslots after the basic calendar is created
    }

    
    public String getId() {
        return id;
    }

    public Map<LocalDate, List<Timeslot>> getTimeslots() {
        return timeslots;
    }

    public LocalTime getStartHour() {
        return startHour;
    }

    public LocalTime getEndHour() {
        return endHour;
    }

    public int getDuration() {
        return duration;
    }

    private List<Timeslot> fillInTimeslots() {
        int range = getTimeslotsBetween(startDay, endDay);
        int slotsPerDay = getTimeslotsPerDay();
        List<Timeslot> timeslots = new ArrayList<>(range);
        //iterate through the date range and this the timeslot we're adding
        LocalDateTime index = LocalDateTime.of(startDay, startHour);
        for (int i = 0; i < range; i++) {
            if (index.toLocalDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
                index = index.plusDays(1).withHour(startHour.getHour())
                        .withMinute(startHour.getMinute());
                i += slotsPerDay;
            } else if (index.toLocalDate().getDayOfWeek() == DayOfWeek.SATURDAY) {
                index = index.plusDays(2).withHour(startHour.getHour())
                        .withMinute(startHour.getMinute());
                i += 2 * slotsPerDay;
            }
            LocalDateTime next = index.plusMinutes(duration);
            timeslots.add(new Timeslot(
                    null,
                    index.toLocalDate(),
                    index.toLocalTime(),
                    next.toLocalTime()));
            index = next;
            if (index.toLocalTime().equals(endHour)) {
                index = index.plusDays(1)
                        .withHour(startHour.getHour())
                        .withMinute(startHour.getMinute());
            }
        }
        return timeslots;
    }

    private int getTimeslotsBetween(LocalDate startDay, LocalDate endDay) {
        int slotsPerDay = getTimeslotsPerDay();
        //we add 1 because the end day is exclusive
        int daysBetween = (int) ChronoUnit.DAYS.between(startDay, endDay.plusDays(1));
        return slotsPerDay * daysBetween;
    }

    private int getTimeslotsPerDay() {
        return ((int) Duration.between(startHour, endHour).toMinutes()) / duration;
    }
}
