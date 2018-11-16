package com.fermion;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fermion.data.database.JdbcMeetingDao;
import com.fermion.data.model.Meeting;
import com.fermion.data.model.response.ApiGatewayResponse;
import com.fermion.data.model.response.MeetingResponseData;
import com.fermion.logger.Logger;
import com.google.gson.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by @author frankegan on 11/9/18.
 */
public class GetMeetingLambda implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	private DateTimeFormatter timef = DateTimeFormatter.ofPattern("HH:mm:ss");
	private JdbcMeetingDao meetingDao;
	private Gson gson;

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		Logger.init(context);
		meetingDao = new JdbcMeetingDao();				
		gson = new GsonBuilder().create();
		try {
			JsonObject body = new JsonParser().parse((String) input.get("body")).getAsJsonObject();
			Meeting meeting = new Meeting(
					LocalTime.parse(body.get("startTime").getAsString(), timef), 
					LocalTime.parse(body.get("endTime").getAsString(), timef), 
					LocalDate.parse(body.get("day").getAsString(), dtf),
					body.get("guest").getAsString(),
					body.get("location").getAsString()
					);
			String calId = body.get("calID").getAsString();

			Logger.log("Starting get meetings for calID"+calId);
			meetingDao.meetingByCalendar(calId);

			Logger.log("Writing json response");
			MeetingResponseData meetingRes = new MeetingResponseData(meeting);

			context.getLogger().log("Fetched Meeting with " + meetingRes.getGuest());
			return new ApiGatewayResponse(202, gson.toJson(meetingRes));
		} catch (Exception e) {
			context.getLogger().log(e.toString());
			Arrays.asList(e.getStackTrace()).forEach(it -> context.getLogger().log(it.toString()));
			return new ApiGatewayResponse(400, gson.toJson(e.toString()));
		}
	}
}
