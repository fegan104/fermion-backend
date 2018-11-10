package com.fermion.data.model.response;

import com.fermion.data.model.Calendar;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by @author frankegan on 10/31/18.
 */
public class CalendarResponseData extends ResponseData {
    String id;
    public List<DayResponseData> days;


    public CalendarResponseData(Calendar calendar) {
        this.id = calendar.getId();
        try {

            this.days = calendar.getTimeslots()
                    .entrySet()
                    .stream()
                    .map(e -> new DayResponseData(e.getKey(),
                            e.getValue().stream()
                                    .map(TimeslotResponseData::new)
                                    .collect(Collectors.toList()),
                            calendar.getMeetings().getOrDefault(e.getKey(), new ArrayList<>())
                                    .stream()
                                    .map(MeetingResponseData::new)
                                    .collect(Collectors.toList()))
                    ).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            this.days = new ArrayList<>();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<DayResponseData> getDays() {
        return days;
    }

    public void setDays(List<DayResponseData> days) {
        this.days = days;
    }
}
