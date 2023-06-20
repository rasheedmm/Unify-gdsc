package com.nexus.unify;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nexus.unify.AdapterClasses.RecyclerContentsAdapter;
import com.nexus.unify.AdapterClasses.RecyclerSharedAdapter;
import com.nexus.unify.ModelClasses.Posts;
import com.nexus.unify.ModelClasses.User;
import com.razorpay.Checkout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SharedActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Posts> postsList;
    CircleImageView iv_profile;
    TextView tv_name,tv_bio;
String pubid;
    RecyclerSharedAdapter recyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared);


        recyclerView = findViewById(R.id.recyclerView);
        iv_profile = findViewById(R.id.iv_profile);
       tv_name = findViewById(R.id.tv_name);
        tv_bio = findViewById(R.id.tv_bio);
        postsList = new ArrayList<Posts>();
        recyclerAdapter = new RecyclerSharedAdapter(postsList, SharedActivity.this);
        recyclerView.setLayoutManager(new GridLayoutManager(SharedActivity.this,1));

        recyclerView.setAdapter(recyclerAdapter);
        SharedPreferences prefs = getSharedPreferences("MY", MODE_PRIVATE);

        pubid=prefs.getString("pubid", "No name defined");
        addPosts();
        userInfo();

    }
    private void addPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Postsnew");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postsList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Posts posts = postSnapshot.getValue(Posts.class);



                    postsList.add(posts);




                }
                Collections.reverse(postsList);
                recyclerAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void userInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("profiles").child(pubid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (getContext() == null) {
                    return;
                }
                User user = snapshot.getValue(User.class);
                if (snapshot.hasChild("username")) {


                    tv_name.setText(user.getName());
                    tv_bio.setText(user.getBio());
                    Glide.with(getApplicationContext()).load(user.getImg1())
                            .placeholder(R.drawable.avatar)
                            .into(iv_profile);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}