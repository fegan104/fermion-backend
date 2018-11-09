package com.fermion;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fermion.data.model.Calendar;
import com.fermion.data.model.response.ApiGatewayResponse;
import com.fermion.data.model.response.CalendarResponseData;
import com.google.gson.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by @author frankegan on 10/31/18.
 */
public class AddCalendarLambda implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for (Map.Entry e : input.entrySet()) {
            context.getLogger().log(e.getKey() + ": " + e.getValue() + "\n");
        }
        Gson gson = new GsonBuilder().create();
        try {
            JsonObject body = new JsonParser().parse((String) input.get("body")).getAsJsonObject();
            Calendar calendar = new Calendar(
                    body.get("name").getAsString(),
                    LocalDate.parse(body.get("startDate").getAsString(), dtf),
                    LocalDate.parse(body.get("endDate").getAsString(), dtf),
                    body.get("startHour").getAsInt(),
                    body.get("endHour").getAsInt(),
                    body.get("duration").getAsInt()
            );
            CalendarResponseData calRes = new CalendarResponseData(calendar);
            context.getLogger().log("Created" + calRes.getId());
            return new ApiGatewayResponse(201, gson.toJson(calRes));
        } catch (Exception e) {
            context.getLogger().log(e.toString());
            Arrays.asList(e.getStackTrace()).forEach(it -> context.getLogger().log(it.toString()));
            return new ApiGatewayResponse(400, gson.toJson(e.toString()));
        }
    }
}
