package dcs.suc.trip;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import dcs.suc.trip.Profilefragment.DisplayReservation;

public class PaymentRejected extends AppCompatActivity {

    private TextView tv_viewPending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_rejected);


        tv_viewPending = (TextView)findViewById(R.id.tv_viewPending);


        tv_viewPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(PaymentRejected.this, DisplayReservation.class);
                startActivity(i);
                finish();
            }
        });

    }
}
