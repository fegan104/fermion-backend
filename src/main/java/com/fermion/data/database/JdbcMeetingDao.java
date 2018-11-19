package com.fermion.data.database;

import com.fermion.data.model.Meeting;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//SQL Meeting has calId, startTime, dayOf, nameMeet, and location.
//Java Meeting has ID, guest, location

/**
 * Created by @author frankegan on 10/31/18.
 */
public class JdbcMeetingDao implements MeetingDataSource {
	
	Connection conn;
	
	public JdbcMeetingDao() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }
	
	
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

    @Override
    public Optional<Boolean> update(String calId, Meeting meeting) {
        try {
            PreparedStatement ps = conn.prepareStatement("UPDATE meetings SET calId=?, endTime=?, nameMeet=?, location=? WHERE startTime=? AND dayOf=?;");
        	ps.setString(1, calId);
        	ps.setTime(2, Time.valueOf(meeting.getEndTime()));
        	ps.setString(3, meeting.getGuest());     
        	ps.setString(4, meeting.getLocation());
        	ps.setTime(5, Time.valueOf(meeting.getStartTime()));
        	ps.setDate(6, Date.valueOf(meeting.getDay()));

        	int rowsUpdated = ps.executeUpdate();
        	if (rowsUpdated > 0) {
        		//successfully updated
        		return Optional.of(true);
        	}
        	//failed to update
        	return Optional.of(false);
        	
        	
        } catch (Exception e) {
        	e.printStackTrace();
            return Optional.of(false);
        }
    }

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

    @Override
    public Optional<Boolean> deleteByCalendar(String calendarId) {
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM meetings WHERE calId=?;");
            ps.setString(1, calendarId);
            int numAffected = ps.executeUpdate();
            ps.close();

            return Optional.of(numAffected == 1);

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Boolean> insert(String calId, Meeting meeting) {
        try {
//        	PreparedStatement ps = conn.prepareStatement("SELECT * FROM slots WHERE startTime=? AND dayOf=?;");
//            ps.setTime(1, Time.valueOf(meeting.getStartTime()));
//            ps.setDate(2, Date.valueOf(meeting.getDay()));
//            ResultSet resultSet = ps.executeQuery();
//
//            // already present? don't insert it.
//            while (resultSet.next()) {
//                resultSet.close();
//                return Optional.of(false);
//            }

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
