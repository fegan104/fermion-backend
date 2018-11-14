package com.fermion;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fermion.data.database.JdbcCalendarDao;
import com.fermion.data.database.JdbcTimeslotDao;
import com.fermion.data.model.Calendar;
import com.fermion.data.model.response.ApiGatewayResponse;
import com.fermion.data.model.response.CalendarResponseData;
import com.fermion.logger.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by @author frankegan on 10/31/18.
 */
public class AddCalendarLambda implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private JdbcCalendarDao calendarDao;
    private JdbcTimeslotDao timeslotDao;
    private Gson gson;


    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        Logger.init(context);

        calendarDao = new JdbcCalendarDao();
        timeslotDao = new JdbcTimeslotDao();
        gson = new GsonBuilder().create();

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

            Logger.log("Starting insert");
//            calendarDao.insert(calendar);
//            timeslotDao.insert(calendar.getId(), calendar.getTimeslots()
//                    .values()
//                    .stream()
//                    .flatMap(Collection::stream)
//                    .collect(Collectors.toList()));

            Logger.log("Writing json response");
            CalendarResponseData calRes = new CalendarResponseData(calendar);

            return new ApiGatewayResponse(201, gson.toJson(calRes));
        } catch (Exception e) {
            context.getLogger().log(e.toString());
            Arrays.asList(e.getStackTrace()).forEach(it -> context.getLogger().log(it.toString()));
            return new ApiGatewayResponse(400, gson.toJson(e.toString()));
        }
    }
}
