package com.nexus.unify;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nexus.unify.Fragments.HomeNewFragment;
import com.nexus.unify.Fragments.LikeFragment;
import com.nexus.unify.Fragments.MyProfileFragment;
import com.nexus.unify.ModelClasses.User;


public class HomeActivity extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;
    BottomNavigationView bottomNavigationView;
    int IMAGE_REQUEST_CODE = 45;
    int CAMERA_REQUEST_CODE = 14;
    int EDITED_IMAGE_RESULT_CODE = 200;
    FirebaseDatabase database;
    FirebaseUser firebaseUser;
    AppUpdateManager appUpdateManager;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
  //   getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        appUpdateManager = AppUpdateManagerFactory.create(HomeActivity.this);

// Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            AppUpdateType.IMMEDIATE,
                            // The current activity making the update request.
                            this,
                            // Include a request code to later monitor this update request.
                            100);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("profiles").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (!snapshot.hasChild("username") || !snapshot.hasChild("img1") || !snapshot.hasChild("intrests")) {
                    finish();
                    startActivity(new Intent(HomeActivity.this, BasicDetailsActivity.class));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        bottomNavigationView = findViewById(R.id.bottom_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.body_container, new HomeNewFragment()).commit();
        bottomNavigationView.setSelectedItemId(R.id.nav_search);
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        fragment = new SwipeNewFragment();

                        break;

                    case R.id.nav_search:
                        fragment = new HomeNewFragment();
                        break;

                    case R.id.nav_like:
         fragment = new LikeFragment();
                        break;

                    case R.id.nav_shop:
                   fragment = new MyProfileFragment();
                        break;
                    case R.id.nav_add:

                       startActivity(new Intent(HomeActivity.this, MainChatsActivity.class));
                        break;

                }
                if (fragment == null) {


                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.body_container, fragment).commit();
                }
                return true;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
     /*   if (requestCode == PICK_IMAGE) {
            if(data.getData()!=null) {
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                intent.setData(data.getData());
                startActivity(intent);
            }
        }*/
    }


}