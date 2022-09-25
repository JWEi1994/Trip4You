package dcs.suc.trip.ListView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import dcs.suc.trip.Global;
import dcs.suc.trip.Preferences.LoadPreferences;
import dcs.suc.trip.Profilefragment.DisplayBought;
import dcs.suc.trip.R;
import okhttp3.OkHttpClient;

import static dcs.suc.trip.Global.DOMAIN_NAME;


public class BoughtAdapter  extends BaseAdapter{

    private Context context;
    LoadPreferences loadPreferences;
    OkHttpClient okHttpClient;
    private ArrayList<Bought>boughtArrayList;
    private LayoutInflater layoutInflater;

    public BoughtAdapter(DisplayBought context, ArrayList<Bought> boughtArrayList) {

        this.context = context;
        loadPreferences = new LoadPreferences(context);
        okHttpClient = new OkHttpClient();
        this.boughtArrayList = boughtArrayList;
        layoutInflater = LayoutInflater.from(context);


    }

    @Override
    public int getCount() {
        return boughtArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return boughtArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    static class Item {
        private TextView tv_tripName;
//        private TextView tv_dep;
        private TextView tv_country;
        private TextView tv_duration;
        private TextView tv_tripDate,tv_price,tv_invoiceNum,tv_totalAmt;
        private ImageView image1;

    }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final BoughtAdapter.Item item;
            if (convertView == null) {

                convertView = layoutInflater.inflate(R.layout.row_bought, null);
                item = new BoughtAdapter.Item();

                item.image1 = (ImageView)convertView.findViewById(R.id.image1);
                item.tv_tripName = (TextView) convertView.findViewById(R.id.tv_tripName);
//                item.tv_dep = (TextView) convertView.findViewById(R.id.tv_dep);
                item.tv_country = (TextView) convertView.findViewById(R.id.tv_country);
                item.tv_duration = (TextView) convertView.findViewById(R.id.tv_duration);
                item.tv_tripDate = (TextView)convertView.findViewById(R.id.tv_tripDate);
                item.tv_price = (TextView)convertView.findViewById(R.id.tv_price);
                item.tv_totalAmt = (TextView)convertView.findViewById(R.id.tv_totalAmt);
                item.tv_invoiceNum = (TextView)convertView.findViewById(R.id.tv_invoiceNum);
                convertView.setTag(item);




            }else {
                item = (BoughtAdapter.Item) convertView.getTag();

            }
            final Bought bought = boughtArrayList.get(position);




//            String create_time = bundle.getString("create_time");


            Picasso.get().load(DOMAIN_NAME + bought.getImg()).into(item.image1);
            item.tv_tripName.setText(bought.getTripName());
//            item.tv_dep.setText(bought.getDep());
            item.tv_country.setText(bought.getCountry());
            item.tv_duration.setText(bought.getDuration());
            item.tv_totalAmt.setText("RM"+bought.getTotalAmt());
            item.tv_invoiceNum.setText("Invoice Number:"+bought.getId());
            item.tv_tripDate.setText(bought.getTripDate());

            return convertView;
        }
    }