package com.fermion.data.database;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;
import com.fermion.data.model.Calendar;
import com.fermion.data.model.Meeting;
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
	                LocalDate.of(2018, 12, 06),
	                LocalDate.of(2018, 12, 12),
	                14,
	                15,
	                30);
	    	Optional<Boolean> b = cd.insert(calendar);
	    	System.out.println("add Calendar: " + b);
	    	//generate timeslots
	    	td.insert(calendar.getId(), calendar.getTimeslots()
                    .values()
                    .stream()
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList()));

	    	
	    	//Meeting tests	    	
	    	
	    	Meeting m1 = new Meeting(LocalTime.of(14,00), LocalTime.of(14,30), LocalDate.of(2018, 12, 10), "Blossom", "Work");
	    	Meeting m2 = new Meeting(LocalTime.of(14,00), LocalTime.of(14,30), LocalDate.of(2018, 12, 07), "Bubbles", "Home");
	    	Meeting m3 = new Meeting(LocalTime.of(14,30), LocalTime.of(15,00), LocalDate.of(2018, 12, 10), "Buttercup", "School");
	    	//insert 3 meetings
	    	assertTrue ((md.insert(calendar.getId(), m1)).get());
	    	assertTrue ((md.insert(calendar.getId(), m2)).get());
	    	assertTrue ((md.insert(calendar.getId(), m3)).get());
	    	
	    	//There should be 3 meetings
	    	assertEquals (3, ((md.meetingsByCalendar(calendar.getId())).get().size()));
	    	
	    	//Delete all meetings on 12/07 (no return value)
	    	assertTrue ((md.delete(calendar.getId(), LocalDate.of(2018, 12, 07))).get());
	    	
	    	//There should be 2 meetings left after that
	    	assertEquals (2, ((md.meetingsByCalendar(calendar.getId())).get().size()));

	    	//Delete m1 meeting
	    	assertTrue ((md.delete(calendar.getId(), LocalDate.of(2018, 12, 10), LocalTime.of(14, 00))).get());
	    	
	    	//There should be 1 meeting left after that
	    	assertEquals (1, ((md.meetingsByCalendar(calendar.getId())).get().size()));

	    	//Delete all remaining meetings (the way DeleteCalendar does)
	    	assertTrue ((md.deleteByCalendar(calendar.getId())).get());
	    	
	    	//There should be 0 meetings now
	    	assertEquals (0, ((md.meetingsByCalendar(calendar.getId())).get().size()));
	    	
	    	
	    	// delete calendar to restore database state
//	    	assertTrue ((cd.delete(calendar.getId())).get()); //delete calendar
	    	System.out.println("deleted Calendar: " + calendar.getName());
//	    	assertTrue ((td.deleteByCalendar(calendar.getId())).get()); //delete the calendar's orphaned timeslots
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
	                14,
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
	    	
	    	//test how many timeslots there are (3 days * 3 per day = 18)
	    	List<Timeslot> timeslotList = td.getByCalendar(calendar.getId()).get();
	    	assertEquals (18, ((timeslotList.size())));
	    	
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

