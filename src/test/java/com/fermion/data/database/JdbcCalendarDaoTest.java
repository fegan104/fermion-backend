package com.fermion.data.database;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import com.fermion.data.model.Calendar;

public class JdbcCalendarDaoTest {

@Test
	public void testGetAll() {
	    JdbcCalendarDao cd = new JdbcCalendarDao();
	    try {
	    	Optional<List<Calendar>> calList = cd.getAll();
	    	for (Calendar c : calList.get()) {
		    	System.out.println("Calendar :" + c.getName() + ", " + c.getStartDay() + ", " + c.getEndDay() + ", " + c.getStartHour() + ", " + c.getEndHour() + ", " + c.getDuration() + ", " + c.getId());
	    	}
	    } catch (Exception e) {
	    	fail ("didn't work:" + e.getMessage());
	    }
	}
	
	@Test
	public void testCreateGetDelete() {
	    JdbcCalendarDao cd = new JdbcCalendarDao();
	    try {
	    	// can add it
	        Calendar calendar = new Calendar(
	                "Personal",
	                LocalDate.of(2018, 11, 20),
	                LocalDate.of(2018, 11, 22),
	                12,
	                13,
	                20);
	    	Optional<Boolean> b = cd.insert(calendar);
	    	System.out.println("add Calendar: " + b);
	    	
	    	// can retrieve it
	    	Optional<Calendar> c2 = cd.calendarById(calendar.getId());
	    	System.out.println("get Calendar:" + c2.get().getName());
	    	
	    	// can delete it
	    	assertTrue ((cd.delete(calendar.getId())).get());
	    } catch (Exception e) {
	    	fail ("delete failed:" + e.getMessage());
	    }
	}
}