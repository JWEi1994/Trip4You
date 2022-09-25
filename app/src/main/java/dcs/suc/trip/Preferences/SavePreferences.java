package dcs.suc.trip.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SavePreferences {

    SharedPreferences userPref;
    SharedPreferences.Editor EdUser;

    public SavePreferences(Context context){

        userPref = context.getSharedPreferences("profile",0);
        EdUser = userPref.edit();
    }

    public void clear(){
        SharedPreferences.Editor editor = userPref.edit();
        editor.clear();
        editor.commit();
    }
    public void saveUserID(String userId){
        EdUser.putString("userId",userId);
        EdUser.apply();
    }

}

