package com.fermion.data.model.response;

import java.util.Map;

/**
 * Created by @author frankegan on 10/31/18.
 */
public class TimeslotResponse extends ApiResponse {
    String id;
    String day;// dd-MM-yyyy
    MeetingResponse meeting;
    String startTime; //hh:mm
    String endTime; //hh:mm

    public TimeslotResponse(int statusCode, Map<String, String> headers) {
        super(statusCode);
    }

}
