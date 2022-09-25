package dcs.suc.trip.Profilefragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import dcs.suc.trip.Global;
import dcs.suc.trip.Homefragment.BookNow.BookResult;
import dcs.suc.trip.Homefragment.BookNow.BookingOptions;
import dcs.suc.trip.ListView.Cart;
import dcs.suc.trip.ListView.CartAdapter;
import dcs.suc.trip.ListView.Reservation;
import dcs.suc.trip.Preferences.LoadPreferences;
import dcs.suc.trip.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditBookingOptions extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "EditBookingOptions";
    private TextView tv_tripName, tv_tripDate, tv_AcounterTxt, tv_CcounterTxt, tv_ScounterTxt, tv_Adisplayprice, tv_Cdisplayprice,
            tv_Sdisplayprice, tv_price, tv_cprice, tv_sprice, tv_totalAmount;
    ;


    private Button btn_editsave, btn_Aminus, btn_Aplus, btn_Cminus, btn_Cplus, btn_Sminus, btn_Splus;
    ProgressDialog progressDialog;

    String strAcounterTxt,strCcounterTxt,strScounterTxt,packageId,reservation_id, cartId, tripName, tripDate, price, sprice, cprice;
    OkHttpClient okHttpClient;
    LoadPreferences loadPreferences;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    int totalAmt = 0;
    int Apax = 0;
    int Cpax = 0;
    int Spax = 0;
    int Atotal = 0;
    int Ctotal = 0;
    int Stotal = 0;
    int newAmt=0;

    String userId, resStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_booking_options);


        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_cprice = (TextView) findViewById(R.id.tv_cprice);
        tv_sprice = (TextView) findViewById(R.id.tv_sprice);
        tv_totalAmount = (TextView) findViewById(R.id.tv_totalAmount);
        tv_tripName = (TextView) findViewById(R.id.tv_tripName);
        tv_tripDate = (TextView) findViewById(R.id.tv_tripDate);
        btn_Aminus = (Button) findViewById(R.id.btn_Aminus);
        btn_Aplus = (Button) findViewById(R.id.btn_Aplus);
        btn_Cminus = (Button) findViewById(R.id.btn_Cminus);
        btn_Cplus = (Button) findViewById(R.id.btn_Cplus);
        btn_Sminus = (Button) findViewById(R.id.btn_Sminus);
        btn_Splus = (Button) findViewById(R.id.btn_Splus);
        btn_Aminus.setOnClickListener(this);
        btn_Aplus.setOnClickListener(this);
        btn_Cminus.setOnClickListener(this);
        btn_Cplus.setOnClickListener(this);
        btn_Sminus.setOnClickListener(this);
        btn_Splus.setOnClickListener(this);

        loadPreferences = new LoadPreferences(EditBookingOptions.this);
        okHttpClient = new OkHttpClient();
        userId = loadPreferences.getUserId();


        tv_AcounterTxt = (TextView) findViewById(R.id.tv_AcounterTxt);
        tv_CcounterTxt = (TextView) findViewById(R.id.tv_CcounterTxt);
        tv_ScounterTxt = (TextView) findViewById(R.id.tv_ScounterTxt);
        tv_Adisplayprice = (TextView) findViewById(R.id.tv_Adisplayprice);
        tv_Cdisplayprice = (TextView) findViewById(R.id.tv_Cdisplayprice);
        tv_Sdisplayprice = (TextView) findViewById(R.id.tv_Sdisplayprice);

        okHttpClient = new OkHttpClient();
        btn_editsave = (Button) findViewById(R.id.btn_editsave);


        cartId = getIntent().getStringExtra("cartId");
        packageId = getIntent().getStringExtra("packageId");
        tripDate = getIntent().getStringExtra("tripDate");
        Apax = getIntent().getIntExtra("Apax", 0);
        Cpax = getIntent().getIntExtra("Cpax", 0);
        Spax = getIntent().getIntExtra("Spax", 0);
        totalAmt = getIntent().getIntExtra("totalAmt",totalAmt);

//        Log.e("card",cartId);
        tv_AcounterTxt.setText(String.valueOf(Apax));
        tv_tripDate.setText(tripDate);
        tv_CcounterTxt.setText(String.valueOf(Cpax));
        tv_ScounterTxt.setText(String.valueOf(Spax));
        tv_totalAmount.setText("RM"+String.valueOf(totalAmt));

        btn_editsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCart();

            }
        });

        tv_tripDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(EditBookingOptions.this, mDateSetListener,
                        year, month, day);
                cal.add(Calendar.YEAR, 0);
                cal.add(Calendar.MONTH, 6);
                cal.add(Calendar.DAY_OF_MONTH, 1);
                dialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
//                cal.add(Calendar.DATE,-10);
                cal.add(Calendar.YEAR, -0);
                cal.add(Calendar.MONTH, -6);
                cal.add(Calendar.DAY_OF_MONTH, -0);
                dialog.getDatePicker().setMinDate(cal.getTimeInMillis());

//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;

                Log.d(TAG, "OnDateSet:mm/dd/yyy:" + month + "/" + day + "/" + year);

                tripDate = month + "/" + day + "/" + year;

                tv_tripDate.setText(tripDate);
            }
        };

        loadPackageInfo(packageId);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_Aminus:


                Apax--;
                if (Apax < 0) {
                    Apax = 0;
                }
                tv_AcounterTxt.setText(Apax + "");
                if (Atotal <= 0) {
                    Atotal = 0;
                }

                Atotal = Apax * Integer.valueOf(price);
