package tania277.project_final.DataAccess.AsyncTask;

/**
 * Created by Tania on 11/19/15.
 */
import android.os.AsyncTask;
import android.util.Log;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import tania277.project_final.DataAccess.QueryBuilders.EventQueryBuilder;
import tania277.project_final.Models.CurrentLocationAllParticipants;
import tania277.project_final.Models.EventItem;
import tania277.project_final.util.JsonToStringParsers;

/**
 * Created by Srinidhi on 11/19/2015.
 */
public class GetEventDetailsAsyncTask extends AsyncTask<EventItem, Void, EventItem> {
    static BasicDBObject eventItem = null;
    static String OriginalObject = "";
    static String server_output = null;
    static String temp_output = null;
    static String eventId;
    JsonToStringParsers parsers= new JsonToStringParsers();

    public void setUserId(String id)
    {

        Log.i("message:","setting event id"+id);
        eventId=id;
    }

    @Override
    protected  EventItem doInBackground(EventItem... arg0) {

        EventItem temp = new EventItem();
        try
        {
            Log.i("message : ", "reached GetEventDetailsAsync");

            EventQueryBuilder qb = new EventQueryBuilder();

            URL url = new URL(qb.buildEventDetailsGetURL(eventId));
            HttpURLConnection conn = (HttpURLConnection) url
                    .openConnection();

            Log.i("message:","Get URL is "+url);

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            Log.i("message : ", "Get connection created. respose is "+conn.getResponseCode() );

            if (conn.getResponseCode() != 200) {
                Log.i("message : ","get response success");
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            Log.i("message: ","reader created");

            while ((temp_output = br.readLine()) != null) {
                server_output = temp_output;
            }

            Log.i("message","server output read" +server_output);



            // create a basic db list
            String mongoarray = "{ artificial_basicdb_list: "+server_output+"}";
            Object o = com.mongodb.util.JSON.parse(mongoarray);

            Log.i("message", "mongo array created");

            DBObject dbObj = (DBObject) o;
            BasicDBList contacts = (BasicDBList) dbObj.get("artificial_basicdb_list");

            Log.i("message", "DBObjects created");

            for (Object obj : contacts) {
                DBObject userObj = (DBObject) obj;

               // EventItem temp = new EventItem();
                Log.i("message:", "id is" + userObj.get("_id").toString());
                temp.setEventId(userObj.get("_id").toString());
                temp.setName(userObj.get("name") + "");
                temp.setAdmin(userObj.get("admin") + "");
                temp.setDate(userObj.get("date") + "");
                temp.setStartTime(userObj.get("start_time") + "");
                temp.setEndTime(userObj.get("end_time") + "");
                temp.setLocation(userObj.get("location") + "");


                //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


                //TODO: parser name is ambiguious
                String participantsString = userObj.get("participants")+"";
                List<String> participants = parsers.ConvertTofriendRequestsList(participantsString);
                temp.setParticipants(participants);



                String invitedString = userObj.get("invited")+"";
                List<String> invited = parsers.ConvertTofriendRequestsList(invitedString);
                temp.setInvitedPeople(invited);

//                String finishedString = userObj.get("finished_users")+"";
//                List<String> finished = parsers.ConvertTofriendRequestsList(finishedString);
//                temp.setFinishedUsers(finished);



                //TODO: show participants /Number of participants


            }

            Log.i("message", "DBObjects parsed");


            Log.i("message", "DBObject parsed");

        }catch (Exception e) {
            e.getMessage();
            Log.i("message : ", "exception in getting contacts12345678" + e.toString() + e.getMessage());
        }

        return temp;
    }
}
