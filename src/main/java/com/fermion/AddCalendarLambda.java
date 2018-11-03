package com.fermion;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fermion.data.model.Calendar;
import com.fermion.data.model.response.CalendarResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Created by @author frankegan on 10/31/18.
 */
public class AddCalendarLambda implements RequestHandler<Map<String, Object>, String> {

    @Override
    public String handleRequest(Map<String, Object> input, Context context) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for (Map.Entry e : input.entrySet()) {
            context.getLogger().log(e.getKey() + ": " + e.getValue() + "\n");
        }
        Gson gson = new GsonBuilder().create();
        try {
            Calendar calendar = new Calendar(
                    LocalDate.parse((String) input.get("startDate"), dtf),
                    LocalDate.parse((String) input.get("endDate"), dtf),
                    (int) input.get("startHour"),
                    (int) input.get("endHour"),
                    (int) input.get("duration")
            );
            CalendarResponse calRes = new CalendarResponse(calendar);
            return gson.toJson(calRes);
        } catch (Exception e) {
            context.getLogger().log(e.getMessage());
            CalendarResponse calRes = new CalendarResponse(new Calendar(LocalDate.now(), LocalDate.now(), 0, 0, 1));
            return gson.toJson(calRes);
        }
    }
}
