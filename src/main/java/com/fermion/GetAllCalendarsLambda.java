package com.fermion;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fermion.data.database.JdbcCalendarDao;
import com.fermion.data.model.response.ApiGatewayResponse;
import com.fermion.data.model.response.CalendarResponseData;
import com.fermion.logger.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by @author frankegan on 11/6/18.
 */
public class GetAllCalendarsLambda implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private JdbcCalendarDao calendarDao;
    private Gson gson;

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        Logger.init(context);

        calendarDao = new JdbcCalendarDao();
        gson = new GsonBuilder().create();

        try {
            List<CalendarResponseData> calRes = new ArrayList<>();
            calendarDao.getAll().ifPresent(it -> it.forEach(c -> calRes.add(new CalendarResponseData(c))));
            return new ApiGatewayResponse(200, gson.toJson(calRes));
        } catch (Exception e) {
            context.getLogger().log(e.toString());
            Arrays.asList(e.getStackTrace()).forEach(it -> context.getLogger().log(it.toString()));
            return new ApiGatewayResponse(400, gson.toJson(e.toString()));
        }
    }
}
