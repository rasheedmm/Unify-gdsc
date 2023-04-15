package com.nexus.unify;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gdacciaro.iOSDialog.iOSDialog;
import com.gdacciaro.iOSDialog.iOSDialogBuilder;
import com.gdacciaro.iOSDialog.iOSDialogClickListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {
    LinearLayout log_out, policy, delete, contact, about,bug_report;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        log_out = findViewById(R.id.logout);
        policy = findViewById(R.id.policy);
        delete = findViewById(R.id.lin_delete_acc);
        contact = findViewById(R.id.contact_us);
      bug_report = findViewById(R.id.bug_report);
      back = findViewById(R.id.back);
        about = findViewById(R.id.about1);
        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bug_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, BugActivity.class);

                startActivity(intent);
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);

                startActivity(intent);
            }
        });
        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, TermsActivity.class);

                startActivity(intent);
            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://instagram.com/lattice_tkmce_?igshid=OGQ2MjdiOTE=");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/xxx")));
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                new iOSDialogBuilder(SettingsActivity.this)
                        .setTitle("Delete!")
                        .setSubtitle("Are you sure want to delete your account?")
                        .setBoldPositiveLabel(true)
                        .setCancelable(false)
                        .setPositiveListener(getString(R.string.ok),new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {
                                Toast.makeText(SettingsActivity.this,"Clicked!",Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                sendToLogin();

                            }
                        })
                        .setNegativeListener(getString(R.string.dismiss), new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .build().show();

            }

        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new iOSDialogBuilder(SettingsActivity.this)
                        .setTitle("Logout!")
                        .setSubtitle("Are you sure want log out?")
                        .setBoldPositiveLabel(true)
                        .setCancelable(false)
                        .setPositiveListener(getString(R.string.ok),new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {
                                Toast.makeText(SettingsActivity.this,"Clicked!",Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                GoogleSignInClient mGoogleSignInClient;
                                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                        .requestIdToken(getString(R.string.default_web_client_id))
                                        .requestEmail()
                                        .build();
                                mGoogleSignInClient = GoogleSignIn.getClient(SettingsActivity.this, gso);
                                mGoogleSignInClient.signOut().addOnCompleteListener(SettingsActivity.this,
                                        new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                FirebaseAuth.getInstance().signOut();

                                                Intent setupIntent = new Intent(SettingsActivity.this, Login2Activity.class);
                                                Toast.makeText(getBaseContext(), "Logged Out", Toast.LENGTH_LONG).show(); //if u want to show some text
                                                setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(setupIntent);
                                                finish();
                                            }
                                        });

                            }
                        })
                        .setNegativeListener(getString(R.string.dismiss), new iOSDialogClickListener() {
                            @Override
                            public void onClick(iOSDialog dialog) {
                                dialog.dismiss();
                            }
                        })
                        .build().show();

            }

        });


    }

    private void sendToLogin() { //funtion
        GoogleSignInClient mGoogleSignInClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(SettingsActivity.this, gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(SettingsActivity.this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseAuth.getInstance().signOut();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("profiles").child(firebaseUser.getUid());
                        databaseReference.removeValue();
                        Intent intent = new Intent(SettingsActivity.this, Login2Activity.class);
                        Toast.makeText(getBaseContext(), "Account deleted", Toast.LENGTH_LONG).show(); //if u want to show some text
                        startActivity(intent);
                        finish();
                    }
                });
    }
}