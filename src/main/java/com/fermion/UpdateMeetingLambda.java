package com.fermion;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fermion.data.model.Meeting;
import com.fermion.data.model.response.ApiGatewayResponse;
import com.fermion.data.model.response.MeetingResponseData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by @author frankegan on 11/9/18.
 */
public class UpdateMeetingLambda implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		DateTimeFormatter timef = DateTimeFormatter.ofPattern("HH:mm:ss");
		for (Map.Entry e : input.entrySet()) {
			context.getLogger().log(e.getKey() + ": " + e.getValue() + "\n");
		}
		Gson gson = new GsonBuilder().create();
		try {
			JsonObject body = new JsonParser().parse((String) input.get("body")).getAsJsonObject();
			Meeting meeting = new Meeting(
					LocalTime.parse(body.get("startTime").getAsString(), timef), 
					LocalTime.parse(body.get("endTime").getAsString(), timef), 
					LocalDate.parse(body.get("day").getAsString(), dtf),
					body.get("guest").getAsString(),
					body.get("location").getAsString()
					);
			MeetingResponseData meetingRes = new MeetingResponseData(meeting);
			context.getLogger().log("Added Meeting with " + meetingRes.getGuest());
			return new ApiGatewayResponse(202, gson.toJson(meetingRes));
		} catch (Exception e) {
			context.getLogger().log(e.toString());
			Arrays.asList(e.getStackTrace()).forEach(it -> context.getLogger().log(it.toString()));
			return new ApiGatewayResponse(400, gson.toJson(e.toString()));
		}
	}
}
