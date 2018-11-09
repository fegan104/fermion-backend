package com.fermion.data.model.response;

import com.fermion.data.model.Meeting;

import javax.annotation.Nullable;
import java.time.format.DateTimeFormatter;

/**
 * Created by @author frankegan on 10/31/18.
 */
public class MeetingResponseData extends ResponseData {
    String calendarId;
    String startTime;
    String day;
    String guest;
    String location;

    public MeetingResponseData(String calendarId, Meeting meeting) {
        this.calendarId = calendarId;
        this.startTime = meeting.getStartTime().format(DateTimeFormatter.ofPattern("hh:mm"));
        this.day = meeting.getDay().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        this.guest = meeting.getGuest();
        this.location = meeting.getLocation();
    }

    @Nullable
    public static MeetingResponseData fromMeeting(String calendarId, @Nullable Meeting meeting) {
        if (meeting != null) {
            return new MeetingResponseData(calendarId, meeting);
        }
        return null;
    }
}
