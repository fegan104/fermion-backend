package com.fermion.data.database;

import com.fermion.data.model.Timeslot;
import com.fermion.util.Logger;

import javax.annotation.Nullable;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTimeslotDao implements TimeslotDataSource {

    Connection conn;

    public JdbcTimeslotDao() {
        try {
            conn = DatabaseUtil.connect();
        } catch (Exception e) {
            conn = null;
        }
    }

    
    /**
     * Return a list of timeslots that match the given calendarId.
     */
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

    /**
     * Insert a single Timeslot into the database, and return true if successful
     */
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

    /**
     * Insert a list of timeslots into the database, and return true if all timeslots were added correctly
     */
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

    /**
     * Delete a set of timeslots from the database, based on the input criteria (whether DayOfWeek, LocalDate, and LocalTime were given)
     */
    @Override
    public Optional<List<Timeslot>> delete(
            String calendarId,
            @Nullable DayOfWeek dayOfWeek,
            @Nullable LocalDate day,
            @Nullable LocalTime time
    ) {
        try {
            PreparedStatement psSelect = null;
            PreparedStatement psDelete = null;
            StringBuilder query = new StringBuilder();

            if (dayOfWeek == null) {
                if (day == null) {
                    if (time == null) {
                        //No filters nothing happens...
                    } else {
                        query.append(
                                "FROM" +
                                "  slots " +
                                "WHERE" +
                                "  calId=?" +
                                "  AND startTime=?;");
                        psDelete = conn.prepareStatement("DELETE " + query);
                        psSelect = conn.prepareStatement("SELECT * " + query);

                        psDelete.setString(1, calendarId);
                        psSelect.setString(1, calendarId);
                        psDelete.setTime(2, Time.valueOf(time));
                        psSelect.setTime(2, Time.valueOf(time));
                    }
                } else {
                    if (time == null) {
                        query.append(
                                "FROM" +
                                "  slots " +
                                "WHERE" +
                                "  calId=?" +
                                "  AND dayOf=?");
                        psDelete = conn.prepareStatement("DELETE " + query);
                        psSelect = conn.prepareStatement("SELECT * " + query);

                        psDelete.setString(1, calendarId);
                        psSelect.setString(1, calendarId);
                        psDelete.setDate(2, Date.valueOf(day));
                        psSelect.setDate(2, Date.valueOf(day));
                    } else {
                        query.append(
                                "FROM" +
                                "  slots " +
                                "WHERE" +
                                "  calId=?" +
                                "  AND startTime=?" +
                                "  AND dayOf=?;");
                        psDelete = conn.prepareStatement("DELETE " + query);
                        psSelect = conn.prepareStatement("SELECT * " + query);

                        psDelete.setString(1, calendarId);
                        psSelect.setString(1, calendarId);
                        psDelete.setTime(2, Time.valueOf(time));
                        psSelect.setTime(2, Time.valueOf(time));
                        psDelete.setDate(3, Date.valueOf(day));
                        psSelect.setDate(3, Date.valueOf(day));
                    }
                }
            } else {
                if (day == null) {
                    if (time == null) {
                        query.append(
                                "FROM" +
                                "  slots " +
                                "WHERE" +
                                "  dayOfWeek=?" +
                                "  AND calId=?;");
                        psDelete = conn.prepareStatement("DELETE " + query);
                        psSelect = conn.prepareStatement("SELECT * " + query);

                        psDelete.setString(1, dayOfWeek.toString().substring(0, 2));
                        psSelect.setString(1, dayOfWeek.toString().substring(0, 2));
                        psDelete.setString(2, calendarId);
                        psSelect.setString(2, calendarId);
                    } else {
                        query.append(
                                "FROM" +
                                "  slots " +
                                "WHERE" +
                                "  calId=?" +
                                "  AND startTime=?" +
                                "  OR (dayOfWeek = ? AND calId = ?);");
                        psDelete = conn.prepareStatement("DELETE " + query);
                        psSelect = conn.prepareStatement("SELECT * " + query);

                        psDelete.setString(1, calendarId);
                        psSelect.setString(1, calendarId);
                        psDelete.setTime(2, Time.valueOf(time));
                        psSelect.setTime(2, Time.valueOf(time));
                        psDelete.setString(3, dayOfWeek.toString().substring(0, 2));
                        psSelect.setString(3, dayOfWeek.toString().substring(0, 2));
                        psDelete.setString(4, calendarId);
                        psSelect.setString(4, calendarId);
                    }
                } else {
                    if (time == null) {
                        query.append(
                                "FROM" +
                                "  slots " +
                                "WHERE" +
                                "  calId=?" +
                                "  AND dayOf=?" +
                                "  OR (dayOfWeek=? AND calId=?);");
                        psDelete = conn.prepareStatement("DELETE " + query);
                        psSelect = conn.prepareStatement("SELECT * " + query);

                        psDelete.setString(1, calendarId);
                        psSelect.setString(1, calendarId);
                        psDelete.setDate(2, Date.valueOf(day));
                        psSelect.setDate(2, Date.valueOf(day));
                        psDelete.setString(3, dayOfWeek.toString().substring(0, 2));
                        psSelect.setString(3, dayOfWeek.toString().substring(0, 2));
                        psDelete.setString(4, calendarId);
                        psSelect.setString(4, calendarId);
                    } else {
                        query.append(
                                "FROM" +
                                "  slots " +
                                "WHERE" +
                                "  calId = ?" +
                                "  AND startTime = ?" +
                                "  AND dayOf = ?" +
                                "  OR (dayOfWeek = ? AND calId = ?);");

                        psDelete = conn.prepareStatement("DELETE " + query);
                        psSelect = conn.prepareStatement("SELECT * " + query);

                        psDelete.setString(1, calendarId);
                        psSelect.setString(1, calendarId);
                        psDelete.setTime(2, Time.valueOf(time));
                        psSelect.setTime(2, Time.valueOf(time));
                        psDelete.setDate(3, Date.valueOf(day));
                        psSelect.setDate(3, Date.valueOf(day));
                        psDelete.setString(4, dayOfWeek.toString().substring(0, 2));
                        psSelect.setString(4, dayOfWeek.toString().substring(0, 2));
                        psDelete.setString(5, calendarId);
                        psSelect.setString(5, calendarId);
                    }
                }
            }
            List<Timeslot> closedTimeslots = new ArrayList<>();
            Logger.log("SELECT * " + query.toString());
            ResultSet timeslotQuery = psSelect.executeQuery();

            //Add all the soon-to-be deleted timeslots to the result
            while (timeslotQuery.next()) {
                closedTimeslots.add(new Timeslot(
                        timeslotQuery.getString("slotId"),
                        timeslotQuery.getDate("dayOf").toLocalDate(),
                        timeslotQuery.getTime("startTime").toLocalTime(),
                        timeslotQuery.getTime("endTime").toLocalTime()
                ));
            }

            psDelete.execute();
            psSelect.close();
            psDelete.close();

            return Optional.of(closedTimeslots);

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Return a java Timeslot based on the resultSet returned from RDS
     */
    private Timeslot generateTimeslot(ResultSet resultSet) throws Exception {
        Time startTime = resultSet.getTime("startTime");
        Time endTime = resultSet.getTime("endTime");
        Date date = resultSet.getDate("dayOf");
        String slotId = resultSet.getString("slotId");

        return new Timeslot(slotId, date.toLocalDate(), startTime.toLocalTime(), endTime.toLocalTime());
    }

}
