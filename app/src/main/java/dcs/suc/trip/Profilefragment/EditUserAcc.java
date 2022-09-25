package dcs.suc.trip.Profilefragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import dcs.suc.trip.Global;
import dcs.suc.trip.Home;
import dcs.suc.trip.Preferences.LoadPreferences;
import dcs.suc.trip.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditUserAcc extends AppCompatActivity {

    private EditText et_firstname, et_lastname, et_contact;
    private Button btn_update;
    OkHttpClient okHttpClient;
    private LoadPreferences loadPreferences;

    String resStr,firstName,lastName,contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        et_firstname = (EditText) findViewById(R.id.et_firstname);
        et_lastname = (EditText) findViewById(R.id.et_lastname);
        et_contact = (EditText) findViewById(R.id.et_contact);
        btn_update = (Button) findViewById(R.id.btn_update);
        okHttpClient = new OkHttpClient();
        resStr = getIntent().getStringExtra("value");

        loadPreferences = new LoadPreferences(EditUserAcc.this);

        et_firstname.addTextChangedListener(loginTextWatcher);
        et_lastname.addTextChangedListener(loginTextWatcher);
        et_contact.addTextChangedListener(loginTextWatcher);
        //TODO: Stopped here

        try {
            JSONObject jsonObject = new JSONObject(resStr);

            firstName = jsonObject.getString("firstName");
            lastName = jsonObject.getString("lastName");
            contact = jsonObject.getString("contact");

              et_firstname.setText(firstName);
              et_lastname.setText(lastName);
              et_contact.setText(contact);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUserProfile();
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK,resultIntent);
                finish();
            }
        });


    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String firstnameInput = et_firstname.getText().toString().trim();
            String lastnameInput = et_lastname.getText().toString().trim();
            String contactInput = et_contact.getText().toString().trim();

            btn_update.setEnabled(!firstnameInput.isEmpty() && !lastnameInput.isEmpty() && !contactInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    public void editUserProfile() {

        String strid = loadPreferences.getUserId();
        String strfirstname = et_firstname.getText().toString();
        String strlastname = et_lastname.getText().toString();
        String strcontact = et_contact.getText().toString();

        RequestBody formbody = new FormBody.Builder()
                .add("id",strid)
                .add("firstname", strfirstname)
                .add("lastname", strlastname)
                .add("contact", strcontact)
                .build();

        Request request = new Request.Builder()
                .url(Global.DOMAIN_NAME+"editUserProfile.php")
                .post(formbody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(EditUserAcc.this, "Connection Error", Toast.LENGTH_SHORT).show();
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

                            JSONObject jsonObject = new JSONObject(resStr);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {

                                Toast.makeText(EditUserAcc.this, "Update Successfully", Toast.LENGTH_SHORT).show();

                            } else if (success.equals("0")) {
                                Toast.makeText(EditUserAcc.this, "Failed,Please try again", Toast.LENGTH_SHORT).show();
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
