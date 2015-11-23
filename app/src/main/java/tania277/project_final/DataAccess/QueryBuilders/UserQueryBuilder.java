package tania277.project_final.DataAccess.QueryBuilders;

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
    public String sendRequest(User user) {

        String url ="{ \"$set\" :"
                + "{\"friend_requests\" :[";

        url=url+user.getFriendRequests().get(0);
        for (int i=1;i<user.getFriendRequests().size();i++) {
            url=url+","+user.getFriendRequests().get(i);
        }
        url=url+"]}}";
        return url;
    }

    public String acceptRequest(User user) {

        String url ="{ \"$set\" :"
                + "{\"friends\" :[";

        url=url+"\""+user.getFriends().get(0).getEmail()+"\"";
        for (int i=1;i<user.getFriends().size();i++) {
            url=url+",\""+user.getFriends().get(i).getEmail()+"\"";
        }
        url=url+"]}}";
        return url;
    }

    public String deleteRequest(User user) {

        String url ="{ \"$set\" :"
                + "{\"friend_requests\" :[";

        url=url+user.getFriendRequests().get(0);
        for (int i=1;i<user.getFriendRequests().size();i++) {
            url=url+","+user.getFriendRequests().get(i);
        }
        url=url+"]}}";
        return url;

    }

    public String createUser(User user)
    {
        return String
                .format(" {\"email\": \"%s\", "
                                + "\"name\": \"%s\", \"avatar\": \"%s\","
                                + "\"friends\": [ ],\n"
                                + "\"run_records\": [ ],\n"
                                + "\"friend_requests\": [ ] }",
                        user.getEmail(), user.getName(),user.getAvatar());
    }



}