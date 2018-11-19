package com.fermion;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fermion.data.database.JdbcMeetingDao;
import com.fermion.data.model.response.ApiGatewayResponse;
import com.fermion.util.Constants;
import com.fermion.util.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by @author frankegan on 11/9/18.
 */
public class DeleteMeetingLambda implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
	private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	private DateTimeFormatter timef = DateTimeFormatter.ofPattern("HH:mm");
	private JdbcMeetingDao meetingDao;
	private Gson gson;

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		Logger.init(context);
		meetingDao = new JdbcMeetingDao();
		gson = new GsonBuilder().create();
		input.forEach((key, value) -> Logger.log(key + ": " + value));

		try {
HashMap<String, String> queryParams = (HashMap<String, String>) input.get(Constants.QUERY_STRING_PARAMS);
			String calendarId = queryParams.get("calendarId");
			LocalDate date = LocalDate.parse(queryParams.get("date"), dtf);
			LocalTime startTime = LocalTime.parse(queryParams.get("startTime"), timef);

			Logger.log("Starting delete meeting");
			// The sql in the dao might need to be changed to add calID
			meetingDao.delete(calendarId, date, startTime);

			Logger.log("Writing json response");
            HashMap<String, Object> result = new HashMap<>();
            result.put("calendarId", calendarId);
            result.put("date", date.format(dtf));
            result.put("startTime", startTime.format(timef));
            Logger.log(result.toString());
			return new ApiGatewayResponse(202, gson.toJson(result));
		} catch (Exception e) {
			Logger.log(e.toString());
			Arrays.asList(e.getStackTrace()).forEach(it -> Logger.log(it.toString()));
			return new ApiGatewayResponse(400, gson.toJson(e.toString()));
		}
	}
}
