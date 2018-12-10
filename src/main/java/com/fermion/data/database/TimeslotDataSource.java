package com.fermion.data.database;

import com.fermion.data.model.Timeslot;

import javax.annotation.Nullable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by @author frankegan on 10/30/18.
 */
public interface TimeslotDataSource {
    Optional<List<Timeslot>> getByCalendar(String calendarId);
	Optional<Boolean> insert(String calId, Timeslot timeslot);
	Optional<Boolean> insert(String calId, List<Timeslot> timeslots);
    Optional<Boolean> deleteByCalendar(String calendarId);
    Optional<List<Timeslot>> delete(String calendarId, @Nullable DayOfWeek dayOfWeek, @Nullable LocalDate day, @Nullable LocalTime time);
}
