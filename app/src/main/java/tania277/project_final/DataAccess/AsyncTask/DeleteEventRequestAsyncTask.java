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
 * Created by Srinidhi on 11/29/2015.
 */
public class DeleteEventRequestAsyncTask extends AsyncTask<Object, Void, Boolean> {

    @Override
    protected Boolean doInBackground(Object... params) {
        EventItem item = (EventItem) params[0];

        try {


            Log.i("message:", "reaching the delete method");
            EventQueryBuilder qb = new EventQueryBuilder();

            URL url = new URL(qb.buildEventUpdateURL(item.getEventId()));

            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();

            Log.i("message:", "Put URL is " + url);
            Log.i("message:","Put data is "+qb.deleteRequest(item));

            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type",
                    "application/json");
            connection.setRequestProperty("Accept", "application/json");

            OutputStreamWriter osw = new OutputStreamWriter(
                    connection.getOutputStream());

            osw.write(qb.deleteRequest(item));
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
