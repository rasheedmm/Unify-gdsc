package com.nexus.unify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nexus.unify.Common.Extras;
import com.nexus.unify.ModelClasses.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class SharedProfileActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
    String profileid;
    ImageView btn_edit_profile, btn_settings;
    TextView username, name;
    CircleImageView img_profile;
    ImageView img_back;
    LinearLayout btn_edit, share;
    TextView tv_crs, tv_dep, bio;
    private ShimmerFrameLayout mShimmerViewContainer;
    private Uri dynamicLink = null;
    private static final String TAG = "DynamicLinks";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_profile);
        btn_settings = findViewById(R.id.img_settings);
        username = findViewById(R.id.tv_username);

        name = findViewById(R.id.tv_name);
        img_profile = findViewById(R.id.avt_view);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        bio = findViewById(R.id.tv_bio);
        img_back = findViewById(R.id.imageView);
        tv_crs = findViewById(R.id.tv_crs);
        share = findViewById(R.id.share);
        tv_dep = findViewById(R.id.tv_dep);
        btn_edit = findViewById(R.id.logout2);
        mShimmerViewContainer.startShimmer();
        btn_edit_profile = findViewById(R.id.img_edit_profile);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (getIntent().hasExtra("uid")) {
            profileid = getIntent().getStringExtra("uid");
        }

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //createDynamicLink();
            }
        });


        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SharedProfileActivity.this, SettingsActivity.class));
            }
        });

        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SharedProfileActivity.this, EditProfileActivity.class));
            }
        });
        userInfo();
    }


    private void userInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("profiles").child(profileid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (SharedProfileActivity.this == null) {
                    return;
                }
                User user = snapshot.getValue(User.class);
                if (snapshot.hasChild("username")) {


                    username.setText(user.getUsername());
                    name.setText(user.getName());
                    bio.setText(user.getBio());
                    tv_crs.setText(user.getCourse());
                    tv_dep.setText(user.getDep());
                    mShimmerViewContainer.stopShimmer();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    //  tv_intrests.setText(user.getIntrests());

                } else {
                    startActivity(new Intent(SharedProfileActivity.this, BasicDetailsActivity.class));
                    Toast.makeText(SharedProfileActivity.this, "Please Add basic details", Toast.LENGTH_SHORT).show();
                }
                if (snapshot.hasChild("img1")) {

                    Glide.with(SharedProfileActivity.this).load(user.getImg1())
                            .placeholder(R.drawable.avatar)
                            .into(img_profile);

                    Glide.with(SharedProfileActivity.this).load(user.getImg1())
                            .placeholder(R.drawable.avatar)
                            .into(img_back);

                } else {
                    startActivity(new Intent(SharedProfileActivity.this, AddPhotosActivity.class));
                    Toast.makeText(SharedProfileActivity.this, "Please Add Photo to Continue", Toast.LENGTH_SHORT).show();
                }

                btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SharedProfileActivity.this, ChatActivity.class);
                        intent.putExtra(Extras.USER_KEY, user.getUid());
                        intent.putExtra(Extras.USER_NAME, user.getName());
                        intent.putExtra(Extras.PHOTO_NAME, user.getImg1());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}