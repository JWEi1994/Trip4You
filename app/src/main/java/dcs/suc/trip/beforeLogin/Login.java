package dcs.suc.trip.beforeLogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import dcs.suc.trip.Global;
import dcs.suc.trip.Home;
import dcs.suc.trip.Preferences.SavePreferences;
import dcs.suc.trip.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {

    private EditText et_email,et_password;
    private Button btn_login;
    private TextView tv_register;
    private SavePreferences savePreferences;
    OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_email=(EditText)findViewById(R.id.et_email);
        et_password=(EditText)findViewById(R.id.et_password);
        btn_login=(Button)findViewById(R.id.btn_login);
        tv_register=(TextView)findViewById(R.id.tv_register);
        okHttpClient = new OkHttpClient();
        savePreferences= new SavePreferences(Login.this);

        et_email.addTextChangedListener(loginTextWatcher);
        et_password.addTextChangedListener(loginTextWatcher);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSignIn();

                //savePreferences.saveUserID("10");
                //                // Intent i =new Intent(Login.this, UserProfile.class);
                //                //startActivity(i);

            }
        });
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Login.this,Register.class);
                startActivity(i);
            }
        });
    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String passwordInput = et_password.getText().toString().trim();
            String emailInput = et_email.getText().toString().trim();

            btn_login.setEnabled(!passwordInput.isEmpty() && !emailInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    public void userSignIn() {

        String strEmail = et_email.getText().toString().trim();
        String strPassword = et_password.getText().toString();

        RequestBody formBody = new FormBody.Builder()
                .add("email", strEmail)
                .add("password", strPassword)
                .build();

        Request request = new Request.Builder()
                .url( Global.DOMAIN_NAME+"userSignIn.php")
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {


                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try  {

                            try {
                                String resStr = response.body().string();

                                JSONObject jsonObject = new JSONObject(resStr);
                                String success = jsonObject.getString("success");

                                if (success.equals("1")) {

                                    String id = jsonObject.getString("userId");
                                    SavePreferences savePreferences = new SavePreferences(Login.this);
                                    savePreferences.saveUserID(id);

                                    startActivity(new Intent(Login.this, Home.class));
                                    Toast.makeText(Login.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else if (success.equals("0")) {
                                    Toast.makeText(Login.this, "Failed,Please try again", Toast.LENGTH_SHORT).show();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();

            }
        });
    }

}
