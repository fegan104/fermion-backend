package com.fermion.data.model.response;

import com.fermion.data.model.Calendar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by @author frankegan on 10/31/18.
 */
public class CalendarResponse extends ApiResponse {
    String id;
    public List<DayResponse> days;
    int startHour;
    int endHour;
    int duration;


    public CalendarResponse(Calendar calendar) {
        this.id = calendar.getId();
        try {
            this.days = calendar.getTimeslots()
                    .entrySet()
                    .stream()
                    .map(e -> new DayResponse(e.getKey(), e.getValue()
                            .stream()
                            .map(TimeslotResponse::new)
                            .collect(Collectors.toList())))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            this.days = new ArrayList<>();
        }
        this.startHour = calendar.getStartHour().getHour();
        this.endHour = calendar.getEndHour().getHour();
        this.duration = calendar.getDuration();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<DayResponse> getDays() {
        return days;
    }

    public void setDays(List<DayResponse> days) {
        this.days = days;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
