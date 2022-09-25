package dcs.suc.trip.ShoppingCartFragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import dcs.suc.trip.Config.Config;
import dcs.suc.trip.Global;
import dcs.suc.trip.Homefragment.BookNow.BookingInfo;
import dcs.suc.trip.ListView.Cart;
import dcs.suc.trip.ListView.CartAdapter;
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

public class CartResult extends AppCompatActivity {

    LoadPreferences loadPreferences;
    OkHttpClient okHttpClient;
    public static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);
    private ListView listview;
    private TextView tv_totalPrice;
    private int totalAmount;
    private Button btn_PayNow;
    String paymentDetails;
    String amount = "";
    private boolean isCancelled = false;
    ArrayList<Cart> cartList;


    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_result);


        okHttpClient = new OkHttpClient();
        loadPreferences = new LoadPreferences(CartResult.this);
        listview = (ListView) findViewById(R.id.listview);
        tv_totalPrice = (TextView) findViewById(R.id.tv_totalPrice);
        btn_PayNow = (Button) findViewById(R.id.btn_PayNow);


        btn_PayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();
            }
        });


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        final String json = bundle.getString("json");

        Type founderListType = new TypeToken<ArrayList<Cart>>() {
        }.getType();
        cartList = new Gson().fromJson(json, founderListType);

        CartResultAdapter cartResultAdapter = new CartResultAdapter(CartResult.this, cartList);
        listview.setAdapter(cartResultAdapter);

        for (int i = 0; i < cartList.size(); i++) {

            Cart cart = cartList.get(i);
            int subAmount = cart.getTotalAmount();

            totalAmount += subAmount;

            tv_totalPrice.setText("RM" + totalAmount);
        }

        Log.e("totalA", totalAmount + "");

    }


    private void processPayment() {

        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(totalAmount)), "MYR",
                "Pay for Trip4You", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(CartResult.this, PaymentActivity.class);
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

                        JSONObject jsonObject = new JSONObject(paymentDetails);
                        JSONObject response = jsonObject.getJSONObject("response");
                        String state = response.getString("state");

                        if (state.equals("approved")) {

                            isCancelled = false;


                            for (int i = 0; i < cartList.size(); i++) {

                                reservation("1", cartList.get(i));
                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                isCancelled = true;

                Toast.makeText(CartResult.this, "Cancel Payment, Check your pending payment", Toast.LENGTH_SHORT).show();


                for (int i = 0; i < cartList.size(); i++) {

                    reservation("0", cartList.get(i));
                }

            }
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
    }

    private void reservation(String orderStatus, Cart cart) {


        String userId = loadPreferences.getUserId();

        String packageID = cart.getPackageId();
        String tripDate = cart.getTripDate();
        int Apax =  cart.getAcounterTxt();
        int Cpax =  cart.getCcounterTxt();
        int Spax =  cart.getScounterTxt();
        int totalAmt =  cart.getTotalAmount();


        RequestBody formBody = new FormBody.Builder()
                .add("userId", userId)
                .add("packageId", packageID)
                .add("tripDate", tripDate)
                .add("totalAmt", String.valueOf(totalAmt))
                .add("Apax", String.valueOf(Apax))
                .add("Cpax", String.valueOf(Cpax))
                .add("Spax", String.valueOf(Spax))
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
                        Toast.makeText(CartResult.this, "Connection Error", Toast.LENGTH_SHORT).show();
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

                                        if (isCancelled == false) {

                                            startActivity(new Intent(CartResult.this, PaymentDetails.class)
                                                    .putExtra("PaymentDetails", paymentDetails)
                                                    .putExtra("PaymentAmount", amount));
                                            finish();
                                        } else if (isCancelled == true) {

                                            Intent i = new Intent(CartResult.this, PaymentRejected.class);
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
