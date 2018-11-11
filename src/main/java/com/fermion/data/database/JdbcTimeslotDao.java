package com.fermion.data.database;

import com.fermion.data.model.Calendar;
import com.fermion.data.model.Timeslot;

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
    	try  {
    		conn = DatabaseUtil.connect();
    	} catch (Exception e) {
    		conn = null;
    	}
    }


    @Override
    public Optional<Timeslot> timeslotById(String id) {
        try {
            Timeslot timeslotResult;
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM slots WHERE slotId = ?;");
            ps.setString(1, id);
            ResultSet resultSet = ps.executeQuery();
            timeslotResult = generateTimeslot(resultSet);

            resultSet.close();
            ps.close();

            return Optional.of(timeslotResult);

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<Timeslot>> getByCalendar(String calendarId) {
        try {
            Timeslot timeslotResult;
            List<Timeslot> timeslotList = new ArrayList<Timeslot>();
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
    public Optional<List<Timeslot>> getFrom(LocalDate from, LocalDate to) {
        try {
            Timeslot timeslotResult;
            List<Timeslot> timeslotList = new ArrayList<Timeslot>();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM slots WHERE (dayOf BETWEEN ? AND ?);");
            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));
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
    public Optional<List<Timeslot>> getFrom(LocalTime from, LocalTime to) {
        try {
            Timeslot timeslotResult;
            List<Timeslot> timeslotList = new ArrayList<Timeslot>();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM slots WHERE ((startTime > ?) AND (endTime < ?));");
            ps.setTime(1, Time.valueOf(from));
            ps.setTime(2, Time.valueOf(to));
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
    public Optional<Boolean> insert(Timeslot timeslot) {
        try {
        	PreparedStatement ps = conn.prepareStatement("SELECT * FROM slots WHERE SlotId = ?;");
            ps.setString(1, timeslot.getId());
            ResultSet resultSet = ps.executeQuery();
            
            // already present? don't insert it.
            while (resultSet.next()) {
                Timeslot t = generateTimeslot(resultSet);
                resultSet.close();
                return Optional.of(false);
            }

            ps = conn.prepareStatement("INSERT INTO slots (calId,startTime,dayOf,dayOfWeek,slotId) values(?,?,?,?,?);");
            ps.setString(1, "calendar ID");//At the moment, the timeslot doesn't know its calendar ID
            ps.setTime(2, Time.valueOf(timeslot.getStartTime()));
            ps.setDate(3, Date.valueOf(timeslot.getDay())); 
            ps.setString(4, "Day Of Week"); //at the moment, timeslot doesn't know its dayOfWeek
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
            //if a timeslot breaks for any one timeslot, change the return value to false
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
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM slots WHERE slotId = ?;");
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
    public Optional<Boolean> delete(DayOfWeek dayOfWeek, LocalDate day, LocalTime time) {
        return Optional.empty();
        //TODO am I going to delete if the dayOfWeek AND date AND time match? Or should I skip each field if it is null?
    }
    
    private Timeslot generateTimeslot(ResultSet resultSet) throws Exception {
        Time startTime = resultSet.getTime("startTime");
        Time endTime = resultSet.getTime("startTime");
        Date date  = resultSet.getDate("dayOf");
        String slotId = resultSet.getString("slotId");
        
        return new Timeslot(slotId, date.toLocalDate(), startTime.toLocalTime(), endTime.toLocalTime());
    }

}
