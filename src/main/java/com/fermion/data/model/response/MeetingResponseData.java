package com.fermion.data.model.response;

import com.fermion.data.model.Meeting;

import java.time.format.DateTimeFormatter;

/**
 * Created by @author frankegan on 10/31/18.
 */
public class MeetingResponseData extends ResponseData {
    String startTime;
    String endTime;
    String day;
    String guest;
    String location;

    public MeetingResponseData(Meeting meeting) {
        this.startTime = meeting.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.endTime = meeting.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        this.day = meeting.getDay().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        this.guest = meeting.getGuest();
        this.location = meeting.getLocation();
    }
}
