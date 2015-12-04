package tania277.project_final;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import tania277.project_final.DataAccess.AsyncTask.GetEventDetailsAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetFriendRequestsAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetUserAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetUserListAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.SendRequestAsyncTask;
import tania277.project_final.Models.AppUser;
import tania277.project_final.Models.EventItem;
import tania277.project_final.Models.User;
import tania277.project_final.util.PrefUtil;

/**
 * Created by Tania on 11/20/15.
 */
public class AddFriend extends Activity {
    private EditText searchInput;
    private ListView friendFound;
    TextView showemail,showname;
    List<String> friends= new ArrayList<>();
    List<String> friendRequests = new ArrayList<>();
    User appUser = new User();
    //PrefUtil prefUtil = new PrefUtil(this);

    GetUserListAsyncTask getUserAsyncTask;
    GetUserAsyncTask appUserAsyncTask;
    ImageView showpic;

    List<User> users;
    ArrayList<User> userReturned = new ArrayList<User>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friends_activity);
        getUserAsyncTask = new GetUserListAsyncTask();

        searchInput = (EditText)findViewById(R.id.search_input);

        friendFound = (ListView)findViewById(R.id.friend_found);

        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Log.i("message:", "Done button pressed");
                   getUserAsyncTask.setUserEmail(searchInput.getText().toString());

                    try {
                        userReturned=getUserAsyncTask.execute().get();
                        List<User> users = new ArrayList<User>();
                        for(User x: userReturned){
                            User u = new User();
                            u.setName(x.getName());
                            u.setFriendRequests(x.getFriendRequests());
                            u.setEmail(x.getEmail());
                            u.setUserId(x.getUserId());
                            u.setAvatar(x.getAvatar());
                            users.add(u);

                            appUserAsyncTask = new GetUserAsyncTask();


                            appUser = appUserAsyncTask.execute().get();

                            for (User frnd:appUser.getFriends()
                                 ) {

                                friends.add((frnd.getEmail().trim()));
                            }
                            }
                        Log.i("message:" ,"myfrnds" +friends +appUser.getFriendRequests());
                        updateFriends(users, friends, appUser.getFriendRequests());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
                return true;
            }
        });



    }
    public void updateFriends(List<User> usersIn, final List<String> f, final List<String> fr){
        this.users = usersIn;
        this.friends =f;
        this.friendRequests =fr;
        ArrayAdapter<User> adapter = new ArrayAdapter<User>(getApplicationContext(), R.layout.friend_item, usersIn) {

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.friend_item, parent, false);
                }


                showpic =(ImageView) convertView.findViewById(R.id.showpic);


                showname = (TextView) convertView.findViewById(R.id.showname);

                final User singleUser = users.get(position);




                if(singleUser.getEmail().trim().equalsIgnoreCase(new PrefUtil(AddFriend.this).getEmailId()))
                {
                    Button addfriend = (Button) convertView.findViewById(R.id.addfriend);
                    addfriend.setVisibility(View.GONE);
                }
                else if(friends.contains(singleUser.getEmail().trim()))
                {
                    Button addfriend = (Button) convertView.findViewById(R.id.addfriend);
                    addfriend.setVisibility(View.GONE);

                }
                else if(friendRequests.contains(singleUser.getEmail().trim()))
                {
                    Button addfriend = (Button) convertView.findViewById(R.id.addfriend);
                    addfriend.setVisibility(View.GONE);

                }
                else {
                               Button addfriend = (Button) convertView.findViewById(R.id.addfriend);
                               addfriend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            User searchResult = users.get(position);
                            Log.i("message:", "getting to the add friend button");
                            for (String request : searchResult.getFriendRequests()) {
                                Log.i("message: ", "request: " + request);
                            }


                            searchResult.getFriendRequests().add(new PrefUtil(AddFriend.this).getEmailId());

                            SendRequestAsyncTask requestAsyncTask = new SendRequestAsyncTask();
                            requestAsyncTask.execute(searchResult);
                            onBackPressed();

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
                }




                showname.setText(singleUser.getName());
                Glide.with(getApplicationContext()).load(singleUser.getAvatar()).asBitmap().into(showpic);

//                Glide.with(getContext()).load(singleUser.getAvatar()).asBitmap().centerCrop().into(new BitmapImageViewTarget(showpic) {
//                    @Override
//                    protected void setResource(Bitmap resource) {
//                        RoundedBitmapDrawable circularBitmapDrawable =
//                                RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
//                        circularBitmapDrawable.setCircular(true);
//                        showpic.setImageDrawable(circularBitmapDrawable);
//                    }
//                });



                return convertView;
            }
        };

        friendFound.setAdapter(adapter);
    }
}
