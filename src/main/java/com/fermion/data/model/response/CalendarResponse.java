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
        super(200);
        this.id = calendar.getId();
        try {
            this.days = calendar.getTimeslots()
                    .entrySet()
                    .stream()
                    .map(e -> new DayResponse(e.getKey(), e.getValue()))
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

    public List<DayResponse> getDays() {
        return days;
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
