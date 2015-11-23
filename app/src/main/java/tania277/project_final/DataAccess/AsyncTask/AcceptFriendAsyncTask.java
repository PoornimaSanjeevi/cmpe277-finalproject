package tania277.project_final.DataAccess.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import tania277.project_final.DataAccess.QueryBuilders.UserQueryBuilder;
import tania277.project_final.Models.User;

/**
 * Created by Srinidhi on 11/22/2015.
 */
public class AcceptFriendAsyncTask extends AsyncTask<Object, Void, Boolean> {

    @Override
    protected Boolean doInBackground(Object... params) {
        User user = (User) params[0];

        try {

            UserQueryBuilder qb = new UserQueryBuilder();

            URL url = new URL(qb.buildUserUpdateURL(user.getUserId()));

            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();

            Log.i("message:", "Put URL is " + url);
            Log.i("message:","Put data is "+qb.acceptRequest(user));

            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type",
                    "application/json");
            connection.setRequestProperty("Accept", "application/json");

            OutputStreamWriter osw = new OutputStreamWriter(
                    connection.getOutputStream());

            osw.write(qb.acceptRequest(user));
            osw.flush();
            osw.close();
            if(connection.getResponseCode() <205)
            {
                Log.i("message:", "Update success");
                return true;
            }
            else
            {
                Log.i("message:", "Update failed status: "+connection.getResponseCode()+" "+connection.getResponseMessage());
                return false;
            }

        } catch (Exception e) {
            Log.i("message: ","Exception in update code"+e.getMessage());
            return false;

        }

    }

}