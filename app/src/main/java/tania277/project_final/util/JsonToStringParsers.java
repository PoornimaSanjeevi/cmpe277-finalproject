package tania277.project_final.util;

import android.util.Log;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;

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
            Log.i("message:friend requests",friendRequestArrayForList[i]);
            friends.add(friendRequestArrayForList[i].trim());
        }
        return friends;
    }

    public List<String> ConvertToRecords(String DBString)
    {


        String[] recordsArray1= DBString.split("\\[");
        String[] recordsArray2 = recordsArray1[1].split("\\]");
        String[] recordsArray3 = recordsArray2[0].split(",");
        List<String> records = new ArrayList<String>();
        for(int i=0;i<recordsArray3.length;i++)
        {
            recordsArray3[i]=recordsArray3[i].replaceAll("\"","");
            records.add(recordsArray3[i].trim());
            Log.i("message", "run records run buddy" + recordsArray3[i]);
        }
        return records;
    }

}
