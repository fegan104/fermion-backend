package com.fermion;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.fermion.data.model.response.ApiGatewayResponse;
import com.fermion.data.request.AddCalendarRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GetDayLambdaTest {
	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}
	String calendarName = "TestGetByIdLambda";
	String calendarId;
	
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	@Before
	public void beforeTest() {
		AddCalendarLambda handler = new AddCalendarLambda(); 
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
		JsonObject body = new JsonParser().parse((String) resp.getBody()).getAsJsonObject();
		calendarId = body.get("id").getAsString();
	}
	@Test
	public void test() throws IOException {
		GetCalendarByIdLambda handler = new GetCalendarByIdLambda(); 
		//Test for presence before
		/*JdbcCalendarDao calDao = new JdbcCalendarDao();
		Optional<List<Calendar>> calList = calDao.getAll();
		Boolean present = false;
		if (calList.isPresent()) {
			for (Calendar c: calList.get()) {
				if (c.getId().equals(calendarName)){
					present = true;
				}
			}
		}
		assertTrue(present); */ // reenable check when dao testing complete
		// input parameters

		//TODO: fix generate input
		/*GetCalendarByIdRequest ar = new GetCalendarByIdRequest(calendarId);
		String ccRequest = new Gson().toJson(ar);
		HashMap<String, Object> input = new HashMap<String, Object>();
		input.put("body", ccRequest);
		ApiGatewayResponse resp = handler.handleRequest(input, createContext("add"));

		// check response value
		assertEquals(200, resp.getStatusCode());
		// check response body
		JsonObject body = new JsonParser().parse((String) resp.getBody()).getAsJsonObject();
		assertTrue(body.get("name").getAsString().equals(calendarName));
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
