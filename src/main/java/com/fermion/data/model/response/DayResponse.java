package com.fermion.data.model.response;

import com.fermion.data.model.Timeslot;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by @author frankegan on 10/31/18.
 */
public class DayResponse extends ApiResponse {
    String day;//dd-MM-yyyy
    Timeslot[] timeslots;


    public DayResponse(LocalDate day, List<Timeslot> timeslots) {
        super(200);
        this.day = DateTimeFormatter.ofPattern("dd-MM-yyyy").format(day);
        this.timeslots = (Timeslot[]) timeslots.toArray();
    }

}
