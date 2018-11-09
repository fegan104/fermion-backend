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
    Optional<Timeslot> timeslotById(String id);
    Optional<List<Timeslot>> getByCalendar(String calendarId);
    Optional<List<Timeslot>> getFrom(LocalDate from, LocalDate to);
    Optional<List<Timeslot>> getFrom(LocalTime from, LocalTime to);
    Optional<Boolean> insert(Timeslot timeslot);
    Optional<Boolean> insert(Timeslot... timeslots);
    Optional<Boolean> update(Timeslot timeslot);
    Optional<Boolean> delete(String id);
    Optional<Boolean> delete(@Nullable DayOfWeek dayOfWeek, @Nullable LocalDate day, @Nullable LocalTime time);
}