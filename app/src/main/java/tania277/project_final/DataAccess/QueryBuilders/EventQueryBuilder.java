package tania277.project_final.DataAccess.QueryBuilders;

import tania277.project_final.Models.EventItem;

/**
 * Created by Tania on 11/19/15.
 */
public class EventQueryBuilder {
    BaseQueryBuilder qb = new BaseQueryBuilder();
    public String getEventCollection()
    {
        return "events_runbuddy";
    }



//    public String buildEventsGetURL()
//    {
//        return qb.getBaseUrl()+getEventCollection()+qb.docApiKeyUrl();
//    }

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

    public String createEvent(EventItem eventItem)
    {
        String url = String
                .format(" {\"name\": \"%s\", "
                                + "\"admin\": \"%s\", \"date\": \"%s\", "
                                + "\"start_time\": \"%s\", "+ "\"end_time\": \"%s\","
                                + " \"location\": \"%s\" ,",
                        eventItem.getName(), eventItem.getAdmin(),eventItem.getDate(), eventItem.getStartTime()
                        ,eventItem.getEndTime(),eventItem.getLocation());

        url=url+ "\"invited\" : [";
                if(eventItem.getInvitedPeople().size() > 0) {
                    url = url + "\""+eventItem.getInvitedPeople().get(0).getEmail() +"\"";
                    for(int i=1;i<eventItem.getInvitedPeople().size();i++)
                    {
                        url=url+ ",\""+eventItem.getInvitedPeople().get(i).getEmail() +"\"";
                    }
                }
        url=url+"], ";

        url=url+ "\"participants\" : ["+ "\"" +eventItem.getAdmin()+ "\" ]}";

        return url;
    }

}
