package com.fermion;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fermion.data.model.Calendar;
import com.fermion.data.model.response.ApiGatewayResponse;
import com.fermion.data.model.response.CalendarResponseData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by @author frankegan on 11/6/18.
 */
public class GetAllCalendarsLambda implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        Gson gson = new GsonBuilder().create();
        CalendarResponseData calRes = new CalendarResponseData(new Calendar(LocalDate.now(), LocalDate.now(), 0, 0, 1));
        List<CalendarResponseData> responseData = new ArrayList<>();
        responseData.add(calRes);
        return new ApiGatewayResponse(200, gson.toJson(responseData));
    }
}