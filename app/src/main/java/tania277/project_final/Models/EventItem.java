package tania277.project_final.Models;

import java.util.List;

/**
 * Created by Tania on 11/16/15.
 */
public class EventItem {
    private String EventId;
    private String Name;
    private String Admin;
    private String Date;
    private String Location;
    private String StartTime;
    private String EndTime;
    private List<User> participants;

    public List<User> getInvitedPeople() {
        return invitedPeople;
    }

    public void setInvitedPeople(List<User> invitedPeople) {
        this.invitedPeople = invitedPeople;
    }

    private List<User> invitedPeople;

    public String getEventId() {
        return EventId;
    }

    public void setEventId(String eventId) {
        EventId = eventId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAdmin() {
        return Admin;
    }

    public void setAdmin(String admin) {
        Admin = admin;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }






}
