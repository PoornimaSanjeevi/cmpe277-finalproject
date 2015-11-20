package tania277.project_final.DataAccess.AsyncTask;

/**
 * Created by Tania on 11/19/15.
 */
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import tania277.project_final.DataAccess.QueryBuilders.EventQueryBuilder;
import tania277.project_final.Models.EventItem;


public class CreateEventAsyncTask extends AsyncTask<EventItem, Void, Boolean> {

    @Override
    protected Boolean doInBackground(EventItem... arg0) {
        try
        {
            Log.i("message:","reaching CreateEventAsync Task");

            EventItem eventItem = arg0[0];
            EventQueryBuilder qb = new EventQueryBuilder();

            //Creating the URL to MongoDB
            URL url = new URL(qb.buildEventsSaveURL());

            //Create Http Connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("content-type", "application/json");
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "application/json");

            Log.i("message :", "request URL is " + qb.buildEventsSaveURL());
            Log.i("message :", "POST data is " + qb.createEvent(eventItem));

            //Sending Data
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
            outputStreamWriter.write(qb.createEvent(eventItem));
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