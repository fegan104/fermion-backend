package com.fermion.data.request;

import static org.junit.Assert.*;

import org.junit.Test;
/** Tests GetDayRequest
 * 
 * @author ttshiz
 *
 */
public class GetDayRequestTest {

	@Test
	public void test() {
		GetDayRequest getday = new GetDayRequest();
		assertTrue(getday.toString().equals("Create([],[])"));
	}

}
