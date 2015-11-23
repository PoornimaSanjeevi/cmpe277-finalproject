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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Objects;

import tania277.project_final.DataAccess.AsyncTask.AcceptFriendAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.CreateUserAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.DeleteFriendRequestAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetFriendsAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetUserAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.SendRequestAsyncTask;
import tania277.project_final.Models.User;
import tania277.project_final.util.PrefUtil;

public class UserFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myInflatedView = inflater.inflate(R.layout.user_fragment, container, false);
        TextView info = (TextView) myInflatedView.findViewById(R.id.info);
        TextView email = (TextView) myInflatedView.findViewById(R.id.emailid);
        ImageView profileImgView = (ImageView) myInflatedView.findViewById(R.id.profile_pic);
        PrefUtil pu = new PrefUtil(getActivity());
        info.setText("Welcome " + pu.getUserId());
        email.setText("Email Id:" + pu.getEmailId());
        Glide.with(getActivity())
                .load(pu.getProfImage())
                .into(profileImgView);


        return myInflatedView;
    }
}