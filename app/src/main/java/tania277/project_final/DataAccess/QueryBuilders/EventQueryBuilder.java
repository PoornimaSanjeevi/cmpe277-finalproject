package tania277.project_final.DataAccess.QueryBuilders;

import android.util.Log;

import tania277.project_final.Models.EventItem;
import tania277.project_final.Models.User;

/**
 * Created by Tania on 11/19/15.
 */
public class EventQueryBuilder {
    BaseQueryBuilder qb = new BaseQueryBuilder();
    public String getEventCollection()
    {
        return "events_runbuddy";
    }


   public String buildEventsSaveURL()
    {
        return qb.getBaseUrl()+getEventCollection()+qb.docApiKeyUrl();
    }

    public String buildEventDetailsGetURL(String id)
    {
        StringBuilder s = new StringBuilder();
        s.append(qb.getBaseUrl()).append(getEventCollection()).append("?q={\"_id\":{\"$in\":[{\"$oid\":\""+id+"\"}]}}&apiKey=lhXxXAw69SlRU8my7e8jQcxW40ZBHigQ");
       return s.toString();
    }

    public String buildEventsParticipatingGetURL(String email)
    {
        return qb.getBaseUrl()+getEventCollection()+"?q={\"participants\":\""+email+"\"}"+qb.andApiKeyUrl();
    }

    public String buildEventsInvitedGetURL(String email)
    {
        return qb.getBaseUrl()+getEventCollection()+"?q={\"invited\":\""+email+"\"}"+qb.andApiKeyUrl();
    }

    public String buildEventUpdateURL(String doc_id)
    {
        return qb.getBaseUrl()+getEventCollection()+qb.docApiKeyUrl(doc_id);
    }

    public String createEvent(EventItem eventItem)
    {
        String url = String
                .format(" {\"name\": \"%s\", "
                                + "\"admin\": \"%s\", \"date\": \"%s\", "
                                + "\"start_time\": \"%s\", "+ "\"end_time\": \"%s\","
                                + " \"location\": \"%s\" ,",
                        eventItem.getName().trim(), eventItem.getAdmin().trim(),eventItem.getDate().trim(), eventItem.getStartTime().trim()
                        ,eventItem.getEndTime().trim(),eventItem.getLocation().trim());

        url=url+ "\"invited\" : [";
                if(eventItem.getInvitedPeople().size() > 0) {
                    url = url + "\""+eventItem.getInvitedPeople().get(0).trim() +"\"";
                    for(int i=1;i<eventItem.getInvitedPeople().size();i++)
                    {
                        url=url+ ",\""+eventItem.getInvitedPeople().get(i).trim() +"\"";
                    }
                }
        url=url+"], ";

        url=url+ "\"participants\" : ["+ "\"" +eventItem.getAdmin().trim()+ "\" ],";

        url=url+"\"finished_users\" :[ ]}";

        return url;
    }

    public String acceptRequest(EventItem item) {

        String url ="{ \"$set\" :"
                + "{\"participants\" :[";

        if (item.getParticipants().size() > 0) {
        url=url+"\""+item.getParticipants().get(0).trim()+"\"";
        for (int i=1;i<item.getParticipants().size();i++) {
            url=url+",\""+item.getParticipants().get(i).trim()+"\"";
        }
        }
        url=url+"]}}";
        return url;
    }



    public String deleteRequest(EventItem item) {

        String url ="{ \"$set\" :"
                + "{\"invited\" :[";

        if(item.getInvitedPeople().size()>0){
        url=url+"\""+item.getInvitedPeople().get(0).trim()+"\"";
        for (int i=1;i<item.getInvitedPeople().size();i++) {
            url=url+",\""+item.getInvitedPeople().get(i).trim()+"\"";
        }}
        url=url+"]}}";
        return url;

    }



}
