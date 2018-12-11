package com.fermion.data.request;

import com.google.gson.JsonArray;
/**
 *  Hellper for testing GetDayLambda
 * @author ttshiz
 *
 */
public class GetDayRequest {
	JsonArray timeslots;
	JsonArray meetings;
	
	public GetDayRequest () {
		this.timeslots = new JsonArray();
		this.meetings = new JsonArray();
	}

	public String toString() {
		return "Create(" + timeslots + "," + meetings +")";
	}

}
