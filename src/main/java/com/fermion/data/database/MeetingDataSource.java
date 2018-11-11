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
    Optional<List<Meeting>> meetingByCalendar(String calendarId);
    Optional<Boolean> update(Meeting meeting);
	Optional<Boolean> delete(LocalDate date, LocalTime time);
    Optional<Boolean> insert(Meeting meeting);
}
