package tania277.project_final.DataAccess.AsyncTask;

/**
 * Created by Srinidhi on 11/22/2015.
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

import tania277.project_final.DataAccess.QueryBuilders.BaseQueryBuilder;
import tania277.project_final.DataAccess.QueryBuilders.UserQueryBuilder;
import tania277.project_final.Models.User;
import tania277.project_final.util.JsonToStringParsers;


public class GetUserListAsyncTask extends AsyncTask<ArrayList<User>, Void, ArrayList<User>> {
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
    protected ArrayList<User> doInBackground(ArrayList<User>... arg0) {

        ArrayList<User> users = new ArrayList<User>();
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

                User user = new User();
                user = new User();
                user.setUserId(userObj.get("_id").toString());
                user.setName(userObj.get("name") + "");
                user.setAvatar(userObj.get("avatar").toString());
                user.setEmail(userObj.get("email") + "");

                String friendRequestsString = userObj.get("friend_requests")+"";
                Log.i("message: ", "friend requests" + friendRequestsString);
                user.setFriendRequests(parsers.ConvertTofriendRequestsList(friendRequestsString));

                Log.i("message: ","fr1"+user.getFriendRequests());

                users.add(user);
            }

            Log.i("message", "DBObjects parsed");

        }catch (Exception e) {
            e.getMessage();
            Log.i("message : ", "exception in getting contacts" + e.toString() );
        }

        return users;
    }
}