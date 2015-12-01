package tania277.project_final;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import tania277.project_final.DataAccess.AsyncTask.CreateEventAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetEventsAsyncTask;
import tania277.project_final.Models.AppUser;
import tania277.project_final.Models.EventItem;
import tania277.project_final.Models.User;
import tania277.project_final.util.PrefUtil;

/**
 * Created by Tania on 11/16/15.
 */
public class CreateEvent extends Activity {
    EditText ename,edate,stime,etime,eloc;
    Button invite, submit,cancel;
    EventItem eventItem = new EventItem();

    static List<String>  invitedPeople = new ArrayList<>();

    public static void setInvitedPeople(List<String> p) {
        invitedPeople = p;
    }



    CreateEventAsyncTask createEventAsyncTask = new CreateEventAsyncTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_create);
        submit = (Button)findViewById(R.id.submit);
        cancel = (Button)findViewById(R.id.cancel);
        invite =(Button) findViewById(R.id.invite);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValuesToMongo();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFriendsList();
            }
        });

    }

    public void showFriendsList()
    {
        Intent intent = new Intent(this, InviteFriendsActivity.class);
        intent.putExtra("previous","CreateEvent");
        startActivity(intent);
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
                ename.setError("Event name is required!");
                ename.requestFocus();
            } else if (edate.getText().toString().trim().equals("")) {
                edate.setError("Date is required!");
                edate.requestFocus();
            } else if (stime.getText().toString().trim().equals("")) {
                stime.setError("Starting time is required!");
                stime.requestFocus();
            } else {
                eloc.setError("Location is required!");
                eloc.requestFocus();
            }
        }else{
            eventItem.setName(ename.getText().toString());
            eventItem.setDate(edate.getText().toString());
            eventItem.setStartTime(stime.getText().toString());
            eventItem.setEndTime(etime.getText().toString());
            eventItem.setLocation(eloc.getText().toString());


            eventItem.setInvitedPeople(invitedPeople);
            invitedPeople = new ArrayList<String>();

            eventItem.setAdmin(new PrefUtil(this).getEmailId());


            createEventAsyncTask.execute(eventItem);

    //        getEventsAsyncTask.execute().get();


            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("fragment","1");
            startActivity(intent);
        }
    }
}
