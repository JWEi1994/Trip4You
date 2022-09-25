package dcs.suc.trip.Destination;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import dcs.suc.trip.Global;
import dcs.suc.trip.Homefragment.PackageInfo;
import dcs.suc.trip.ListView.PackageAdapter;
import dcs.suc.trip.ListView.Packages;
import dcs.suc.trip.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JohorStatePackage extends AppCompatActivity {

    private ListView listView;
    OkHttpClient okHttpClient;
    ArrayList<Packages> packagesArrayList = new ArrayList<>();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_johor_state_package);

        listView = (ListView) findViewById(R.id.listview);
        okHttpClient = new OkHttpClient();

        viewJohorPackages();
    }

    private void viewJohorPackages() {
        progressDialog = new ProgressDialog(JohorStatePackage.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        Request request = new Request.Builder()
                .url(Global.DOMAIN_NAME + "viewJohorPackages.php")
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        progressDialog.dismiss();
                        Toast.makeText(JohorStatePackage.this, "Connection Error", Toast.LENGTH_SHORT).show();
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

                            String resStr = response.body().string().toString();

                            JSONObject jsonObject = new JSONObject(resStr);

                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                JSONArray jArray = jsonObject.getJSONArray("list");
//
                                for (int i = 0; i < jArray.length(); i++) {
//
                                    jsonObject = jArray.getJSONObject(i);

                                    Packages packages = new Packages();
                                    packages.setImg(jsonObject.getString("image1"));
                                    packages.setPackageId(jsonObject.getString("packageId"));
                                    packages.setTripName(jsonObject.getString("tripName"));
                                    packages.setCountry(jsonObject.getString("country"));
                                    packages.setDuration(jsonObject.getString("duration"));
                                    packages.setPrice(jsonObject.getString("price"));
//                                    packages.setDep(jsonObject.getString("dep"));
                                    packages.setRemaining(jsonObject.getString("remaining"));

                                    packagesArrayList.add(packages);


                                }
                                PackageAdapter packageAdapter = new PackageAdapter(JohorStatePackage.this, packagesArrayList);
                                listView.setAdapter(packageAdapter);


                            } else if (success.equals("0")) {
                                Toast.makeText(JohorStatePackage.this, "Failed,Please try again", Toast.LENGTH_SHORT).show();
                            }


                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    Intent i = new Intent(JohorStatePackage.this, PackageInfo.class);
                                    i.putExtra("packageId", packagesArrayList.get(position).getPackageId());
                                    startActivity(i);

//                                    Toast.makeText(getContext(), "aaa", Toast.LENGTH_SHORT).show();
                                }
                            });


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