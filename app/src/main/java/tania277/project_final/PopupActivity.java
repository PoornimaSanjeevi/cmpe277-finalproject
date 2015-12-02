package tania277.project_final;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import tania277.project_final.DataAccess.AsyncTask.AcceptEventAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.DeleteEventRequestAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetEventDetailsAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetEventsAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetFriendRequestsAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetUserListAsyncTask;
import tania277.project_final.Models.EventItem;
import tania277.project_final.Models.User;
import tania277.project_final.util.PrefUtil;

/**
 * Created by Tania on 11/19/15.
 */
public class PopupActivity extends Activity {

    private ListView showParticipantslist;
    TextView showname, showdate, showadmin, showstime, showetime, showloc;
    EventItem item;
    List<User> participants = new ArrayList<User>();
    List<User> returnedParticipants = new ArrayList<User>();
    Button invite, done;

    public EventItem getEventItem()
    {
        return item;
    }

    static List<String>  invitedPeople = new ArrayList<>();

    public static void setInvitedPeople(List<String> p) {
        invitedPeople = p;
    }

    // this below task returns users in general
    //TODO: name is confusing. Refactor
    GetFriendRequestsAsyncTask userListTask = new GetFriendRequestsAsyncTask();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_popup);
        invite =(Button) findViewById(R.id.invite);
        done =(Button) findViewById(R.id.done);

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFriendsList();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("message","run buddy"+"done");
                UpdateEvent();
            }
        });



        showParticipantslist = (ListView) findViewById(R.id.showparticipantslist);
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

    public void showFriendsList()
    {
        Intent intent = new Intent(this, InviteFriendsActivity.class);
        intent.putExtra("previous", "PopupActivity");
        Log.i("message","participants number"+returnedParticipants.size());
        List<String> participants = new ArrayList<>();
        for (User u:
             returnedParticipants) {
            Log.i("message:","1 participants"+u.getEmail().trim());
            participants.add(u.getEmail().trim());
        }
        intent.putStringArrayListExtra("plist", (ArrayList<String>) item.getParticipants());
        intent.putStringArrayListExtra("ilist",(ArrayList<String>) item.getInvitedPeople());
        startActivity(intent);

    }

    public void UpdateEvent()
    {
        for (String invited:invitedPeople
             ) {
            getEventItem().getInvitedPeople().add(invited);
        }

        new DeleteEventRequestAsyncTask().execute(getEventItem());
        onBackPressed();
    }

    public void updateParticipants(List<User> p)
    {
        this.participants = p;
        Log.i("message:", "update participants reached");
        ArrayAdapter<User> adapter = new ArrayAdapter<User>(getApplicationContext(), R.layout.show_friend_item, p) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.show_friend_item, parent, false);
                }
                ImageView showpic = (ImageView)convertView.findViewById(R.id.showpic);
                TextView showname =(TextView) convertView.findViewById(R.id.showname);
//                TextView showemail = (TextView) convertView.findViewById(R.id.showemail);
                //TextView adminEvent =(TextView) convertView.findViewById(R.id.admin_event);

                User singleParticipant = participants.get(position);
                Button viewFriend = (Button)convertView.findViewById(R.id.viewFriend);
                viewFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("message:", "getting to the view friend button");
                        User searchUser = participants.get(position);
                        Log.i("message:", "name: " + searchUser.getName() + "email: " + searchUser.getEmail());

                        Intent intent = new Intent(getApplicationContext(), ViewFriendActivity.class);
                        intent.putExtra("UserEmail", searchUser.getEmail());
                        startActivity(intent);
                    }
                });

                Glide.with(getApplicationContext()).load(singleParticipant.getAvatar()).asBitmap().into(showpic);
                showname.setText(singleParticipant.getName());
//                showemail.setText(singleParticipant.getEmail());

                return convertView;
            }
        };

        showParticipantslist.setAdapter(adapter);

    }
}
