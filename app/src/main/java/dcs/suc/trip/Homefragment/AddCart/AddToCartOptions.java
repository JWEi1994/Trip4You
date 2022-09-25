package dcs.suc.trip.Homefragment.AddCart;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import dcs.suc.trip.Global;
import dcs.suc.trip.Homefragment.BookNow.BookingOptions;
import dcs.suc.trip.ListView.Cart;
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

public class AddToCartOptions extends AppCompatActivity {

//    private Readable AdultView;
    private Button btn_Aminus, btn_Aplus, btn_Cminus, btn_Cplus, btn_Sminus, btn_Splus, btn_addtocart;
    LoadPreferences loadPreferences;
    String aa,userId,tripDate,price,adultView,sprice,cprice;
    OkHttpClient okHttpClient;
    private TextView tv_tripName, tv_price,AdultView, tv_cprice, tv_sprice, tv_dep, tv_AcounterTxt, tv_CcounterTxt, tv_ScounterTxt, tv_Adisplayprice, tv_Cdisplayprice,
            tv_Sdisplayprice,tv_totalAmount,tv_tripDate;
    String resStr;
    Packages packages;
    private String tripName, dep;
    int Apax = 0;
    int Cpax = 0;
    int Spax = 0;
    int Atotal = 0;
    int Ctotal = 0;
    int Stotal = 0;
    int totalAmt = 0;
    String totalAmount;
    int min;
    String packageId;
    private DatePickerDialog.OnDateSetListener mDateSetListener;



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
        setContentView(R.layout.activity_add_to_cart_options);


//        getSupportActionBar().setTitle("Booking Options");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tv_tripName = (TextView) findViewById(R.id.tv_tripName);
        tv_tripDate = (TextView) findViewById(R.id.tv_tripDate);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_cprice = (TextView) findViewById(R.id.tv_cprice);
        tv_sprice = (TextView) findViewById(R.id.tv_sprice);
        btn_Cplus = (Button) findViewById(R.id.btn_Cplus);
        btn_Splus = (Button) findViewById(R.id.btn_Splus);
        tv_AcounterTxt = (TextView) findViewById(R.id.tv_AcounterTxt);
        tv_CcounterTxt = (TextView) findViewById(R.id.tv_CcounterTxt);
        tv_ScounterTxt = (TextView) findViewById(R.id.tv_ScounterTxt);
        tv_Adisplayprice = (TextView) findViewById(R.id.tv_Adisplayprice);
        tv_Cdisplayprice = (TextView) findViewById(R.id.tv_Cdisplayprice);
        tv_Sdisplayprice = (TextView) findViewById(R.id.tv_Sdisplayprice);
        tv_totalAmount = (TextView)findViewById(R.id.tv_totalAmount);
        btn_Aminus = (Button) findViewById(R.id.btn_Aminus);
        btn_Aplus = (Button) findViewById(R.id.btn_Aplus);
        btn_Cminus = (Button) findViewById(R.id.btn_Cminus);
        btn_Sminus = (Button) findViewById(R.id.btn_Sminus);
        btn_addtocart = (Button) findViewById(R.id.btn_addtocart);
        loadPreferences = new LoadPreferences(AddToCartOptions.this);
        okHttpClient = new OkHttpClient();
        userId = loadPreferences.getUserId();


        tv_tripDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddToCartOptions.this, mDateSetListener,
                        year,month,day);
                cal.add(Calendar.YEAR, 0);
                cal.add(Calendar.MONTH, 6);
                cal.add(Calendar.DAY_OF_MONTH, 1);
                dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());

                cal.add(Calendar.YEAR, -0);
                cal.add(Calendar.MONTH, -6);
                cal.add(Calendar.DAY_OF_MONTH, -0);
                dialog.getDatePicker().setMinDate(cal.getTimeInMillis());


                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month +1;

//                Log.d(TAG,"OnDateSet:mm/dd/yyy:"+month+"/"+day+"/"+year);

                tripDate =month+"/"+day+"/"+year;

                tv_tripDate.setText(tripDate);
            }
        };

        price = getIntent().getStringExtra("price");

        packageId = getIntent().getStringExtra("packageId");

        Cart cart = new Cart();

        btn_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(tripDate == null){

                    Toast.makeText(AddToCartOptions.this, "Please select a trip date.", Toast.LENGTH_SHORT).show();
                }else {

                    addToCartOptions();
                }
            }
        });
        btn_Aminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Apax--;
                if (Apax < 0) {
                    Apax = 0;
                }
                tv_AcounterTxt.setText(Apax + "");
                if (Atotal <= 0) {
                    Atotal = 0;
                } else {
                    Atotal = Atotal - Integer.valueOf(price);
                }
//                tv_Adisplayprice.setText("Adult : RM" + Atotal);

                totalAmt = Atotal + Ctotal + Stotal;
                tv_totalAmount.setText(" RM" + totalAmt);
            }
        });

        btn_Aplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Apax++;
                if (Apax <= 10) {
                    tv_AcounterTxt.setText(Apax + "");
                }
                if (Atotal >= 3650) {
                    Atotal = 0;
                    Apax = 0;
                } else {
                    Atotal = Atotal + Integer.valueOf(price);
                }
