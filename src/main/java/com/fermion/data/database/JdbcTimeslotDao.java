package com.fermion.data.database;

import com.fermion.data.model.Timeslot;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by @author frankegan on 10/31/18.
 */
public class JdbcTimeslotDao implements TimeslotDataSource {
	
	Connection conn;
	
	public JdbcTimeslotDao() {
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }


    @Override
    public Optional<Timeslot> timeslotById(String id) {
    	
        return Optional.empty();
    }

    @Override
    public Optional<List<Timeslot>> getByCalendar(String calendarId) {
    	
        return Optional.empty();
    }

    @Override
    public Optional<List<Timeslot>> getFrom(LocalDate from, LocalDate to) {
    	
        return Optional.empty();
    }

    @Override
    public Optional<List<Timeslot>> getFrom(LocalTime from, LocalTime to) {
    	
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> insert(Timeslot timeslot) {
        try {
        	PreparedStatement ps = conn.prepareStatement("SELECT * FROM Timeslots WHERE SlotId = ?;");
            ps.setString(1, timeslot.getId());
            ResultSet resultSet = ps.executeQuery();
            
            // already present?
            while (resultSet.next()) {
                Timeslot t = generateTimeslot(resultSet);
                resultSet.close();
                return Optional.of(false);
            }

            ps = conn.prepareStatement("INSERT INTO slots (calId,startTime,dayOf,dayOfWeek,slotId) values(?,?,?,?,?);");
            ps.setString(1, "calendar ID");
            ps.setTime(2, Time.valueOf(timeslot.getStartTime()));
            ps.setDate(3, Date.valueOf(timeslot.getDay()));
            ps.setString(4, "Day Of Week");
            ps.setString(5, timeslot.getId());
            ps.execute();
            return Optional.of(true);

        } catch (Exception e) {
        	e.printStackTrace();
            return Optional.empty();
        }

    }

    @Override
    public Optional<Boolean> insert(Timeslot... timeslots) {
    	boolean success = true;
    	
    	for (Timeslot t : timeslots) {
            //if a timeslot breaks, change the return value to false
    	    Optional<Boolean> insertOptional =  insert(t);
    		if (insertOptional.isPresent() && insertOptional.get()) success = false;
    	}
        return Optional.of(success);
    }

    @Override
    public Optional<Boolean> update(Timeslot timeslot) {
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> delete(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> delete(DayOfWeek dayOfWeek, LocalDate day, LocalTime time) {
        return Optional.empty();
    }
    
    private Timeslot generateTimeslot(ResultSet resultSet) throws Exception {
        Time startTime = resultSet.getTime("startTime");
        Time endTime = resultSet.getTime("startTime");
        Date date  = resultSet.getDate("dayOf");
        String slotId = resultSet.getString("slotId");
        
        return new Timeslot(slotId, date.toLocalDate(), startTime.toLocalTime(), endTime.toLocalTime());
    }

}
