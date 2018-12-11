package com.fermion.data.request;
/**
 * Helper Class for Lambda Testing
 * 
 * @author ttshiz
 *
 */
public class AddMeetingRequest {
	String startTime;
	String endTime;
	String day;
	String guest;
	String location;
	String calendar;
	
    public AddMeetingRequest (String calendarId, String startTime, String endTime, String day, String guest, String location) {
    	this.calendar = calendarId;
    	this.startTime = startTime;
    	this.endTime = endTime;
    	this.day = day;
    	this.guest = guest;
    	this.location = location;
    }
    
    public String toString() {
            return "Create(" + calendar + "," + startTime + "," + endTime + "," + day +"," + guest +"," + location +")";
    }

}
