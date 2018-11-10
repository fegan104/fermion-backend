package com.fermion.data.database;

import com.fermion.data.model.Calendar;
import com.fermion.data.model.Meeting;
import com.fermion.data.model.Timeslot;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            conn = null;
        }
    }


    @Override
    public Optional<Calendar> calendarById(String calendarId) {
        try {
            Calendar calendarResult;
            //populate the meetings and timeslots
            PreparedStatement ps2 = conn.prepareStatement(calendarJoinQuery(calendarId));
            //returns a set of mega-rows that have some combo of calendar, timeslot and meeting output
            ResultSet resultSet = ps2.executeQuery();
            calendarResult = generateCalendar(resultSet);

            resultSet.close();
            ps2.close();

            return Optional.of(calendarResult);

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }

    }


    @Override
    public Optional<List<Calendar>> getAll() {
        try {
            List<Calendar> calendars = new ArrayList<>();
            PreparedStatement ps = conn.prepareStatement("SELECT id FROM calendars;");
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) calendars.add(generateCalendar(resultSet));

            resultSet.close();
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
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM calendars WHERE id = ?;");
            ps.setString(1, calendar.getId());
            ResultSet resultSet = ps.executeQuery();

            // already present?
            while (resultSet.next()) {
                Calendar c = generateCalendar(resultSet);
                resultSet.close();
                return Optional.of(false);
            }

            ps = conn.prepareStatement("INSERT INTO calendars (id,ownerName,calName,startHr,endHr,duration) values(?,?,?,?,?,?);");
            ps.setString(1, calendar.getId());
            ps.setString(2, "fermion"); //unsure how to "get owner"
            ps.setString(3, calendar.getName());
            ps.setTime(4, Time.valueOf(calendar.getStartHour()));
            ps.setTime(5, Time.valueOf(calendar.getEndHour()));
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

    private String calendarJoinQuery(String calendarId) {
        return "SELECT" +
                "  id," +
                "  calName," +
                "  meeting.startHr AS meetingStartHr," +
                "  meeting.endHr AS meetingEndHr," +
                "  meeting.dayOf AS meetingDayOf," +
                "  slot.startHr AS slotStartHr," +
                "  slot.endHr AS slotEndHr," +
                "  slot.dayOf AS slotDayOf," +
                "  nameMeet," +
                "  location," +
                "  dayOfWeek," +
                "  slotId" +
                "FROM calendars" +
                "LEFT JOIN slots ON " + calendarId + " = slots.calId" +
                "LEFT JOIN meetings ON " + calendarId + " = meetings.calId";
    }

}


