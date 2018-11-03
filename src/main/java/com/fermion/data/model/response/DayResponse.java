package com.fermion.data.model.response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by @author frankegan on 10/31/18.
 */
public class DayResponse extends ApiResponse {
    public String day;//dd-MM-yyyy
    List<TimeslotResponse> timeslots;


    public DayResponse(LocalDate day, List<TimeslotResponse> timeslots) {
        this.day = DateTimeFormatter.ofPattern("dd-MM-yyyy").format(day);
        this.timeslots = timeslots;
    }

    public String getDay() {
        return day;
    }

    public List<TimeslotResponse> getTimeslots() {
        return timeslots;
    }
}
