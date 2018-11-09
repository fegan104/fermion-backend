package com.fermion.data.database;

import com.fermion.data.model.Meeting;
import com.fermion.data.model.Timeslot;

import java.util.List;
import java.util.Optional;

//SQL Meeting has calId, startTime, dayIf, nameMeet, and location.
//Java Meeting has ID, guest, location

/**
 * Created by @author frankegan on 10/31/18.
 */
public class JdbcMeetingDao implements MeetingDataSource {
    @Override
    public Optional<List<Meeting>> getByTimeslot(Timeslot timeslot) {
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
