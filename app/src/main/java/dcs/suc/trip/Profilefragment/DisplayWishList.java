package dcs.suc.trip.Profilefragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import dcs.suc.trip.ListView.WishList;
import dcs.suc.trip.ListView.WishListAdapter;
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

public class DisplayWishList extends AppCompatActivity implements WishListAdapter.onitemClick {

    private ListView listview;
    private OkHttpClient okHttpClient;
    private String userId;
    private LoadPreferences loadPreferences;
    ArrayList<WishList> wishListArrayList = new ArrayList<>();
    WishListAdapter wishListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_wish_list);

        listview = (ListView) findViewById(R.id.listview);
        wishListAdapter = new WishListAdapter(DisplayWishList.this, wishListArrayList, DisplayWishList.this);
        listview.setAdapter(wishListAdapter);


        okHttpClient = new OkHttpClient();
        loadPreferences = new LoadPreferences(DisplayWishList.this);

        userId = loadPreferences.getUserId();


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                WishList wishList = wishListArrayList.get(position);
                String packageId = wishList.getPackageId();


                Intent i = new Intent(DisplayWishList.this, PackageInfo.class);
                i.putExtra("packageId", packageId);
                startActivity(i);

            }
        });
//        WishList wishList1 = new WishList();
//        wishList1.setPackageId("1");
//        wishList1.setTripName("acom");
////        wishList1.setDuration("aa");
////        wishList1.setPrice("RM1");
//
//        WishList wishList2 = new WishList();
//        wishList2.setPackageId("2");
//        wishList2.setTripName("bcom");
//        wishList2.setDuration("bb");
//        wishList2.setPrice("RM2");
//
//        wishListArrayList.add(wishList1);
//        wishListArrayList.add(wishList2);

        viewWishlist();

    }

    private void viewWishlist() {

        RequestBody formBody = new FormBody.Builder()
                .add("userId", userId)
                .build();

        Request request = new Request.Builder()
                .url(Global.DOMAIN_NAME + "viewWishlist.php")
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DisplayWishList.this, "Connnection Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final String resStr = response.body().string().toString();

//                            Log.e("peiting",resStr);
//                            Toast.makeText(DisplayWishList.this, resStr, Toast.LENGTH_SHORT).show();

                            JSONObject jsonObject = new JSONObject(resStr);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {

                                JSONArray jArray = jsonObject.getJSONArray("list");

                                for (int i = 0; i < jArray.length(); i++) {

                                    jsonObject = jArray.getJSONObject(i);

                                    WishList wishlist = new WishList();
                                    wishlist.setPackageId(jsonObject.getString("packageId"));
                                    wishlist.setImage1(jsonObject.getString("image1"));
                                    wishlist.setTripName(jsonObject.getString("tripName"));
                                    wishlist.setDuration(jsonObject.getString("duration"));
                                    wishlist.setPrice(jsonObject.getString("price"));
                                    wishlist.setDep(jsonObject.getString("dep"));
                                    wishlist.setRemaining(jsonObject.getString("remaining"));
                                    wishListArrayList.add(wishlist);


                                }

                                 wishListAdapter = new WishListAdapter(DisplayWishList.this, wishListArrayList, DisplayWishList.this);
                                listview.setAdapter(wishListAdapter);


                            } else if (success.equals("0")) {
                                wishListAdapter.notifyDataSetChanged();

                                Toast.makeText(DisplayWishList.this, "No WishList", Toast.LENGTH_SHORT).show();
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

    @Override
    public void itemOnClick(String packageId) {
        deleteWishList(packageId);
    }


    private void deleteWishList(String packageId) {

        final ProgressDialog progressDialog = new ProgressDialog(DisplayWishList.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        String userId = loadPreferences.getUserId();

        RequestBody formBody = new FormBody.Builder()
                .add("packageId", packageId)
                .build();

        Request request = new Request.Builder()
                .url(DOMAIN_NAME + "deleteWishList.php")
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        //Code for the UiThread
                        Toast.makeText(DisplayWishList.this, "Delete Failed", Toast.LENGTH_SHORT).show();

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

                                wishListArrayList.clear();
                                viewWishlist();
                                Toast.makeText(DisplayWishList.this, "Delete Successfully", Toast.LENGTH_SHORT).show();
                            } else if (success.equals("0")) {
                                Toast.makeText(DisplayWishList.this, "Delete Failed", Toast.LENGTH_SHORT).show();
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

}
