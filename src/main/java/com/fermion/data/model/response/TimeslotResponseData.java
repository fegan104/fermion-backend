package com.fermion.data.model.response;

import com.fermion.data.model.Timeslot;

import java.time.format.DateTimeFormatter;

/**
 * Created by @author frankegan on 10/31/18.
 */
public class TimeslotResponseData extends ResponseData {
    String id;
    String day;// dd-MM-yyyy
    String startTime; //HH:mm
    String endTime; //HH:mm

    public TimeslotResponseData(Timeslot timeslot) {
        this(
                timeslot.getId(),
                timeslot.getDay().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                timeslot.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")),
                timeslot.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"))
        );
    }

    public TimeslotResponseData(
            String id,
            String day,
            String startTime,
            String endTime
    ) {
        this.id = id;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
