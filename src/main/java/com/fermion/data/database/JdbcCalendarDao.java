package com.fermion.data.database;

import com.fermion.data.model.Calendar;

import java.util.List;
import java.util.Optional;

/**
 * Created by @author frankegan on 10/31/18.
 */
public class JdbcCalendarDao implements CalendarDataSource {
    @Override
    public Optional<Calendar> calendarById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<List<Calendar>> getAll() {
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> delete(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> insert(Calendar calendar) {
        return Optional.empty();
    }
}
