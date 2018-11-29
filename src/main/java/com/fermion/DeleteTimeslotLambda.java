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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by @author frankegan on 11/9/18.
 */
public class DeleteTimeslotLambda implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    DateTimeFormatter timef = DateTimeFormatter.ofPattern("HH:mm");
    Gson gson = new GsonBuilder().create();
    TimeslotDataSource timeslotDao;

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        Logger.init(context);
        timeslotDao = new JdbcTimeslotDao();

        try {
            Map<String, String> pathParams = (Map<String, String>) input.get(Constants.QUERY_STRING_PARAMS);
            Logger.log(pathParams.toString());
            String calendarId = pathParams.getOrDefault("calendarId", null);
            String dayOfWeekParam = pathParams.getOrDefault("dayOfWeek", null);
            String dateParam = pathParams.getOrDefault("date", null);
            String timeParam = pathParams.getOrDefault("time", null);

            DayOfWeek dayOfWeek = null;
            LocalDate date = null;
            LocalTime time = null;

            if (dayOfWeekParam != null) {
                dayOfWeek = Arrays.stream(DayOfWeek.values())
                        .filter(d -> {
                            String sub = d.toString().substring(0, 3);
                            return sub.equals(dayOfWeekParam);
                        })
                        .findFirst()
                        .orElse(null);
            }
            if (dateParam != null) {
                date = LocalDate.parse(dateParam, dtf);
            }
            if (timeParam != null) {
                time = LocalTime.parse(timeParam, timef);
            }

            List<TimeslotResponseData> closed = timeslotDao.delete(calendarId, dayOfWeek, date, time)
                    .orElse(new ArrayList<>())
                    .stream()
                    .map(TimeslotResponseData::new)
                    .collect(Collectors.toList());
            return new ApiGatewayResponse(202, gson.toJson(closed));
        } catch (Exception e) {
            context.getLogger().log(e.toString());
            Arrays.asList(e.getStackTrace()).forEach(it -> Logger.log(it.toString()));
            return new ApiGatewayResponse(400, gson.toJson(e.toString()));
        }
    }
}
