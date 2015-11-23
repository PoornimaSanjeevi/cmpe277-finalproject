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

import tania277.project_final.DataAccess.AsyncTask.GetFriendsAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetUserAsyncTask;
import tania277.project_final.Models.EventItem;
import tania277.project_final.Models.User;

/**
 * Created by Tania on 11/16/15.
 */
public class FriendFragment extends Fragment{

    private ListView showfriendlist;
    GetFriendsAsyncTask tasks;
    List<User> frnds;
    User user;
    ArrayList<User>  userReturned;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.friend_fragment, container, false);

        showfriendlist = (ListView)rootView.findViewById(R.id.showfriendlist);

        tasks = new GetFriendsAsyncTask();
        tasks.setUserEmail("user@gmail.com");
        try {
           userReturned= tasks.execute().get();
            //user=userReturned;
            Log.i("message:","Is user null "+(user==null));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        List<User> temp = new ArrayList<User>(); ;
        for(User x: userReturned){
            temp.add(x);
        }
        updateShowFriends(temp);

        Button addfriend = (Button) rootView.findViewById(R.id.addfriend);
        Log.i("message:",""+(addfriend==null));
        addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), AddFriend.class);
//                intent.putExtra("VIDEO_ID", searchResults.get(pos).getId());
                startActivity(intent);
            }
        });
        return rootView;
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

                TextView showname =(TextView) convertView.findViewById(R.id.showname);
//                TextView eventName = (TextView) convertView.findViewById(R.id.event_name);
//                TextView eventDate=(TextView) convertView.findViewById(R.id.event_date);
//                TextView eventAdmin = (TextView)convertView.findViewById(R.id.event_admin);
//                TextView startTime =(TextView)convertView.findViewById(R.id.start_time);
//                TextView endTime =(TextView)convertView.findViewById(R.id.end_time);
//                TextView location =(TextView)convertView.findViewById(R.id.event_loc);
                User singleFriend = FriendFragment.this.frnds.get(position);
//                Button viewbutton = (Button)convertView.findViewById(R.id.view_event);
//                viewbutton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.i("message:", "getting to the view button");
//                        EventItem searchResult = EventFragment.this.attendingEvents.get(position);
//
////                        Log.i("message:", "name: " + searchResult.getName() + "id: " + searchResult.getEventId());
//
////                        addToPlayList(searchResult.getId(), searchResult.getTitle());
//                        Intent intent = new Intent(getActivity().getApplicationContext(), PopupActivity.class);
//                        intent.putExtra("Event_ID", searchResult.getEventId());
//                        startActivity(intent);
//
//                    }
//                });

//                Picasso.with(getActivity().getApplicationContext()).load(searchResult.getThumbnailURL()).into(thumbnail);
                showname.setText(singleFriend.getName());
//                eventDate.setText(singleAttendingItem.getDate());
//                eventAdmin.setText(singleAttendingItem.getAdmin());
//                startTime.setText(singleAttendingItem.getStartTime());
//                endTime.setText(singleAttendingItem.getEndTime());
//                location.setText(singleAttendingItem.getLocation());

//                Log.i("message","The event id is when setting"+singleAttendingItem.getEventId());
//                eventId.setText((singleAttendingItem.getEventId()));

                return convertView;
            }
        };

        showfriendlist.setAdapter(adapter);
    }
}
