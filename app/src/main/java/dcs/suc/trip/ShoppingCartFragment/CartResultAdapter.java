package dcs.suc.trip.ShoppingCartFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import dcs.suc.trip.ListView.Cart;
import dcs.suc.trip.ListView.CartAdapter;
import dcs.suc.trip.Preferences.LoadPreferences;
import dcs.suc.trip.R;
import okhttp3.OkHttpClient;

public class CartResultAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Cart> cartArrayList;
    private LayoutInflater layoutInflater;
    private OkHttpClient okHttpClient;
    private LoadPreferences loadPreferences;
    ProgressDialog progressDialog;


    public CartResultAdapter(Context context, ArrayList<Cart> cartArrayList) {

        this.context = context;
        this.cartArrayList = cartArrayList;
        layoutInflater = LayoutInflater.from(context);
        okHttpClient = new OkHttpClient();
        loadPreferences = new LoadPreferences(context);
    }


    @Override
    public int getCount() {
        return cartArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return cartArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class Item{

            private TextView tv_tripName;
            private TextView tv_tripDate;
            private TextView tv_Apax;
            private TextView tv_Cpax;
            private TextView tv_Spax;
            private TextView tv_totalAmount;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Item item;
        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.row_cart_result, null);
            item = new Item();
            item.tv_tripName = (TextView)convertView.findViewById(R.id.tv_tripName);
            item.tv_tripDate = (TextView)convertView.findViewById(R.id.tv_tripDate);
            item.tv_Apax = (TextView)convertView.findViewById(R.id.tv_Apax);
            item.tv_Cpax = (TextView)convertView.findViewById(R.id.tv_Cpax);
            item.tv_Spax = (TextView)convertView.findViewById(R.id.tv_Spax);
            item.tv_totalAmount = (TextView)convertView.findViewById(R.id.tv_totalAmount);
            convertView.setTag(item);

        }else{
            item =(Item)convertView.getTag();

        }
        final Cart cart = cartArrayList.get(position);

            item.tv_tripName.setText(cart.getTripName());
            item.tv_tripDate.setText(cart.getTripDate());

        Log.e("tripDate",cart.getTripDate());

            item.tv_Apax.setText("Adult x "+cart.getAcounterTxt());
            item.tv_Cpax.setText("Child x "+cart.getCcounterTxt());
            item.tv_Spax.setText("Senior x "+cart.getScounterTxt());
            item.tv_totalAmount.setText("RM"+cart.getTotalAmount());


            return convertView;
        }

}