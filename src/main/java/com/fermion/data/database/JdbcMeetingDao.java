package com.fermion.data.database;

import com.fermion.data.model.Meeting;

import java.util.List;
import java.util.Optional;

//SQL Meeting has calId, startTime, dayOf, nameMeet, and location.
//Java Meeting has ID, guest, location

/**
 * Created by @author frankegan on 10/31/18.
 */
public class JdbcMeetingDao implements MeetingDataSource {
    @Override
    public Optional<List<Meeting>> meetingByCalendar(String calendarId) {
		PreparedStatement ps = conn.prepareStatement("SELECT * from meetings where calId = ?;");
		ps.setString(1, calendarId);
        ResultSet resultSet = ps.executeQuery();
		List<Meeting> meetings = new ArrayList<>();
		while (resultSet.next()) {
                meetings.add(new Meeting(
                        resultSet.getTime("meetingStartHr").toLocalTime(),
                        resultSet.getTime("meetingEndHr").toLocalTime(),
                        resultSet.getDate("meetingDayOf").toLocalDate(),
                        resultSet.getString("nameMeet"),
                        resultSet.getString("location")));
            }

        }
        return meetings;
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
