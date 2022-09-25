package dcs.suc.trip.Profilefragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import dcs.suc.trip.Global;
import dcs.suc.trip.Preferences.LoadPreferences;
import dcs.suc.trip.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ContactUs extends AppCompatActivity {

    private EditText et_username,et_contactnumber,et_email,et_message;
    private Button btn_submit;
    OkHttpClient okHttpClient;
    LoadPreferences loadPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        et_username = (EditText) findViewById(R.id.et_username);
        et_contactnumber = (EditText) findViewById(R.id.et_contactnumber);
        et_email = (EditText) findViewById(R.id.et_email);
        et_message = (EditText) findViewById(R.id.et_message);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        okHttpClient = new OkHttpClient();
        loadPreferences = new LoadPreferences(ContactUs.this);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                message();
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK,resultIntent);
                finish();
            }

            public void message() {

                String username = et_username.getText().toString();
                String contactnumber = et_contactnumber.getText().toString();
                String email = et_email.getText().toString();
                String message = et_message.getText().toString();

                RequestBody formBody = new FormBody.Builder()
                        .add("username", username)
                        .add("contactnumber", contactnumber)
                        .add("email", email)
                        .add("message", message)
                        .build();

                Request request = new Request.Builder()
                        .url(Global.DOMAIN_NAME+"message.php")
                        .post(formBody)
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ContactUs.this, "Connection Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        try {

                            String resStr = response.body().string().toString();

                            JSONObject jsonObject = new JSONObject(resStr);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ContactUs.this, "Submit done", Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });
    }


}