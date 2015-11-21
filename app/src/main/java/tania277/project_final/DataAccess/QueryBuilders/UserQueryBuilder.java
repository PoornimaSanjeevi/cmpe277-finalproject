package tania277.project_final.DataAccess.QueryBuilders;

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

    public String sendRequest(User user) {

        String url ="{ \"$set\" :"
                + "{\"friend_requests\" :[";

        url=url+"\""+user.getFriendRequests().get(0)+"\"";
        for (int i=1;i<user.getFriendRequests().size();i++) {
            url=url+",\""+user.getFriendRequests().get(i)+"\"";
        }
        url=url+"]}}";
        return url;
    }

}