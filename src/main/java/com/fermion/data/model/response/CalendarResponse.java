package com.fermion.data.model.response;

import com.fermion.data.model.Calendar;

/**
 * Created by @author frankegan on 10/31/18.
 */
public class CalendarResponse extends ApiResponse {
    String id;
    DayResponse[] days;
    int startHour;
    int endHour;
    int duration;


    public CalendarResponse(Calendar calendar) {
        super(200);
        this.id = calendar.getId();
        this.days = (DayResponse[]) calendar.getTimeslots()
                .entrySet()
                .stream()
                .map(e -> new DayResponse(e.getKey(), e.getValue()))
                .toArray();
        this.startHour = calendar.getStartHour();
        this.endHour = calendar.getEndHour();
        this.duration = calendar.getDuration();
    }
}
