package dcs.suc.trip.ListView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import dcs.suc.trip.R;

import static dcs.suc.trip.Global.DOMAIN_NAME;

public class FlightAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Flight> flightArrayList;
    private LayoutInflater layoutInflater;
    public FlightAdapter(Context context,ArrayList<Flight>flightArrayList){

        this.context = context;
        this.flightArrayList = flightArrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return flightArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return flightArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class Item{
        private TextView tv_AflightPrice,tv_CflightPrice,tv_source,tv_destination,tv_departDate,tv_departTime,tv_arrivedDate,tv_arrivedTime,
                tv_Adisplayprice,tv_Cdisplayprice,tv_AcounterTxt,tv_CcounterTxt;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FlightAdapter.Item item;

        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.row_flight,null);
            item = new FlightAdapter.Item();

            item.tv_AflightPrice = (TextView)convertView.findViewById(R.id.tv_AflightPrice);
            item.tv_CflightPrice = (TextView)convertView.findViewById(R.id.tv_CflightPrice);
            item.tv_AcounterTxt = (TextView)convertView.findViewById(R.id.tv_AcounterTxt);
            item.tv_CcounterTxt = (TextView)convertView.findViewById(R.id.tv_CcounterTxt);
            item.tv_source = (TextView)convertView.findViewById(R.id.tv_source);
            item.tv_destination = (TextView)convertView.findViewById(R.id.tv_destination);
            item.tv_departDate = (TextView)convertView.findViewById(R.id.tv_departDate);
            item.tv_departTime = (TextView)convertView.findViewById(R.id.tv_departTime);
            item.tv_arrivedDate = (TextView)convertView.findViewById(R.id.tv_arrivedDate);
            item.tv_arrivedTime = (TextView)convertView.findViewById(R.id.tv_arrivedTime);
            item.tv_Adisplayprice = (TextView)convertView.findViewById(R.id.tv_Adisplayprice);
            item.tv_Cdisplayprice = (TextView)convertView.findViewById(R.id.tv_Cdisplayprice);

            convertView.setTag(item);




        }else {
            item = (FlightAdapter.Item) convertView.getTag();
        }

        Flight flight = flightArrayList.get(position);


        Intent i = ((Activity)context).getIntent();
        Bundle bundle = i.getExtras();

        String AcounterTxt = bundle.getString("AcounterTxt");
        String CcounterTxt = bundle.getString("CcounterTxt");

        item.tv_AcounterTxt.setText(" x "+AcounterTxt);
        item.tv_CcounterTxt.setText(" x "+CcounterTxt);

        item.tv_AflightPrice.setText("RM"+flight.getAFlightprice());
        item.tv_CflightPrice.setText("RM"+flight.getCFlightPrice());


        item.tv_Adisplayprice.setText("RM"+String.valueOf(Integer.valueOf(AcounterTxt)*Integer.valueOf(flight.getAFlightprice())));
        item.tv_Cdisplayprice.setText("RM"+String.valueOf(Integer.valueOf(CcounterTxt)*Integer.valueOf(flight.getCFlightPrice())));
        item.tv_source.setText(flight.getSource());
        item.tv_destination.setText(flight.getDestination());
        item.tv_departDate.setText(flight.getDepartDate());
        item.tv_departTime.setText(flight.getDepartTime());
        item.tv_arrivedDate.setText(flight.getArrivedDate());
        item.tv_arrivedTime.setText(flight.getArrivedTime());
        return convertView;
    }

}
