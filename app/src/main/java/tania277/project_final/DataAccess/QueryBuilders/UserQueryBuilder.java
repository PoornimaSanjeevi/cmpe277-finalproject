package tania277.project_final.DataAccess.QueryBuilders;

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


}