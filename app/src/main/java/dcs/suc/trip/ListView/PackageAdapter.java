package dcs.suc.trip.ListView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import dcs.suc.trip.R;

import static android.content.ContentValues.TAG;
import static dcs.suc.trip.Global.DOMAIN_NAME;

public class PackageAdapter extends BaseAdapter {


    private Context context;
    private ArrayList<Packages> packagesArrayList;
    private LayoutInflater layoutInflater;


    public PackageAdapter(Context context,ArrayList<Packages>packagesArrayList){

        this.context = context;
        this.packagesArrayList = packagesArrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return packagesArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return packagesArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class Item{
        TextView tv_tripName,tv_country,tv_duration,tv_price,tv_dep,tv_remaining;
        ImageView image1,image2,image3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_packages, null);

            item = new Item();

            item.image1 = (ImageView)convertView.findViewById(R.id.image1);
            item.image2 = (ImageView)convertView.findViewById(R.id.image2);
            item.image3 = (ImageView)convertView.findViewById(R.id.image3);
            item.tv_tripName = (TextView) convertView.findViewById(R.id.tv_tripName);
            item.tv_country = (TextView)convertView.findViewById(R.id.tv_country);
            item.tv_duration = (TextView) convertView.findViewById(R.id.tv_duration);
            item.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
//            item.tv_dep = (TextView)convertView.findViewById(R.id.tv_dep);
            item.tv_remaining = (TextView) convertView.findViewById(R.id.tv_remaining);
            convertView.setTag(item);

        } else {
            item = (Item) convertView.getTag();
        }

        Packages packages = packagesArrayList.get(position);


        Picasso.get().load(DOMAIN_NAME + packages.getImg()).into(item.image1);
        item.tv_tripName.setText(packages.getTripName());
        item.tv_country.setText(packages.getCountry());
        item.tv_duration.setText(packages.getDuration());
        item.tv_price.setText("RM"+packages.getPrice());
//        item.tv_dep.setText("DEP: "+packages.getDep());
        item.tv_remaining.setText(packages.getRemaining());

        return convertView;


    }
}
