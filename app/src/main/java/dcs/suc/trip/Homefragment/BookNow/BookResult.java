package dcs.suc.trip.Homefragment.BookNow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import dcs.suc.trip.Global;
import dcs.suc.trip.Preferences.LoadPreferences;
import dcs.suc.trip.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BookResult extends AppCompatActivity {

    private Button btn_proceed;
    private TextView tv_tripName;
    private TextView tv_tripDate;
    private TextView tv_Apax;
    private TextView tv_Cpax;
    private TextView tv_Spax;
    private TextView tv_totalAmount;
    String totalAmount;
    OkHttpClient okHttpClient;
    String packageId,userId;
    LoadPreferences loadPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);

        btn_proceed = (Button)findViewById(R.id.btn_proceed);
        tv_tripName = (TextView)findViewById(R.id.tv_tripName);
        tv_tripDate = (TextView)findViewById(R.id.tv_tripDate);
        tv_Apax = (TextView)findViewById(R.id.tv_Apax);
        tv_Cpax = (TextView)findViewById(R.id.tv_Cpax);
        tv_Spax = (TextView)findViewById(R.id.tv_Spax);
        tv_totalAmount = (TextView)findViewById(R.id.tv_totalAmount);
        okHttpClient = new OkHttpClient();
        loadPreferences = new LoadPreferences(BookResult.this);
//        totalAmount = String.valueOf(Integer.valueOf(Apax * Integer.valueOf(price))+(Cpax * Integer.valueOf(cprice))+ (Spax * Integer.valueOf(sprice)));

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        final String tripName = bundle.getString("tripName");
        final String tripDate = bundle.getString("tripDate");
//        final String dep = bundle.getString("dep");
        final String Apax = bundle.getString("Apax");
        final String Cpax = bundle.getString("Cpax");
        final String Spax = bundle.getString("Spax");
        final String price = bundle.getString("price");
        final String cprice = bundle.getString("cprice");
        final String sprice = bundle.getString("sprice");
        final String totalAmt = bundle.getString("totalAmt");

        tv_tripName.setText(tripName);
        tv_tripDate.setText(tripDate);
        tv_Apax.setText("Adult  x "+Apax );
        tv_Cpax.setText("Child  x "+Cpax );
        tv_Spax.setText("Senior  x "+Spax);
        tv_totalAmount.setText("RM"+totalAmt);

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(BookResult.this, BookingInfo.class);
                intent.putExtra("packageId",getIntent().getStringExtra("packageId"));

                Bundle bundle = new Bundle();
                bundle.putString("tripName",tripName);
                bundle.putString("tripDate",tripDate);
//                bundle.putString("dep",dep);
                bundle.putString("Apax",String.valueOf(Apax));
                bundle.putString("price",String.valueOf(price));
                bundle.putString("Cpax",String.valueOf(Cpax));
                bundle.putString("cprice",String.valueOf(cprice));
                bundle.putString("Spax",String.valueOf(Spax));
                bundle.putString("sprice",String.valueOf(sprice));
                bundle.putString("totalAmt",String.valueOf(totalAmt));

                intent.putExtras(bundle);
                startActivity(intent);
                finish();


//                reservation();
            }


//            private void reservation() {
//
//
//                userId = loadPreferences.getUserId();
//                packageId = getIntent().getStringExtra("packageId");
//
//                RequestBody formBody = new FormBody.Builder()
//                        .add("userId",userId)
//                        .add("packageId",packageId)
//                        .add("Apax",Apax)
//                        .add("Cpax",Cpax)
//                        .add("Spax",Spax)
//                        .build();
//
//                Request request = new Request.Builder()
//                        .url(Global.DOMAIN_NAME+"reservation.php")
//                        .post(formBody)
//                        .build();
//
//                okHttpClient.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(BookResult.this, "Connection Error", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                    }
//
//                    @Override
//                    public void onResponse(Call call, final Response response) throws IOException {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                try {
//                                    String resStr = response.body().string();
//                                    JSONObject jsonObject = new JSONObject(resStr);
//
//                                    String success = jsonObject.getString("success");
//
//                                    if (success.equals("1")) {
//                                        runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//
//
//                                                Toast.makeText(BookResult.this, "Add Reservation", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//
//                                    }
//
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//
//                            }
//                        });
//                    }
//                });
//
//
//            }
        });

    }
}
