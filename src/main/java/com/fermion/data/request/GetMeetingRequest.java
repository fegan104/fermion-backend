package com.fermion.data.request;
/**
 * Helper Class for Lambda Testing
 * @author ttshiz
 *
 *
 */
public class GetMeetingRequest {
	String startTime;
	String endTime;
	String day;
	String guest;
	String location;
	String calID;
	
    public GetMeetingRequest (String calendarId, String startTime, String endTime, String day, String guest, String location) {
    	this.calID = calendarId;
    	this.startTime = startTime;
    	this.endTime = endTime;
    	this.day = day;
    	this.guest = guest;
    	this.location = location;
    }
    
    public String toString() {
            return "Create(" + calID + "," + startTime + "," + endTime + "," + day +"," + guest +"," + location +")";
    }

}
