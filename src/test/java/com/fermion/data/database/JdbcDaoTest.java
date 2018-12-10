package com.fermion.data.database;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;
import com.fermion.data.model.Calendar;
import com.fermion.data.model.Timeslot;

public class JdbcDaoTest {

	@Test
	public void testGetAll() {
	    JdbcCalendarDao cd = new JdbcCalendarDao();
	    try {
	    	Optional<List<Calendar>> calList = cd.getAll();
	    	for (Calendar c : calList.get()) {
		    	System.out.println(c.toString());
	    	}
	    } catch (Exception e) {
	    	fail ("didn't work:" + e.getMessage());
	    }
	}
	
	@Test
	public void testCreateGetDelete() {
	    JdbcCalendarDao cd = new JdbcCalendarDao();
	    JdbcTimeslotDao td = new JdbcTimeslotDao();
	    
	    try {
	    	// can add it
	        Calendar calendar = new Calendar(
	                "JdbcDaoTest1",
	                LocalDate.of(2018, 11, 20),
	                LocalDate.of(2018, 11, 22),
	                9,
	                12,
	                10);
	    	Optional<Boolean> b = cd.insert(calendar);
	    	System.out.println("add Calendar: " + b);
	    	
	    	// can retrieve it
	    	Optional<Calendar> c2 = cd.calendarById(calendar.getId());
	    	System.out.println("get Calendar:" + c2.get().getName());

	    	// can delete it
	    	assertTrue ((cd.delete(calendar.getId())).get()); //delete calendar
	    	System.out.println("deleted Calendar: " + c2.get().getName());
	    	assertTrue ((td.deleteByCalendar(c2.get().getId())).get()); //delete the calendar's orphaned timeslots
	    	System.out.println("deleted Calendar: " + c2.get().getName() + "'s timeslots");
	    	assertEquals (0, ((td.getByCalendar(c2.get().getId())).get().size())); //ensure that the database has no more copies of that timeslot
	    	
	    } catch (Exception e) {
	    	fail ("didn't work:" + e.getMessage());
	    }
	}


/*Meeting tests*/
	@Test
	public void testScheduleCancelMeeting() {
	    JdbcCalendarDao cd = new JdbcCalendarDao();
	    JdbcMeetingDao md = new JdbcMeetingDao();
	    JdbcTimeslotDao td = new JdbcTimeslotDao();	    
	    try {
	    	// calendar setup
	        Calendar calendar = new Calendar(
	                "JdbcDaoTest2",
	                LocalDate.of(2018, 12, 01),
	                LocalDate.of(2018, 12, 02),
	                14,
	                15,
	                30);
	    	Optional<Boolean> b = cd.insert(calendar);
	    	System.out.println("add Calendar: " + b);
	    	
	    	//Meeting tests
	    	
	    	
	    	
	    	
	    	
	    	// delete calendar to restore database state
	    	assertTrue ((cd.delete(calendar.getId())).get()); //delete calendar
	    	System.out.println("deleted Calendar: " + calendar.getName());
	    	assertTrue ((td.deleteByCalendar(calendar.getId())).get()); //delete the calendar's orphaned timeslots
	    	System.out.println("deleted Calendar: " + calendar.getName() + "'s timeslots");
	    } catch (Exception e) {
	    	fail ("didn't work:" + e.getMessage());
	    }
	}


	@Test
	public void testTimeslotCreateDelete() {
	    JdbcCalendarDao cd = new JdbcCalendarDao();
	    JdbcTimeslotDao td = new JdbcTimeslotDao();
	    
	    try {
	    	// can add it
	        Calendar calendar = new Calendar(
	                "JdbcDaoTest3",
	                LocalDate.of(2018, 11, 20),
	                LocalDate.of(2018, 11, 22),
	                12,
	                13,
	                20);
	    	Optional<Boolean> b = cd.insert(calendar);
	    	System.out.println("add Calendar: " + b);
	    	
	    	//can generate the proper timeslots
            td.insert(calendar.getId(), calendar.getTimeslots()
                    .values()
                    .stream()
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList()));
	    	
	    	// can retrieve it
	    	Optional<Calendar> c2 = cd.calendarById(calendar.getId());
	    	System.out.println("get Calendar:" + c2.get().getName());
	    	
	    	//test how many timeslots there are (3 days * 3 per day = 9)
	    	List<Timeslot> timeslotList = td.getByCalendar(calendar.getId()).get();
	    	assertEquals (9, ((timeslotList.size())));
	    	
	    	//now delete weekdays, daily, one slot, and all day
	    	
	    	// can delete it
	    	assertTrue ((cd.delete(calendar.getId())).get()); //delete calendar
	    	System.out.println("deleted Calendar: " + c2.get().getName());
	    	assertTrue ((td.deleteByCalendar(c2.get().getId())).get()); //delete the calendar's orphaned timeslots
	    	System.out.println("deleted Calendar: " + c2.get().getName() + "'s timeslots");
	    	assertEquals (0, ((td.getByCalendar(c2.get().getId())).get().size())); //ensure that the database has no more copies of that timeslot
	    	
	    } catch (Exception e) {
	    	fail ("didn't work:" + e.getMessage());
	    }
	}


}

