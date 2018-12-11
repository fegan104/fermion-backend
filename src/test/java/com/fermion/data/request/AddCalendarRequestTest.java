package com.fermion.data.request;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.Test;
/**
 * Tests AddCalendarRequest
 * @author ttshiz
 *
 */
public class AddCalendarRequestTest {
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	@Test
	public void test() {
		String name = "testRequest";
		int startHour = 9;
		int endHour = 17;
		String startDate = LocalDate.of(2018, 12, 8).format(dtf);
		String endDate = LocalDate.of(2018, 12, 8).format(dtf);
		int duration = 30;
		AddCalendarRequest addCal = new AddCalendarRequest(name, startHour, endHour, startDate, endDate, duration);
		assertTrue(addCal.toString().equals("Create(testRequest,9,17,08-12-2018,08-12-2018,30)"));
	}

}
