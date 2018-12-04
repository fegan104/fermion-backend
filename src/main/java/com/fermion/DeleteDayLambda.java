package com.fermion;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fermion.data.database.JdbcMeetingDao;
import com.fermion.data.database.JdbcTimeslotDao;
import com.fermion.data.database.MeetingDataSource;
import com.fermion.data.database.TimeslotDataSource;
import com.fermion.data.model.response.ApiGatewayResponse;
import com.fermion.data.model.response.DayResponseData;
import com.fermion.util.Constants;
import com.fermion.util.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Created by @author frankegan on 11/9/18.
 */
public class DeleteDayLambda implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    Gson gson = new GsonBuilder().create();
    TimeslotDataSource timeslotDao;
    MeetingDataSource meetingDao;

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        Logger.init(context);
        Map<String, String> pathParams = (Map<String, String>) input.get(Constants.QUERY_STRING_PARAMS);
        Logger.log(pathParams.toString());
        String calendarId = pathParams.getOrDefault("calendarId", "");
        String dateString = pathParams.getOrDefault("date", "");
        LocalDate localDate = LocalDate.parse(dateString, dtf);
        timeslotDao = new JdbcTimeslotDao();
        meetingDao = new JdbcMeetingDao();
        timeslotDao.delete(calendarId, null, localDate, null);
        meetingDao.delete(calendarId, localDate);

        return new ApiGatewayResponse(202, gson.toJson(new DayResponseData(localDate, null, null)));
    }
}
