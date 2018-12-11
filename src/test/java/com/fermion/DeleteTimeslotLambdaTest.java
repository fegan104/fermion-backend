package com.fermion;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.fermion.data.model.response.ApiGatewayResponse;
import com.fermion.data.request.AddCalendarRequest;
import com.fermion.util.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
/** Tests DeleteTimeslotLambda
 * 
 * @author ttshiz
 *
 */
public class DeleteTimeslotLambdaTest {
	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}
	String calendarName = "TestDeleteTimeslot";
	String calendarId;

	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	DateTimeFormatter timef = DateTimeFormatter.ofPattern("HH:mm");

	@Before
	public void beforeTest() {
		AddCalendarLambda handler = new AddCalendarLambda(); 
		int startHour = 9;
		int endHour = 10;
		String startDate = LocalDate.of(2018, 12, 8).format(dtf); // need to figure out what this should be
		String endDate = LocalDate.of(2018, 12, 10).format(dtf);
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
	public void test() {
		DeleteTimeslotLambda handler = new DeleteTimeslotLambda(); 

		// generate input		
		String day = LocalDate.of(2018, 12, 10).getDayOfWeek().toString();
		String date = LocalDate.of(2018, 12, 9).format(dtf);
		String time = LocalTime.of(9, 30).format(timef);
		HashMap<String,String> prms = new HashMap<String, String>();
		prms.put("calendarId", calendarId);
		prms.put("dayOfWeek", day);
		prms.put("date", date);
		prms.put("time", time);
		HashMap<String, Object> input = new HashMap<String, Object>();
		input.put(Constants.QUERY_STRING_PARAMS, prms);
		
		// run lambda
		ApiGatewayResponse resp = handler.handleRequest(input, createContext("add"));

		// check response value
		assertEquals(202, resp.getStatusCode());
	}

}
