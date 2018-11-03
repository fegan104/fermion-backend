package com.fermion.data.model.response;

import com.fermion.data.model.Timeslot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by @author frankegan on 10/31/18.
 */
public class DayResponse extends ApiResponse {
    public String day;//dd-MM-yyyy
    List<Timeslot> timeslots;


    public DayResponse(LocalDate day, List<Timeslot> timeslots) {
        super(200);
        this.day = DateTimeFormatter.ofPattern("dd-MM-yyyy").format(day);
        this.timeslots = timeslots;
    }

    public String getDay() {
        return day;
    }

    public List<Timeslot> getTimeslots() {
        return timeslots;
    }
}
