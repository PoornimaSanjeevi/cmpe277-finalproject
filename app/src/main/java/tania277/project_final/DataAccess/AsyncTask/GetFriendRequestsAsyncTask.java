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
import java.util.List;

import tania277.project_final.DataAccess.QueryBuilders.UserQueryBuilder;
import tania277.project_final.Models.User;
import tania277.project_final.util.JsonToStringParsers;

/**
 * Created by Srinidhi on 11/23/2015.
 */
public class GetFriendRequestsAsyncTask extends AsyncTask<List<String>, Void, ArrayList<User>> {
    static BasicDBObject eventItem = null;
    static String OriginalObject = "";
    static String server_output = null;
    static String temp_output = null;
    static List<String> emailList;
    JsonToStringParsers parsers = new JsonToStringParsers();

    public void setRequestEmails(List<String> emails)
    {
        emailList=emails;
    }

    @Override
    protected ArrayList<User> doInBackground(List<String>... arg0) {

        if(arg0.length>0)
            emailList=arg0[0];

        ArrayList<User> users = new ArrayList<User>();
        try
        {
            Log.i("message : ", "reached ");
            UserQueryBuilder qb = new UserQueryBuilder();
            URL url = new URL(qb.buildFriendRequestsGetURL(emailList));
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
                temp.setUserId(userObj.get("_id")+"");
                Log.i("message:", "id is" + userObj.get("_id") + "");
                temp.setName(userObj.get("name") + "");
                temp.setEmail(userObj.get("email") + "");
                temp.setAvatar(userObj.get("avatar") + "");
//                Log.i("message:", "1");
                if(userObj.containsField("current_location")) {
//                    Log.i("messageABCD", "latLang " +"in friends before parse"+userObj.get("current_location").toString() );
                    temp.setLatLang(parsers.ConvertToLatLang(userObj.get("current_location").toString()));
                }

                String friendRequestsString = userObj.get("friend_requests")+"";
                temp.setFriendRequests(parsers.ConvertTofriendRequestsList(friendRequestsString));

                Log.i("message:", "2");
                String friendsString = userObj.get("friends")+"";
                List<String> friends = parsers.ConvertTofriendRequestsList(friendsString);

                Log.i("message:", "3");
                List<User> friendsObj = new ArrayList<User>();
                for (String friend:friends
                     ) { User u = new User();
                    u.setEmail(friend);
                    friendsObj.add(u);
                }
                temp.setFriends(friendsObj);
                Log.i("message:", "4");

                Log.i("message: ", "friends" + friends);

                Log.i("message:", "5");
                users.add(temp);
            }
            Log.i("message","DBObjects parsed");

        }catch (Exception e) {
            e.printStackTrace();
            Log.i("message : ", "exception in getting contacts" + e.getMessage());
        }
        return users;
    }
}
