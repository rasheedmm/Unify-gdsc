package com.nexus.unify.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nexus.unify.ModelClasses.User;
import com.nexus.unify.R;
import com.nexus.unify.lottiedialogfragment;

import org.jetbrains.annotations.NotNull;


public class HomeFragment extends Fragment {



    FirebaseAuth auth;
    FirebaseDatabase database;


    long coins = 0;
    String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
    private final int requestCode = 1;
    User user;
    lottiedialogfragment lottie;

    ImageView profilePicture;
    Button find_btn;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        profilePicture =view. findViewById(R.id.profilePicture);
        find_btn = view.findViewById(R.id.findButton);




        lottie = new lottiedialogfragment(getContext());
        lottie.show();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        FirebaseUser currentUser = auth.getCurrentUser();


        database.getReference().child("profiles")
                .child(currentUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                        if (getContext()==null){

                            return;
                        }
                        lottie.dismiss();
                        user = snapshot.getValue(User.class);



                        Glide.with(getContext())
                                .load(user.getProfileImage())
                                .into(profilePicture);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

        find_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if (isPermissionsGranted()) {

                    Intent intent = new Intent(getContext(), ConnectingActivity.class);
                    intent.putExtra("profile", user.getProfileImage());
                    startActivity(intent);

                } else {
                    askPermissions();
                }*/
            }
        });




        return  view;
    }
    void askPermissions() {
        ActivityCompat.requestPermissions((Activity) getContext(), permissions, requestCode);
    }

    private boolean isPermissionsGranted() {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }

        return true;
    }
}