package com.fermion.data.database;

import com.fermion.data.model.Calendar;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by @author frankegan on 10/31/18.
 */
public class JdbcCalendarDao implements CalendarDataSource {

	java.sql.Connection conn;

	public JdbcCalendarDao() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }


    @Override
    public Optional<Calendar> calendarById(String id) {
        try {
        	Calendar cal = null;
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM calendars WHERE id=?;");
            ps.setString(1, id);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                cal = generateCalendar(resultSet);
            }

            //populate the meetings and timeslots
            PreparedStatement ps2 = conn.prepareStatement(
            		"SELECT * FROM calendars "
            		+ "LEFT JOIN slots ON " + id + " = slots.calId "
            		+ "LEFT JOIN meetings ON " + id + " = meetings.calId");
            //returns a set of mega-rows that have some combo of calendar, timeslot and meeting output
            resultSet = ps.executeQuery();

            while (resultSet.next()) {
                //TODO new function to add each row to the output
            	populateSlot(resultSet, cal);
}
            resultSet.close();
            ps.close();

            if (cal == null) {
                throw new Exception("Calendar not found");
            }

            return Optional.of(cal);

        } catch (Exception e) {
        	e.printStackTrace();
            return Optional.empty();
        }

    }


	@Override
    public Optional<List<Calendar>> getAll() {
        try {
        	List<Calendar> list = new ArrayList<Calendar>(); //I arbitrarily picked arraylist, we can use something else
        	Calendar cal = null;
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM calendars;");
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                cal = generateCalendar(resultSet);
                list.add(cal);
            }
            resultSet.close();
            ps.close();

            if (cal == null) {
                throw new Exception("Calendar not found");
            }

            return Optional.of(list);

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


    //TODO Do we need an "Update" calendar option?


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
            ps.setString(2, "admin"); //unsure how to "get owner"
            ps.setString(3, "calendar name"); //unsure how to "get calendar name"
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


    private Calendar generateCalendar(ResultSet resultSet) throws Exception {
        String id  = resultSet.getString("id");
        String ownerName  = resultSet.getString("ownerName");
        String calName  = resultSet.getString("calName");
        int startHr = resultSet.getInt("startHr");
        int endHr = resultSet.getInt("endHr");
        int duration = resultSet.getInt("duration");

        Calendar cal = new Calendar(id, ownerName, calName, startHr, endHr, duration);

        //TODO add the timeslots and meetings to the calendar
        return cal;
    }

    private void populateSlot(ResultSet resultSet, Calendar cal) {


	}



}


