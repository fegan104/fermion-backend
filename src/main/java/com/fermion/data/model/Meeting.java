package com.fermion.data.model;

/**
 * Created by @author frankegan on 10/30/18.
 */
public class Meeting {
    private String id;
    private String guest;
    private String location;

    public Meeting(String id, String guest, String location) {
        this.id = id;
        this.guest = guest;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getGuest() {
        return guest;
    }

    public String getLocation() {
        return location;
    }
}
