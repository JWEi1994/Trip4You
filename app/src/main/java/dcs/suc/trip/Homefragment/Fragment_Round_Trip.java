package dcs.suc.trip.Homefragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import dcs.suc.trip.R;

public class Fragment_Round_Trip extends Fragment {

    private Spinner spinner_Source,spinner_Destination;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_round_trip, null);

        spinner_Source = (Spinner) view.findViewById(R.id.spinner_Source);
        spinner_Destination = (Spinner) view.findViewById(R.id.spinner_Destination);

        String[] items = new String[]{"KUL", "JHB", "IPH"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Source.setAdapter(adapter);


        String[] destination = new String[]{"IPH", "KUL", "JHB"};
        ArrayAdapter<String> adapters = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, destination);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_Destination.setAdapter(adapters);

        return view;
    }

}
