package com.fermion.model;

import com.fermion.data.model.Calendar;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * Created by @author frankegan on 11/2/18.
 */
public class CalendarTest {

    @Test
    public void testGenTimeslots() {
        Calendar calendar = new Calendar(
                LocalDate.of(2018, 11, 3),
                LocalDate.of(2018, 11, 6),
                12,
                13,
                20);
        assertEquals(2, calendar.getTimeslots().size());
    }
}
