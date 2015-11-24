package tania277.project_final;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import tania277.project_final.DataAccess.AsyncTask.GetFriendRequestsAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetFriendsAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetUserAsyncTask;
import tania277.project_final.Models.EventItem;
import tania277.project_final.Models.User;

/**
 * Created by Tania on 11/16/15.
 */
//Display all the Friends
public class FriendFragment extends Fragment{

    private ListView showfriendlist;
    GetFriendsAsyncTask friendTask;
    GetUserAsyncTask usertask;
    GetFriendRequestsAsyncTask requesttask;

    //returned from DB
    ArrayList<User>  userReturned;

    //List to pass it to adapter
    List<User> frnds;

    User user = new User();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friend_fragment, container, false);

        showfriendlist = (ListView)rootView.findViewById(R.id.showfriendlist);

        friendTask = new GetFriendsAsyncTask();
        usertask = new GetUserAsyncTask();
        requesttask = new GetFriendRequestsAsyncTask();

        //TODO : Should happen dynamically. Dont hard code
        friendTask.setUserEmail("user@gmail.com");
        usertask.setUserEmail("user@gmail.com");


        //Call backend
        try
        {
           userReturned= friendTask.execute().get();
           user= usertask.execute().get();
        }
        catch (Exception e) {
            Log.i("message","Exception : "+e.getMessage());
        }

        List<User> friendRequestSenders = new ArrayList<User>();


        requesttask.setRequestEmails(user.getFriendRequests());
            try {

                friendRequestSenders=requesttask.execute().get();
                updateFriendRequestSenders(friendRequestSenders);
            } catch (Exception e) {
                e.printStackTrace();
            }





        //Convert ArrayList to List
        List<User> temp = new ArrayList<User>(); ;
        for(User x: userReturned){
            temp.add(x);
        }

        //Calling Adapter to display friends
        updateShowFriends(temp);



        //Clicking takes to a search page, to add new friends
        Button addfriend = (Button) rootView.findViewById(R.id.addfriend);

        addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AddFriend.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

    public void updateFriendRequestSenders(List<User> users){
        FriendFragment.this.frnds = users;
        Log.i("message:", "update attending addapter reached");
        ArrayAdapter<User> adapter = new ArrayAdapter<User>(getActivity().getApplicationContext(), R.layout.show_friend_item, users) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.show_friend_item, parent, false);
                }

                //TODO: Map Image View
                TextView showname =(TextView) convertView.findViewById(R.id.showname);
                TextView showemail =(TextView) convertView.findViewById(R.id.showemail);

                User singleFriend = FriendFragment.this.frnds.get(position);
                Button viewbutton = (Button)convertView.findViewById(R.id.viewFriend);
                viewbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("message:", "getting to the view friend button");
                        User searchUser = FriendFragment.this.frnds.get(position);
                        Log.i("message:", "name: " + searchUser.getName() + "email: " + searchUser.getEmail());

                        Intent intent = new Intent(getActivity().getApplicationContext(), ViewFriendActivity.class);
                        intent.putExtra("UserEmail", searchUser.getEmail());
                        startActivity(intent);
                    }
                });

                //TODO: set avatar for friends
                //Picasso.with(getActivity().getApplicationContext()).load(searchResult.getThumbnailURL()).into(thumbnail);
                showname.setText(singleFriend.getName());
                showemail.setText(singleFriend.getEmail());


                return convertView;
            }
        };

        showfriendlist.setAdapter(adapter);
    }

    public void updateShowFriends(List<User> users){
        FriendFragment.this.frnds = users;
        Log.i("message:", "update attending addapter reached");
        ArrayAdapter<User> adapter = new ArrayAdapter<User>(getActivity().getApplicationContext(), R.layout.show_friend_item, users) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.show_friend_item, parent, false);
                }

                //TODO: Map Image View
                TextView showname =(TextView) convertView.findViewById(R.id.showname);
                TextView showemail =(TextView) convertView.findViewById(R.id.showemail);

                User singleFriend = FriendFragment.this.frnds.get(position);
                Button viewbutton = (Button)convertView.findViewById(R.id.viewFriend);
                viewbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("message:", "getting to the view friend button");
                        User searchUser = FriendFragment.this.frnds.get(position);
                        Log.i("message:", "name: " + searchUser.getName() + "email: " + searchUser.getEmail());

                        Intent intent = new Intent(getActivity().getApplicationContext(), ViewFriendActivity.class);
                        intent.putExtra("UserEmail", searchUser.getEmail());
                        startActivity(intent);
                    }
                });

                //TODO: set avatar for friends
                //Picasso.with(getActivity().getApplicationContext()).load(searchResult.getThumbnailURL()).into(thumbnail);
                showname.setText(singleFriend.getName());
                showemail.setText(singleFriend.getEmail());


                return convertView;
            }
        };

        showfriendlist.setAdapter(adapter);
    }
}
