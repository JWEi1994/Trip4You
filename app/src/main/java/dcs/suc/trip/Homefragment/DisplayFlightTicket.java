package dcs.suc.trip.Homefragment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.concurrent.LinkedTransferQueue;

import dcs.suc.trip.Global;
import dcs.suc.trip.ListView.Flight;
import dcs.suc.trip.ListView.FlightAdapter;
import dcs.suc.trip.ListView.PackageAdapter;
import dcs.suc.trip.ListView.Packages;
import dcs.suc.trip.Preferences.LoadPreferences;
import dcs.suc.trip.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DisplayFlightTicket extends AppCompatActivity {

    private ListView listview;
    OkHttpClient okHttpClient;
    LoadPreferences loadPreferences;
    ArrayList<Flight>flightArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_flight_ticket);


        listview = (ListView)findViewById(R.id.listview);
        okHttpClient = new OkHttpClient();
        loadPreferences = new LoadPreferences(DisplayFlightTicket.this);



        viewFlightTicket();
    }

    private void viewFlightTicket() {

        Request request = new Request.Builder()
                .url(Global.DOMAIN_NAME + "viewFlightTicket.php")
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DisplayFlightTicket.this, "Connection Error", Toast.LENGTH_SHORT).show();

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
                                JSONArray jArray = jsonObject.getJSONArray("list");
                                for (int i = 0; i < jArray.length(); i++) {
//
                                    jsonObject = jArray.getJSONObject(i);
                                    Flight flight = new Flight();

                                    flight.setFlightId(jsonObject.getString("flightId"));
                                    flight.setAFlightprice(jsonObject.getString("AflightPrice"));
                                    flight.setCFlightPrice(jsonObject.getString("CflightPrice"));
//                                    flight.setAdisplayPrice(jsonObject.getString("AdisplayPrice"));
                                    flight.setSource(jsonObject.getString("source"));
                                    flight.setDestination(jsonObject.getString("destination"));
                                    flight.setDepartDate(jsonObject.getString("departDate"));
                                    flight.setDepartTime(jsonObject.getString("departTime"));
                                    flight.setArrivedDate(jsonObject.getString("arrivedDate"));
                                    flight.setArrivedTime(jsonObject.getString("arrivedTime"));

                                    flightArrayList.add(flight);


                                }
                                FlightAdapter flightAdapter = new FlightAdapter(DisplayFlightTicket.this, flightArrayList);
                                listview.setAdapter(flightAdapter);


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
