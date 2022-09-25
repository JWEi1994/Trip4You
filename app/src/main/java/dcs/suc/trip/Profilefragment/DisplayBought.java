package dcs.suc.trip.Profilefragment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dcs.suc.trip.Global;
import dcs.suc.trip.Homefragment.PackageInfo;
import dcs.suc.trip.ListView.Bought;
import dcs.suc.trip.ListView.BoughtAdapter;
import dcs.suc.trip.PaymentDetails;
import dcs.suc.trip.Preferences.LoadPreferences;
import dcs.suc.trip.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DisplayBought extends AppCompatActivity {

    private TextView tv_create_time;
    private ListView listview;
    private OkHttpClient okHttpClient;
    private LoadPreferences loadPreferences;
    ArrayList<Bought>boughtArrayList = new ArrayList<>();
    String userId,paypal_Id,paymentDetails,state,create_time,paymentAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_bought);


        listview = (ListView)findViewById(R.id.listview);
        okHttpClient = new OkHttpClient();
        loadPreferences = new LoadPreferences(DisplayBought.this);
         userId = loadPreferences.getUserId();

//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Intent i = new Intent(DisplayBought.this, PaymentDetails.class);
////                i.putExtra("reservation_id", boughtArrayList.get(position).getId());
//                startActivityForResult(i,RESULT_OK);
//
//            }
//        });




        viewBought();



    }

    private void viewBought() {


        RequestBody formBody = new FormBody.Builder()
                .add("userId",userId)
                .build();


        Request request = new Request.Builder()
                .url(Global.DOMAIN_NAME+"viewBought.php")
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DisplayBought.this, "Conection Error", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            final String resStr = response.body().string();

                            JSONObject jsonObject = new JSONObject(resStr);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")){
                                JSONArray jArray = jsonObject.getJSONArray("list");
                                for (int i=0; i<jArray.length();i++){

                                    jsonObject = jArray.getJSONObject(i);
                                    Bought bought = new Bought();

                                    bought.setId(jsonObject.getString("reservation_id"));
                                    bought.setImg(jsonObject.getString("image1"));
                                    bought.setTripName(jsonObject.getString("tripName"));
                                    bought.setCountry(jsonObject.getString("country"));
                                    bought.setDuration(jsonObject.getString("duration"));
                                    bought.setTripDate(jsonObject.getString("tripDate"));
                                    bought.setTotalAmt(jsonObject.getString("totalAmt"));
//

                                    boughtArrayList.add(bought);

                                }

                                BoughtAdapter boughtAdapter = new BoughtAdapter(DisplayBought.this,boughtArrayList);
                                listview.setAdapter(boughtAdapter);
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
