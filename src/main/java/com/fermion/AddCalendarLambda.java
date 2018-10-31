package com.fermion;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fermion.data.model.Calendar;
import com.fermion.data.model.response.CalendarResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;

/**
 * Created by @author frankegan on 10/31/18.
 */
public class AddCalendarLambda implements RequestHandler<String, String> {

    @Override
    public String handleRequest(String input, Context context) {
        context.getLogger().log("reqBody: " + input);
        Gson gson = new GsonBuilder().create();
        try {
            Calendar calendar = gson.fromJson(input, Calendar.class);
            CalendarResponse calRes = new CalendarResponse(calendar);
            return gson.toJson(calRes);
        } catch (JsonSyntaxException e) {
            context.getLogger().log(e.getMessage());
            CalendarResponse calRes = new CalendarResponse(new Calendar("", new ArrayList<>(), 0, 0, 0));
            return gson.toJson(calRes);
        }
    }
}
