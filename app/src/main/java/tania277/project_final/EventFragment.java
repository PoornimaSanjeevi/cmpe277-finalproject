package tania277.project_final;
//import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import tania277.project_final.DataAccess.AsyncTask.GetEventDetailsAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetEventsAsyncTask;
import tania277.project_final.Models.EventItem;

/**
 * Created by Tania on 11/16/15.
 */
public class EventFragment extends Fragment {

     ListView attending_list, invited_list;
    List<EventItem> attendingEvents;
    ArrayList<EventItem> returnValues = new ArrayList<EventItem>();
    TextView title_Attending, title_Invited;
    Button create,view;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.event_fragment, container, false);
        title_Attending =(TextView)getActivity().findViewById(R.id.attending);
        title_Invited =(TextView)getActivity().findViewById(R.id.invited);
        attending_list = (ListView)rootView.findViewById(R.id.attending_list);
//        onClickCallView();

        //TODO: Toggle function
//        title_Attending.setVisibility(View.GONE);
//        title_Invited.setVisibility(View.GONE);
//        return inflater.inflate(R.layout.event_fragment, container, false);
        return rootView;




    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        invited_list = (ListView)getActivity().findViewById(R.id.invited_list);

        create = (Button)getActivity().findViewById(R.id.create_event);


        GetEventsAsyncTask task = new GetEventsAsyncTask();
        try
        {
            returnValues = task.execute().get();
            Log.i("Message: ", "GetEventsAsyncTask success!");
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        //TODO: Get the event details and put here
        List<EventItem> temp = new ArrayList<EventItem>(); ;
        for(EventItem x: returnValues){


            EventItem e = new EventItem();
            e.setName(x.getName());
            e.setDate(x.getDate());
            e.setAdmin(x.getAdmin());
            e.setEndTime(x.getEndTime());
            e.setLocation(x.getLocation());
            e.setStartTime(x.getStartTime());
            e.setEventId(x.getEventId());
            temp.add(e);
        }
        updateAttending(temp);


        Log.i("message:", ""+ (create== null));
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), CreateEvent.class);
                startActivity(intent);
            }
        });
    }

    public void toggle_contents_attend(View v){
        attending_list.setVisibility( attending_list.isShown()
                ? View.GONE
                : View.VISIBLE );
    }

    public void toggle_contents_invite(View v){
        invited_list.setVisibility( invited_list.isShown()
                ? View.GONE
                : View.VISIBLE );
    }

    public void updateAttending(List<EventItem> attendingItems){
        EventFragment.this.attendingEvents = attendingItems;
        Log.i("message:", "update attending addapter reached");
        ArrayAdapter<EventItem> adapter = new ArrayAdapter<EventItem>(getActivity().getApplicationContext(), R.layout.attending_item, attendingItems) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.attending_item, parent, false);
                }

                TextView eventId =(TextView) convertView.findViewById(R.id.event_id);
                TextView eventName = (TextView) convertView.findViewById(R.id.event_name);
                TextView eventDate=(TextView) convertView.findViewById(R.id.event_date);
                TextView eventAdmin = (TextView)convertView.findViewById(R.id.event_admin);
                TextView startTime =(TextView)convertView.findViewById(R.id.start_time);
                TextView endTime =(TextView)convertView.findViewById(R.id.end_time);
                TextView location =(TextView)convertView.findViewById(R.id.event_loc);
                EventItem singleAttendingItem = EventFragment.this.attendingEvents.get(position);
                Button viewbutton = (Button)convertView.findViewById(R.id.view_event);
                viewbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("message:", "getting to the view button");
                        EventItem searchResult = EventFragment.this.attendingEvents.get(position);

//                        Log.i("message:", "name: " + searchResult.getName() + "id: " + searchResult.getEventId());

//                        addToPlayList(searchResult.getId(), searchResult.getTitle());
                        Intent intent = new Intent(getActivity().getApplicationContext(), PopupActivity.class);
                intent.putExtra("Event_ID", searchResult.getEventId());
                        startActivity(intent);

                    }
                });

//                Picasso.with(getActivity().getApplicationContext()).load(searchResult.getThumbnailURL()).into(thumbnail);
                eventName.setText(singleAttendingItem.getName());
                eventDate.setText(singleAttendingItem.getDate());
                eventAdmin.setText(singleAttendingItem.getAdmin());
                startTime.setText(singleAttendingItem.getStartTime());
                endTime.setText(singleAttendingItem.getEndTime());
                location.setText(singleAttendingItem.getLocation());

               // Log.i("message","The event id is when setting"+singleAttendingItem.getEventId());
                eventId.setText((singleAttendingItem.getEventId()));

                return convertView;
            }
        };

        attending_list.setAdapter(adapter);
    }

////TODO: Add this in the onClick() of list item of attending or invited.
//    LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//    PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.details_popup, null, false),100,100, true);
//
//    pw.showAtLocation(this.findViewById(R.id.popup), Gravity.CENTER, 0, 0);

}
