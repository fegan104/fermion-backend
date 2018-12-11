package com.fermion.data.request;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.junit.Test;
/**
 * Tests AddMeetingRequest
 * @author ttshiz
 *
 */
public class AddMeetingRequestTest {
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	DateTimeFormatter timef = DateTimeFormatter.ofPattern("HH:mm");

	@Test
	public void test() {
		String startTime = LocalTime.of(9, 30).format(timef);
		String endTime = LocalTime.of(10, 0).format(timef);
		String day = LocalDate.of(2018, 12, 8).format(dtf);
		String guest = "testguest";
		String location = "testloc";
		String calendarId = UUID.randomUUID().toString();
		
		AddMeetingRequest addMeet = new AddMeetingRequest (calendarId, startTime, endTime, day, guest, location);
		
		assertTrue(addMeet.toString().equals("Create("+calendarId+",09:30,10:00,08-12-2018,testguest,testloc)"));
	}

}
