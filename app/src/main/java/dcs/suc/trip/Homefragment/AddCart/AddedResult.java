package dcs.suc.trip.Homefragment.AddCart;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import dcs.suc.trip.Home;
import dcs.suc.trip.Homefragment.Home_fragment;
import dcs.suc.trip.Preferences.LoadPreferences;
import dcs.suc.trip.Profilefragment.DisplayCart;
import dcs.suc.trip.R;
import dcs.suc.trip.ShoppingCartFragment.ShoppingCart_fragment;
import okhttp3.OkHttpClient;

public class AddedResult extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private Button btn_proceed,btn_ctnShop;
    private TextView tv_tripName,tv_tripDate;
    private TextView tv_dep;
    private TextView tv_Apax;
    private TextView tv_Cpax;
    private TextView tv_Spax;
    private TextView tv_totalAmount;
    String totalAmount;
    OkHttpClient okHttpClient;
    String packageId,userId,aa;
    LoadPreferences loadPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_result);

        btn_ctnShop = (Button)findViewById(R.id.btn_ctnShop);
        btn_proceed = (Button)findViewById(R.id.btn_proceed);
        tv_tripName = (TextView)findViewById(R.id.tv_tripName);
        tv_tripDate = (TextView)findViewById(R.id.tv_tripDate);
//        tv_dep = (TextView)findViewById(R.id.tv_dep);
        tv_Apax = (TextView)findViewById(R.id.tv_Apax);
        tv_Cpax = (TextView)findViewById(R.id.tv_Cpax);
        tv_Spax = (TextView)findViewById(R.id.tv_Spax);
        tv_totalAmount = (TextView)findViewById(R.id.tv_totalAmount);
        okHttpClient = new OkHttpClient();
        loadPreferences = new LoadPreferences(AddedResult.this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String packageId = getIntent().getStringExtra("packageId");


        final String tripName = bundle.getString("tripName");
//        final String dep = bundle.getString("dep");
        final String tripDate = bundle.getString("tripDate");
        final String Apax = bundle.getString("Apax");
        final String Cpax = bundle.getString("Cpax");
        final String Spax = bundle.getString("Spax");
        final String price = bundle.getString("price");
        final String cprice = bundle.getString("cprice");
        final String sprice = bundle.getString("sprice");
        final String totalAmt = bundle.getString("totalAmt");

        tv_tripName.setText(tripName);
        tv_tripDate.setText(tripDate);
//        tv_dep.setText(dep);
        tv_Apax.setText("Adult  x "+Apax );
        tv_Cpax.setText("Child  x "+Cpax );
        tv_Spax.setText("Senior  x "+Spax);
        tv_totalAmount.setText("RM"+totalAmt);


        btn_ctnShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddedResult.this, Home.class);

                setResult(AddedResult.RESULT_OK, intent);
                finish();
//                startActivityForResult(intent,1);
//                finish();
            }
        });

        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddedResult.this, DisplayCart.class);
                intent.putExtra("packageId",getIntent().getStringExtra("packageId"));
                Bundle bundle = new Bundle();
                bundle.putString("tripDate",tripDate);
                startActivityForResult(intent,1);



            }



        });




    }

}

