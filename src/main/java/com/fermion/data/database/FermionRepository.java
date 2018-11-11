package com.fermion.data.database;

import com.fermion.data.model.Calendar;
import com.fermion.data.model.Meeting;
import com.fermion.data.model.Timeslot;

import javax.annotation.Nullable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by @author frankegan on 10/31/18.
 */
public class FermionRepository implements MeetingDataSource, TimeslotDataSource, CalendarDataSource {
    private CalendarDataSource calendarDao = new JdbcCalendarDao();
    private TimeslotDataSource timeslotDao = new JdbcTimeslotDao();
    private MeetingDataSource meetingDao = new JdbcMeetingDao();

    @Override
    public Optional<Calendar> calendarById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Calendar>> getAll() {
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> insert(Calendar calendar) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Meeting>> meetingByCalendar(String calendarId) {
        return Optional.empty();
    }

    @Override
	public Optional<Boolean> update(String calId, Meeting meeting) {
        return Optional.empty();
    }

	@Override
	public Optional<Boolean> delete(String id) {
        return Optional.empty();
	}

	@Override
	public Optional<Boolean> delete(LocalDate date, LocalTime time) {
        return Optional.empty();
	}

    @Override
	public Optional<Boolean> insert(String calId, Meeting meeting) {
        return Optional.empty();
    }

    @Override
    public Optional<Timeslot> timeslotById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Timeslot>> getByCalendar(String calendarId) {
        return Optional.empty();
    }

	@Override
	public Optional<List<Timeslot>> getFrom(String calendarId, LocalDate from, LocalDate to) {
        return Optional.empty();
	}

	@Override
	public Optional<List<Timeslot>> getFrom(String calendarID, LocalTime from, LocalTime to) {
        return Optional.empty();
	}

	@Override
	public Optional<Boolean> insert(String calId, Timeslot timeslot) {
        return Optional.empty();
	}

	@Override
	public Optional<Boolean> insert(String calId, Timeslot... timeslots) {
        return Optional.empty();
	}

    @Override
	public Optional<Boolean> update(String calId, Timeslot timeslot) {
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> delete(@Nullable DayOfWeek dayOfWeek, @Nullable LocalDate day, @Nullable LocalTime time) {
        return Optional.empty();
    }






}
