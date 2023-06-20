package com.nexus.unify.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nexus.unify.AdapterClasses.RecyclerContentsAdapter;
import com.nexus.unify.ModelClasses.Posts;
import com.nexus.unify.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ContentsFragment extends Fragment implements PaymentResultListener {

    RecyclerView recyclerView;
    List<Posts> postsList;

    RecyclerContentsAdapter recyclerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_contents, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        postsList = new ArrayList<Posts>();
        recyclerAdapter = new RecyclerContentsAdapter(postsList, getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));

        recyclerView.setAdapter(recyclerAdapter);
        addPosts();








        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */


        return view;
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

    @Override
    public void onPaymentSuccess(String s) {

    }

    @Override
    public void onPaymentError(int i, String s) {

    }
}