package com.fermion.data.request;

public class GetCalendarByIdRequest {
		String id;
		
	    public GetCalendarByIdRequest (String calendarId) {
	    	this.id = calendarId;
	    }
	    
	    public String toString() {
	            return "Create(" + id +")";
	    }

}
