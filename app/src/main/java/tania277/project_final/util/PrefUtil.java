package tania277.project_final.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import tania277.project_final.LoginActivity;

/**
 * Created by poornima on 11.18.15.
 */
public class PrefUtil {

    private Activity activity;

    // constructor
    public PrefUtil(Activity activity) {
        this.activity = activity;
    }

    public String getUserId() {
        SharedPreferences sp = activity.getSharedPreferences("user", activity.MODE_PRIVATE);
        return sp.getString("username", null);
    }

    public String getProfImage() {
        SharedPreferences sp = activity.getSharedPreferences("user", activity.MODE_PRIVATE);
        return sp.getString("profileImage", null);
    }

    public String getEmailId(){
        SharedPreferences sp = activity.getSharedPreferences("user", activity.MODE_PRIVATE);
        return sp.getString("email", null);
    }
    public void clearData() {
        SharedPreferences sp = activity.getSharedPreferences("user", activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public void saveUserInfo(String userId, String photoURL,String emailId) {
        SharedPreferences sp = activity.getSharedPreferences("user", activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", userId);
        editor.putString("profileImage", photoURL);
        editor.putString("email",emailId);
        editor.apply();
        editor.commit();
    }
}
