package tania277.project_final.util;

import android.util.Log;

import java.util.ArrayList;

import tania277.project_final.DataAccess.AsyncTask.AcceptFriendAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.CreateUserAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetFriendsAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.GetUserAsyncTask;
import tania277.project_final.DataAccess.AsyncTask.SendRequestAsyncTask;
import tania277.project_final.Models.User;

/**
 * Created by Srinidhi on 11/22/2015.
 */
public class AsyncTasksDocumentation {
    public void GetUserGetFriendRequestsSendRequestExample(){
        GetUserAsyncTask task = new GetUserAsyncTask();
        task.setUserEmail("user@gmail.com");

        SendRequestAsyncTask sendtask = new SendRequestAsyncTask();
        try {
            User user = task.execute().get();
            Log.i("message: ", "name,email,avatar" + user.getName() + user.getEmail() + user.getAvatar());
            for(String friendRequest:user.getFriendRequests()) {
                Log.i("message: ", "friend requests: " + friendRequest);
            }
            user.getFriendRequests().add("\"user1@gmail.com\"");
            sendtask.execute(user);

            for(String friendRequest:user.getFriendRequests()) {
                Log.i("message: ", "friend requests: " + friendRequest);
            }

        }catch(Exception e){
            Log.i("message: ",""+e.getMessage());
        }
    }

    public void getFriendsExample()
    {
        GetFriendsAsyncTask task = new GetFriendsAsyncTask();
        task.setUserEmail("user@gmail.com");

        SendRequestAsyncTask sendtask = new SendRequestAsyncTask();
        try {
            ArrayList<User> friends = task.execute().get();

                for(User friend: friends ) {
                Log.i("message: ", "friends: " + friend.getName() +friend.getEmail() +friend.getAvatar());
            }
            User user = new User();
            user.setEmail("user4@gmail.com");

            friends.add(user);




        }catch(Exception e){
            Log.i("message: ",""+e.getMessage().toString());
        }

    }

    public void AccepRequestExample(){
        GetUserAsyncTask userTask = new GetUserAsyncTask();

        GetFriendsAsyncTask task = new GetFriendsAsyncTask();

        AcceptFriendAsyncTask acceptTask = new AcceptFriendAsyncTask();

        try {
            userTask.setUserEmail("user@gmail.com");
            User user = userTask.execute().get();

            task.setUserEmail(user.getEmail());
            ArrayList<User> friends = task.execute().get();

            for(User friend: friends ) {
                Log.i("message: ", "friends: " + friend.getName() +friend.getEmail() +friend.getAvatar());
            }

            User friend = new User();
            friend.setEmail("f4");

            friends.add(friend);

            user.setFriends(friends);

            acceptTask.execute(user);

        }catch(Exception e){
            Log.i("message: ",""+e.getMessage().toString());
        }






    }

    public void createUserExample()
    {
        User  user = new User();
        user.setName("name1");
        user.setEmail("email1");
        user.setAvatar("avatar 3");
        CreateUserAsyncTask task = new CreateUserAsyncTask();
        task.execute(user);
    }

}
