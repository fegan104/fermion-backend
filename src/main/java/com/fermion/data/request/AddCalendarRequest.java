package com.fermion.data.request;
/**
 * Helper Class for Lambda Testing
 * @author ttshiz
 *
 */
public class AddCalendarRequest {
	String name;
	int startHour;
	int endHour;
	String startDate;
	String endDate;
	int duration;
	
    public AddCalendarRequest (String calendarName, int startHour, int endHour, String startDate, String endDate, int duration) {
    	this.name = calendarName;
    	this.startHour = startHour;
    	this.endHour = endHour;
    	this.startDate = startDate;
    	this.endDate = endDate;
    	this.duration = duration;
    }
    
    public String toString() {
            return "Create(" + name + "," + startHour + "," + endHour + "," + startDate +"," + endDate +"," + duration +")";
    }

}
