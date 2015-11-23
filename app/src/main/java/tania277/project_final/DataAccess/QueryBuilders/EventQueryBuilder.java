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



    public String buildEventsGetURL()
    {
        return qb.getBaseUrl()+getEventCollection()+qb.docApiKeyUrl();
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
    public String createEvent(EventItem eventItem)
    {
        return String
                .format(" {\"name\": \"%s\", "
                                + "\"admin\": \"%s\", \"date\": \"%s\", "
                                + "\"start_time\": \"%s\", "+ "\"end_time\": \"%s\","
                                + " \"location\": \"%s\" }",
                        eventItem.getName(), eventItem.getAdmin(),eventItem.getDate(), eventItem.getStartTime()
                        ,eventItem.getEndTime(),eventItem.getLocation());
    }

}
