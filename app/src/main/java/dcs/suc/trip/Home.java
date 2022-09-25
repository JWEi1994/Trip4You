package dcs.suc.trip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import dcs.suc.trip.Destination.LocationFragment;
import dcs.suc.trip.Homefragment.Home_fragment;
import dcs.suc.trip.ShoppingCartFragment.ShoppingCart_fragment;
import dcs.suc.trip.Profilefragment.Profile_fragment;

public class Home extends AppCompatActivity {


    private static final int REQUEST_CODE =1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //default
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,new Home_fragment()).commit();


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame,new Home_fragment()).commit();

                        break;

                    case R.id.navigation_location:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame,new LocationFragment()).commit();

                        break;


                    case R.id.navigation_notifications:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame,new ShoppingCart_fragment()).commit();

                        break;

                    case R.id.navigation_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame,new Profile_fragment()).commit();

                        break;



                }

                return  true;
            }
        });


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && requestCode == RESULT_OK){

        }
    }

}
