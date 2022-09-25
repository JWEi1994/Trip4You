package dcs.suc.trip.Homefragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import dcs.suc.trip.R;

public class Fragment_One_Way extends Fragment {

    private EditText et_name,et_email,et_phone;
    private Spinner spinner_Source,spinner_Destination;
    private Button btn_Search,btn_Cminus,btn_Cplus,btn_Aminus,btn_Aplus;
    private TextView tv_CcounterTxt,tv_AcounterTxt;
    int CcounterTxt=0;
    int AcounterTxt=0;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_way, null);

        et_name = (EditText)view.findViewById(R.id.et_name);
        et_email = (EditText)view.findViewById(R.id.et_email);
        et_phone = (EditText)view.findViewById(R.id.et_phone);
        tv_CcounterTxt = (TextView)view.findViewById(R.id.tv_CcounterTxt);
        tv_AcounterTxt = (TextView)view.findViewById(R.id.tv_AcounterTxt);
        btn_Search = (Button)view.findViewById(R.id.btn_Search);
        btn_Cminus = (Button)view.findViewById(R.id.btn_Cminus);
        btn_Aminus = (Button)view.findViewById(R.id.btn_Aminus);
        btn_Cplus = (Button)view.findViewById(R.id.btn_Cplus);
        btn_Aplus = (Button)view.findViewById(R.id.btn_Aplus);
        spinner_Source = (Spinner) view.findViewById(R.id.spinner_Source);
        spinner_Destination = (Spinner) view.findViewById(R.id.spinner_Destination);

        et_name.addTextChangedListener(OneWayFlight);
        et_email.addTextChangedListener(OneWayFlight);
        et_phone.addTextChangedListener(OneWayFlight);

        String[] items = new String[]{"KUL", "JHB", "IPH"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Source.setAdapter(adapter);


        String[] destination = new String[]{"IPH", "KUL", "JHB"};
        ArrayAdapter<String> adapters = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, destination);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Destination.setAdapter(adapters);

        btn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String AcounterTxt = tv_AcounterTxt.getText().toString().trim();
                String CcounterTxt = tv_CcounterTxt.getText().toString().trim();

                Intent i = new Intent(getContext(),DisplayFlightTicket.class);
                Bundle bundle = new Bundle();
                bundle.putString("AcounterTxt",AcounterTxt);
                bundle.putString("CcounterTxt",CcounterTxt);

                i.putExtras(bundle);
                startActivity(i);


            }
        });

        btn_Cminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CcounterTxt--;
                if (CcounterTxt<0){
                    CcounterTxt=0;
                }
                tv_CcounterTxt.setText(CcounterTxt+"");

            }
        });

        btn_Cplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CcounterTxt++;

                if (CcounterTxt>=10)
                    CcounterTxt=10;
                    tv_CcounterTxt.setText(CcounterTxt+"");

            }
        });

        btn_Aminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AcounterTxt--;
                if (AcounterTxt<=0) {
                    AcounterTxt = 0;
                }
                    tv_AcounterTxt.setText(AcounterTxt+"");

            }
        });


        btn_Aplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AcounterTxt++;
                if (AcounterTxt>=10)
                    AcounterTxt = 10;

                tv_AcounterTxt.setText(AcounterTxt+"");
            }
        });
    return view;

    }

    private TextWatcher OneWayFlight = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String nameInput = et_name.getText().toString().trim();
            String emailInput = et_email.getText().toString().trim();
            String phoneInput = et_phone.getText().toString().trim();

            btn_Search.setEnabled(!nameInput.isEmpty() && !emailInput.isEmpty() && !phoneInput.isEmpty());


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
