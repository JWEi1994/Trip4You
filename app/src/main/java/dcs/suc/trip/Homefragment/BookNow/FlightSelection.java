package dcs.suc.trip.Homefragment.BookNow;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import dcs.suc.trip.R;

public class FlightSelection extends AppCompatActivity {

    TextView oneWay,roundTrip;
    ViewPager viewPager;
    PagerViewAdapter pagerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_selection);

        oneWay = (TextView)findViewById(R.id.tv_OneWay);
        roundTrip = (TextView)findViewById(R.id.tv_RoundTrip);
        viewPager = (ViewPager)findViewById(R.id.fragment_container);

        pagerViewAdapter =  new PagerViewAdapter(getSupportFragmentManager());

        viewPager.setAdapter(pagerViewAdapter);

        oneWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        roundTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onPageSelected(int position) {
                onChangeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void onChangeTab(int position) {

        if (position==0){
            oneWay.setTextSize(25);
            oneWay.setTextColor(getColor(R.color.bright_color));

            roundTrip.setTextSize(20);
            roundTrip.setTextColor(getColor(R.color.light_color));
        }

        if (position==1){
            oneWay.setTextSize(20);
            oneWay.setTextColor(getColor(R.color.light_color));

            roundTrip.setTextSize(25);
            roundTrip.setTextColor(getColor(R.color.bright_color));
        }
    }
}
