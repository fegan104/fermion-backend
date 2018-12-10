package com.fermion;
import com.fermion.data.request.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import com.amazonaws.services.lambda.runtime.Context;
import com.fermion.data.database.JdbcCalendarDao;
import com.fermion.data.model.Calendar;
import com.fermion.data.model.response.ApiGatewayResponse;

/** Tests the Add Calendar Lambda
 * 
 * @author ttshiz
 *
 */
public class AddCalendarLambdaTest {
	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}
	String calendarName = "TestAddLambda";
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	@Test
	public void test() throws IOException {
		AddCalendarLambda handler = new AddCalendarLambda(); 

		//Test for presence before
		/*JdbcCalendarDao calDao = new JdbcCalendarDao();
		Optional<List<Calendar>> calList = calDao.getAll();
		Boolean present = false;
		if (calList.isPresent()) {
			for (Calendar c: calList.get()) {
				if (c.getName().equals(calendarName)){
					present = true;
				}
			}
		}

		assertFalse(present); */ // reenable check when dao testing complete
		// input parameters

		int startHour = 9;
		int endHour = 10;
		String startDate = LocalDate.of(2018, 12, 8).format(dtf); // need to figure out what this should be
		String endDate = LocalDate.of(2018, 12, 8).format(dtf);
		int duration = 30;

		// generate input
		AddCalendarRequest ar = new AddCalendarRequest(calendarName, startHour, endHour, startDate, endDate, duration);
		String ccRequest = new Gson().toJson(ar);
		HashMap<String, Object> input = new HashMap<String, Object>();
		input.put("body", ccRequest);
		ApiGatewayResponse resp = handler.handleRequest(input, createContext("add"));

		// check response value
		assertEquals(201, resp.getStatusCode());
		// check response body
		JsonObject body = new JsonParser().parse((String) resp.getBody()).getAsJsonObject();
		assertTrue(body.get("name").getAsString().equals(calendarName));
		System.out.println(body);
		// check for change in system?
		
		// Test for presence after
		/*calList = calDao.getAll();
		 present = false;
		if (calList.isPresent()) {
			for (Calendar c: calList.get()) {
				if (c.getName().equals(calendarName)){
					present = true;
				}
			}
		}
		assertTrue(present); */ //reenable check when dao testing complete
	}
}
