package dcs.suc.trip.Profilefragment;

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

public class ChangePassword extends AppCompatActivity {

    private EditText et_currentpass,et_newpass,et_retypepass;
    private Button btn_save;
    OkHttpClient okHttpClient;
    LoadPreferences loadPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        et_currentpass=(EditText)findViewById(R.id.et_currentpass);
        et_newpass=(EditText)findViewById(R.id.et_newpass);
        et_retypepass=(EditText)findViewById(R.id.et_retypepass);
        btn_save=(Button)findViewById(R.id.btn_save);

        okHttpClient = new OkHttpClient();
        loadPreferences = new LoadPreferences(ChangePassword.this);

        et_currentpass.addTextChangedListener(loginTextWatcher);
        et_newpass.addTextChangedListener(loginTextWatcher);
        et_retypepass.addTextChangedListener(loginTextWatcher);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strcurrentpass = et_currentpass.getText().toString();
                String strnewpass = et_newpass.getText().toString();
                String strretypepass = et_retypepass.getText().toString();

               if (strnewpass.equals(strretypepass)){
                    changepass();
//                   Intent i = new Intent();
//                   setResult(RESULT_OK,i);
                   finish();
                }else{
                    Toast.makeText(ChangePassword.this, "Retype password is incorrect", Toast.LENGTH_SHORT).show();
                }

            }

            public void changepass(){
                String strcurrentpass = et_currentpass.getText().toString();
                String strnewpass = et_newpass.getText().toString();

                String userId = loadPreferences.getUserId();

                RequestBody formbody = new FormBody.Builder()
                        .add("id",userId)
                        .add("currentpass",strcurrentpass)
                        .add("newpass",strnewpass)
                        .build();

                Request request = new Request.Builder()
                        .url(Global.DOMAIN_NAME+"changepass.php")
                        .post(formbody)
                        .build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ChangePassword.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    String resStr = response.body().string().toString();

                                    JSONObject jsonObject = new JSONObject(resStr);
                                    String success = jsonObject.getString("success");

                                    if(success.equals("1")){
                                        Toast.makeText(ChangePassword.this, "Changed Successfully", Toast.LENGTH_SHORT).show();
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
        });


    }

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String currentpassInput = et_currentpass.getText().toString().trim();
            String newpassInput = et_newpass.getText().toString().trim();
            String retypepassInput = et_retypepass.getText().toString().trim();

            btn_save.setEnabled(!currentpassInput.isEmpty() && !newpassInput.isEmpty() && !retypepassInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}
