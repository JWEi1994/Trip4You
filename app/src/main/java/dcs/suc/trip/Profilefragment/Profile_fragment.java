package dcs.suc.trip.Profilefragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import dcs.suc.trip.Global;
import dcs.suc.trip.Preferences.LoadPreferences;
import dcs.suc.trip.Preferences.SavePreferences;
import dcs.suc.trip.R;
import dcs.suc.trip.beforeLogin.SplashScreen;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.support.v4.provider.FontsContractCompat.FontRequestCallback.RESULT_OK;
import static dcs.suc.trip.Global.DOMAIN_NAME;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile_fragment extends Fragment {

    CardView setting;
    Toolbar toolbar;
    private LinearLayout btn_contactus,btn_toPay,btn_about;
    private LinearLayout tv_logout, tv_editUserAcc,tv_changePassword,tv_bought,tv_wishlist;
    TextView tv_name, tv_email;
    View view;
    LoadPreferences loadPreferences;
    String userId;
    OkHttpClient okHttpClient;
    String firstName, lastName, password, email, contact;
    String resStr;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile_fragment, container, false);
//        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
//        toolbar.setTitle("Profile");
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        setHasOptionsMenu(true);
        btn_contactus = (LinearLayout) view.findViewById(R.id.btn_contactus);
        btn_about = (LinearLayout) view.findViewById(R.id.btn_about);
        tv_logout = (LinearLayout) view.findViewById(R.id.tv_logout);
        tv_editUserAcc = (LinearLayout)view.findViewById(R.id.tv_editUserAcc);
        tv_changePassword = (LinearLayout)view.findViewById(R.id.tv_changePassword);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_email = (TextView) view.findViewById(R.id.tv_email);
        tv_wishlist = (LinearLayout) view.findViewById(R.id.tv_wishlist);
        tv_bought = (LinearLayout) view.findViewById(R.id.tv_bought);
        btn_toPay = (LinearLayout) view.findViewById(R.id.btn_toPay);
        setting =  (CardView) view.findViewById(R.id.setting);
        loadPreferences = new LoadPreferences(getContext());
        userId = loadPreferences.getUserId();
        okHttpClient = new OkHttpClient();
        progressDialog = new ProgressDialog(getContext());

        getUserData();

//        setting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getContext(), EditOption.class);
//
//                i.putExtra("resStr", resStr);
////                i.putExtra("firstName", firstName);
////                i.putExtra("lastName", lastName);
////                i.putExtra("password", password);
////                i.putExtra("email", email);
////                i.putExtra("contact", contact);
//                startActivity(i);
//            }
//        });

        tv_editUserAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),EditUserAcc.class);
                i.putExtra("value",resStr);
                startActivityForResult(i,1);
            }
        });

        tv_changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),ChangePassword.class);
                startActivity(i);
            }
        });

        btn_toPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),DisplayReservation.class);
                intent.putExtra("resStr",resStr);
                startActivity(intent);
            }
        });


        btn_contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(),ContactUs.class);
                startActivityForResult(i,1);
            }
        });

        btn_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(),About.class);
                startActivity(i);

            }
        });
        tv_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),DisplayWishList.class);
                i.putExtra("resStr",resStr);
                startActivity(i);
            }
        });



        tv_bought.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),DisplayBought.class);
                i.putExtra("resStr",resStr);
                startActivity(i);
            }
        });


        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Confirm Exit!!!");
                alertDialog.setMessage("Are you sure you want to exit?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SavePreferences savePreferences;

                        savePreferences = new SavePreferences(getContext());
                        savePreferences.clear();

                        alertDialog.dismiss();

                        Intent i = new Intent(getContext(), SplashScreen.class);
                        startActivity(i);
                    }
                });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });     alertDialog.show();
            }
        });
        return view;
    }

    public void getUserData() {

        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        RequestBody formBody = new FormBody.Builder()
                .add("userId", userId)
                .build();

        Request request = new Request.Builder()
                .url(Global.DOMAIN_NAME + "LoadUserProfile.php")
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                progressDialog.dismiss();

                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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
                                 resStr = response.body().string().toString();

                                JSONObject jsonObject = new JSONObject(resStr);
                                String success = jsonObject.getString("success");

                                if (success.equals("1")) {


                                    firstName = jsonObject.getString("firstName");
                                    lastName = jsonObject.getString("lastName");
                                    password = jsonObject.getString("password");
                                    email = jsonObject.getString("email");
                                    contact = jsonObject.getString("contact");


                                    tv_name.setText(firstName + lastName);
                                    tv_email.setText(email);

                                } else {
                                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
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

    public void viewReservation(){

        Request request = new Request.Builder()
                .url(Global.DOMAIN_NAME+"viewReservation.php")
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Connection Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String resStr = null;
                        try {
                            resStr = response.body().string();
                            JSONObject jsonObject = new JSONObject(resStr);

                            String success =jsonObject.getString("success");

                            if (success.equals("1")){

                                JSONArray jsonArray = jsonObject.getJSONArray("list");

                                for (int i=0; i<jsonArray.length(); i++){

                                    jsonObject = jsonArray.getJSONObject(i);


                                }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK){
            getUserData();
        }
    }
}


