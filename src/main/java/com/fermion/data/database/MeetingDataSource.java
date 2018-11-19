package com.fermion.data.database;

import com.fermion.data.model.Meeting;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by @author frankegan on 10/30/18.
 */
public interface MeetingDataSource {
    Optional<List<Meeting>> meetingsByCalendar(String calendarId);
	Optional<Boolean> update(String calId, Meeting meeting);
	Optional<Boolean> delete(String calendarId, LocalDate date, LocalTime time);
	Optional<Boolean> deleteByCalendar(String calendarId);
	Optional<Boolean> insert(String calId, Meeting meeting);
}
