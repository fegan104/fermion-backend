package com.fermion;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fermion.data.database.CalendarDataSource;
import com.fermion.data.database.JdbcCalendarDao;
import com.fermion.data.database.JdbcTimeslotDao;
import com.fermion.data.database.TimeslotDataSource;
import com.fermion.data.model.Calendar;
import com.fermion.data.model.Timeslot;
import com.fermion.data.model.response.ApiGatewayResponse;
import com.fermion.data.model.response.DayResponseData;
import com.fermion.data.model.response.TimeslotResponseData;
import com.fermion.util.Constants;
import com.fermion.util.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by @author frankegan on 11/9/18.
 */
public class AddDayLambda implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    Gson gson = new GsonBuilder().create();
    CalendarDataSource calendarDao;
    TimeslotDataSource timeslotDao;

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        Map<String, String> pathParams = (Map<String, String>) input.get(Constants.QUERY_STRING_PARAMS);
        Logger.log(pathParams.toString());
        String calendarId = pathParams.getOrDefault("calendarId", "");
        String dateString = pathParams.getOrDefault("date", "");
        LocalDate localDate = LocalDate.parse(dateString, dtf);

        calendarDao = new JdbcCalendarDao();
        timeslotDao = new JdbcTimeslotDao();

        Calendar calendar = calendarDao.calendarById(calendarId).orElse(null);
        if (calendar == null) return new ApiGatewayResponse(400, "No calendar found with that ID.");

        List<Timeslot> newSlots = generateTimeslots(
                localDate,
                calendar.getStartHour(),
                calendar.getEndHour(),
                calendar.getDuration());

        timeslotDao.insert(calendarId, newSlots);

        return new ApiGatewayResponse(202, gson.toJson(
                new DayResponseData(
                        localDate,
                        newSlots.stream().map(TimeslotResponseData::new).collect(Collectors.toList()),
                        null)));
    }

    private List<Timeslot> generateTimeslots(LocalDate date, LocalTime startHour, LocalTime endHour, int duration) {
        Calendar temp = new Calendar(
                "",
                date,
                date,
                startHour.getHour(),
                endHour.getHour(),
                duration);
        return temp.getTimeslots().entrySet().stream().flatMap(e -> e.getValue().stream()).collect(Collectors.toList());
    }
}
