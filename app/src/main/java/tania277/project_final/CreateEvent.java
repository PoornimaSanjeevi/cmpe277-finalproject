package tania277.project_final;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import tania277.project_final.DataAccess.AsyncTask.CreateEventAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetEventsAsyncTask;
import tania277.project_final.Models.EventItem;

/**
 * Created by Tania on 11/16/15.
 */
public class CreateEvent extends AppCompatActivity {
    EditText ename,edate,stime,etime,eloc;
    Button invite, submit;
    EventItem eventItem = new EventItem();
    CreateEventAsyncTask createEventAsyncTask = new CreateEventAsyncTask();
    GetEventsAsyncTask getEventsAsyncTask = new GetEventsAsyncTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_create);
        submit = (Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValuesToMongo();
            }
        });

    }

    public void setValuesToMongo(){
        ename = (EditText)findViewById(R.id.ename);
        edate = (EditText)findViewById(R.id.edate);
        stime = (EditText)findViewById(R.id.stime);
        etime = (EditText)findViewById(R.id.etime);
        eloc = (EditText)findViewById(R.id.eloc);
        if (ename.getText().toString().trim().equals("") || edate.getText().toString().trim().equals("")
                || stime.getText().toString().trim().equals("") || eloc.getText().toString().equals("")) {


            if (ename.getText().toString().trim().equals("")) {
                ename.setError("Home Value is required!");
                ename.requestFocus();
            } else if (edate.getText().toString().trim().equals("")) {
                edate.setError("Downpayment Value is required!");
                edate.requestFocus();
            } else if (stime.getText().toString().trim().equals("")) {
                stime.setError("Downpayment Value is required!");
                stime.requestFocus();
            } else {
                eloc.setError("Interest is required!");
                eloc.requestFocus();
            }
        } else {

            eventItem.setName(ename.getText().toString());
            eventItem.setDate(edate.getText().toString());
            eventItem.setStartTime(stime.getText().toString());
            eventItem.setEndTime(etime.getText().toString());
            eventItem.setLocation(eloc.getText().toString());
            eventItem.setAdmin("user@gmail.com");
            createEventAsyncTask.execute(eventItem);

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
