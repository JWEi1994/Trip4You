package dcs.suc.trip.Profilefragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import dcs.suc.trip.Global;
import dcs.suc.trip.ListView.Reservation;
import dcs.suc.trip.ListView.ReservationAdapter;
import dcs.suc.trip.Preferences.LoadPreferences;
import dcs.suc.trip.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DisplayReservation extends AppCompatActivity {

    private ListView listview;
    private OkHttpClient okHttpClient;
    private String userId, packageId, cartId;
    private LoadPreferences loadPreferences;
    ArrayList<Reservation> reservationArrayList = new ArrayList<>();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_reservation);

        listview = (ListView) findViewById(R.id.listview);
        okHttpClient = new OkHttpClient();
        loadPreferences = new LoadPreferences(DisplayReservation.this);
        userId = loadPreferences.getUserId();


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        viewReservation();


    }


    private void viewReservation() {

        progressDialog = new ProgressDialog(DisplayReservation.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        RequestBody formBody = new FormBody.Builder()
                .add("userId", userId)
                .build();

        Request request = new Request.Builder()
                .url(Global.DOMAIN_NAME + "viewReservation.php")
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(DisplayReservation.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();


                        try {
                            final String resStr = response.body().string();


                            JSONObject jsonObject = new JSONObject(resStr);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                JSONArray jArray = jsonObject.getJSONArray("list");
                                for (int i = 0; i < jArray.length(); i++) {

                                    jsonObject = jArray.getJSONObject(i);

                                    Reservation reservation = new Reservation();

                                    reservation.setImg(jsonObject.getString("image1"));
                                    reservation.setId(jsonObject.getString("reservation_id"));
                                    reservation.setOrderDate(jsonObject.getString("orderDate"));
                                    reservation.setPackageId(jsonObject.getString("packageId"));
                                    reservation.setTripName(jsonObject.getString("tripName"));
                                    reservation.setTripDate(jsonObject.getString("tripDate"));
                                    reservation.setCountry(jsonObject.getString("country"));
                                    reservation.setDuration(jsonObject.getString("duration"));
                                    reservation.setTotalAmt(jsonObject.getString("totalAmt"));
                                    reservation.setRemaining(jsonObject.getString("remaining"));


                                    reservationArrayList.add(reservation);



                                }

                                ReservationAdapter reservationAdapter = new ReservationAdapter(DisplayReservation.this, reservationArrayList);
                                listview.setAdapter(reservationAdapter);


                            } else if (success.equals("0")) {
                                Toast.makeText(DisplayReservation.this, "Pending payment is empty", Toast.LENGTH_SHORT).show();
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



