package com.fermion;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fermion.data.database.JdbcCalendarDao;
import com.fermion.data.database.JdbcMeetingDao;
import com.fermion.data.database.JdbcTimeslotDao;
import com.fermion.data.model.Calendar;
import com.fermion.data.model.Meeting;
import com.fermion.data.model.Timeslot;
import com.fermion.data.model.response.ApiGatewayResponse;
import com.fermion.data.model.response.CalendarResponseData;
import com.fermion.util.Constants;
import com.fermion.util.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by @author frankegan on 11/9/18.
 */
public class GetCalendarByIdLambda implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private JdbcCalendarDao calendarDao;
    private JdbcTimeslotDao timeslotDao;
    private JdbcMeetingDao meetingDao;
    private Gson gson;

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        Logger.init(context);

        calendarDao = new JdbcCalendarDao();
        timeslotDao = new JdbcTimeslotDao();
        meetingDao = new JdbcMeetingDao();
        gson = new GsonBuilder().create();

        try {
            Map<String, Object> pathParams = (Map<String, Object>) input.get(Constants.PATH_PARAMS);
            String parsedId = pathParams.get("id").toString();
            Logger.log(parsedId);

            final Calendar calData = calendarDao
                    .calendarById(parsedId)
                    .orElse(null);

            final List<Timeslot> timeslotData = timeslotDao
                    .getByCalendar(parsedId)
                    .orElse(new ArrayList<>());

            final List<Meeting> meetingData = meetingDao
                    .meetingsByCalendar(parsedId)
                    .orElse(new ArrayList<>());

            Logger.log(timeslotData.toString());
            Logger.log(meetingData.toString());

            calData.setTimeslots(timeslotData.stream().collect(Collectors.groupingBy(Timeslot::getDay)));
            calData.setMeetings(meetingData.stream().collect(Collectors.groupingBy(Meeting::getDay)));

            return new ApiGatewayResponse(200, gson.toJson(new CalendarResponseData(calData)));
        } catch (Exception e) {
            context.getLogger().log(e.toString());
            Arrays.asList(e.getStackTrace()).forEach(it -> context.getLogger().log(it.toString()));
            return new ApiGatewayResponse(400, gson.toJson(e.toString()));
        }
    }
}