//                tv_Adisplayprice.setText("Adult : RM" + Atotal);

                totalAmt = Atotal + Ctotal + Stotal;
                tv_totalAmount.setText(" RM" + totalAmt);
                break;

            case R.id.btn_Aplus:

                Apax++;
                if (Apax <= 10) {
                    tv_AcounterTxt.setText(Apax + "");
                }

                Atotal = Apax * Integer.valueOf(price);
//
//                tv_Adisplayprice.setText("Adult : RM" + Atotal);

                totalAmt = Atotal + Ctotal + Stotal;
                tv_totalAmount.setText(" RM" + totalAmt);
                break;

            case R.id.btn_Cminus:

                Cpax--;
                if (Cpax < 0) {
                    Cpax = 0;
                }
                tv_CcounterTxt.setText(Cpax + "");
                if (Ctotal <= 0) {
                    Ctotal = 0;
                }

                Ctotal = Cpax * Integer.valueOf(cprice);
//                tv_Cdisplayprice.setText("Child : RM" + Ctotal);

                totalAmt = Atotal + Ctotal + Stotal;
                tv_totalAmount.setText(" RM" + totalAmt);
                break;

            case R.id.btn_Cplus:

                Cpax++;
                if (Cpax <= 10) {
                    tv_CcounterTxt.setText(Cpax + "");
                }
                if (Ctotal >= 3000) {
                    Ctotal = 0;
                    Cpax = 0;
                }
                Ctotal = Cpax * Integer.valueOf(cprice);

//                tv_Cdisplayprice.setText("Child : RM" + Ctotal);

                totalAmt = Atotal + Ctotal + Stotal;
                tv_totalAmount.setText(" RM" + totalAmt);
                break;

            case R.id.btn_Sminus:

                Spax--;
                if (Spax < 0) {
                    Spax = 0;
                }
                tv_ScounterTxt.setText(Spax + "");
                if (Stotal <= 0) {
                    Stotal = 0;
                }

                Stotal = Spax * Integer.valueOf(sprice);
//                tv_Sdisplayprice.setText("Senior : RM" + Stotal);

                totalAmt = Atotal + Ctotal + Stotal;
                tv_totalAmount.setText(" RM" + totalAmt);
                break;

            case R.id.btn_Splus:

                Spax++;
                if (Spax <= 10) {
                    tv_ScounterTxt.setText(Spax + "");
                }
                if (Stotal >= 2000) {
                    Stotal = 0;
                    Spax = 0;
                }
                Stotal = Spax * Integer.valueOf(sprice);

//                tv_Sdisplayprice.setText("Senior : RM" + Stotal);

                totalAmt = Atotal + Ctotal + Stotal;
                tv_totalAmount.setText(" RM" + totalAmt);
                break;


        }

    }


    public void editCart() {
        progressDialog = new ProgressDialog(EditBookingOptions.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        String cartId = getIntent().getStringExtra("cartId");
        String userId = loadPreferences.getUserId();
        String tripDate = tv_tripDate.getText().toString();
         strAcounterTxt = tv_AcounterTxt.getText().toString();
         strCcounterTxt = tv_CcounterTxt.getText().toString();
         strScounterTxt = tv_ScounterTxt.getText().toString();

         newAmt= totalAmt;
         Log.e("newAmt",""+newAmt);

        RequestBody formBody = new FormBody.Builder()
                .add("cartId", cartId)
                .add("userId", userId)
                .add("tripDate", tripDate)
                .add("AcounterTxt", strAcounterTxt)
                .add("CcounterTxt", strCcounterTxt)
                .add("ScounterTxt", strScounterTxt)
                .add("totalAmount", ""+newAmt)
                .build();

        Request request = new Request.Builder()
                .url(Global.DOMAIN_NAME + "editCart.php")
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
                        Toast.makeText(EditBookingOptions.this, "Edit Failed", Toast.LENGTH_SHORT).show();

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

                            resStr = response.body().string().toString();

                            JSONObject jsonObject = new JSONObject(resStr);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {



                                Toast.makeText(EditBookingOptions.this, "Save Successfully", Toast.LENGTH_SHORT).show();
                            } else if (success.equals("0")) {
                                Toast.makeText(EditBookingOptions.this, "Edit Failed", Toast.LENGTH_SHORT).show();
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





    public void loadPackageInfo(String packageId) {

        RequestBody formBody = new FormBody.Builder()
                .add("packageId", packageId)
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
                        Toast.makeText(EditBookingOptions.this, "Connnection Error", Toast.LENGTH_SHORT).show();
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

                            Log.e("peiting", resStr);

                            JSONObject jsonObject = new JSONObject(resStr);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
//                                        Toast.makeText(BookingOptions.this, "tripName", Toast.LENGTH_SHORT).show();

                                tripName = jsonObject.getString("tripName");
                                price = jsonObject.getString("price");
                                cprice = jsonObject.getString("cprice");
                                sprice = jsonObject.getString("sprice");


                                tv_tripName.setText(tripName);
                                tv_price.setText("RM" + price);
                                tv_cprice.setText("RM" + cprice);
                                tv_sprice.setText("RM" + sprice);


                            } else {
                                Toast.makeText(EditBookingOptions.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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


}
