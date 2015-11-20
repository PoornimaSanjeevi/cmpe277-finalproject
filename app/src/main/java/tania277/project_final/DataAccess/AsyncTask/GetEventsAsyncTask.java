package tania277.project_final.DataAccess.AsyncTask;

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

import tania277.project_final.DataAccess.QueryBuilders.BaseQueryBuilder;
import tania277.project_final.Models.EventItem;

/**
 * Created by Tania on 11/16/15.
 */
public class GetEventsAsyncTask extends AsyncTask<EventItem, Void, ArrayList<EventItem>> {
    static BasicDBObject eventItem = null;
    static String OriginalObject = "";
    static String server_output = null;
    static String temp_output = null;

    @Override
    protected ArrayList<EventItem> doInBackground(EventItem... arg0) {

        ArrayList<EventItem> myEvents = new ArrayList<EventItem>();
        try
        {
            Log.i("message : ", "reached GetEventsAsync");
            BaseQueryBuilder qb = new BaseQueryBuilder();
            URL url = new URL(qb.buildEventsGetURL());
            Log.i("message : ", "reached Get URL built "+url);

            HttpURLConnection conn = (HttpURLConnection) url
                    .openConnection();


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

            Log.i("message","server output read");

            // create a basic db list
            String mongoarray = "{ artificial_basicdb_list: "+server_output+"}";
            Object o = com.mongodb.util.JSON.parse(mongoarray);

            Log.i("message","mongo array created");

            DBObject dbObj = (DBObject) o;
            BasicDBList contacts = (BasicDBList) dbObj.get("artificial_basicdb_list");

            Log.i("message","DBObjects created");

            for (Object obj : contacts) {
                DBObject userObj = (DBObject) obj;

                EventItem temp = new EventItem();
                Log.i("message:","id is"+userObj.get("_id").toString());
                temp.setEventId(userObj.get("_id").toString());
                temp.setName(userObj.get("name") + "");
                temp.setAdmin(userObj.get("admin")+"");
                temp.setDate(userObj.get("date") + "");
                temp.setStartTime(userObj.get("start_time") + "");
                temp.setEndTime(userObj.get("end_time")+"");
                temp.setLocation(userObj.get("location") + "");
//                temp.setParticipants(userObj.get("email")+"");
                //TODO: show participants /Number of participants

                myEvents.add(temp);
            }

            Log.i("message","DBObjects parsed");

        }catch (Exception e) {
            e.getMessage();
            Log.i("message : ", "exception in getting contacts" + e.toString() + e.getMessage());
        }

        return myEvents;
    }
}