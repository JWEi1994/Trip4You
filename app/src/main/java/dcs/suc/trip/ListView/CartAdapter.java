package dcs.suc.trip.ListView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import dcs.suc.trip.Profilefragment.EditBookingOptions;
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

public class CartAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Cart> cartArrayList;
    private LayoutInflater layoutInflater;
    private OkHttpClient okHttpClient;
    private LoadPreferences loadPreferences;
    ProgressDialog progressDialog;

    CakeBox cakeBox;

    public CartAdapter(Context context, ArrayList<Cart> cartArrayList,CakeBox cakeBox) {

        this.context = context;
        this.cartArrayList = cartArrayList;
        layoutInflater = LayoutInflater.from(context);
        okHttpClient = new OkHttpClient();
        loadPreferences = new LoadPreferences(context);
        this.cakeBox = cakeBox;
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

    static class Item {
        private TextView tv_tripName;
        private TextView tv_tripDate;
        private TextView tv_remaining;
        private TextView tv_country;
        private TextView tv_AcounterTxt;
        private TextView tv_CcounterTxt;
        private TextView tv_ScounterTxt;
        private Button delete;
        private Button btn_edit;
        private TextView tv_totalAmount;
        private CardView cv_cart;
        private ImageView image1;
        private CheckBox checkBox;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Item item;
        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.row_cart, null);
            item = new Item();
            item.image1 = (ImageView) convertView.findViewById(R.id.image1);
            item.tv_tripName = (TextView) convertView.findViewById(R.id.tv_tripName);
            item.tv_tripDate = (TextView) convertView.findViewById(R.id.tv_tripDate);
            item.tv_remaining = (TextView) convertView.findViewById(R.id.tv_remaining);
            item.tv_country = (TextView) convertView.findViewById(R.id.tv_country);
            item.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            item.tv_AcounterTxt = (TextView) convertView.findViewById(R.id.tv_AcounterTxt);
            item.tv_CcounterTxt = (TextView) convertView.findViewById(R.id.tv_CcounterTxt);
            item.tv_ScounterTxt = (TextView) convertView.findViewById(R.id.tv_ScounterTxt);
            item.tv_totalAmount = (TextView) convertView.findViewById(R.id.tv_totalAmount);
            item.btn_edit = (Button) convertView.findViewById(R.id.btn_edit);
            item.delete = (Button) convertView.findViewById(R.id.delete);
            item.cv_cart = (CardView) convertView.findViewById(R.id.cv_cart);
            convertView.setTag(item);


        } else {
            item = (Item) convertView.getTag();

        }

        final Cart cart = cartArrayList.get(position);

        Picasso.get().load(DOMAIN_NAME + cart.getImg()).into(item.image1);
        item.tv_tripName.setText(cart.getTripName());
        item.tv_tripDate.setText(cart.getTripDate());
        item.tv_remaining.setText(cart.getRemaining());
        item.tv_country.setText(cart.getCountry());
        item.tv_AcounterTxt.setText("Adult " + " x " + cart.getAcounterTxt());
        item.tv_CcounterTxt.setText("Child " + "x " + cart.getCcounterTxt());
        item.tv_ScounterTxt.setText("Senior " + "x " + cart.getScounterTxt());
        item.tv_totalAmount.setText(("RM " + cart.gettotalAmount()));


        item.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteCart(cart.getCartId());
            }
        });

        item.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String cartId = cart.getCartId();

                Intent intent = new Intent(v.getContext(), EditBookingOptions.class);
                intent.putExtra("cartId", cartId);
                intent.putExtra("packageId", cart.getPackageId());
                intent.putExtra("tripDate", cart.getTripDate());
                intent.putExtra("Apax", cart.getAcounterTxt());
                intent.putExtra("Cpax", cart.getCcounterTxt());
                intent.putExtra("Spax", cart.getScounterTxt());
                intent.putExtra("totalAmt", cart.getTotalAmount());


                context.startActivity(intent);

            }
        });



        item.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (item.checkBox.isChecked()){

                    cakeBox.CakeA(cartArrayList.get(position),true);

                }else {

                    cakeBox.CakeA(cartArrayList.get(position),false);

                }
            }
        });

        return convertView;

    }


    public interface CakeBox {
        void CakeA(Cart cart,Boolean isChecked);
    }

    private void deleteCart(String cartId) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        String userId = loadPreferences.getUserId();


        RequestBody formBody = new FormBody.Builder()
                .add("cartId", cartId)
//                .add("userId",userId)

                .build();

        Request request = new Request.Builder()
                .url(DOMAIN_NAME + "deleteCart.php")
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                ((Activity) context).runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        //Code for the UiThread
                        Toast.makeText(context, "Delete Failed", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            progressDialog.dismiss();

                            String resStr = response.body().string().toString();

                            JSONObject jsonObject = new JSONObject(resStr);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {

                                Toast.makeText(context, "Delete Successfully", Toast.LENGTH_SHORT).show();

                            } else if (success.equals("0")) {

                                Toast.makeText(context, "Delete Failed", Toast.LENGTH_SHORT).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }




}
