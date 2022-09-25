package dcs.suc.trip.Homefragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import dcs.suc.trip.Homefragment.AddCart.AddToCartOptions;
import dcs.suc.trip.Homefragment.BookNow.BookingOptions;
import dcs.suc.trip.R;

public class DatePicker extends AppCompatActivity {

    TextView tv_tripDate;
    Calendar myCurrentTime;
    int day,month,year;
    private Button btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);

        tv_tripDate = (TextView)findViewById(R.id.tv_tripDate);
        btn_next = (Button) findViewById(R.id.btn_next);
        myCurrentTime = Calendar.getInstance();

        day = myCurrentTime.get(Calendar.DAY_OF_MONTH);
        month = myCurrentTime.get(Calendar.MONTH);
        year = myCurrentTime.get(Calendar.YEAR);

//        day = day+1;

//        tv_tripDate.setText(day+"/"+month+"/"+year);

        tv_tripDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatePickerDialog datePickerDialog = new DatePickerDialog(DatePicker.this,
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        month = month+1;


                        tv_tripDate.setText(dayOfMonth+"/"+month+"/"+year);
                        final String aa = dayOfMonth+"/"+month+"/"+year;


                        btn_next.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                Intent i = new Intent(DatePicker.this, AddToCartOptions.class);
                                i.putExtra("packageId",getIntent().getStringExtra("packageId"));
                                Bundle bundle = new Bundle();
                                bundle.putString("aa",aa);
                                i.putExtras(bundle);
                                Log.e("bb",aa);
                                startActivity(i);
                            }
                        });

                    }
                },year,month,day);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() -1000);
//                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();

                Calendar maxDate = Calendar.getInstance();
                maxDate.set(Calendar.DAY_OF_MONTH, day);
                maxDate.set(Calendar.MONTH, month+6);
                maxDate.set(Calendar.YEAR, year );
                datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
            }
        });



    }
}
