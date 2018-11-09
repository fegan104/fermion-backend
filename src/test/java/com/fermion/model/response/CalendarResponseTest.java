package com.fermion.model.response;

import com.fermion.data.model.Calendar;
import com.fermion.data.model.response.CalendarResponseData;
import org.junit.Test;

import java.time.LocalDate;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * Created by @author frankegan on 11/2/18.
 */
public class CalendarResponseTest {

    @Test
    public void testMake(){
        Calendar calendar = new Calendar(
                LocalDate.of(2018, 11, 6),
                LocalDate.of(2018, 11, 10),
                12,
                13,
                20);
        calendar.getTimeslots().forEach((day, timeslots) -> System.out.println(day.toString()));
        CalendarResponseData res = new CalendarResponseData(calendar);
        res.days.forEach(dayResponse -> System.out.println(dayResponse.day));
        assertEquals(4, res.getDays().size());
        assertEquals(12, res.getDays()
                .stream()
                .flatMap(dayResponse -> dayResponse.getTimeslots().stream())
                .collect(Collectors.toList())
                .size());
    }
}
