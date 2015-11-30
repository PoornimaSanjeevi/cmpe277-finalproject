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

import tania277.project_final.DataAccess.AsyncTask.AcceptEventAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.DeleteEventRequestAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetEventDetailsAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetEventsAsyncTask;
import tania277.project_final.Models.AppUser;
import tania277.project_final.Models.EventItem;
import tania277.project_final.Models.User;
import tania277.project_final.util.PrefUtil;

/**
 * Created by Tania on 11/16/15.
 */
public class EventFragment extends Fragment {

    ListView attending_list, invited_list;
    //PrefUtil prefUtil = new PrefUtil(getActivity());
    List<EventItem> attendingEvents, invitedEvents;
    ArrayList<EventItem> returnValuesAttending = new ArrayList<EventItem>();
    ArrayList<EventItem> returnValuesInvited = new ArrayList<EventItem>();
    TextView title_Attending, title_Invited;
    Button create,view;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.event_fragment, container, false);
        title_Attending =(TextView)getActivity().findViewById(R.id.attending);
        title_Invited =(TextView)getActivity().findViewById(R.id.invited);
        attending_list = (ListView)rootView.findViewById(R.id.attending_list);
        attending_list.setDivider(null);
        attending_list.setDividerHeight(0);
        invited_list = (ListView)rootView.findViewById(R.id.invited_list);
        invited_list.setDivider(null);
        invited_list.setDividerHeight(0);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        create = (Button)getActivity().findViewById(R.id.create_event);

        GetEventsAsyncTask task = new GetEventsAsyncTask();
        task.setUserEmail(new PrefUtil(getActivity()).getEmailId());
        task.setEventType("P");
        try
        {
            returnValuesAttending = task.execute().get();
            GetEventsAsyncTask task2 = new GetEventsAsyncTask();
            task2.setEventType("I");
            returnValuesInvited = task2.execute().get();

        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        //TODO: Get the event details and put here
        List<EventItem> temp = new ArrayList<EventItem>(); ;
        for(EventItem x: returnValuesAttending){

            EventItem e = new EventItem();
            e.setName(x.getName());
            e.setDate(x.getDate());
            e.setAdmin(x.getAdmin());
            e.setEndTime(x.getEndTime());
            e.setLocation(x.getLocation());
            e.setStartTime(x.getStartTime());
            e.setEventId(x.getEventId());
            e.setInvitedPeople(x.getInvitedPeople());
            e.setParticipants(x.getParticipants());
            temp.add(e);
        }
        updateAttending(temp);

        List<EventItem> temp2 = new ArrayList<EventItem>();
        for(EventItem x: returnValuesInvited){

            EventItem e = new EventItem();
            e.setName(x.getName());
            e.setDate(x.getDate());
            e.setAdmin(x.getAdmin());
            e.setEndTime(x.getEndTime());
            e.setLocation(x.getLocation());
            e.setStartTime(x.getStartTime());
            e.setEventId(x.getEventId());
            e.setInvitedPeople(x.getInvitedPeople());
            e.setParticipants(x.getParticipants());
            temp2.add(e);
        }
        updateInvited(temp2);


        Log.i("message:", ""+ (create== null));
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), CreateEvent.class);
                startActivity(intent);
            }
        });
    }

//    public void toggle_contents_attend(View v){
//        attending_list.setVisibility( attending_list.isShown()
//                ? View.GONE
//                : View.VISIBLE );
//    }
//
//    public void toggle_contents_invite(View v){
//        invited_list.setVisibility( invited_list.isShown()
//                ? View.GONE
//                : View.VISIBLE );
//    }

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
                TextView startTime =(TextView)convertView.findViewById(R.id.start_time);
                EventItem singleAttendingItem = EventFragment.this.attendingEvents.get(position);
                Button viewbutton = (Button)convertView.findViewById(R.id.view_event);
                viewbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("message:", "getting to the view button");
                        EventItem searchResult = EventFragment.this.attendingEvents.get(position);

                        Intent intent = new Intent(getActivity().getApplicationContext(), PopupActivity.class);
                        intent.putExtra("Event_ID", searchResult.getEventId());
                        startActivity(intent);

                    }
                });

                eventName.setText(singleAttendingItem.getName());
                eventDate.setText(singleAttendingItem.getDate());
                startTime.setText(singleAttendingItem.getStartTime());
                eventId.setText((singleAttendingItem.getEventId()));

                return convertView;
            }
        };

        attending_list.setAdapter(adapter);
    }


    public void updateInvited(List<EventItem> invitedItems)
    {
        EventFragment.this.invitedEvents = invitedItems;
        Log.i("message:", "update invited addapter reached");
        ArrayAdapter<EventItem> adapter = new ArrayAdapter<EventItem>(getActivity().getApplicationContext(), R.layout.invited_item, invitedItems) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.invited_item, parent, false);
                }

                TextView eventId =(TextView) convertView.findViewById(R.id.event_id);
                TextView eventName = (TextView) convertView.findViewById(R.id.event_name);
                TextView adminEvent =(TextView) convertView.findViewById(R.id.admin_event);

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

////TODO: Add this in the onClick() of list item of attending or invited.
//    LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//    PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.details_popup, null, false),100,100, true);
//
//    pw.showAtLocation(this.findViewById(R.id.popup), Gravity.CENTER, 0, 0);

}
