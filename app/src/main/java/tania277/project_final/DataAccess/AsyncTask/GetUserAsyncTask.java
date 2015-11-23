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

import tania277.project_final.DataAccess.QueryBuilders.BaseQueryBuilder;
import tania277.project_final.DataAccess.QueryBuilders.UserQueryBuilder;
import tania277.project_final.Models.User;


public class GetUserAsyncTask extends AsyncTask<User, Void, User> {
    static BasicDBObject user = null;
    static String OriginalObject = "";
    static String server_output = null;
    static String temp_output = null;
    static String userId;
    static String userEmail ;

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

                String[] friendRequestsArray1= friendRequestsString.split("\\[");

                String[] friendRequestsArray2 = friendRequestsArray1[1].split("\\]");

                String[] friendRequestArrayForList = friendRequestsArray2[0].split(",");
                List<String> friends = new ArrayList<String>();


                for(int i=0;i<friendRequestArrayForList.length;i++)
                {
                    friends.add(friendRequestArrayForList[i].toString());
                    Log.i("message",""+friendRequestArrayForList[i]);
                }
                user.setFriendRequests(friends);
            }
            Log.i("message", "DBObjects parsed");

        }catch (Exception e) {
            e.getMessage();
            Log.i("message : ", "exception in getting contacts" + e.toString() + e.getMessage());
        }

        return user;
    }
}