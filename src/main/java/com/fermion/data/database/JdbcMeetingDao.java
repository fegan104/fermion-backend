package com.fermion.data.database;

import com.fermion.data.model.Meeting;

import java.util.List;
import java.util.Optional;

/**
 * Created by @author frankegan on 10/31/18.
 */
public class JdbcMeetingDao implements MeetingDataSource {
    @Override
    public Optional<List<Meeting>> getByCalendar(String calendarId) {
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> update(Meeting meeting) {
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> delete(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<Boolean> insert(Meeting meeting) {
        return Optional.empty();
    }
}
