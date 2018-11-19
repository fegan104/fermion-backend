package com.fermion;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fermion.data.database.CalendarDataSource;
import com.fermion.data.database.JdbcCalendarDao;
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
        Gson gson = new GsonBuilder().create();
        try {
            HashMap<String, String> queryParams = (HashMap<String, String>) input.get(Constants.QUERY_STRING_PARAMS);
            Calendar calendar = calendarDao.calendarById(queryParams.get("calendarId")).orElse(null);
            calendarDao.delete(calendar.getId());
            CalendarResponseData calRes = new CalendarResponseData(calendar);
            context.getLogger().log("Deleted " + calRes.getId());
            return new ApiGatewayResponse(202, gson.toJson(calRes));
        } catch (Exception e) {
            context.getLogger().log(e.toString());
            Arrays.asList(e.getStackTrace()).forEach(it -> Logger.log(it.toString()));
            return new ApiGatewayResponse(400, gson.toJson(e.toString()));
        }
    }
}
