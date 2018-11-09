package com.fermion.data.database;

import com.fermion.data.model.Meeting;

import java.util.List;
import java.util.Optional;

/**
 * Created by @author frankegan on 10/30/18.
 */
public interface MeetingDataSource {
    Optional<List<Meeting>> getByCalendar(String calendarId);
    Optional<Boolean> update(Meeting meeting);
    Optional<Boolean> delete(String id);
    Optional<Boolean> insert(Meeting meeting);
}
