package com.fermion.data.model;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
/**
 * Tests Timeslot Model Object
 * @author ttshiz
 *
 */
public class TimeslotTest {
	private Timeslot timeslot;
	private String id;
	LocalTime start;
	LocalTime end;
	LocalDate day;
	@Before
	public void beforeEachTest() {
		start = LocalTime.of(10, 10);
		end = LocalTime.of(10, 20);
		day = LocalDate.of(2018, 11, 3);
		id = UUID.randomUUID().toString();
		timeslot = new Timeslot(id, day, start, end);
	}
	
	@Test
	public void testTimeslotConstructorLocal() {
		Timeslot ts = new Timeslot(day, start, end);
		assertEquals(ts.getClass(), Timeslot.class);
		assertEquals(ts.getDay(), this.timeslot.getDay());
		assertEquals(ts.getEndTime(), this.timeslot.getEndTime());
		assertEquals(ts.getLocalDate(), this.timeslot.getLocalDate());
		assertEquals(ts.getStartTime(), this.timeslot.getStartTime());
	}

	@Test
	public void testTimeslotConstructorRDS() {
		assertEquals(this.timeslot.getClass(), Timeslot.class);
	}

	@Test
	public void testGetId() {
		assertEquals(id, this.timeslot.getId());
	}

	@Test
	public void testGetDay() {
		assertEquals(day, this.timeslot.getDay());
	}

	@Test
	public void testGetLocalDate() {
		assertEquals(day, this.timeslot.getLocalDate());
	}

	@Test
	public void testGetStartTime() {
		assertEquals(start, this.timeslot.getStartTime());
	}

	@Test
	public void testGetEndTime() {
		assertEquals(end, this.timeslot.getEndTime());
	}

}
