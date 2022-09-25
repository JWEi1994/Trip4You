package dcs.suc.trip.ListView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

import dcs.suc.trip.Config.Config;
import dcs.suc.trip.Homefragment.BookNow.BookingInfo;
import dcs.suc.trip.Preferences.LoadPreferences;
import dcs.suc.trip.R;
import okhttp3.OkHttpClient;

import static android.app.Activity.RESULT_OK;
import static dcs.suc.trip.Global.DOMAIN_NAME;

public class ReservationAdapter extends BaseAdapter {

    public static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);
    String paymentDetails ;
    private boolean isCancelled= false;

    private Context context;
    private ArrayList<Reservation> reservationArrayList;
    private LayoutInflater layoutInflater;
    private OkHttpClient okHttpClient;
    private LoadPreferences loadPreferences;
    ProgressDialog progressDialog;
    String totalAmt;

    public ReservationAdapter(Context context, ArrayList<Reservation> reservationArrayList) {

        this.context = context;
        this.reservationArrayList = reservationArrayList;
        layoutInflater = LayoutInflater.from(context);
        okHttpClient = new OkHttpClient();
        loadPreferences = new LoadPreferences(context);
    }

    @Override
    public int getCount() {
        return reservationArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return reservationArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class Item {
        private TextView tv_tripName;
        private TextView tv_country;
        private TextView tv_duration;
        private TextView tv_tripDate;
        private TextView tv_orderDate;
        private TextView tv_remaining;

        private TextView tv_price;
        private ImageView image1;
        private Button btn_toPay;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ReservationAdapter.Item item;
        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.row_reservation, null);
            item = new ReservationAdapter.Item();


            item.image1 = (ImageView)convertView.findViewById(R.id.image1);
            item.btn_toPay = (Button)convertView.findViewById(R.id.btn_toPay);
            item.tv_tripName = (TextView) convertView.findViewById(R.id.tv_tripName);
            item.tv_tripDate = (TextView) convertView.findViewById(R.id.tv_tripDate);
            item.tv_country = (TextView) convertView.findViewById(R.id.tv_country);
            item.tv_duration = (TextView) convertView.findViewById(R.id.tv_duration);
            item.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            item.tv_remaining = (TextView) convertView.findViewById(R.id.tv_remaining);
            item.tv_orderDate = (TextView)convertView.findViewById(R.id.tv_orderDate);
            convertView.setTag(item);




        }else {
            item = (ReservationAdapter.Item) convertView.getTag();

        }

        item.btn_toPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPayment();

            }
        });


         final Reservation reservation = reservationArrayList.get(position);

        Picasso.get().load(DOMAIN_NAME + reservation.getImg()).into(item.image1);
        item.tv_tripName.setText(reservation.getTripName());
        item.tv_tripDate.setText(reservation.getTripDate());
        item.tv_country.setText(reservation.getCountry());
        item.tv_remaining.setText(reservation.getRemaining());
        item.tv_duration.setText(reservation.getDuration());
        item.tv_price.setText("RM "+reservation.getTotalAmt());
        item.tv_orderDate.setText("Booking Date : "+reservation.getOrderDate());

        totalAmt = reservation.getTotalAmt();

        return convertView;
    }

    private void processPayment() {


    }


}