package tania277.project_final;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
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

/**
 * Created by Tania on 11/20/15.
 */
public class AddFriend extends Activity {
    private EditText searchInput;
    private ListView friendFound;
    TextView showemail,showname;
    GetUserListAsyncTask getUserAsyncTask;

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
                            Log.i("message:", "the user from DB is" + x.getEmail());
                            u.setEmail(x.getEmail());
                            users.add(u);
                        }
//                        ArrayAdapter<User> userforadapter = new ArrayAdapter<User>(this,)
//                        setListAdapter(new ArrayAdapter<User>(R.layout.friend_item,R.id.friend_found,userReturned));
                        updateFriends(users);
//                        Log.i("message:", "Event item obtained" + item.getName());
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
    public void updateFriends(List<User> usersIn){
        this.users = usersIn;
        Log.i("message:", "update attending addapter reached");
        ArrayAdapter<User> adapter = new ArrayAdapter<User>(getApplicationContext(), R.layout.friend_item, usersIn) {

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                Log.i("message:", "getView in adapter is reached");
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.friend_item, parent, false);
                }

//                ImageView showpic =(ImageView) findViewById(R.id.showpic);
               // showemail = (TextView) convertView.findViewById(R.id.showemail);

                showname = (TextView) convertView.findViewById(R.id.showname);

                final User singleUser = users.get(position);
                Log.i("message: ","fr"+singleUser.getFriendRequests());

                Button addfriend = (Button)convertView.findViewById(R.id.addfriend);
                Log.i("message: ", ""+(addfriend==null));
                addfriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        User searchResult = users.get(position);
                        Log.i("message:", "getting to the add friend button" + (searchResult == null));
                        for (String request:searchResult.getFriendRequests()) {
                            Log.i("message: ","request: "+request);
                        }

                        searchResult.getFriendRequests().add(AppUser.EMAIL);

                        SendRequestAsyncTask requestAsyncTask = new SendRequestAsyncTask();
                        requestAsyncTask.execute(searchResult);

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

                Log.i("message:",""+(singleUser==null) + (showemail==null));
//                Picasso.with(getActivity().getApplicationContext()).load(searchResult.getThumbnailURL()).into(thumbnail);
               // showemail.setText(singleUser.getEmail());
                showname.setText(singleUser.getName());

                Log.i("message", "The User email is when setting" + singleUser.getEmail());

                return convertView;
            }
        };
        Log.i("message:", "adapter ending");
        friendFound.setAdapter(adapter);
    }
}
