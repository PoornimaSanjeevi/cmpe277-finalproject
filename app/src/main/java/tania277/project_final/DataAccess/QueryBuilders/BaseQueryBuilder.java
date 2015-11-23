package tania277.project_final.DataAccess.QueryBuilders;

/**
 * Created by Tania on 11/16/15.
 */
public class BaseQueryBuilder {
   public String getDatabaseName() {
        return "cmpe277project_runbuddy";
    }

    public String getApiKey() {
        return "lhXxXAw69SlRU8my7e8jQcxW40ZBHigQ";
    }


    public String getBaseUrl()
    {
        return "https://api.mongolab.com/api/1/databases/"+getDatabaseName()+"/collections/";
    }


    public String docApiKeyUrl()
    {

        return "?apiKey="+getApiKey();
    }

    public String andApiKeyUrl()
    {
        return "&apiKey="+getApiKey();
    }

    public String docApiKeyUrl(String docid)
    {
        return "/"+docid+"?apiKey="+getApiKey();
    }

//
//    public String getEventCollection()
//    {
//        return "events_runbuddy";
//    }


//    public String buildContactsSaveURL()
//    {
//        return getBaseUrl()+getEventCollection()+docApiKeyUrl();
//    }


//    public String buildEventsGetURL()
//    {
//        return getBaseUrl()+getEventCollection()+docApiKeyUrl();
//    }


//    public String buildContactsUpdateURL(String doc_id)
//    {
//        return getBaseUrl()+getEventCollection()+docApiKeyUrl(doc_id);
//    }


//    public String createContact(MyContact contact)
//    {
//        return String
//                .format("{\"document\" : {\"first_name\": \"%s\", "
//                                + "\"last_name\": \"%s\", \"email\": \"%s\", "
//                                + "\"phone\": \"%s\"}"+"}",
//                        contact.first_name, contact.last_name, contact.email, contact.phone);
//    }
//
//    public String setContactData(MyContact contact) {
//        return String.format("{ \"$set\" : "
//                        + "{\"first_name\" : \"%s\", "
//                        + "\"last_name\" : \"%s\", "
//                        + "\"email\" : \"%s\", "
//                        + "\"phone\" : \"%s\" }" + "}",
//                contact.getFirst_name(),
//                contact.getLast_name(), contact.getEmail(),
//                contact.getPhone());
//    }



}
