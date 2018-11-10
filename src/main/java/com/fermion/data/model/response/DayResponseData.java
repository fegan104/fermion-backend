package com.fermion.data.model.response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by @author frankegan on 10/31/18.
 */
public class DayResponseData extends ResponseData {
    public String day;//dd-MM-yyyy
    List<TimeslotResponseData> timeslots;
    List<MeetingResponseData> meetings;


    public DayResponseData(LocalDate day,
                           List<TimeslotResponseData> timeslots,
                           List<MeetingResponseData> meetings) {
        this.day = DateTimeFormatter.ofPattern("dd-MM-yyyy").format(day);
        this.timeslots = timeslots;
    }

    public String getDay() {
        return day;
    }

    public List<TimeslotResponseData> getTimeslots() {
        return timeslots;
    }
}
