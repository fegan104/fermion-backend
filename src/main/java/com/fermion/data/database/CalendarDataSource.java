package com.fermion.data.database;

import com.fermion.data.model.Calendar;

import java.util.List;
import java.util.Optional;

/**
 * Created by @author frankegan on 10/30/18.
 */
public interface CalendarDataSource {
    Optional<Calendar> calendarById(String id);
    Optional<List<Calendar>> getAll();
    Optional<Boolean> delete(String id);
    Optional<Boolean> insert(Calendar calendar);
}
