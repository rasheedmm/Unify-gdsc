package com;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nexus.unify.ModelClasses.Posts;
import com.nexus.unify.R;
import com.nexus.unify.VideoPlayerActivity;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;

public class ContentDetailsActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    String pid;
    private AlertDialog.Builder alertDialogBuilder;
    ImageView img_card;
    TextView tv_price, tv_title, tv_desc;
    Button bt_buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_details);

        Checkout.preload(getApplicationContext());


        // ...


        alertDialogBuilder = new AlertDialog.Builder(ContentDetailsActivity.this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Payment Result");
        alertDialogBuilder.setPositiveButton("Ok", (dialog, which) -> {
            //do nothing
        });


        SharedPreferences prefs = getSharedPreferences("MY", MODE_PRIVATE);
        pid = prefs.getString("pid", "No name defined");

        img_card = findViewById(R.id.img_card);
        tv_price = findViewById(R.id.tv_price);
        tv_title = findViewById(R.id.tv_title);
        tv_desc = findViewById(R.id.tv_desc);
        bt_buy = findViewById(R.id.bt_buy);


        addPosts();

        bt_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
            }
        });

    }

    private void addPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Postsnew").child(pid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                Posts posts = snapshot.getValue(Posts.class);


                tv_title.setText(posts.getText());
                tv_price.setText(posts.getPrice());
                tv_desc.setText(posts.getText());

                Glide.with(ContentDetailsActivity.this).load(posts.getUrl())
                        .placeholder(R.drawable.post_place)
                        .into(img_card);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();


        co.setKeyID("rzp_live_lLGiG4KocIDLGX");


        try {
            JSONObject options = new JSONObject();
            options.put("name", "Book Ease TKM");
            options.put("description", "Demoing Charges");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", "10000");

            JSONObject preFill = new JSONObject();
            preFill.put("email", "test@razorpay.com");
            preFill.put("contact", "9876543210");

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();

        }


    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try {
            alertDialogBuilder.setMessage("Payment Successful :\nPayment ID: " + s + "\nPayment Data: " + paymentData.getData());
            alertDialogBuilder.show();

            FirebaseUser user;
            user = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Unblocks").child(pid);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(user.getUid(), "true");


            // reference.updateChildren(hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        try {
            alertDialogBuilder.setMessage("Payment Failed:\nPayment Data: " + paymentData.getData());
            alertDialogBuilder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}