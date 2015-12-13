package tania277.project_final;

/**
 * Created by Tania on 11/16/15.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ImageButton;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.List;

import tania277.project_final.Adapter.RunRecordAdapter;
import tania277.project_final.DataAccess.AsyncTask.CreateUserAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetUserAsyncTask;
import tania277.project_final.Models.AppUser;
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
    RunRecordAdapter runRecordAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.user_fragment, container, false);
        TextView info = (TextView) rootView.findViewById(R.id.info);
        TextView email = (TextView) rootView.findViewById(R.id.emailid);
        final ImageView profileImgView = (ImageView) rootView.findViewById(R.id.profile_pic);


        PrefUtil pu = new PrefUtil(getActivity());
        info.setText("Welcome: " + pu.getUserId());
        info.setShadowLayer(1, 0, 0, Color.BLACK);
        email.setText(pu.getEmailId());

        Glide.with(getActivity()).load(pu.getProfImage()).asBitmap().centerCrop().into(new BitmapImageViewTarget(profileImgView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                profileImgView.setImageDrawable(circularBitmapDrawable);
            }
        });

        return rootView;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PrefUtil pu = new PrefUtil(getActivity());
        GetUserAsyncTask getUserDetailsAsyncTask = new GetUserAsyncTask();
        run_records = (ListView)rootView.findViewById(R.id.run_records);
        run_records.addHeaderView(new View(getActivity()));
        run_records.addFooterView(new View(getActivity()));

        getUserDetailsAsyncTask.setUserEmail(pu.getEmailId());
        try {
            user = getUserDetailsAsyncTask.execute().get();

            if ((user.getName() == null) && (user.getEmail() == null) && (user.getAvatar() == null)) {
                Log.i("message:", "user is null, new");
                user.setEmail(pu.getEmailId());
                user.setName(pu.getUserId());
                user.setAvatar(pu.getProfImage());
                createUserAsyncTask = new CreateUserAsyncTask();
                createUserAsyncTask.execute(user);
            } else {

                Log.i("message:", "Event item obtained" + user.getName());
                if(user.getRunRecords()!=null) {
                    runRecordAdapter = new RunRecordAdapter(getActivity().getApplicationContext(), R.layout.run_record_item, this);

//                    for (int i = 0; i < user.getRunRecords().size(); i++)
                        for(int i=user.getRunRecords().size()-1;i>=0;i--){
                        Log.i("message_runbuddy","inside for");
//                        RunRecord card = new RunRecord("Card " + (i+1) + " Line 1", "Card " + (i+1) + " Line 2", "Card " + (i+1));
                        runRecordAdapter.add(user.getRunRecords().get(i));
                    }
                    run_records.setAdapter(runRecordAdapter);
//                    updateRunRecords(user.getRunRecords());
                }
            }



        }
        catch(Exception e){
            Log.i("message", "Exception" + e.getMessage());
        }

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

                Button map=(Button) convertView.findViewById(R.id.mapBtn);
               final RunRecord singleRunRecord = records.get(position);
                map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), PlotTrack.class);
                        intent.putExtra("latlongs", singleRunRecord.getPath());
                        intent.putExtra("distance", singleRunRecord.getDistanceRan());
                        intent.putExtra("timeTaken", singleRunRecord.getTimeRan());
                        intent.putExtra("name", singleRunRecord.getEventName());
                        startActivity(intent);
                    }
                });




                showename.setText(singleRunRecord.getEventName());
                showdistance.setText(singleRunRecord.getDistanceRan());
                showtime.setText(singleRunRecord.getTimeRan());

                return convertView;
            }
        };

        run_records.setAdapter(adapter);
    }


}