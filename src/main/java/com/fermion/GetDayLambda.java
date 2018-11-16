package com.fermion;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fermion.data.model.Meeting;
import com.fermion.data.model.response.ApiGatewayResponse;
import com.fermion.data.model.response.DayResponseData;
import com.fermion.data.model.response.MeetingResponseData;
import com.fermion.data.model.response.TimeslotResponseData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by @author frankegan on 11/9/18.
 */
public class GetDayLambda implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		for (Map.Entry e : input.entrySet()) {
			context.getLogger().log(e.getKey() + ": " + e.getValue() + "\n");
		}
		Gson gson = new GsonBuilder().create();
		try {
			JsonObject body = new JsonParser().parse((String) input.get("body")).getAsJsonObject();
			JsonArray timeslots = body.get("timeslots").getAsJsonArray(); 
			JsonArray meetings = body.get("meetings").getAsJsonArray();
			return new ApiGatewayResponse (202, null); //return new ApiGatewayResponse(202, gson.toJson(dayRes));
		} catch (Exception e) {
			context.getLogger().log(e.toString());
			Arrays.asList(e.getStackTrace()).forEach(it -> context.getLogger().log(it.toString()));
			return new ApiGatewayResponse(400, gson.toJson(e.toString()));
		}
	}
}