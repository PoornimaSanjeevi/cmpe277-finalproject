package tania277.project_final.DataAccess.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import tania277.project_final.DataAccess.QueryBuilders.EventQueryBuilder;
import tania277.project_final.DataAccess.QueryBuilders.UserQueryBuilder;
import tania277.project_final.Models.EventItem;
import tania277.project_final.Models.User;

/**
 * Created by Srinidhi on 11/22/2015.
 */
public class CreateUserAsyncTask extends AsyncTask<User, Void, Boolean> {

    @Override
    protected Boolean doInBackground(User... arg0) {
        try
        {
            Log.i("message:", "reaching CreateEventAsync Task");

            User user = arg0[0];
            UserQueryBuilder qb = new UserQueryBuilder();

            //Creating the URL to MongoDB
            URL url = new URL(qb.buildUserSaveURL());

            //Create Http Connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("content-type", "application/json");
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "application/json");

            Log.i("message :", "request URL is " + url);
            Log.i("message :", "POST data is " + qb.createUser(user));

            //Sending Data
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
            outputStreamWriter.write(qb.createUser(user));
            outputStreamWriter.flush();
            outputStreamWriter.close();

            Log.i("message: ", "data sent to the database");

            int responseCode = conn.getResponseCode();

            Log.i("message :", "response code is " +responseCode );
            if(responseCode<205)
            {
                Log.i("message:","response : success, code is" + responseCode +" message is "+conn.getResponseMessage());
                return true;
            }
            else
            {
                Log.i("message:","response : fail, code is " + responseCode);
                return false;
            }
        } catch (Exception e) {
            Log.i("message:","Exception occured " +e.toString());
            return false;
        }
    }

}
