package com.fermion.data.model.response;

import com.fermion.data.model.Timeslot;

import java.time.format.DateTimeFormatter;

/**
 * Created by @author frankegan on 10/31/18.
 */
public class TimeslotResponseData extends ResponseData {
    String id;
    String day;// dd-MM-yyyy
    MeetingResponseData meeting;
    String startTime; //hh:mm
    String endTime; //hh:mm

    public TimeslotResponseData(Timeslot timeslot) {
        this(timeslot.getId(),
                timeslot.getDay().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                MeetingResponseData.fromMeeting(timeslot.getMeeting()),
                timeslot.getStartTime().format(DateTimeFormatter.ofPattern("hh:mm")),
                timeslot.getEndTime().format(DateTimeFormatter.ofPattern("hh:mm"))
        );
    }

    public TimeslotResponseData(
            String id,
            String day,
            MeetingResponseData meeting,
            String startTime,
            String endTime
    ) {
        this.id = id;
        this.day = day;
        this.meeting = meeting;
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

    public MeetingResponseData getMeeting() {
        return meeting;
    }

    public void setMeeting(MeetingResponseData meeting) {
        this.meeting = meeting;
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
