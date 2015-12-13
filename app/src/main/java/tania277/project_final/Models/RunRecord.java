package tania277.project_final.Models;

import java.util.List;

/**
 * Created by Srinidhi on 11/23/2015.
 */
public class RunRecord {
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    String eventId;
    String eventName;
    String timeRan;
    String distanceRan;

    public List<LatLang> getPath() {
        return path;
    }

    public void setPath(List<LatLang> path) {
        this.path = path;
    }

    List<LatLang> path;

    public  RunRecord(){}

    public RunRecord(String e, String d, String t){
        eventName = e;
        distanceRan = d;
        timeRan = t;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getTimeRan() {
        return timeRan;
    }

    public void setTimeRan(String timeRan) {
        this.timeRan = timeRan;
    }


    public String getDistanceRan() {
        return distanceRan;
    }

    public void setDistanceRan(String distanceRan) {
        this.distanceRan = distanceRan;
    }


}
