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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import tania277.project_final.DataAccess.AsyncTask.GetFriendsAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetUserListAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.SendRequestAsyncTask;
import tania277.project_final.Models.AppUser;
import tania277.project_final.Models.User;
import tania277.project_final.util.PrefUtil;

/**
 * Created by Srinidhi on 11/26/2015.
 */
public class InviteFriendsActivity extends Activity {
    private List<User> myFriendList;
    private List<String> invitedFriends =new ArrayList<String>();
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
        getFreindsAsyncTask.setUserEmail(new PrefUtil(this).getEmailId());
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


                final CheckBox sendinvite=(CheckBox)convertView.findViewById(R.id.sendinvite);

                sendinvite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        User searchResult = myFriendList.get(position);
                        invitedFriends.add(searchResult.getEmail());
                        Log.i("message:", "getting to the invite friend button");
                    }
                });


                final User singleFriend = myFriendList.get(position);
                showname.setText(singleFriend.getName());
                Glide.with(getApplicationContext()).load(singleFriend.getAvatar()).asBitmap().into(showpic);
                return convertView;
            }
        };
        Log.i("message:", "adapter ending");
        myFriendsListView.setAdapter(adapter);
    }

}

