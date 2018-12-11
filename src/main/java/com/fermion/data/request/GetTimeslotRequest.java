package com.fermion.data.request;
/**
 * Helper Class for Lambda Testing
 * @author ttshiz
 *
 */
public class GetTimeslotRequest {
	String id;
	String localDate;
	String startTime;
	String endTime;
	
    public GetTimeslotRequest(String id, String localDate, String startTime, String endTime) {
    	this.id = id;
    	this.localDate = localDate;
    	this.startTime = startTime;
    	this.endTime = endTime;
    }
    
    public String toString() {
            return "Create(" + id + "," + localDate + "," + startTime +"," + endTime +")";
    }
}
