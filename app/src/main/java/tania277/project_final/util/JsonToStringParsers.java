package tania277.project_final.util;

import android.util.Log;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;

import tania277.project_final.Models.LatLang;

/**
 * Created by Srinidhi on 11/23/2015.
 */
public class JsonToStringParsers {

    public List<String> ConvertTofriendRequestsList(String DBString)
    {
        String[] friendRequestsArray1= DBString.split("\\[");
        String[] friendRequestsArray2 = friendRequestsArray1[1].split("\\]");
        String[] friendRequestArrayForList = friendRequestsArray2[0].split(",");
        List<String> friends = new ArrayList<String>();
        for(int i=0;i<friendRequestArrayForList.length;i++)
        {
            friendRequestArrayForList[i]=friendRequestArrayForList[i].replaceAll("\"", "");
            //Log.i("message:friend requests", friendRequestArrayForList[i]);
            friends.add(friendRequestArrayForList[i].trim());
        }
        return friends;
    }

    public List<String> ConvertToRecords(String DBString)
    {
        Log.i("message:","before split "+DBString);
        String[] recordsArray1= DBString.split("\\[");
        String[] recordsArray2 = recordsArray1[1].split("\\]");
        String[] recordsArray3 = recordsArray2[0].split(",");
        List<String> records = new ArrayList<String>();
        for(int i=0;i<recordsArray3.length;i++)
        {
            recordsArray3[i]=recordsArray3[i].replaceAll("\"","");
            records.add(recordsArray3[i].trim());
           // Log.i("message", "run records run buddy" + recordsArray3[i]);
        }
        return records;
    }

    public List<String> ConvertToParticipantsLocation(String DBString)
    {
        List<String> values= new ArrayList<>();
        String string= DBString.replace("\"","").trim();
        String[] array1= string.split("\\|");

        for(int i=0;i<array1.length;i++)
        {
            values.add(array1[i]);
        }

        return values;
    }

    public LatLang ConvertToLatLang(String DBString)
    {
//        Log.i("message: 1 latLang ",DBString);
        DBString=DBString.replace("\"","").trim();
//        Log.i("message: 2 latLang ",DBString);

        String[] one = DBString.split("\\[");
        String[] two = one[1].split("\\]");
        LatLang latLang = new LatLang();
//        Log.i("message: 3 latLang ",two[0]);
//        if(two[0].contains("\\|")) {
//            Log.i("message: 3.5 latLang ",two[0]);

            String[] array1 = two[0].split("\\|");


            latLang.setLatitude(array1[0]);
            latLang.setLongitude(array1[1]);

//            Log.i("message", "latLang 4" + latLang.getLatitude() + latLang.getLongitude());
//        }
        return latLang;
    }

    public List<LatLang> ConvertToLatLangString(String DBString)
    {
        List<LatLang> list = new ArrayList<>();
        String[] array1=DBString.trim().split("\\|");
        for(int i=0;i<array1.length;i++) {

            Log.i("message","latLang user"+array1[i]);
            String[] array2 = array1[i].split("=");
            LatLang latLang = new LatLang();
            latLang.setLatitude(array2[0]);
            latLang.setLongitude(array2[1]);

            Log.i("message:", "latLang user in user run record" + latLang.getLatitude() + latLang.getLongitude());

            list.add(latLang);
        }

        return list;
    }

}
