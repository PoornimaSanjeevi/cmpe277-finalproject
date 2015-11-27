package tania277.project_final;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;
import java.util.List;

import tania277.project_final.DataAccess.AsyncTask.GetFriendsAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetUserListAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.SendRequestAsyncTask;
import tania277.project_final.Models.AppUser;
import tania277.project_final.Models.User;

/**
 * Created by Srinidhi on 11/26/2015.
 */
public class InviteFriendsActivity extends Activity {
    private List<User> myFriendList;
    private List<User> invitedFriends =new ArrayList<User>();
    private ListView myFriendsListView;
    Button inviteFriend, doneInvite;
    TextView showname;
    ImageView showpic;



    GetFriendsAsyncTask getFreindsAsyncTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);
        myFriendsListView = (ListView)findViewById(R.id.showfriendlist);
        doneInvite =(Button) findViewById(R.id.doneInvite);

        doneInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateEvent.setInvitedPeople(invitedFriends);
                onBackPressed();
            }
        });

        getFreindsAsyncTask = new GetFriendsAsyncTask();
        getFreindsAsyncTask.setUserEmail(AppUser.EMAIL);
        try {
            myFriendList = getFreindsAsyncTask.execute().get();

            updateFriendList(myFriendList);
        }catch(Exception e)
        {
            e.printStackTrace();
        }


    }

    public void updateFriendList(List<User> myfriends)
    {
        this.myFriendList = myfriends;
        ArrayAdapter<User> adapter = new ArrayAdapter<User>(getApplicationContext(), R.layout.invite_friend_item, myfriends) {

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.invite_friend_item, parent, false);
                }


                showpic =(ImageView) convertView.findViewById(R.id.showpic);
                showname = (TextView) convertView.findViewById(R.id.showname);
                inviteFriend =(Button) convertView.findViewById(R.id.sendinvite);

                final User singleFriend = myFriendList.get(position);

                inviteFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        User searchResult = myFriendList.get(position);
                        invitedFriends.add(searchResult);
                        Log.i("message:", "getting to the invite friend button");
//                        for (String request:searchResult.getFriendRequests()) {
//                    Log.i("message: ","request: "+request);
//                }
//
//                searchResult.getFriendRequests().add(AppUser.EMAIL);
//
//                SendRequestAsyncTask requestAsyncTask = new SendRequestAsyncTask();
//                requestAsyncTask.execute(searchResult);

//                        EventItem searchResult = EventFragment.this.attendingEvents.get(position);
//
//                        Log.i("message:", "name: " + searchResult.getName() + "id: " + searchResult.getEventId());
//
////                        addToPlayList(searchResult.getId(), searchResult.getTitle());
//                        Intent intent = new Intent(getActivity().getApplicationContext(), PopupActivity.class);
//                        intent.putExtra("Event_ID", searchResult.getEventId());
//                        startActivity(intent);

                    }
                });


                showname.setText(singleFriend.getName());

                Glide.with(getContext()).load(singleFriend.getAvatar()).asBitmap().centerCrop().into(new BitmapImageViewTarget(showpic) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        showpic.setImageDrawable(circularBitmapDrawable);
                    }
                });
                return convertView;
            }
        };
        Log.i("message:", "adapter ending");
        myFriendsListView.setAdapter(adapter);
    }
}

