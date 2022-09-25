package dcs.suc.trip.ListView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import dcs.suc.trip.Global;
import dcs.suc.trip.Preferences.LoadPreferences;
import dcs.suc.trip.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static dcs.suc.trip.Global.DOMAIN_NAME;

public class WishListAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<WishList> wishListArrayList;
    private LayoutInflater layoutInflater;
    private OkHttpClient okHttpClient;
    private LoadPreferences loadPreferences;
    ProgressDialog progressDialog;
    onitemClick onitemClick;

    public WishListAdapter(Context context, ArrayList<WishList>wishListArrayList,onitemClick onitemClick){

        this.context = context;
        this.wishListArrayList = wishListArrayList;
        layoutInflater = LayoutInflater.from(context);

        okHttpClient = new OkHttpClient();
        loadPreferences = new LoadPreferences(context);
        this.onitemClick = onitemClick ;


    }

    @Override
    public int getCount() {
        return wishListArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return wishListArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class Item {

        private ImageView image1;
        private TextView tv_tripName;
        private TextView tv_duration;
        private TextView tv_price;
        private TextView tv_dep;
        private TextView tv_remaining;
        private ImageView delete;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item;
        if (convertView == null){

            convertView = layoutInflater.inflate(R.layout.row_wishlist,null);

            item = new Item();
            item.image1 = (ImageView)convertView.findViewById(R.id.image1);
            item.tv_tripName = (TextView) convertView.findViewById(R.id.tv_tripName);
            item.tv_duration = (TextView) convertView.findViewById(R.id.tv_duration);
            item.tv_price = (TextView)convertView.findViewById(R.id.tv_price);
            item.tv_dep = (TextView)convertView.findViewById(R.id.tv_dep);
            item.tv_remaining = (TextView)convertView.findViewById(R.id.tv_remaining);
            item.delete = (ImageView) convertView.findViewById(R.id.delete);
            convertView.setTag(item);

//            item.cv_wishlist.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(context,PackageInfo.class);
//                    context.startActivity(i);
//
//                }
//            });

        }else{
            item = (Item)convertView.getTag();
        }

        final WishList wishList = wishListArrayList.get(position);


        Picasso.get().load(DOMAIN_NAME +wishList.getImage1()).into(item.image1);
        item.tv_tripName.setText(wishList.getTripName());
        item.tv_duration.setText(wishList.getDuration());
        item.tv_price.setText("RM"+wishList.getPrice());
        item.tv_dep.setText("DEP: "+wishList.getDep());
        item.tv_remaining.setText(wishList.getRemaining());

        item.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onitemClick.itemOnClick(wishList.getPackageId());

            }
        });


        return  convertView;
    }


    public interface onitemClick{
        void itemOnClick(String packageId);

    }

}
