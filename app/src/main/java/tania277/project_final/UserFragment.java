package tania277.project_final;

/**
 * Created by Tania on 11/16/15.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import tania277.project_final.DataAccess.AsyncTask.AcceptFriendAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.CreateUserAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.DeleteFriendRequestAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetFriendsAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetUserAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.SendRequestAsyncTask;
import tania277.project_final.Models.EventItem;
import tania277.project_final.Models.RunRecord;
import tania277.project_final.Models.User;
import tania277.project_final.util.PrefUtil;

public class UserFragment extends Fragment {

    ListView run_records;
    List<RunRecord> records;
    CreateUserAsyncTask createUserAsyncTask;
//    ArrayList<EventItem> returnValues = new ArrayList<EventItem>();
    View rootView;
    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.user_fragment, container, false);
        TextView info = (TextView) rootView.findViewById(R.id.info);
        TextView email = (TextView) rootView.findViewById(R.id.emailid);
        ImageView profileImgView = (ImageView) rootView.findViewById(R.id.profile_pic);
        run_records = (ListView)rootView.findViewById(R.id.run_records);
        PrefUtil pu = new PrefUtil(getActivity());
        info.setText("Welcome " + pu.getUserId());
        email.setText(pu.getEmailId());
        Glide.with(getActivity())
                .load(pu.getProfImage())
                .into(profileImgView);





        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        GetUserAsyncTask getUSerDetailsAsyncTask = new GetUserAsyncTask();



        getUSerDetailsAsyncTask.setUserEmail("user@gmail.com");

        try {
            user=getUSerDetailsAsyncTask.execute().get();
            Log.i("message:", "Event item obtained" + user.getName());
            for(RunRecord record : user.getRunRecords())
            {
                Log.i("message: ",""+record.getEventName()+record.getDistanceRan()+record.getTimeRan());
            }
        } catch (Exception e) {
            Log.i("message", "Exception" + e.getMessage());
        }

        updateRunRecords(user.getRunRecords());
    }

    public void updateRunRecords(final List<RunRecord> records){
        this.records = records;
        Log.i("message:", "update Run Records adapter reached");
        ArrayAdapter<RunRecord> adapter = new ArrayAdapter<RunRecord>(getActivity().getApplicationContext(), R.layout.run_record_item, records) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.run_record_item, parent, false);
                }

                TextView showename =(TextView) convertView.findViewById(R.id.showename);
                TextView showdistance = (TextView) convertView.findViewById(R.id.showdistance);
                TextView showtime=(TextView) convertView.findViewById(R.id.showtime);


                RunRecord singleRunRecord = records.get(position);


                showename.setText(singleRunRecord.getEventName());
                showdistance.setText(singleRunRecord.getDistanceRan());
                showtime.setText(singleRunRecord.getTimeRan());

                return convertView;
            }
        };

        run_records.setAdapter(adapter);
    }
}