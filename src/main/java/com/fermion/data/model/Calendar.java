package com.fermion.data.model;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

/**
 * Created by @author frankegan on 10/30/18.
 */
public class Calendar {
    private String id;
    private String name;
    private Map<LocalDate, List<Timeslot>> timeslots;
    private Map<LocalDate, List<Meeting>> meetings;
    private LocalDate startDay;
    private LocalDate endDay;
    private LocalTime startHour;
    private LocalTime endHour;
    private int duration;

    //Constructor used when creating a calendar from scratch
    public Calendar(
            String name,
            LocalDate startDate,
            LocalDate endDate,
            int startHour,
            int endHour,
            int duration
    ) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.startHour = LocalTime.of(startHour, 0);
        this.endHour = LocalTime.of(endHour, 0);
        this.duration = duration;
        this.startDay = startDate;
        this.endDay = endDate;
        this.timeslots = fillInTimeslots().stream().collect(groupingBy(Timeslot::getDay));
        this.meetings = new HashMap<>();
    }

    //Constructor used when loading a calendar from RDS
    public Calendar(
            String id,
            String name,
            Map<LocalDate, List<Timeslot>> timeslots,
            Map<LocalDate, List<Meeting>> meetings
    ) {
        this.id = id;
        this.name = name;
        this.timeslots = timeslots;
        this.meetings = meetings;
    }

    public String getId() {
        return id;
    }

    public Map<LocalDate, List<Timeslot>> getTimeslots() {
        return timeslots;
    }

    public Map<LocalDate, List<Meeting>> getMeetings() {
        return meetings;
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
            if(i > range) break;
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

    public String getName() {
        return name;
    }

    public LocalDate getStartDay() {
        return startDay;
    }

    public LocalDate getEndDay() {
        return endDay;
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
