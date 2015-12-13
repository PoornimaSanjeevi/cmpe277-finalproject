package tania277.project_final.DataAccess.QueryBuilders;

import android.util.Log;
import java.util.List;

import tania277.project_final.Models.EventItem;
import tania277.project_final.Models.User;

/**
 * Created by Tania on 11/19/15.
 */
public class UserQueryBuilder {

    BaseQueryBuilder qb= new BaseQueryBuilder();
    public String getUserCollection()
    {
        return "users_runbuddy";
    }

    public String buildUserDetailsGetURL(String email)
    {
        StringBuilder s = new StringBuilder();
        s.append(qb.getBaseUrl()).append(getUserCollection()).append("?q={\"email\":\""+email+"\"}"+qb.andApiKeyUrl());
        return s.toString();
    }

    public String buildUserUpdateURL(String doc_id)
    {
        return qb.getBaseUrl()+getUserCollection()+qb.docApiKeyUrl(doc_id);
    }

    public String buildUserSaveURL()
    {
        return qb.getBaseUrl()+getUserCollection()+qb.docApiKeyUrl();
    }

    public String buildFriendsGetURL(String email)
    {
        return qb.getBaseUrl()+getUserCollection()+"?q={\"friends\":\""+email+"\"}"+qb.andApiKeyUrl();
    }

    public String buildFriendRequestsGetURL(List<String> emailList) {
        //TODO: Make this a valid Query q={"email":{"$in":["user3@gmail.com"]}}
        //q={"email":{"$in":["user@gmail.com","user2@gmail.com"]}}
        String url = qb.getBaseUrl() + getUserCollection() + "?q={\"email\":{\"$in\":[";
        if (emailList.size() > 0) {
            url = url + "\""+emailList.get(0).trim()+"\"";
            for (int i = 1; i < emailList.size(); i++)
                url = url + ",\"" + emailList.get(i).trim() + "\"";
        }

        url = url + "]}}" +qb.andApiKeyUrl();
        return url;
    }

    public String sendRequest(User user) {

        String url ="{ \"$set\" :"
                + "{\"friend_requests\" :[";

        if(user.getFriendRequests().size()>0){
        url=url+"\""+user.getFriendRequests().get(0).trim()+"\"";
        for (int i=1;i<user.getFriendRequests().size();i++) {
            url=url+",\""+user.getFriendRequests().get(i).trim()+"\"";
        }}
        url=url+"]}}";
        return url;
    }

    public String acceptRequest(User user) {

        String url ="{ \"$set\" :"
                + "{\"friends\" :[";

        if(user.getFriends().size()>0){
        url=url+"\""+user.getFriends().get(0).getEmail().trim()+"\"";
        for (int i=1;i<user.getFriends().size();i++) {
            url=url+",\""+user.getFriends().get(i).getEmail().trim()+"\"";
        }}
        url=url+"]}}";
        return url;
    }

    public String deleteRequest(User user) {

        String url ="{ \"$set\" :"
                + "{\"friend_requests\" :[";

        if(user.getFriendRequests().size()>0){
        url=url+"\""+user.getFriendRequests().get(0).trim()+"\"";
        for (int i=1;i<user.getFriendRequests().size();i++) {
            url=url+",\""+user.getFriendRequests().get(i).trim()+"\"";
        }}
        url=url+"]}}";
        return url;

    }

    public String createUser(User user)
    {
        return String
                .format(" {\"email\": \"%s\", "
                                + "\"name\": \"%s\", \"avatar\": \"%s\","
                                + "\"friends\": [ ],\n"
                                + "\"run_records\": { },\n"
                                + "\"friend_requests\": [ ] }",
                        user.getEmail().trim(), user.getName().trim(),user.getAvatar().trim());
    }

    public String updateCoordinates(User user) {

        String url ="{ \"$set\" :"
                + "{\"run_records\" :{";

        if(user.getRunRecords().size()>0){
            url=url+"\"1\" : ["+user.getRunRecords().get(0).getEventId().trim()+"\"";
            url=url+"\","+user.getRunRecords().get(0).getEventName().trim()+"\"";
            url=url+"\","+user.getRunRecords().get(0).getDistanceRan().trim()+"\"";
            url=url+"\","+user.getRunRecords().get(0).getTimeRan().trim()+"\"";

            if(user.getRunRecords().get(0).getPath().size()>0) {
                url = url + "\"," + user.getRunRecords().get(0).getPath().get(0).getLatitude()
                        + "," + user.getRunRecords().get(0).getPath().get(0).getLongitude()
                        + "\"";

                for (int i = 1; i < user.getRunRecords().get(0).getPath().size(); i++) {
                    url = url + ",\"," + user.getRunRecords().get(0).getPath().get(i).getLatitude()
                            + "," + user.getRunRecords().get(0).getPath().get(i).getLongitude()
                            + "\"";
                }
            }


            for (int i=1;i<user.getRunRecords().size();i++) {
                url=url+",\""+i+1+"\" : ["+user.getRunRecords().get(i).getEventId().trim()+"\"";
                url=url+"\","+user.getRunRecords().get(i).getEventName().trim()+"\"";
                url=url+"\","+user.getRunRecords().get(i).getDistanceRan().trim()+"\"";
                url=url+"\","+user.getRunRecords().get(i).getTimeRan().trim()+"\"";

                if(user.getRunRecords().get(i).getPath().size()>0) {
                    url = url + "\"," + user.getRunRecords().get(i).getPath().get(0).getLatitude()
                            + "," + user.getRunRecords().get(i).getPath().get(0).getLongitude()
                            + "\"";

                    for (int j = 1; j < user.getRunRecords().get(i).getPath().size(); j++) {
                        url = url + ",\"," + user.getRunRecords().get(i).getPath().get(j).getLatitude()
                                + "," + user.getRunRecords().get(i).getPath().get(j).getLongitude()
                                + "\"";
                    }
                }
            }}
        url=url+"]}}";
        return url;
    }


    public String updateCurrentLocation(User user)
    {
        String url ="{ \"$set\" :"
                + "{\"current_location\" :[ \"";

      url=url+user.getLatLang().getLatitude()+"|"+user.getLatLang().getLongitude()+"\"]}}";

        return url;
    }



}
