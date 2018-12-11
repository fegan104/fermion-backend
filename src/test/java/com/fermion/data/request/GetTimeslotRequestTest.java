package com.fermion.data.request;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.junit.Test;
/**
 * Tests GetTimeslotRequest
 * @author ttshiz
 *
 */
public class GetTimeslotRequestTest {
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	DateTimeFormatter timef = DateTimeFormatter.ofPattern("HH:mm");

	@Test
	public void test() {
		String startTime = LocalTime.of(9, 30).format(timef);
		String endTime = LocalTime.of(10, 0).format(timef);
		String localDate = LocalDate.of(2018, 12, 8).format(dtf);
		String id = UUID.randomUUID().toString();
		
		GetTimeslotRequest gettime = new GetTimeslotRequest(id, localDate, startTime, endTime);
		assertTrue(gettime.toString().equals("Create(" +id+ ",08-12-2018,09:30,10:00)"));
	}

}
