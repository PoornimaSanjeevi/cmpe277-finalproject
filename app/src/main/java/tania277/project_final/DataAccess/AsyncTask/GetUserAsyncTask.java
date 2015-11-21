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


public class GetUserAsyncTask extends AsyncTask<User, Void, ArrayList<User>> {
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
    protected ArrayList<User> doInBackground(User... arg0) {

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

                User temp = new User();
                temp.setUserId(userObj.get("_id").toString());
                temp.setName(userObj.get("name") + "");
                temp.setAvatar("");
                temp.setEmail(userObj.get("email") + "");

                String friendRequestString = userObj.get("friend_requests")+"";

                String[] friendRequestArray1= friendRequestString.split("\\[");

                String[] friendRequestArray2 = friendRequestArray1[1].split("\\]");

                String[] friendRequestArrayForList = friendRequestArray2[0].split(",");
                List<User> friends = new ArrayList<User>();


                for(int i=0;i<friendRequestArrayForList.length;i++)
                {
                    User tempFriend = new User();
                    tempFriend.setName(friendRequestArrayForList[i].toString());
                    friends.add(tempFriend);
                    //friendRequests.add(friendRequestArrayForList[i]);
                    Log.i("message",""+friendRequestArrayForList[i]);
                }
                temp.setFriends(friends);



                users.add(temp);
            }

            Log.i("message", "DBObjects parsed");

        }catch (Exception e) {
            e.getMessage();
            Log.i("message : ", "exception in getting contacts" + e.toString() + e.getMessage());
        }

        return users;
    }
}