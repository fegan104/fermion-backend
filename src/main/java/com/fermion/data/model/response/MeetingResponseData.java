package com.fermion.data.model.response;

import com.fermion.data.model.Meeting;

import javax.annotation.Nullable;

/**
 * Created by @author frankegan on 10/31/18.
 */
public class MeetingResponseData extends ResponseData {
    String id;
    String guest;
    String location;

    public MeetingResponseData(Meeting meeting) {
        this.id = meeting.getId();
        this.guest = meeting.getGuest();
        this.location = meeting.getLocation();
    }

    @Nullable public static MeetingResponseData fromMeeting(@Nullable Meeting meeting){
        if (meeting != null){
            return new MeetingResponseData(meeting);
        }
        return null;
    }
}
