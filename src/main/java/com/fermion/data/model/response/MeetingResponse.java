package com.fermion.data.model.response;

import com.fermion.data.model.Meeting;

import javax.annotation.Nullable;

/**
 * Created by @author frankegan on 10/31/18.
 */
public class MeetingResponse extends ApiResponse {
    String id;
    String guest;
    String location;

    public MeetingResponse(Meeting meeting) {
        this.id = meeting.getId();
        this.guest = meeting.getGuest();
        this.location = meeting.getLocation();
    }

    @Nullable public static MeetingResponse fromMeeting(@Nullable Meeting meeting){
        if (meeting != null){
            return new MeetingResponse(meeting);
        }
        return null;
    }
}
