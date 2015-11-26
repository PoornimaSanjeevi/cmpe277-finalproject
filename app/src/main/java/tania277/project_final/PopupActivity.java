package tania277.project_final;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.ExecutionException;

import tania277.project_final.DataAccess.AsyncTask.GetEventDetailsAsyncTask;
import tania277.project_final.Models.EventItem;

/**
 * Created by Tania on 11/19/15.
 */
public class PopupActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_popup);

        String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("Event_ID");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("Event_ID");
        }


        GetEventDetailsAsyncTask getEventDetailsAsyncTask = new GetEventDetailsAsyncTask();
        getEventDetailsAsyncTask.setUserId(newString);



        try {
            EventItem item=getEventDetailsAsyncTask.execute().get();
            Log.i("message:", "Event item obtained" + item.getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }
}
