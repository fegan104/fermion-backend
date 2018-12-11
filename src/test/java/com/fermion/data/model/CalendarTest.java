package com.fermion.data.model;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.*;
import java.util.*;
import org.joda.time.Days;
import org.junit.Before;
import org.junit.Test;
/**
 * Tests calendar model object
 * @author ttshiz
 *
 */
public class CalendarTest {
	private Calendar cal;
	LocalDate startDate = LocalDate.of(2018, 11, 3);
	LocalDate endDate = LocalDate.of(2018, 12, 3);
	@Before
	public void beforeEachTest() {
		String name = "George";
		LocalDate startDate = LocalDate.of(2018, 11, 3);
		LocalDate endDate = LocalDate.of(2018, 12, 3);
		int startHour = 9;
		int endHour = 17;
		int duration = 30;
		cal = new Calendar(name, startDate, endDate, startHour, endHour, duration);
	}
	
	@Test
	public void testCalendarLocalConstructor() {
		assertEquals(this.cal.getClass(), Calendar.class);
	}

	@Test
	public void testCalendarRDSConstructor() {
		String id = UUID.randomUUID().toString();
		String name = "George";
		LocalDate startDate = LocalDate.of(2018, 11, 3);
		LocalDate endDate = LocalDate.of(2018, 12, 3);
		LocalTime startHour = LocalTime.of(9, 0);
		LocalTime endHour = LocalTime.of(17,0);
		int duration = 30;
        Map<LocalDate, List<Timeslot>> timeslots = null;
        Map<LocalDate, List<Meeting>> meetings = null;
		cal = new Calendar(id, name, startHour, endHour, duration, timeslots, meetings);
		assertEquals(cal.getClass(), this.cal.getClass());
		assertEquals(cal.getDuration(), duration);
		assertEquals(cal.getEndHour(), endHour);
		assertEquals(cal.getId(), id);
		assertEquals(cal.getMeetings(), null);
		assertEquals(cal.getName(), name);
		assertEquals(cal.getStartHour(), startHour);
		assertEquals(cal.getTimeslots(), null);
		// the following values are not set since can be dynamically calculated
		assertEquals(cal.getEndDay(), null);
		assertEquals(cal.getStartDay(), null);
		
	}

	@Test
	public void testGetId() {
		assertEquals(this.cal.getId().getClass(), String.class);
	}

	@Test
	public void testGetTimeslots() {
		assertEquals(21, cal.getTimeslots().size());
	}

	@Test
	public void testGetName() {
		assertEquals(this.cal.getName(), "George");
	}

	@Test
	public void testGetStartDay() {
		assertEquals(this.cal.getStartDay(), LocalDate.of(2018,  11,  3));
	}

	@Test
	public void testGetEndDay() {
		assertEquals(this.cal.getEndDay(), LocalDate.of(2018, 12, 3));
	}

	@Test
	public void testGetStartHour() {
		assertEquals(this.cal.getStartHour(), LocalTime.of(9,0));
	}

	@Test
	public void testGetEndHour() {
		assertEquals(this.cal.getEndHour(), LocalTime.of(17,0));
	}

	@Test
	public void testGetDuration() {
		assertEquals(this.cal.getDuration(), 30);
	}

	@Test
	public void testGetandSetTimeslots() {
	Map<LocalDate, List<Timeslot>> timeslots = this.cal.getTimeslots();
	String id = UUID.randomUUID().toString();
	LocalTime startHour = LocalTime.of(9, 0);
	LocalTime endHour = LocalTime.of(17,0);
	Timeslot ts = new Timeslot(id, endDate, startHour, endHour);
	ArrayList<Timeslot> newslots = new ArrayList<Timeslot>();
	newslots.add(ts);
	timeslots.put(endDate.plusDays(1), newslots);
	this.cal.setTimeslots(timeslots);
	assertEquals(this.cal.getTimeslots(), timeslots);
	}
	
	@Test
	public void testToString() {
		assertEquals("Calendar{id='" + this.cal.getId()+"', name='George', timeslots="+this.cal.getTimeslots()+", meetings={}, startDay=2018-11-03, endDay=2018-12-03, startHour=09:00, endHour=17:00, duration=30}", this.cal.toString());

	}
	

}
