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
import tania277.project_final.DataAccess.QueryBuilders.UserQueryBuilder;
import tania277.project_final.Models.EventItem;
import tania277.project_final.Models.User;

/**
 * Created by Srinidhi on 11/22/2015.
 */
public class GetFriendsAsyncTask extends AsyncTask<User, Void, ArrayList<User>> {
    static BasicDBObject eventItem = null;
    static String OriginalObject = "";
    static String server_output = null;
    static String temp_output = null;
    static String userEmail;

    public void setUserEmail(String email)
    {
        userEmail=email;
    }

    @Override
    protected ArrayList<User> doInBackground(User... arg0) {

        ArrayList<User> users = new ArrayList<User>();
        try
        {
            Log.i("message : ", "reached GetEventsAsync");
            UserQueryBuilder qb = new UserQueryBuilder();
            URL url = new URL(qb.buildFriendsGetURL(userEmail));
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

                User temp = new User();
                temp.setUserId(userObj.get("_id").toString());
                temp.setName(userObj.get("name").toString());
                temp.setEmail(userObj.get("email") + "");
                temp.setAvatar(userObj.get("avatar") + "");
                users.add(temp);
            }
            Log.i("message","DBObjects parsed");

        }catch (Exception e) {
            e.getMessage();
            Log.i("message : ", "exception in getting contacts" + e.toString() + e.getMessage());
        }
        return users;
    }
}
