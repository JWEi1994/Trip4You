package dcs.suc.trip.Homefragment.BookNow;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;

import dcs.suc.trip.Config.Config;
import dcs.suc.trip.Global;
import dcs.suc.trip.ListView.Reservation;
import dcs.suc.trip.PaymentDetails;
import dcs.suc.trip.PaymentRejected;
import dcs.suc.trip.Preferences.LoadPreferences;
import dcs.suc.trip.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BookingInfo extends AppCompatActivity {

    public static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    private Button btn_PayNow;
    OkHttpClient okHttpClient;
    LoadPreferences loadPreferences;
    private TextView tv_tripName, tv_tripDate, tv_Apax, tv_Cpax,
            tv_Spax, tv_totalPrice;
    String amount = "";
    private String tripName,reservation_id,tripDate,Apax,Cpax,Spax;
    String totalAmt;

    private boolean isCancelled= false;
    String paymentDetails ;


    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_info);

        Intent intent = new Intent(BookingInfo.this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        btn_PayNow = findViewById(R.id.btn_PayNow);
        tv_totalPrice = findViewById(R.id.tv_totalPrice);
        tv_tripName = findViewById(R.id.tv_tripName);
        tv_tripDate = findViewById(R.id.tv_tripDate);
        tv_Apax = findViewById(R.id.tv_Apax);
        tv_Spax = findViewById(R.id.tv_Spax);
        tv_Cpax = findViewById(R.id.tv_Cpax);
        okHttpClient = new OkHttpClient();
        loadPreferences = new LoadPreferences(BookingInfo.this);

        Reservation reservation = new Reservation();
        reservation_id = reservation.getId();



        Intent i = getIntent();
        Bundle bundle = i.getExtras();

         tripName = bundle.getString("tripName");
         tripDate = bundle.getString("tripDate");
         Apax = bundle.getString("Apax");
         Cpax = bundle.getString("Cpax");
         Spax = bundle.getString("Spax");
        amount = bundle.getString("totalAmt");


        tv_tripName.setText(tripName);
        tv_tripDate.setText(tripDate);
        tv_Apax.setText("Adult  x " + Apax);
        tv_Cpax.setText("Child  x " + Cpax);
        tv_Spax.setText("Senior  x " + Spax);
        tv_totalPrice.setText("RM" + amount);


        btn_PayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });

    }

    private void processPayment() {

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

      totalAmt = bundle.getString("totalAmt");


        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(totalAmt)), "MYR",
                "Pay for Trip4You", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(BookingInfo.this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {

                    try {

                         paymentDetails = confirmation.toJSONObject().toString(4);


                         Log.e("paymentdetails",paymentDetails);

                        JSONObject jsonObject = new JSONObject(paymentDetails);
                        JSONObject response = jsonObject.getJSONObject("response");
                        String state = response.getString("state");

                        if (state.equals("approved")) {

                            isCancelled =false;

                            reservation("1");

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                isCancelled = true;
                reservation("0");
                Toast.makeText(BookingInfo.this, "Cancel Payment, Check your pending payment", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
    }

    private void reservation(String orderStatus) {


        String userId = loadPreferences.getUserId();
        String packageId = getIntent().getStringExtra("packageId");



        RequestBody formBody = new FormBody.Builder()
                .add("userId", userId)
                .add("packageId", packageId)
                .add("tripDate", tripDate)
                .add("Apax", String.valueOf(Apax))
                .add("Cpax", String.valueOf(Cpax))
                .add("Spax", String.valueOf(Spax))
                .add("totalAmt", totalAmt)
                .add("orderStatus", orderStatus)
                .build();

        Request request = new Request.Builder()
                .url(Global.DOMAIN_NAME + "reservation.php")
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BookingInfo.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            String resStr = response.body().string();
                            JSONObject jsonObject = new JSONObject(resStr);

                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                    if (isCancelled==false){

                                        startActivity(new Intent(BookingInfo.this, PaymentDetails.class)
//                                                .putExtra("reservation_id",reservation_id)
                                                .putExtra("PaymentDetails", paymentDetails)
                                                .putExtra("PaymentAmount", amount));
                                        finish();

                                    }else if(isCancelled==true){

                                        Intent i = new Intent(BookingInfo.this,PaymentRejected.class);
                                        startActivity(i);
                                        finish();

                                    }
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