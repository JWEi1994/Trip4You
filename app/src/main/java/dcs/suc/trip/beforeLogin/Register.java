package dcs.suc.trip.beforeLogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import dcs.suc.trip.Global;
import dcs.suc.trip.Preferences.SavePreferences;
import dcs.suc.trip.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {

    private EditText et_firstname,et_lastname,et_password,et_email,et_contact,et_nationality;
    private Button btn_register;
    private OkHttpClient okHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_nationality = (EditText)findViewById(R.id.et_nationality);
        et_firstname=(EditText)findViewById(R.id.et_firstname);
        et_lastname=(EditText)findViewById(R.id.et_lastname);
        et_password=(EditText)findViewById(R.id.et_password);
        et_email=(EditText)findViewById(R.id.et_email);
        et_contact=(EditText)findViewById(R.id.et_contact);
        btn_register=(Button)findViewById(R.id.btn_register);

        okHttpClient= new OkHttpClient();

        et_firstname.addTextChangedListener(loginTextWatcher);
        et_lastname.addTextChangedListener(loginTextWatcher);
        et_password.addTextChangedListener(loginTextWatcher);
        et_email.addTextChangedListener(loginTextWatcher);
        et_contact.addTextChangedListener(loginTextWatcher);
        et_nationality.addTextChangedListener(loginTextWatcher);


//        btn_register.setEnabled(false);

//        et_firstname.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                for (int i = 0; i < 1; i++) {
//                    if (s.length() != 0) {
//                        btn_register.setEnabled(true);
//                    } else {
//                        btn_register.setEnabled(false);
//                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        et_lastname.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                for (int i = 0; i < 1; i++) {
//                    if (s.length() != 0) {
//                        btn_register.setEnabled(true);
//                    } else {
//                        btn_register.setEnabled(false);
//                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        et_password.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                for (int i = 0; i < 1; i++) {
//                    if (s.length() != 0) {
//                        btn_register.setEnabled(true);
//                    } else {
//                        btn_register.setEnabled(false);
//                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        et_email.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                for (int i = 0; i < 1; i++) {
//                    if (s.length() != 0) {
//                        btn_register.setEnabled(true);
//                    } else {
//                        btn_register.setEnabled(false);
//                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        et_contact.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                for (int i = 0; i < 1; i++) {
//                    if (s.length() != 0) {
//                        btn_register.setEnabled(true);
//                    } else {
//                        btn_register.setEnabled(false);
//                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        et_nationality.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                for (int i = 0; i < 1; i++) {
//                    if (s.length() != 0) {
//                        btn_register.setEnabled(true);
//                    } else {
//                        btn_register.setEnabled(false);
//                    }
//                }
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createAcc();
            }
        });

    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String usernameInput = et_firstname.getText().toString().trim();
            String lastnameInput = et_lastname.getText().toString().trim();
            String passwordInput = et_password.getText().toString().trim();
            String emailInput = et_email.getText().toString().trim();
            String contactInput = et_contact.getText().toString().trim();
            String nationalityInput = et_nationality.getText().toString().trim();

            btn_register.setEnabled(!usernameInput.isEmpty() && !lastnameInput.isEmpty() && !passwordInput.isEmpty()
            && !emailInput.isEmpty() && !contactInput.isEmpty() && !nationalityInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void createAcc(){
        String strfirstname = et_firstname.getText().toString();
        String strlastname = et_lastname.getText().toString();
        String strpassword = et_password.getText().toString();
        String stremail = et_email.getText().toString();
        String strcontact = et_contact.getText().toString();
        String strnationality = et_nationality.getText().toString();

        RequestBody formbody = new FormBody.Builder()
                .add("firstname",strfirstname)
                .add("lastname",strlastname)
                .add("password",strpassword)
                .add("email",stremail)
                .add("contact",strcontact)
                .add("nationality",strnationality)
                .build();

        Request request = new Request.Builder()
                .url(Global.DOMAIN_NAME+"createAcc.php")
                .post(formbody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Register.this, "Connection Error", Toast.LENGTH_SHORT).show();
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

                            JSONObject jsonObject = new JSONObject(resStr);
                            String success = jsonObject.getString("success");

                            if(success.equals("1")) {

                                String id = jsonObject.getString("id");
                                SavePreferences savePreferences = new SavePreferences(Register.this);
                                savePreferences.saveUserID(id);

                                startActivity(new Intent(Register.this, Login.class));
                                Toast.makeText(Register.this, "Register Successfully", Toast.LENGTH_SHORT).show();

                            }else if (success.equals("0")){
                                Toast.makeText(Register.this, "Failed,Please try again", Toast.LENGTH_SHORT).show();
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

    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
