package tania277.project_final.DataAccess.AsyncTask;

/**
 * Created by Tania on 11/16/15.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import org.json.JSONArray;
import org.json.JSONObject;

import tania277.project_final.DataAccess.QueryBuilders.BaseQueryBuilder;
import tania277.project_final.DataAccess.QueryBuilders.UserQueryBuilder;
import tania277.project_final.Models.RunRecord;
import tania277.project_final.Models.User;
import tania277.project_final.util.JsonToStringParsers;


public class GetUserAsyncTask extends AsyncTask<User, Void, User> {
    static BasicDBObject user = null;
    static String OriginalObject = "";
    static String server_output = null;
    static String temp_output = null;
    static String userId;
    static String userEmail ;

    JsonToStringParsers parsers = new JsonToStringParsers();
    public void setUserEmail(String email)
    {
        userEmail=email;
    }

    @Override
    protected User doInBackground(User... arg0) {

        User user = new User();
        try
        {
            Log.i("message : ", "reached GetUserAsync");
            UserQueryBuilder qb = new UserQueryBuilder();
            URL url = new URL(qb.buildUserDetailsGetURL(userEmail));
            Log.i("message : ", " Get URL is built" + url);

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
            BasicDBList dbusers = (BasicDBList) dbObj.get("artificial_basicdb_list");

            Log.i("message","DBObjects created");

            for (Object obj : dbusers) {
                DBObject userObj = (DBObject) obj;

                user = new User();
                user.setUserId(userObj.get("_id").toString());
                user.setName(userObj.get("name") + "");
                user.setAvatar(userObj.get("avatar") + "");
                user.setEmail(userObj.get("email") + "");

                String friendRequestsString = userObj.get("friend_requests")+"";
                Log.i("message: ","friend requests"+friendRequestsString);

                user.setFriendRequests(parsers.ConvertTofriendRequestsList(friendRequestsString));

                String friendsString = userObj.get("friends")+"";
                List<String> friends = parsers.ConvertTofriendRequestsList(friendsString);

                List<User> friendsObj = new ArrayList<User>();
                for (String friend:friends
                        ) { User u = new User();
                    u.setEmail(friend);
                    friendsObj.add(u);
                }
                user.setFriends(friendsObj);

                String recordsArray = "{ artificial_records_list: "+userObj.get("run_records")+"}";



                Object records = com.mongodb.util.JSON.parse(recordsArray);
                DBObject recordsObj = (DBObject) records;
                BasicDBList dbRecords = (BasicDBList) recordsObj.get("artificial_records_list");

                List<RunRecord> runRecordList = new ArrayList<RunRecord>();

                int i=1;
                for (Object record : dbRecords) {
                    DBObject recordObj = (DBObject) record;
                    RunRecord runRecord = new RunRecord();
                    List<String> recordsList =parsers.ConvertToRecords(recordObj.get(i + "") + "");
                    runRecord.setEventName(recordsList.get(0));
                    runRecord.setDistanceRan(recordsList.get(1));
                    runRecord.setTimeRan(recordsList.get(2));
                    runRecordList.add(runRecord);
                    i++;
                }
                user.setRunRecords(runRecordList);
            }
            Log.i("message", "DBObjects parsed");

        }catch (Exception e) {
            e.printStackTrace();
            Log.i("message : ", "exception in getting users" );
        }

        return user;
    }
}

