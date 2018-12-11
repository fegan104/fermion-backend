package com.fermion;
import com.fermion.data.request.*;
import com.fermion.util.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import org.junit.After;
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
	String calendarId;
	
	@After
	public void afterTest() {
		DeleteCalendarLambda handler = new DeleteCalendarLambda(); 
		// generate input
		HashMap<String,String> prms = new HashMap<String, String>();
		prms.put("id", calendarId);
		HashMap<String, Object> input = new HashMap<String, Object>();
		input.put(Constants.PATH_PARAMS, prms);
		
		handler.handleRequest(input, createContext("add"));
	}
	
	@Test
	public void test() throws IOException {
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

		// check response value
		assertEquals(201, resp.getStatusCode());
		// check response body
		JsonObject body = new JsonParser().parse((String) resp.getBody()).getAsJsonObject();
		assertTrue(body.get("name").getAsString().equals(calendarName));
		calendarId = body.get("id").getAsString();
	}
}
