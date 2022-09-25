package dcs.suc.trip.Profilefragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import dcs.suc.trip.R;
import dcs.suc.trip.Preferences.SavePreferences;
import dcs.suc.trip.beforeLogin.SplashScreen;

import static android.support.v4.provider.FontsContractCompat.FontRequestCallback.RESULT_OK;

public class EditOption extends AppCompatActivity {

    private Button btn_logout, btn_editUserAcc,btn_changepassword;
    String resStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_editUserAcc = (Button)findViewById(R.id.btn_editUserAcc);
        btn_changepassword = (Button)findViewById(R.id.btn_changepassword);

        resStr = getIntent().getStringExtra("resStr");

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(EditOption.this).create();
                alertDialog.setTitle("Confirm Exit!!!");
                alertDialog.setMessage("Are you sure you want to exit?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SavePreferences savePreferences;

                        savePreferences = new SavePreferences(EditOption.this);
                        savePreferences.clear();

                        alertDialog.dismiss();

                        Intent i = new Intent(EditOption.this, SplashScreen.class);
                        startActivity(i);
                        finish();
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

        btn_editUserAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditOption.this,EditUserAcc.class);
                i.putExtra("value",resStr);
//                startActivityForResult(i,1);
                startActivityForResult(i,1);
            }
        });

        btn_changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditOption.this,ChangePassword.class);
                startActivity(i);
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && requestCode == RESULT_OK){

        }
    }

}