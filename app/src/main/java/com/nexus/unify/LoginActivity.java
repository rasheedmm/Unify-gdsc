package com.nexus.unify;


import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;


public class LoginActivity extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 11;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    TextView privacy, tv_admin;
    public CheckBox checkbox1;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    FirebaseRemoteConfigSettings configSettings;
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        privacy = findViewById(R.id.textView16);
        tv_admin = findViewById(R.id.textView4);
        checkbox1 = findViewById(R.id.checkbox1);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            String updated = String.valueOf(task.getResult());

                            value = mFirebaseRemoteConfig.getString("swipe_limit");
                        } else {

                        }
                        //
                    }
                });

       tv_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,AdminActivity.class));
            }
        });

        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   startActivity(new Intent(LoginActivity.this, TermsActivity.class));
            }
        });

        if (mAuth.getCurrentUser() != null) {
            goToNextActivity();
        }


        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkbox1.isChecked()) {


                    if (value.equals("yes")) {

                        mAuth.signInAnonymously()
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "signInAnonymously:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            adddata();
                                        } else {

                                        }
                                    }
                                });


                    } else {


                        Intent intent = mGoogleSignInClient.getSignInIntent();
                        startActivityForResult(intent, RC_SIGN_IN);

                    }


                } else {

                    Toast.makeText(LoginActivity.this, "Accept terms of use and UGC conditions to login", Toast.LENGTH_SHORT).show();

                }


            }


        });
    }

    void goToNextActivity() {
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {


            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);


            GoogleSignInAccount acct = null;
            try {
                acct = task.getResult(ApiException.class);
            } catch (ApiException e) {
                e.printStackTrace();
            }
            String email = acct.getEmail();
            String[] split = email.split("@");
            String domain = split[1]; //This Will Give You The Domain After '@' changeeeee
            if (!domain.equals("null")) {
                authWithGoogle(acct.getIdToken());
            } else {

                Toast.makeText(this, "Please use tkm mail id", Toast.LENGTH_SHORT).show();
                mGoogleSignInClient.signOut();
            }
        } else {
            // Signed out, show unauthenticated UI.

        }


    }


    void authWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            adddata();
                        }


                    }
                });
    }

    private void adddata() {
        //  FirebaseUser user = mAuth.getCurrentUser();
        //  DatabaseReference reference = FirebaseDatabase.getInstance().getReference("profiles").child(user.getUid());
        //   HashMap<String, Object> hashMap = new HashMap<>();
        //  hashMap.put("uid", user.getUid());


        // reference.updateChildren(hashMap);


        startActivity(new Intent(LoginActivity.this, BasicDetailsActivity.class));
        finishAffinity();


    }
}