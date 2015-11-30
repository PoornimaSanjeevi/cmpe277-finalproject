package tania277.project_final;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import tania277.project_final.DataAccess.AsyncTask.AcceptEventAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.DeleteEventRequestAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetEventDetailsAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetFriendRequestsAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetUserListAsyncTask;
import tania277.project_final.Models.EventItem;
import tania277.project_final.Models.User;
import tania277.project_final.util.PrefUtil;

/**
 * Created by Tania on 11/19/15.
 */
public class PopupActivity extends Activity {

    TextView showname, showdate, showadmin, showstime, showetime, showloc;
    EventItem item;
    List<User> participants = new ArrayList<User>();
    List<User> returnedParticipants = new ArrayList<User>();

    // this below task returns users in general
    //TODO: name is confusing. Refactor
    GetFriendRequestsAsyncTask userListTask = new GetFriendRequestsAsyncTask();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_popup);

        showname = (TextView) findViewById(R.id.showname);
        showdate =(TextView) findViewById(R.id.showdate);
        showadmin =(TextView) findViewById(R.id.showadmin);
        showstime= (TextView) findViewById(R.id.showstime);
        showetime=(TextView) findViewById(R.id.showetime);
        showloc =(TextView) findViewById(R.id.showloc);

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
            item=getEventDetailsAsyncTask.execute().get();
            Log.i("message:", "Event item obtained" + item.getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        userListTask.setRequestEmails(item.getParticipants());
        try{
        returnedParticipants=userListTask.execute().get();}
        catch(Exception e)
        {
            e.printStackTrace();
        }
        updateParticipants(returnedParticipants);

        showname.setText(item.getName());
        showloc.setText(item.getLocation());
        showadmin.setText(item.getAdmin());
        showdate.setText(item.getDate());
        showstime.setText(item.getStartTime());
        showetime.setText(item.getEndTime());



    }

    public void updateParticipants(List<User> p)
    {
        this.participants = p;
        Log.i("message:", "update participants reached");
        ArrayAdapter<EventItem> adapter = new ArrayAdapter<EventItem>(getApplicationContext(), R.layout.show_friend_item, p) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.show_friend_item, parent, false);
                }

                TextView showname =(TextView) convertView.findViewById(R.id.event_id);
                TextView shoeemail = (TextView) convertView.findViewById(R.id.event_name);
                //TextView adminEvent =(TextView) convertView.findViewById(R.id.admin_event);

                EventItem singleInvitedItem = EventFragment.this.invitedEvents.get(position);
                Button acceptEvent = (Button)convertView.findViewById(R.id.acceptEvent);
                acceptEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("message:", "getting to the accept Button clicked");
                        EventItem searchResult = EventFragment.this.invitedEvents.get(position);

                        List<String> tempInvited = new ArrayList<String>();
                        for(int i=0;i<searchResult.getInvitedPeople().size();i++)
                        {
                            if(searchResult.getInvitedPeople().get(i).trim().equalsIgnoreCase(new PrefUtil(getActivity()).getEmailId().toString()))
                            {

                            }
                            else
                            {
                                tempInvited.add(searchResult.getInvitedPeople().get(i));
                            }
                        }
                        searchResult.setInvitedPeople(tempInvited);

                        searchResult.getParticipants().add(new PrefUtil(getActivity()).getEmailId());

                        AcceptEventAsyncTask eventAsyncTask= new AcceptEventAsyncTask();
                        eventAsyncTask.execute(searchResult);

                        new DeleteEventRequestAsyncTask().execute(searchResult);


//                        Intent intent = new Intent(getActivity().getApplicationContext(), PopupActivity.class);
//                        intent.putExtra("Event_ID", searchResult.getEventId());
//                        startActivity(intent);

                    }
                });

                Button rejectEvent = (Button)convertView.findViewById(R.id.rejectEvent);
                rejectEvent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("message:", "getting to the reject Button clicked");
                        EventItem searchResult = EventFragment.this.invitedEvents.get(position);

                        List<String> tempInvited = new ArrayList<String>();
                        for(int i=0;i<searchResult.getInvitedPeople().size();i++)
                        {
                            if(searchResult.getInvitedPeople().get(i).trim().equalsIgnoreCase(new PrefUtil(getActivity()).getEmailId()))
                            {

                            }
                            else
                            {
                                tempInvited.add(searchResult.getInvitedPeople().get(i));
                            }
                        }
                        searchResult.setInvitedPeople(tempInvited);
                        new DeleteEventRequestAsyncTask().execute(searchResult);

                    }
                });

                eventName.setText(singleInvitedItem.getName());
                adminEvent.setText(singleInvitedItem.getDate());
                eventId.setText((singleInvitedItem.getEventId()));

                return convertView;
            }
        };

        invited_list.setAdapter(adapter);

    }
}
