package com.fermion.data.database;

import com.fermion.data.model.Timeslot;

import javax.annotation.Nullable;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by @author frankegan on 10/31/18.
 */
public class JdbcTimeslotDao implements TimeslotDataSource {

    Connection conn;

    public JdbcTimeslotDao() {
        try {
            conn = DatabaseUtil.connect();
        } catch (Exception e) {
            conn = null;
        }
    }

    @Override
    public Optional<List<Timeslot>> getByCalendar(String calendarId) {
        try {
            Timeslot timeslotResult;
            List<Timeslot> timeslotList = new ArrayList<>();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM slots WHERE calId = ?;");
            ps.setString(1, calendarId);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                timeslotResult = generateTimeslot(resultSet);
                timeslotList.add(timeslotResult);
            }

            resultSet.close();
            ps.close();

            return Optional.of(timeslotList);

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Boolean> insert(String calId, Timeslot timeslot) {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM slots WHERE slotId = ?;");
            ps.setString(1, timeslot.getId());
            ResultSet resultSet = ps.executeQuery();

            // already present? don't insert it.
            while (resultSet.next()) {
                resultSet.close();
                return Optional.of(false);
            }

            String weekDay = timeslot.getDay().getDayOfWeek().name().substring(0, 2); //This should return a 2-character string, like "SU, MO, etc"

            ps = conn.prepareStatement("INSERT INTO slots (calId,startTime,endTime,dayOf,dayOfWeek,slotId) values(?,?,?,?,?,?);");
            ps.setString(1, calId);
            ps.setTime(2, Time.valueOf(timeslot.getStartTime()));
            ps.setTime(3, Time.valueOf(timeslot.getEndTime()));
            ps.setDate(4, Date.valueOf(timeslot.getDay()));
            ps.setString(5, weekDay);
            ps.setString(6, timeslot.getId());
            ps.execute();
            return Optional.of(true);

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }

    }

    @Override
    public Optional<Boolean> insert(String calId, List<Timeslot> timeslots) {
        boolean success = true;

        for (Timeslot t : timeslots) {
            //if a timeslot breaks for any one timeslot, change the return value to false
            Optional<Boolean> insertOptional = insert(calId, t);
            if (insertOptional.isPresent() && insertOptional.get()) success = false;
        }
        return Optional.of(success);
    }

    /**
     * Delete every timeslot with the given calendarId, and returns true if there was no exception.
     */
    @Override
    public Optional<Boolean> deleteByCalendar(String calendarId) {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM slots WHERE calId = ?;");
            ps.setString(1, calendarId);
            int numAffected = ps.executeUpdate();
            ps.close();

            return Optional.of(true);

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<Timeslot>> delete(
            @Nullable DayOfWeek dayOfWeek,
            @Nullable LocalDate day,
            @Nullable LocalTime time
    ) {
        try {
            PreparedStatement psSelect;
            PreparedStatement psDelete;
//            if (dayOfWeek == null)
//                PreparedStatement psRead = conn.prepareStatement("SELECT * FROM slots WHERE dayOf = ? OR date = ? AND startTime = ?;");

            PreparedStatement ps = conn.prepareStatement("DELETE FROM slots WHERE dayOf = ? AND startTime = ?;");
            ps.setDate(1, Date.valueOf(day));
            ps.setTime(2, Time.valueOf(time));
            int numAffected = ps.executeUpdate();
            ps.close();

            return Optional.empty();

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private Timeslot generateTimeslot(ResultSet resultSet) throws Exception {
        Time startTime = resultSet.getTime("startTime");
        Time endTime = resultSet.getTime("endTime");
        Date date = resultSet.getDate("dayOf");
        String slotId = resultSet.getString("slotId");

        return new Timeslot(slotId, date.toLocalDate(), startTime.toLocalTime(), endTime.toLocalTime());
    }

    public Optional<List<Timeslot>> getAll() {
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM slots;");
            List<Timeslot> timeslots = new ArrayList<Timeslot>();
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) timeslots.add(generateTimeslot(resultSet));

            resultSet.close();
            ps.close();

            return Optional.of(timeslots);

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

}
