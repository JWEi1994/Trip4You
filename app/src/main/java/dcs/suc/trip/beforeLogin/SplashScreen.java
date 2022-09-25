package dcs.suc.trip.beforeLogin;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import dcs.suc.trip.Home;
import dcs.suc.trip.Preferences.LoadPreferences;
import dcs.suc.trip.R;

public class SplashScreen extends AppCompatActivity {

    LoadPreferences loadPreferences;
    String strid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen); setContentView(R.layout.activity_splash_screen);

        loadPreferences = new LoadPreferences(SplashScreen.this);
        strid = loadPreferences.getUserId();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (strid == null) {

                    // Do something after 5s = 5000ms
                    Intent i = new Intent(SplashScreen.this, Login.class);
                    startActivity(i);
                    finish();

                } else {
                    Intent j = new Intent(SplashScreen.this, Home.class);
                    startActivity(j);
                    finish();
                }
            }
        }, 2000);

    }
}
