package dcs.suc.trip;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import dcs.suc.trip.ListView.Reservation;
import dcs.suc.trip.Preferences.LoadPreferences;
import dcs.suc.trip.Profilefragment.DisplayBought;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PaymentDetails extends AppCompatActivity {

    private ListView listview;
    OkHttpClient okHttpClient;
    private TextView txtId, txtAmount,amount, txtStatus, txtTime,tv_subtotal,tv_total,tv_payment;
    private Button tv_viewBought;
    String userId,tripDate,paypal_Id,reservation_id, create_time, state,paymentDetails,paymentAmount;
    LoadPreferences loadPreferences;
    ArrayList<Reservation> reservationArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        listview = (ListView)findViewById(R.id.listview);
        loadPreferences = new LoadPreferences(PaymentDetails.this);
        okHttpClient = new OkHttpClient();
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtId = (TextView) findViewById(R.id.txtId);
        txtAmount = (TextView) findViewById(R.id.txtAmount);
        amount = (TextView) findViewById(R.id.amount);
        tv_subtotal = (TextView) findViewById(R.id.subtotal);
        tv_total = (TextView) findViewById(R.id.total);
        tv_payment = (TextView) findViewById(R.id.payment);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        tv_viewBought = (Button) findViewById(R.id.tv_viewBought);

        userId = loadPreferences.getUserId();


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        paymentDetails = bundle.getString("PaymentDetails");
         paymentAmount = bundle.getString("PaymentAmount");


//        Log.e("PaymentDetails", paymentDetails);
//        Log.e("PaymentAmount", paymentAmount);

        try {
            JSONObject jsonObject = new JSONObject(paymentDetails);
            JSONObject response = jsonObject.getJSONObject("response");
            create_time = response.getString("create_time");
            paypal_Id = response.getString("id");
            state = response.getString("state");



                txtId.setText("ID:" + paypal_Id);
                txtStatus.setText("Status: " + state);
                txtAmount.setText("RM" + paymentAmount);
                    amount.setText("RM" + paymentAmount);
                tv_total.setText("RM" + paymentAmount);
                tv_payment.setText("RM" + paymentAmount);
                tv_subtotal.setText("RM" + paymentAmount);
                txtTime.setText("Payment Time: " + create_time);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        tv_viewBought.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(PaymentDetails.this, DisplayBought.class);

                startActivity(i);
            }
        });

        addPayment();

    }



    private void addPayment() {

        String userId = loadPreferences.getUserId();

        Reservation reservation= new Reservation();
        reservation_id = reservation.getId();

        RequestBody formBody = new FormBody.Builder()
                .add("userId",userId)
                .add("paypal_Id", paypal_Id)
                .add("state", state)
                .add("create_time", create_time)
                .add("paymentAmount", paymentAmount)
                .build();

        Request request = new Request.Builder()
                .url(Global.DOMAIN_NAME + "addPaymentDetails.php")
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PaymentDetails.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            String resStr = response.body().string().toString();

                            final JSONObject jsonObject = new JSONObject(resStr);

                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {


//


                                    }
                                });
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }




}
