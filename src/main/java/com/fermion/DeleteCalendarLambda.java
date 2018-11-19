package com.fermion;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fermion.data.database.*;
import com.fermion.data.model.Calendar;
import com.fermion.data.model.response.ApiGatewayResponse;
import com.fermion.data.model.response.CalendarResponseData;
import com.fermion.util.Constants;
import com.fermion.util.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by @author frankegan on 11/9/18.
 */
public class DeleteCalendarLambda implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        Logger.init(context);

        CalendarDataSource calendarDao = new JdbcCalendarDao();
        TimeslotDataSource timeslotDao = new JdbcTimeslotDao();
        MeetingDataSource meetingDao = new JdbcMeetingDao();

        Gson gson = new GsonBuilder().create();

        try {
            HashMap<String, String> pathParams = (HashMap<String, String>) input.get(Constants.PATH_PARAMS);
            Logger.log(pathParams.toString());
            Calendar calendar = calendarDao.calendarById(pathParams.get("id")).orElse(null);

            timeslotDao.deleteByCalendar(calendar.getId());
            meetingDao.deleteByCalendar(calendar.getId());
            calendarDao.delete(calendar.getId());

            context.getLogger().log("Deleted " + calendar.getId());
            return new ApiGatewayResponse(202, gson.toJson(new CalendarResponseData(calendar)));
        } catch (Exception e) {
            context.getLogger().log(e.toString());
            Arrays.asList(e.getStackTrace()).forEach(it -> Logger.log(it.toString()));
            return new ApiGatewayResponse(400, gson.toJson(e.toString()));
        }
    }
}
