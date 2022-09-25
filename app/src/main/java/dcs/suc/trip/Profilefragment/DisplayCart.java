package dcs.suc.trip.Profilefragment;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import dcs.suc.trip.Global;
import dcs.suc.trip.ListView.Cart;
import dcs.suc.trip.ListView.CartAdapter;
import dcs.suc.trip.Preferences.LoadPreferences;
import dcs.suc.trip.R;
import dcs.suc.trip.ShoppingCartFragment.CartResult;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DisplayCart extends AppCompatActivity implements CartAdapter.CakeBox {

    Toolbar toolbar;
    private Button btn_proceed;
    private ListView listview;
    private OkHttpClient okHttpClient;
    private String userId,packageId,cartId;
    private LoadPreferences loadPreferences;
    ArrayList<Cart>cartArrayList = new ArrayList<>();
    ArrayList<Cart>cartSelected = new ArrayList<>();
    CartAdapter cartAdapter;
    int totalAmount =0;
    private TextView tv_total;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_cart);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Shopping Cart");


        btn_proceed = (Button)findViewById(R.id.btn_proceed);
        tv_total = (TextView)findViewById(R.id.tv_total);
        listview = (ListView) findViewById(R.id.listview);
        okHttpClient = new OkHttpClient();
        loadPreferences = new LoadPreferences(DisplayCart.this);
        userId = loadPreferences.getUserId();


        cartId = getIntent().getStringExtra("cartId");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String json = new Gson().toJson(cartSelected);

//                Log.e("json",json);
                Intent intent = new Intent(DisplayCart.this, CartResult.class);

                Bundle bundle = new Bundle();
                bundle.putString("json",json);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });



        viewCart();
    }

    private void viewCart() {

        RequestBody formBody = new FormBody.Builder()
                .add("userId", userId)
                .build();

        Request request = new Request.Builder()
                .url(Global.DOMAIN_NAME + "viewCart.php")
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DisplayCart.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        try {
                            final String resStr = response.body().string();

//                            Toast.makeText(DisplayCart.this, resStr, Toast.LENGTH_SHORT).show();
//
                            JSONObject jsonObject = new JSONObject(resStr);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
//                                Toast.makeText(DisplayCart.this, jsonObject.getString("tripName"), Toast.LENGTH_SHORT).show();
                                JSONArray jArray = jsonObject.getJSONArray("list");
                                for (int i = 0; i < jArray.length(); i++) {

                                jsonObject = jArray.getJSONObject(i);

                                Cart cart = new Cart();

                                    cart.setPackageId(jsonObject.getString("packageId"));
                                    cart.setImg(jsonObject.getString("image1"));
                                    cart.setTripName(jsonObject.getString("tripName"));
                                    cart.setTripDate(jsonObject.getString("tripDate"));
                                    cart.setDuration(jsonObject.getString("duration"));
                                    cart.setCountry(jsonObject.getString("country"));
                                    cart.setAcounterTxt(jsonObject.getInt("AcounterTxt"));
                                    cart.setCcounterTxt(jsonObject.getInt("CcounterTxt"));
                                    cart.setScounterTxt(jsonObject.getInt("ScounterTxt"));
                                    cart.setTotalAmount(jsonObject.getInt("totalAmount"));

                                    cartArrayList.add(cart);


//

                                }

                                CartAdapter cartAdapter = new CartAdapter(DisplayCart.this,cartArrayList,DisplayCart.this);
                                listview.setAdapter(cartAdapter);



                            }else if(success.equals("0")){
                                Toast.makeText(DisplayCart.this, "Your shopping cart is empty", Toast.LENGTH_SHORT).show();
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
    public void CakeA(Cart cart, Boolean isChecked) {

        if (isChecked ==true){
            cartSelected.add(cart);

            int subAmount =cart.getTotalAmount();
            totalAmount =  totalAmount + subAmount;
            tv_total.setText("RM"+totalAmount+"");


        }else {
            cartSelected.remove(cart);

            int subAmount =cart.getTotalAmount();
            totalAmount =  totalAmount - subAmount;
            tv_total.setText("RM"+totalAmount+"");
        }

        Log.e("size",cartSelected.size()+"");

    }
}
