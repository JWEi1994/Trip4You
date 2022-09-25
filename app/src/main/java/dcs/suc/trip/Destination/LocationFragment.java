package dcs.suc.trip.Destination;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import dcs.suc.trip.Destination.KLStatePackage;
import dcs.suc.trip.R;
import okhttp3.OkHttpClient;

public class LocationFragment extends Fragment {
    OkHttpClient okHttpClient;
    private ImageView img_kl,img_sabah,img_kedah,img_malacca,img_johor;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.row_location, container, false);
        okHttpClient = new OkHttpClient();
        img_kl = (ImageView)view.findViewById(R.id.img_kl);
        img_sabah = (ImageView)view.findViewById(R.id.img_sabah);
        img_kedah = (ImageView)view.findViewById(R.id.img_kedah);
        img_malacca = (ImageView)view.findViewById(R.id.img_malacca);
        img_johor = (ImageView)view.findViewById(R.id.img_johor);

        img_kl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(),KLStatePackage.class);
                startActivity(i);
            }
        });


        img_kedah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(),KedahStatePackage.class);
                startActivity(i);
            }
        });

        img_johor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(),JohorStatePackage.class);
                startActivity(i);
            }
        });

        img_malacca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(),MalaccaStatePackage.class);
                startActivity(i);
            }
        });

        img_sabah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getContext(),SabahStatePackage.class);
                startActivity(i);
            }
        });

        return view;
    }
}
