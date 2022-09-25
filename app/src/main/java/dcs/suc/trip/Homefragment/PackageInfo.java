package dcs.suc.trip.Homefragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import dcs.suc.trip.Homefragment.AddCart.AddToCartOptions;
import dcs.suc.trip.Homefragment.BookNow.BookingOptions;
import dcs.suc.trip.Global;
import dcs.suc.trip.ListView.Packages;
import dcs.suc.trip.Preferences.LoadPreferences;
import dcs.suc.trip.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static dcs.suc.trip.Global.DOMAIN_NAME;

public class PackageInfo extends AppCompatActivity {


    private ImageView image1,image2,image3,image4;
    private TextView tv_tripName, tv_duration, tv_price, tv_shows,tv_comLanguage, tv_remaining,tv_description,
            tv_description2,tv_description3,tv_description4, tv_inclusions, tv_knowbeforeyoubook,tv_meetUpInfo;
    private String tripName, duration, shows,comLanguage,price,remaining, description,description2,description3,
            description4, inclusions, knowbeforeyoubook,meetUpInfo;
    private ImageView favorite;
    private Button minusBtn, plusBtn, cart, btn_booknow, btn_addcart;
    private ListView listView;
    LoadPreferences loadPreferences;
    OkHttpClient okHttpClient;
    String packageId;
    ArrayList<Packages> packagesArrayList = new ArrayList<>();
    ProgressDialog progressDialog;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        };
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_info);

//        getSupportActionBar().setTitle("Package Info");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        image1 = (ImageView)findViewById(R.id.image1);
        image2 = (ImageView)findViewById(R.id.image2);
        image3 = (ImageView)findViewById(R.id.image3);
        image4 = (ImageView)findViewById(R.id.image4);
        btn_addcart = (Button) findViewById(R.id.btn_addcart);
        btn_booknow = (Button) findViewById(R.id.btn_booknow);
        listView = (ListView) findViewById(R.id.listview);
        tv_tripName = (TextView) findViewById(R.id.tv_tripName);
        tv_duration = (TextView) findViewById(R.id.tv_duration);
        tv_shows = (TextView) findViewById(R.id.tv_shows);
        tv_shows = (TextView) findViewById(R.id.tv_shows);
        tv_comLanguage = (TextView) findViewById(R.id.tv_comLanguage);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_meetUpInfo = (TextView) findViewById(R.id.tv_meetUpInfo);
//        tv_dep = (TextView) findViewById(R.id.tv_dep);
        tv_remaining = (TextView) findViewById(R.id.tv_remaining);
//        tv_route = (TextView) findViewById(R.id.tv_route);
//        tv_deadline = (TextView) findViewById(R.id.tv_deadline);

        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_description2 = (TextView) findViewById(R.id.tv_description2);
        tv_description3 = (TextView) findViewById(R.id.tv_description3);
        tv_description4 = (TextView) findViewById(R.id.tv_description4);
        tv_inclusions = (TextView) findViewById(R.id.tv_inclusions);
        tv_knowbeforeyoubook = (TextView) findViewById(R.id.tv_knowbeforeyoubook);
        favorite = (ImageView) findViewById(R.id.favorite);
        loadPreferences = new LoadPreferences(PackageInfo.this);
        okHttpClient = new OkHttpClient();

        packageId = getIntent().getStringExtra("packageId");


        viewPackagesInfo();


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);


        btn_addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                Intent intent = new Intent(PackageInfo.this, AddToCartOptions.class);
                Intent intent = new Intent(PackageInfo.this, AddToCartOptions.class);
                intent.putExtra("packageId",getIntent().getStringExtra("packageId"));
                startActivityForResult(intent,RESULT_OK);
                finish();


            }
        });


        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addwishlist();
            }
        });


        btn_booknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(PackageInfo.this, BookingOptions.class);
                i.putExtra("packageId",getIntent().getStringExtra("packageId"));
                startActivity(i);
                finish();
            }
        });


    }



    public void addwishlist() {

        String userId = loadPreferences.getUserId();
       final String packageId = getIntent().getStringExtra("packageId");

        RequestBody formBody = new FormBody.Builder()
                .add("packageId", packageId)
                .add("userId", userId)
                .build();

        Request request = new Request.Builder()
                .url(Global.DOMAIN_NAME + "addwishlist.php")
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PackageInfo.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            String resStr = response.body().string().toString();

                            final JSONObject jsonObject = new JSONObject(resStr);
                            String success = jsonObject.getString("success");

//                    Log.e("outPut", resStr);

                            if (success.equals("1")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Toast.makeText(PackageInfo.this, "Add to Wishlist", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

    public void viewPackagesInfo() {


        progressDialog = new ProgressDialog(PackageInfo.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        Log.e("PID",packageId+"");

//        String packageId = getIntent().getStringExtra("packageId");

        RequestBody formBody = new FormBody.Builder()
                .add("packageId", packageId)
                .build();

        Request request = new Request.Builder()
                .url(Global.DOMAIN_NAME + "viewPackagesInfo.php")
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        progressDialog.dismiss();
                        Toast.makeText(PackageInfo.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }


            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            progressDialog.dismiss();
                            String resStr = response.body().string();

                            Log.e("PID",resStr);

//                            Toast.makeText(PackageInfo.this, resStr, Toast.LENGTH_SHORT).show();
                            JSONObject jsonObject = new JSONObject(resStr);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {

                                Packages packages = new Packages();
                                packages.setImg(jsonObject.getString("image1"));
                                packages.setImg2(jsonObject.getString("image2"));
                                packages.setImg3(jsonObject.getString("image3"));
                                packages.setImg4(jsonObject.getString("image4"));
                                tripName = jsonObject.getString("tripName");
                                duration = jsonObject.getString("duration");
                                shows = jsonObject.getString("shows");
                                comLanguage = jsonObject.getString("comLanguage");
                                price = jsonObject.getString("price");
                                remaining = jsonObject.getString("remaining");
                                description = jsonObject.getString("description");
                                description2 = jsonObject.getString("description2");
                                description3 = jsonObject.getString("description3");
                                description4 = jsonObject.getString("description4");
                                inclusions = jsonObject.getString("inclusions");
                                knowbeforeyoubook = jsonObject.getString("knowbeforeyoubook");
                                meetUpInfo = jsonObject.getString("meetUpInfo");


                                Picasso.get().load(DOMAIN_NAME + packages.getImg()).into(image1);
                                Picasso.get().load(DOMAIN_NAME + packages.getImg2()).into(image2);
                                Picasso.get().load(DOMAIN_NAME + packages.getImg3()).into(image3);
                                Picasso.get().load(DOMAIN_NAME + packages.getImg4()).into(image4);
                                tv_tripName.setText(tripName);
                                tv_duration.setText(duration);
                                tv_shows.setText(shows);
                                tv_comLanguage.setText(comLanguage);
                                tv_price.setText(" RM" + price);
//                                tv_cprice.setText("Child（0-11）   : RM" + cprice);
//                                tv_sprice.setText("Senior（65+） : RM" + sprice);
//                                tv_dep.setText("Departure Date: " + dep);
//
                                tv_remaining.setText(remaining);
//                                tv_route.setText(route);
//                                tv_deadline.setText("Deadline: " + deadline);
                                tv_description.setText(description);
                                tv_description2.setText(description2);
                                tv_description3.setText(description3);
                                tv_description4.setText(description4);
                                tv_inclusions.setText(inclusions);
                                tv_knowbeforeyoubook.setText(knowbeforeyoubook);
                                tv_meetUpInfo.setText(meetUpInfo);

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
