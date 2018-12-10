package com.fermion.data.database;

import com.fermion.data.model.Calendar;
import com.fermion.data.model.Meeting;
import com.fermion.data.model.Timeslot;
import com.fermion.util.Logger;

import java.sql.*;
import java.time.LocalTime;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

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


    
    /**
    Return the calendar with the specified calendarId, if it exists.
    */
    @Override
    public Optional<Calendar> calendarById(String calendarId) {
        try {
            Calendar calendarResult = null;
            //populate the meetings and timeslots
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM calendars WHERE id = ?;");
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
     * Returns a list of Calendars, without their timeslots. Used for the calendar selection page.
     *
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
                int duration = calQuery.getInt("duration");
                int startHr = calQuery.getInt("startHr");
                int endHr = calQuery.getInt("endHr");

                calendars.add(new Calendar(id, calName,
                		LocalTime.of((int) startHr, 0, 0),
                        LocalTime.of((int) endHr, 0, 0),
                		duration, new HashMap<>(), new HashMap<>()));
            }

            calQuery.close();
            ps.close();

            return Optional.of(calendars);

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Delete the Calendar with the specified name, if it exists. The timeslots and meetings of this calendar must be deleted separately.
     *
     */
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
        long startHr = resultSet.getLong("startHr");
        long endHr = resultSet.getLong("endHr");
        int duration = resultSet.getInt("duration");

        List<Timeslot> timeslots = new ArrayList<>();
        List<Meeting> meetings = new ArrayList<>();
        while (resultSet.next()) {
            //Add slot data from this row to the list
            if (resultSet.getString("slotId") != null) {
                timeslots.add(new Timeslot(
                        resultSet.getString("slotId"),
                        resultSet.getDate("dayOf").toLocalDate(),
                        resultSet.getTime("startHr").toLocalTime(),
                        resultSet.getTime("endHr").toLocalTime()));
            }
            //Add meeting data from this row to the list
            if (resultSet.getString("nameMeet") != null) {
                meetings.add(new Meeting(
                        resultSet.getTime("startHr").toLocalTime(),
                        resultSet.getTime("endHr").toLocalTime(),
                        resultSet.getDate("dayOf").toLocalDate(),
                        resultSet.getString("nameMeet"),
                        resultSet.getString("location")));
            }

        }

        return new Calendar(
                calendarId,
                calName,
                LocalTime.of((int) startHr, 0, 0),
                LocalTime.of((int) endHr, 0, 0),
                duration,
                timeslots.stream().collect(groupingBy(Timeslot::getDay)),
                meetings.stream().collect(groupingBy(Meeting::getDay))
        );
    }
}