//                tv_Adisplayprice.setText("Adult : RM" + Atotal);

                totalAmt = Atotal + Ctotal + Stotal;
                tv_totalAmount.setText(" RM" + totalAmt);
            }
        });

        btn_Cminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cpax--;
                if (Cpax < 0) {
                    Cpax = 0;
                }
                tv_CcounterTxt.setText(Cpax + "");
                if (Ctotal <= 0) {
                    Ctotal = 0;
                } else {
                    Ctotal = Ctotal - Integer.valueOf(cprice);
                }
//                tv_Cdisplayprice.setText("Child : RM" + Ctotal);

                totalAmt = Atotal + Ctotal + Stotal;
                tv_totalAmount.setText(" RM" + totalAmt);

            }
        });

        btn_Cplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cpax++;
                if (Cpax <= 10) {
                    tv_CcounterTxt.setText(Cpax + "");
                }
                if (Ctotal >= 3000) {
                    Ctotal = 0;
                    Cpax = 0;
                } else {
                    Ctotal = Ctotal +Integer.valueOf(cprice);
                }
//                tv_Cdisplayprice.setText("Child : RM" + Ctotal);
                totalAmt = Atotal + Ctotal + Stotal;
                tv_totalAmount.setText(" RM" + totalAmt);

            }
        });

        btn_Sminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spax--;
                if (Spax < 0) {
                    Spax = 0;
                }
                tv_ScounterTxt.setText(Spax + "");
                if (Stotal <= 0) {
                    Stotal = 0;
                } else {
                    Stotal = Stotal - Integer.valueOf(sprice);
                }
//                tv_Sdisplayprice.setText("Senior : RM" + Stotal);

                totalAmt = Atotal + Ctotal + Stotal;
                tv_totalAmount.setText(" RM" + totalAmt);

            }
        });

        btn_Splus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Spax++;
                if (Spax <= 10) {
                    tv_ScounterTxt.setText(Spax + "");
                }
                if (Stotal >= 2000) {
                    Stotal = 0;
                    Spax = 0;
                } else {
                    Stotal = Stotal + Integer.valueOf(sprice);
                }
//                tv_Sdisplayprice.setText("Senior : RM" + Stotal);

                totalAmt = Atotal + Ctotal + Stotal;
                tv_totalAmount.setText(" RM" + totalAmt);

            }
        });


        viewDetails();



    }

    private void viewDetails() {

        RequestBody formBody = new FormBody.Builder()
                .add("packageId",packageId)
                .build();

        Request request = new Request.Builder()
                .url(Global.DOMAIN_NAME + "viewDetails.php")
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddToCartOptions.this, "Connnection Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            resStr = response.body().string().toString();

//                                    Log.e("peiting",resStr);

                            JSONObject jsonObject = new JSONObject(resStr);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
//                                        Toast.makeText(BookingOptions.this, "tripName", Toast.LENGTH_SHORT).show();


                                tripName = jsonObject.getString("tripName");
//                                tripDate = jsonObject.getString("tripDate");
                                price = jsonObject.getString("price");
                                cprice = jsonObject.getString("cprice");
//                                price = jsonObject.getString("price");
                                sprice = jsonObject.getString("sprice");

                                tv_tripName.setText(tripName);
//                                tv_tripDate.setText(tripDate);
                                tv_price.setText("RM"+ price);
                                tv_cprice.setText("RM"+ cprice);
                                tv_sprice.setText("RM"+ sprice);


                            } else {
                                Toast.makeText(AddToCartOptions.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }

//

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }


                    }

                });
            }


        });
    }




    private void addToCartOptions() {

        String userId = loadPreferences.getUserId();

        RequestBody formBody = new FormBody.Builder()
                .add("userId", userId)
                .add("packageId", packageId)
                .add("tripDate", tripDate)
                .add("price", price)
                .add("AcounterTxt", String.valueOf(Apax))
                .add("CcounterTxt", String.valueOf(Cpax))
                .add("ScounterTxt", String.valueOf(Spax))
                .add("totalAmount", String.valueOf(Integer.valueOf((Apax * Integer.valueOf(price))+(Cpax * Integer.valueOf(cprice))+ (Spax * Integer.valueOf(sprice)))))
                .build();
        Request request = new Request.Builder()
                .url(Global.DOMAIN_NAME + "bookingoptions.php")
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AddToCartOptions.this, "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            String resStr = response.body().string().toString();

                            final JSONObject jsonObject = new JSONObject(resStr);

                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {


                                        Toast.makeText(AddToCartOptions.this, "Add to cart", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AddToCartOptions.this, AddedResult.class);
                                        intent.putExtra("packageId",getIntent().getStringExtra("packageId"));
                                        Bundle bundle = new Bundle();
                                        bundle.putString("tripName",tripName);
                                        bundle.putString("tripDate",tripDate);
                                        bundle.putString("Apax",String.valueOf(Apax));
                                        bundle.putString("price",String.valueOf(price));
                                        bundle.putString("Cpax",String.valueOf(Cpax));
                                        bundle.putString("cprice",String.valueOf(cprice));
                                        bundle.putString("Spax",String.valueOf(Spax));
                                        bundle.putString("sprice",String.valueOf(sprice));
                                        bundle.putString("totalAmt",String.valueOf(totalAmt));
                                        intent.putExtras(bundle);
                                        startActivityForResult(intent,RESULT_OK);


//                                        setResult(AddToCartOptions.RESULT_OK, intent);
                                        finish();
//                                        startActivity(intent);


                                    }
                                });




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

