package com.fermion;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.fermion.data.model.response.ApiGatewayResponse;

/**
 * Created by @author frankegan on 11/15/18.
 */
public class GetAllCalendarsTest {
	Context createContext(String apiCall) {
		TestContext ctx = new TestContext();
		ctx.setFunctionName(apiCall);
		return ctx;
	}

	@Test
	public void testGetAllCalendarsTest() throws IOException { // name of test
		GetAllCalendarsLambda handler = new GetAllCalendarsLambda(); // name of lambda being tested

		// no input needed
		HashMap<String, Object> input = new HashMap();

		ApiGatewayResponse resp = handler.handleRequest(input, createContext("add"));// fix inputs

		// response value
		assertEquals(200, resp.getStatusCode());
	}

}
