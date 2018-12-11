package com.fermion.data.model;

import java.lang.reflect.*;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
/**
 * Tests Meeting Model object
 * @author ttshiz
 *
 */
public class MeetingTest {
	private Meeting meeting;
	@Before
	public void beforeEachTest() {
		LocalTime start = LocalTime.of(10, 10);
		LocalTime end = LocalTime.of(10, 20);
		LocalDate day = LocalDate.of(2018, 11, 3);
		String guest = "George";
		String loc = "Fuller";
		meeting = new Meeting(start, end, day, guest, loc);
	}
	
	@Test
	public void testMeeting() { // Constructor	
		assertEquals(this.meeting.getClass(), Meeting.class);
	}

	@Test
	public void testGetStartTime() {
		assertEquals(this.meeting.getStartTime(), LocalTime.of(10, 10));
	}

	@Test
	public void testGetEndTime() {
		assertEquals(this.meeting.getEndTime(), LocalTime.of(10, 20));
	}

	@Test
	public void testGetDay() {
		assertEquals(this.meeting.getDay(), LocalDate.of(2018, 11, 3));
	}

	@Test
	public void testGetGuest() {
		assertEquals(this.meeting.getGuest(), "George");
	}

	@Test
	public void testGetLocation() {
		assertEquals(this.meeting.getLocation(), "Fuller");
	}

}
