package com.fermion.data.model.response;

import com.fermion.data.model.Meeting;

/**
 * Created by @author frankegan on 10/31/18.
 */
public class MeetingResponse extends ApiResponse {
    String id;
    String guest;
    String location;

    public MeetingResponse(Meeting meeting) {
        super(200);
        this.id = meeting.getId();
        this.guest = meeting.getGuest();
        this.location = meeting.getLocation();
    }
}
