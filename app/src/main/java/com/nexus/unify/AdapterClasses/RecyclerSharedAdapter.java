package com.nexus.unify.AdapterClasses;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ContentDetailsActivity;
import com.bumptech.glide.Glide;
import com.nexus.unify.ModelClasses.Posts;

import com.nexus.unify.R;
import com.nexus.unify.SharedActivity;
import com.razorpay.Checkout;
import com.razorpay.ExternalWalletListener;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.util.List;

public class RecyclerSharedAdapter extends RecyclerView.Adapter implements PaymentResultListener {
    Context context;
    private static final String TAG = "RecyclerAdapter";
    List<Posts> moviesList;
    private final int limit = 10;

    public RecyclerSharedAdapter(List<Posts> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
//                     if (position % 2 == 0) {
//            return 0;
//        }
//        return 1;

        if (moviesList.get(position).getType().equals("image")) {
            return 1;
        } else {
            return 0;

        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == 1) {
            view = layoutInflater.inflate(R.layout.row_item, parent, false);
            return new ViewHolderOne(view);
        }

        view = layoutInflater.inflate(R.layout.item_shared, parent, false);
        return new ViewHolderTwo(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // setAnimation(holder.itemView, position);
//        if (position % 2 == 0) {
//            ViewHolderOne viewHolderOne = (ViewHolderOne) holder;
//            viewHolderOne.textView.setText(moviesList.get(position));
//            viewHolderOne.rowCountTextView.setText(String.valueOf(position));
//        } else {
//            ViewHolderTwo viewHolderTwo = (ViewHolderTwo) holder;
//            viewHolderTwo.textView.setText(moviesList.get(position));
//        }



        SharedPreferences.Editor editor = context.getSharedPreferences("MY", MODE_PRIVATE).edit();
       // Toast.makeText(context, ""+moviesList.get(position).getPublisher(), Toast.LENGTH_SHORT).show();
        editor.putString("pubid", moviesList.get(position).getPublisher());
        editor.apply();
        if (moviesList.get(position).getType().equals("image")) {


            ViewHolderOne viewHolderOne = (ViewHolderOne) holder;
            Glide.with(context).load(moviesList.get(position).getUrl())
                    .placeholder(R.drawable.post_place)
                    .into(viewHolderOne.imageView);
            viewHolderOne.price.setText("₹" + moviesList.get(position).getPrice());
            viewHolderOne.textView.setText(moviesList.get(position).getText());




            viewHolderOne.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context == null) {
                        return;
                    } else {


                    }

                }
            });
            if (moviesList.get(position).getUrl().equals("null")) {
                viewHolderOne.imageView.setVisibility(View.GONE);
                viewHolderOne.textView.setVisibility(View.VISIBLE);
                viewHolderOne.textView.setText(moviesList.get(position).getText());
                viewHolderOne.price.setText(moviesList.get(position).getPrice());
            } else {

            }

        } else {
            ViewHolderTwo viewHolderTwo = (ViewHolderTwo) holder;
            Glide.with(context).load(moviesList.get(position).getUrl())
                    .placeholder(R.drawable.avatar)
                    .into(viewHolderTwo.imageView1);
            viewHolderTwo.TV_DESC.setText(moviesList.get(position).getText());
            viewHolderTwo.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context == null) {
                        return;
                    } else {

                        try {
                            try {
                                Intent i = new Intent(context, ContentDetailsActivity.class);
                                SharedPreferences.Editor editor = context.getSharedPreferences("MY", MODE_PRIVATE).edit();
                                editor.putString("pid", moviesList.get(position).getPostid());

                                editor.apply();




                                context.startActivity(i);
                            } catch (Exception e) {


                            }
                        } catch (Exception e) {


                        }
                    }

                }
            });
            viewHolderTwo.bt_unlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Checkout.preload(context);
                    startPayment();
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        if (moviesList.size() > limit) {
            return limit;
        } else {
            return moviesList.size();
        }
    }


    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(context, "s" + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(context, "s" + s, Toast.LENGTH_SHORT).show();
    }

    class ViewHolderOne extends RecyclerView.ViewHolder {


        ImageView imageView;
        TextView textView, price;
        CardView cardView;

        public ViewHolderOne(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.text_desc);
            cardView = itemView.findViewById(R.id.view2);
            price = itemView.findViewById(R.id.prize);

        }
    }

    class ViewHolderTwo extends RecyclerView.ViewHolder {

        ImageView imageView1;
        TextView TV_DESC;
        Button bt_unlock;

        public ViewHolderTwo(@NonNull View itemView) {
            super(itemView);

            imageView1 = itemView.findViewById(R.id.imageView1);
            TV_DESC = itemView.findViewById(R.id.text_desc);
            bt_unlock = itemView.findViewById(R.id.bt_unlock);

        }
    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */


        final Checkout co = new Checkout();
        co.setKeyID("<rzp_live_lLGiG4KocIDLGX>");


        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "Merchant Name");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg");
            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", "2");//pass amount in currency subunits
            options.put("prefill.email", "gaurav.kumar@example.com");
            options.put("prefill.contact", "9988776655");
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            co.open((Activity) context, options);

        } catch (Exception e) {
            Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


}
