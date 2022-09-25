package dcs.suc.trip.ShoppingCartFragment;



import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
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
import dcs.suc.trip.Homefragment.PackageInfo;
import dcs.suc.trip.ListView.Cart;
import dcs.suc.trip.ListView.CartAdapter;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingCart_fragment extends Fragment implements CartAdapter.CakeBox {

    Toolbar toolbar;
    private CheckBox checkBox;
    private TextView tv_total,tv_totalAmount;
    private Button btn_proceed;
    private ListView listview;
    private OkHttpClient okHttpClient;
    private String userId,packageId,cartId;
    private LoadPreferences loadPreferences;
    ArrayList<Cart> cartArrayList = new ArrayList<>();
    ArrayList<Packages> packagesArrayList = new ArrayList<>();
    int totalAmount=0;
    int subTotal=0;
    View view;
    ArrayList<Cart>cartSelected = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.shoppingcart_fragment, container, false);
        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        toolbar.setTitle("Shopping Cart");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        listview = (ListView) view.findViewById(R.id.listview);
        okHttpClient = new OkHttpClient();
        loadPreferences = new LoadPreferences(getContext());
        userId = loadPreferences.getUserId();

        btn_proceed = (Button)view.findViewById(R.id.btn_proceed);
        tv_total = (TextView)view.findViewById(R.id.tv_total);
        tv_totalAmount = (TextView)view.findViewById(R.id.tv_totalAmount);



        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = new Gson().toJson(cartSelected);
                Intent intent = new Intent(getContext(), CartResult.class);
                Bundle bundle = new Bundle();
                bundle.putString("json",json);
                intent.putExtras(bundle);
                getContext().startActivity(intent);

            }
        });
//        cartId = getIntent().getStringExtra("cartId");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        viewCart();
        return view;
    }




    private void viewCart() {

        RequestBody formBody = new FormBody.Builder()
                .add("userId", userId)
//                .add("cartId", cartId)
                .build();

        Request request = new Request.Builder()
                .url(Global.DOMAIN_NAME + "viewCart.php")
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                if (getActivity() == null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).
                                    show();
                        }
                    });
                }
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
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

                                    cart.setImg(jsonObject.getString("image1"));
                                    cart.setCartId(jsonObject.getString("cartId"));
                                    cart.setUserId(jsonObject.getString("userId"));
                                    cart.setPackageId(jsonObject.getString("packageId"));
                                    cart.setTripName(jsonObject.getString("tripName"));
                                    cart.setTripDate(jsonObject.getString("tripDate"));
                                    cart.setRemaining(jsonObject.getString("remaining"));
                                    cart.setCountry(jsonObject.getString("country"));
                                    cart.setDuration(jsonObject.getString("duration"));
                                    cart.setAcounterTxt(jsonObject.getInt("AcounterTxt"));
                                    cart.setCcounterTxt(jsonObject.getInt("CcounterTxt"));
                                    cart.setScounterTxt(jsonObject.getInt("ScounterTxt"));
                                    cart.setTotalAmount(jsonObject.getInt("totalAmount"));
//                                    totalAmount = jsonObject.getInt("totalAmount");
//                                    tv_totalAmount.setText(""+totalAmount);
                                    cartArrayList.add(cart);



//

                                }

                                CartAdapter cartAdapter = new CartAdapter(getContext(), cartArrayList,
                                        ShoppingCart_fragment.this);
                                listview.setAdapter(cartAdapter);


                            } else if (success.equals("0")) {
                                Toast.makeText(getContext(), "Shopping Cart is empty", Toast.LENGTH_SHORT).show();
                            }






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


    }
}
