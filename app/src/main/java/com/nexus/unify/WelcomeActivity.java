package com.nexus.unify;


import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nexus.unify.Common.Util;

import java.util.Objects;


public class WelcomeActivity extends AppCompatActivity {

    FirebaseAuth auth;
    private static final String ONESIGNAL_APP_ID = "36859cc2-1a89-4ebc-82ed-3338f2862074";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_welcome);
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            // Enable verbose OneSignal logging to debug issues if needed.

            //end for do something first time

            //for detect dynamic link
            FirebaseDynamicLinks.getInstance()
                    .getDynamicLink(getIntent())
                    .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                        @Override
                        public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                            // Get deep link from result (may be null if no link is found)
                            Uri deepLink = null;
                            if (pendingDynamicLinkData != null) {
                                deepLink = pendingDynamicLinkData.getLink();

                                Log.e(TAG, "my referLink = " + deepLink.toString());


                                //we will get this link
                                // https://www.bncodeing.com/cust_id=cust123-prod345"

                                String refferlinkGet = deepLink.toString();
                                try {
                                    refferlinkGet = refferlinkGet.substring(refferlinkGet.lastIndexOf("=") + 1);
                                    Log.e(TAG, "===substring = " + refferlinkGet);//cust123-prod345

                                    String custid = refferlinkGet.substring(0, refferlinkGet.indexOf("-"));
                                    String prodid = refferlinkGet.substring(refferlinkGet.indexOf("-") + 1);

                                    Log.e(TAG, "===cust_id = " + custid + "--------- ProductId ==== " + prodid);

                                    Toast.makeText(WelcomeActivity.this, "cust id = " + custid + "podduct id = " + prodid, Toast.LENGTH_LONG).show();

                                    //shareprefarace for save data

                                } catch (Exception e) {
                                    Log.e(TAG, "" + e.getMessage());
                                }
                            }


                            // Handle the deep link. For example, open the linked
                            // content, or apply promotional credit to the user's
                            // account.
                            // ...

                            // ...
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "getDynamicLink:onFailure", e);
                        }
                    });
            FirebaseMessaging.getInstance ().getToken ()
                    .addOnCompleteListener ( task -> {
                        if (!task.isSuccessful ()) {
                            //Could not get FirebaseMessagingToken
                            return;
                        }
                        if (null != task.getResult ()) {
                            //Got FirebaseMessagingToken
                            String firebaseMessagingToken = Objects.requireNonNull (task.getResult());
                            //Use firebaseMessagingToken further
                            Util.updateDeviceToken(WelcomeActivity.this, firebaseMessagingToken);
                        }
                    } );



            goToNextActivity();
        }

        findViewById(R.id.getStarted).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToNextActivity();
            }
        });
    }

    void goToNextActivity() {
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        finish();
    }


}