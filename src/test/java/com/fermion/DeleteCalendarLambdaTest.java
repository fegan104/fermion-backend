package com.fermion;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.fermion.data.model.response.ApiGatewayResponse;
import com.fermion.data.request.AddCalendarRequest;
import com.fermion.util.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
/**
 * Tests DeleteCalendarLambda
 * @author ttshiz
 *
 */
public class DeleteCalendarLambdaTest {
	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}
	String calendarName = "TestDeleteCalendarLambda";
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
	public void test() {
		DeleteCalendarLambda handler = new DeleteCalendarLambda(); 

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

		assertTrue(present); */ // reenable check when dao testing complete
		// input parameters

		// generate input
		HashMap<String,String> prms = new HashMap<String, String>();
		prms.put("id", calendarId);
		HashMap<String, Object> input = new HashMap<String, Object>();
		input.put(Constants.PATH_PARAMS, prms);
		
		ApiGatewayResponse resp = handler.handleRequest(input, createContext("add"));

		// check response value
		assertEquals(202, resp.getStatusCode());

		// check response body
		JsonObject body = new JsonParser().parse((String) resp.getBody()).getAsJsonObject();
		assertTrue(body.get("name").getAsString().equals(calendarName));
		assertTrue(body.get("id").getAsString().equals(calendarId));
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
