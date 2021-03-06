package com.fermion.data.database;

import com.fermion.data.model.Meeting;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMeetingDao implements MeetingDataSource {
	
	Connection conn;
	
	public JdbcMeetingDao() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }
	
	
	/**
	 * Returns a list of meetings that match the given calendarId
	 */
    @Override
    public Optional<List<Meeting>> meetingsByCalendar(String calendarId) {
    	try {
    		PreparedStatement ps = conn.prepareStatement("SELECT * FROM meetings WHERE calId = ?;");
    		ps.setString(1, calendarId);
            ResultSet resultSet = ps.executeQuery();
    		List<Meeting> meetings = new ArrayList<>();
    		while (resultSet.next()) {
                meetings.add(new Meeting(
                        resultSet.getTime("startTime").toLocalTime(),
                        resultSet.getTime("endTime").toLocalTime(),
                        resultSet.getDate("dayOf").toLocalDate(),
                        resultSet.getString("nameMeet"),
                        resultSet.getString("location")));
            }

            return Optional.of(meetings);
            
    	} catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }

    }

    /**
     * Delete the meeting in calendarId that matches the given date and time. Returns true if a meeting was deleted.
     */
    @Override
    public Optional<Boolean> delete(String calendarId, LocalDate date, LocalTime time) {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM meetings WHERE calId=? AND startTime=? AND dayOf=?;");
            ps.setString(1, calendarId);
            ps.setTime(2, Time.valueOf(time));
            ps.setDate(3, Date.valueOf(date));
            int numAffected = ps.executeUpdate();
            ps.close();

            return Optional.of(numAffected == 1);

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }        
    }

    /**
     * Delete every meeting in calendarId on the specified date. Return true if no exception.
     */
    @Override
    public Optional<Boolean> delete(String calendarId, LocalDate date) {
    	try {
	        PreparedStatement ps = conn.prepareStatement("DELETE FROM meetings WHERE calId=? AND dayOf=?;");
	        ps.setString(1, calendarId);
	        ps.setDate(2, Date.valueOf(date));
	        ps.executeUpdate();
	        ps.close();
	
	        return Optional.of(true);
	
	    } catch (Exception e) {
	        e.printStackTrace();
	        return Optional.empty();
	    }
    }        


    /**
     * Delete every meeting in the calendar specified by calendarId, and return true if no exception occurs
     */
@Override
    public Optional<Boolean> deleteByCalendar(String calendarId) {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM meetings WHERE calId=?;");
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
     * Insert one meeting into the database, and return true if successful
     */
    @Override
    public Optional<Boolean> insert(String calId, Meeting meeting) {
        try {

            PreparedStatement ps = conn.prepareStatement("INSERT INTO meetings (calId,startTime,endTime,dayOf,nameMeet,location) values(?,?,?,?,?,?);");
            ps.setString(1, calId);
            ps.setTime(2, Time.valueOf(meeting.getStartTime()));
            ps.setTime(3, Time.valueOf(meeting.getEndTime()));
            ps.setDate(4, Date.valueOf(meeting.getDay())); 
            ps.setString(5, meeting.getGuest());
            ps.setString(6, meeting.getLocation());
            ps.execute();
            return Optional.of(true);

        } catch (Exception e) {
        	e.printStackTrace();
            return Optional.empty();
        }
    }
}
