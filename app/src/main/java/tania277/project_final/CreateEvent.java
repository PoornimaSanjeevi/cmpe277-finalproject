package tania277.project_final;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
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
    EditText ename, edate, stime, etime, eloc;

    Button invite, submit, cancel;
    EventItem eventItem = new EventItem();
    private ImageButton ib;
    private ImageButton ib1;
    private ImageButton ib2;
    private Calendar cal;
    private int hour;
    private int minute;
    private int day;
    private int month;
    private int year;

    static List<String> invitedPeople = new ArrayList<>();

    public static void setInvitedPeople(List<String> p) {
        invitedPeople = p;
    }


    CreateEventAsyncTask createEventAsyncTask = new CreateEventAsyncTask();

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        if (id == 0) {
            return new DatePickerDialog(this, datePickerListener, year, month, day);
        } else if (id == 1) {
            return new TimePickerDialog(this, timePickerListener, hour, minute, true);
        } else

            return new TimePickerDialog(this, timePickerListener1, hour, minute, true);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            edate.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                    + selectedYear);
        }
    };

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            String minute1 = String.format("%02d", minute);
            stime.setText(hourOfDay + ":" + minute1);
        }

    };
    private TimePickerDialog.OnTimeSetListener timePickerListener1 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            String minute1 = String.format("%02d", minute);
            etime.setText(hourOfDay + ":" + minute1);
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_create);
        submit = (Button) findViewById(R.id.submit);
        cancel = (Button) findViewById(R.id.cancel);
        invite = (Button) findViewById(R.id.invite);
        ib = (ImageButton) findViewById(R.id.imageButton1);
        ib1 = (ImageButton) findViewById(R.id.imageButton2);
        ib2 = (ImageButton) findViewById(R.id.imageButton3);
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        edate = (EditText) findViewById(R.id.edate);
        stime = (EditText) findViewById(R.id.stime);
        etime = (EditText) findViewById(R.id.etime);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });

        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });

        ib2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(2);
            }
        });

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

    public void showFriendsList() {
        Intent intent = new Intent(this, InviteFriendsActivity.class);
        intent.putExtra("previous", "CreateEvent");
        startActivity(intent);
    }

    public void setValuesToMongo() {

        ename = (EditText) findViewById(R.id.ename);
        edate = (EditText) findViewById(R.id.edate);

//       pickerDate = (DatePicker)findViewById(R.id.pickerdate);
        stime = (EditText) findViewById(R.id.stime);
        etime = (EditText) findViewById(R.id.etime);
        eloc = (EditText) findViewById(R.id.eloc);
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
        } else {
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
            intent.putExtra("fragment", "1");
            startActivity(intent);
        }
    }
}
