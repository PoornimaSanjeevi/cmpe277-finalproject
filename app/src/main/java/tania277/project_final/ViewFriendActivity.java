package tania277.project_final;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import tania277.project_final.DataAccess.AsyncTask.GetEventDetailsAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetUserAsyncTask;
import tania277.project_final.Models.EventItem;
import tania277.project_final.Models.RunRecord;
import tania277.project_final.Models.User;

/**
 * Created by Srinidhi on 11/23/2015.
 */
public class ViewFriendActivity extends AppCompatActivity {
    ListView run_records;
    List<RunRecord> runRecords;
    TextView showname;
    TextView showemail;
    User friend=new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_viewfriend);

        //Map UI
        showname = (TextView) findViewById(R.id.showname);
        showemail =(TextView) findViewById(R.id.showemail);
        run_records = (ListView)findViewById(R.id.run_records);
        //get the args that come with Intent
        String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("UserEmail");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("UserEmail");
        }

        //Call to DB
        GetUserAsyncTask getUSerDetailsAsyncTask = new GetUserAsyncTask();
        getUSerDetailsAsyncTask.setUserEmail(newString);

        try {
            friend=getUSerDetailsAsyncTask.execute().get();
            Log.i("message:", "Event item obtained" + friend.getName());
            for(RunRecord record : friend.getRunRecords())
            {
                Log.i("message: ",""+record.getEventName()+record.getDistanceRan()+record.getTimeRan());
            }
        } catch (Exception e) {
            Log.i("message", "Exception" + e.getMessage());
        }

        updateRunRecords(friend.getRunRecords());
        //Map UI
        showname.setText(friend.getName());
        showemail.setText(friend.getEmail());

    }

    public void updateRunRecords(List<RunRecord> records){
        this.runRecords = records;
        Log.i("message:", "update Run Records adapter reached");
        ArrayAdapter<RunRecord> adapter = new ArrayAdapter<RunRecord>(getApplicationContext(), R.layout.run_record_item, records) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.run_record_item, parent, false);
                }

                TextView showename =(TextView) convertView.findViewById(R.id.showename);
                TextView showdistance = (TextView) convertView.findViewById(R.id.showdistance);
                TextView showtime=(TextView) convertView.findViewById(R.id.showtime);


                RunRecord singleRunRecord = runRecords.get(position);


                showename.setText(singleRunRecord.getEventName());
                showdistance.setText(singleRunRecord.getDistanceRan());
                showtime.setText(singleRunRecord.getTimeRan());

                return convertView;
            }
        };

        run_records.setAdapter(adapter);
    }
}