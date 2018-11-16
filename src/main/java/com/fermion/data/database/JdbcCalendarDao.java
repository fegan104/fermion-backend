package com.fermion.data.database;

import com.fermion.data.model.Calendar;
import com.fermion.data.model.Meeting;
import com.fermion.data.model.Timeslot;
import com.fermion.logger.Logger;

import java.sql.*;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

/**
 * Created by @author frankegan on 10/31/18.
 */
public class JdbcCalendarDao implements CalendarDataSource {

    Connection conn;

    public JdbcCalendarDao() {
        try {
            conn = DatabaseUtil.connect();
        } catch (Exception e) {
            Logger.log(e.getMessage());
            Arrays.asList(e.getStackTrace()).forEach(it ->
                    Logger.log(it.toString())
            );
            conn = null;
        }
    }


    @Override
    public Optional<Calendar> calendarById(String calendarId) {
        try {
            Calendar calendarResult = null;
            //populate the meetings and timeslots
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM calendars WHERE id = ?;");//calendarJoinQuery(calendarId);
            ps.setString(1, calendarId);
            //returns a set of mega-rows that have some combo of calendar, timeslot and meeting output
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                calendarResult = generateCalendar(resultSet);
            }

            resultSet.close();
            ps.close();

            return Optional.of(calendarResult);

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }

    }


    /**
     * Experimenting with just returning calendar top level data not it's nested info.
     *
     * @return
     */
    @Override
    public Optional<List<Calendar>> getAll() {
        try {
            List<Calendar> calendars = new ArrayList<>();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM calendars;");
            ResultSet calQuery = ps.executeQuery();

            while (calQuery.next()) {
                String id = calQuery.getString("id");
                String calName = calQuery.getString("calName");
                calendars.add(new Calendar(id, calName, new HashMap<>(), new HashMap<>()));
            }

            calQuery.close();
            ps.close();

            return Optional.of(calendars);

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Boolean> delete(String id) {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM calendars WHERE id = ?;");
            ps.setString(1, id);
            int numAffected = ps.executeUpdate();
            ps.close();

            return Optional.of(numAffected == 1);

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }


    @Override
    public Optional<Boolean> insert(Calendar calendar) {
        if (conn == null) {
            Logger.log("No connection to database available");
            return Optional.of(false);
        }
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM calendars WHERE id = ?;");
            ps.setString(1, calendar.getId());
            ResultSet resultSet = ps.executeQuery();

            // already present?
            if (resultSet.next()) {
                resultSet.close();
                return Optional.of(false);
            }

            ps = conn.prepareStatement("INSERT INTO calendars (id,ownerName,calName,startHr,endHr,duration) values(?,?,?,?,?,?);");
            ps.setString(1, calendar.getId());
            ps.setString(2, "fermion"); //unsure how to "get owner"
            ps.setString(3, calendar.getName());
            ps.setInt(4, calendar.getStartHour().getHour());
            ps.setInt(5, calendar.getEndHour().getHour());
            ps.setInt(6, calendar.getDuration());
            ps.execute();
            return Optional.of(true);

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }


    /**
     * Returns an {@link Calendar} model with all the appropriate timeslots and meetings populated.
     */
    private Calendar generateCalendar(ResultSet resultSet) throws SQLException {
        String calendarId = resultSet.getString("id");
        String calName = resultSet.getString("calName");

        List<Timeslot> timeslots = new ArrayList<>();
        List<Meeting> meetings = new ArrayList<>();
        while (resultSet.next()) {
            //Add slot data from this row to the list
            if (resultSet.getString("slotId") != null) {
                timeslots.add(new Timeslot(
                        resultSet.getString("slotId"),
                        resultSet.getDate("slotDayOf").toLocalDate(),
                        resultSet.getTime("slotStartHr").toLocalTime(),
                        resultSet.getTime("slotEndHr").toLocalTime()));
            }
            //Add meeting data from this row to the list
            if (resultSet.getString("nameMeet") != null) {
                meetings.add(new Meeting(
                        resultSet.getTime("meetingStartHr").toLocalTime(),
                        resultSet.getTime("meetingEndHr").toLocalTime(),
                        resultSet.getDate("meetingDayOf").toLocalDate(),
                        resultSet.getString("nameMeet"),
                        resultSet.getString("location")));
            }

        }

        return new Calendar(
                calendarId,
                calName,
                timeslots.stream().collect(groupingBy(Timeslot::getDay)),
                meetings.stream().collect(groupingBy(Meeting::getDay))
        );
    }

    private PreparedStatement calendarJoinQuery(String calendarId) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(
                    "SELECT id, calName, meetings.startTime AS meetingStartHr, meetings.endTime AS meetingEndHr, meetings.dayOf AS meetingDayOf, slots.startTime AS slotStartHr, slots.endTime AS slotEndHr, slots.dayOf AS slotDayOf, nameMeet, location, dayOfWeek, slotId" +
                            "FROM calendars LEFT JOIN slots ON id = slots.calId LEFT JOIN meetings ON meetings.startTime = slots.startTime AND meetings.dayOf = slots.dayOf AND meetings.calId = id" +
                            "WHERE id = ?;");
            ps.setString(1, calendarId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ps;
    }

}


