package tania277.project_final.Models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Srinidhi on 12/12/2015.
 */
public class CurrentLocationAllParticipants implements Serializable {
    private String emailId;
    private String latitude;
    private String longitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }


}
