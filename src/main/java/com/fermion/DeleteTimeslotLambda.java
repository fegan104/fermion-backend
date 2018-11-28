package com.fermion;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fermion.data.database.JdbcTimeslotDao;
import com.fermion.data.database.TimeslotDataSource;
import com.fermion.data.model.response.ApiGatewayResponse;
import com.fermion.data.model.response.TimeslotResponseData;
import com.fermion.util.Constants;
import com.fermion.util.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by @author frankegan on 11/9/18.
 */
public class DeleteTimeslotLambda implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    DateTimeFormatter timef = DateTimeFormatter.ofPattern("HH:mm");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE", Locale.forLanguageTag("en-US"));
    Gson gson = new GsonBuilder().create();
    TimeslotDataSource timeslotDao;

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		Logger.init(context);
		timeslotDao = new JdbcTimeslotDao();

		try {
            Map<String, String> pathParams = (Map<String, String>) input.get(Constants.PATH_PARAMS);
            timeslotDao.delete(
                    DayOfWeek.from(formatter.parse(pathParams.getOrDefault("dayOfWeek", null))),
                    LocalDate.parse(pathParams.getOrDefault("date", null), dtf),
                    LocalTime.parse(pathParams.getOrDefault("time", null), timef)
            );

			List<TimeslotResponseData> closed = new ArrayList<>();
			return new ApiGatewayResponse(202, gson.toJson(closed));
		} catch (Exception e) {
			context.getLogger().log(e.toString());
			Arrays.asList(e.getStackTrace()).forEach(it -> Logger.log(it.toString()));
			return new ApiGatewayResponse(400, gson.toJson(e.toString()));
		}
	}
}
