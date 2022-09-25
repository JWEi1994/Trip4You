package dcs.suc.trip.Homefragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import dcs.suc.trip.Global;
import dcs.suc.trip.ListView.PackageAdapter;
import dcs.suc.trip.ListView.Packages;
import dcs.suc.trip.Preferences.LoadPreferences;
import dcs.suc.trip.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home_fragment extends Fragment {

//    private EditText et_search;
    Toolbar toolbar;
    private ListView listView;
    private String packageId;
    OkHttpClient okHttpClient;
    LoadPreferences loadPreferences;
    ArrayList<Packages> packagesArrayList = new ArrayList<>();
    ProgressDialog progressDialog;
    View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home_fragment, container, false);


//        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
//        toolbar.setTitle("Home");
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        setHasOptionsMenu(true);

//        et_search = (EditText)view.findViewById(R.id.et_search);
        listView = (ListView) view.findViewById(R.id.listview);
        okHttpClient = new OkHttpClient();
        loadPreferences = new LoadPreferences(getContext());
        packageId = loadPreferences.getUserId();

        viewPackages();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        return view;
    }

    public void viewPackages() {

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        final Request request = new Request.Builder()
                .url(Global.DOMAIN_NAME + "viewPackages.php")
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
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
//                                        packages.setDep(jsonObject.getString("dep"));
                                        packages.setRemaining(jsonObject.getString("remaining"));

                                        packagesArrayList.add(packages);


                                    }
                                    PackageAdapter packageAdapter = new PackageAdapter(getContext(), packagesArrayList);
                                    listView.setAdapter(packageAdapter);


                                } else if (success.equals("0")) {
                                    Toast.makeText(getContext(), "Failed,Please try again", Toast.LENGTH_SHORT).show();
                                }


                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                                        Intent i = new Intent(getContext(), PackageInfo.class);
                                        i.putExtra("packageId", packagesArrayList.get(position).getPackageId());


                                        startActivityForResult(i,RESULT_OK);

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
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


            if(resultCode == RESULT_OK){


            }


    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_filter, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.btn_filter:
//                Toast.makeText(getContext(), "filter", Toast.LENGTH_SHORT).show();
//                break;
//        };
//        return super.onOptionsItemSelected(item);
//    }

}
