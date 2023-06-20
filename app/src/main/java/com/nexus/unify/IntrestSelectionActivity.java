package com.nexus.unify;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nexus.unify.AdapterClasses.MultiAdapter;
import com.nexus.unify.ModelClasses.Intrests;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IntrestSelectionActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private ArrayList<Intrests> intrestsArrayList = new ArrayList<>();
    private ArrayList<String> selected_intrests = new ArrayList<String>();
    private MultiAdapter adapter;
    Button btn;
    String intrests, tokenid;
    FirebaseUser firebaseUser;
    lottiedialogfragment lottie;
    DatabaseReference reference;
    String name, username, course, anmsname, dep, bio, img1, anmsimg, token, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intrest_selection);

        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        recyclerView = findViewById(R.id.multiple_rv);
        btn = findViewById(R.id.btn_submit);


        SharedPreferences sh = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);


        String name = sh.getString("name", "");
        String username = sh.getString("username", "");
        String course = sh.getString("course", "");
        String anmsname = sh.getString("anmsname", "");
        String dep = sh.getString("dep", "");
        String bio = sh.getString("bio", "");
        String img1 = sh.getString("img1", "");
        String anmsimg = sh.getString("anmsimg", "");


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        email = firebaseUser.getEmail();
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter = new MultiAdapter(this, intrestsArrayList);
        recyclerView.setAdapter(adapter);

        createListOfData();
        FirebaseMessaging.getInstance()
                .getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String token) {
                        //noinspection MismatchedQueryAndUpdateOfCollection
                        tokenid = token;
                    }
                });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Intrests> selectedTvShows = adapter.getSelected();
                if (adapter.getSelected().size() >= 5&&adapter.getSelected().size() <= 10) {
                    lottie = new lottiedialogfragment(IntrestSelectionActivity.this);
                    lottie.show();
                    StringBuilder tvShowName = new StringBuilder();
                    for (int i = 0; i < selectedTvShows.size(); i++) {
                        if (i == 0) {
                            tvShowName.append(selectedTvShows.get(i).getName() + ",");
                        } else {
                            tvShowName.append("\n").append(selectedTvShows.get(i).getName() + ",");
                        }
                    }
                    intrests = tvShowName.toString();
                    reference = FirebaseDatabase.getInstance().getReference("profiles").child(firebaseUser.getUid());
                    HashMap<String, Object> hashMap = new HashMap<>();

                    hashMap.put("intrests", intrests);
                    hashMap.put("name", name);
                    hashMap.put("username", username);
                    hashMap.put("course", course);
                    hashMap.put("anmsname", anmsname);
                    hashMap.put("dep", dep);
                    hashMap.put("token", tokenid);
                    hashMap.put("bio", bio);
                    hashMap.put("img1", img1);
                    hashMap.put("anmsimg", anmsimg);
                    hashMap.put("uid", firebaseUser.getUid());
                    hashMap.put("email", email);




                    reference.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                lottie.dismiss();


                                Toast.makeText(IntrestSelectionActivity.this, "Set up completed", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(intent);
                                finish();


                            }
                        }
                    });


                }else if(adapter.getSelected().size() < 5){
                    Toast.makeText(IntrestSelectionActivity.this, "Please Add atleast 5 Intrests", Toast.LENGTH_SHORT).show();
                }else if(adapter.getSelected().size() > 10){
                    Toast.makeText(IntrestSelectionActivity.this, "Only 10 Intrests are allowed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void createListOfData() {

        intrestsArrayList = new ArrayList<>();


        Intrests intrest1 = new Intrests();
        intrest1.setName("Handicrafts");
        intrestsArrayList.add(intrest1);

        Intrests intrest2 = new Intrests();
        intrest2.setName("Ayurvedic products");
        intrestsArrayList.add(intrest2);

        Intrests intrest3 = new Intrests();
        intrest3.setName("Handmade soaps and cosmetics");
        intrestsArrayList.add(intrest3);

        Intrests intrest4 = new Intrests();
        intrest4.setName("Sweets");
        intrestsArrayList.add(intrest4);

        Intrests intrest5 = new Intrests();
        intrest5.setName("Textiles");
        intrestsArrayList.add(intrest5);

        Intrests intrest6 = new Intrests();
        intrest6.setName("Vegitables");
        intrestsArrayList.add(intrest6);

        Intrests intrest7 = new Intrests();
        intrest7.setName("Coir products");
        intrestsArrayList.add(intrest7);

        adapter.setIntrests(intrestsArrayList);

    }

    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}