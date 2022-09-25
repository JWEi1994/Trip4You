package dcs.suc.trip.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class LoadPreferences {
    String userId,packageId;
    public SharedPreferences userPref;

    public LoadPreferences(Context context)
    {
        userPref = context.getSharedPreferences("profile",0);
        userId = userPref.getString("userId",null);

    }

    public String getUserId()
    {
        return userId;
    }
}
