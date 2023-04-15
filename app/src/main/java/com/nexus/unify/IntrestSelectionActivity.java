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
        intrest1.setName("Bike riding");
        intrestsArrayList.add(intrest1);

        Intrests intrest2 = new Intrests();
        intrest2.setName("Tennis");
        intrestsArrayList.add(intrest2);

        Intrests intrest3 = new Intrests();
        intrest3.setName("Swimming");
        intrestsArrayList.add(intrest3);

        Intrests intrest4 = new Intrests();
        intrest4.setName("Martial arts");
        intrestsArrayList.add(intrest4);

        Intrests intrest5 = new Intrests();
        intrest5.setName("Bodybuilding");
        intrestsArrayList.add(intrest5);

        Intrests intrest6 = new Intrests();
        intrest6.setName("Hiking");
        intrestsArrayList.add(intrest6);

        Intrests intrest7 = new Intrests();
        intrest7.setName("Basketball");
        intrestsArrayList.add(intrest7);

        Intrests intrest8 = new Intrests();
        intrest8.setName("Cricket");
        intrestsArrayList.add(intrest8);

        Intrests intrest9 = new Intrests();
        intrest9.setName("Soccer");
        intrestsArrayList.add(intrest9);

        Intrests intrest10 = new Intrests();
        intrest10.setName("Community service");
        intrestsArrayList.add(intrest10);

        Intrests intrest11 = new Intrests();
        intrest11.setName("Yoga");
        intrestsArrayList.add(intrest11);

        Intrests intrest12 = new Intrests();
        intrest12.setName("Reading Books");
        intrestsArrayList.add(intrest12);

        Intrests intrest13 = new Intrests();
        intrest13.setName("Writing Stories");
        intrestsArrayList.add(intrest13);

        Intrests intrest14 = new Intrests();
        intrest14.setName("Android Development");
        intrestsArrayList.add(intrest14);

        Intrests intrest15 = new Intrests();
        intrest15.setName("Web Development");
        intrestsArrayList.add(intrest15);

        Intrests intrest16 = new Intrests();
        intrest16.setName("Machine Learning");
        intrestsArrayList.add(intrest16);

        Intrests intrest19 = new Intrests();
        intrest19.setName("Coding");
        intrestsArrayList.add(intrest19);

        Intrests intrest18 = new Intrests();
        intrest18.setName("UI Design");
        intrestsArrayList.add(intrest18);

        Intrests intrest17 = new Intrests();
        intrest17.setName("Dancing");
        intrestsArrayList.add(intrest17);


        Intrests intrest20 = new Intrests();
        intrest20.setName("Fishing");
        intrestsArrayList.add(intrest20);

        Intrests intrest21 = new Intrests();
        intrest21.setName("Fashion");
        intrestsArrayList.add(intrest21);

        Intrests intrest22 = new Intrests();
        intrest22.setName("Politics");
        intrestsArrayList.add(intrest22);

        Intrests intrest23 = new Intrests();
        intrest23.setName("Musician");
        intrestsArrayList.add(intrest23);

        Intrests intrest24 = new Intrests();
        intrest24.setName("Music");
        intrestsArrayList.add(intrest24);

        Intrests intrest25 = new Intrests();
        intrest25.setName("Foodie");
        intrestsArrayList.add(intrest25);

        Intrests intrest26 = new Intrests();
        intrest26.setName("Baking");
        intrestsArrayList.add(intrest26);

        Intrests intrest27 = new Intrests();
        intrest27.setName("Instagram");
        intrestsArrayList.add(intrest27);

        Intrests intrest28 = new Intrests();
        intrest28.setName("Artist");
        intrestsArrayList.add(intrest28);

        Intrests intrest29 = new Intrests();
        intrest29.setName("Gamer");
        intrestsArrayList.add(intrest29);

        Intrests intrest30 = new Intrests();
        intrest30.setName("Athlete");
        intrestsArrayList.add(intrest30);

        Intrests intrest31 = new Intrests();
        intrest31.setName("Vlogging");
        intrestsArrayList.add(intrest31);

        Intrests intrest32 = new Intrests();
        intrest32.setName("Netflix");
        intrestsArrayList.add(intrest32);

        Intrests intrest33 = new Intrests();
        intrest33.setName("Travel");
        intrestsArrayList.add(intrest33);

        Intrests intrest34 = new Intrests();
        intrest34.setName("Cycling");
        intrestsArrayList.add(intrest34);

        Intrests intrest35 = new Intrests();
        intrest35.setName("Astronomy");
        intrestsArrayList.add(intrest35);

        Intrests intrest36 = new Intrests();
        intrest36.setName("Cooking");
        intrestsArrayList.add(intrest36);

        Intrests intrest37 = new Intrests();
        intrest37.setName("Photography");
        intrestsArrayList.add(intrest37);

        Intrests intrest38 = new Intrests();
        intrest38.setName("Activism");
        intrestsArrayList.add(intrest38);

        Intrests intrest39 = new Intrests();
        intrest39.setName("Movies");
        intrestsArrayList.add(intrest39);

        Intrests intrest40= new Intrests();
        intrest40.setName("GDSC");
        intrestsArrayList.add(intrest40);

        Intrests intrest41 = new Intrests();
        intrest41.setName("IEDC");
        intrestsArrayList.add(intrest41);

        Intrests intrest42 = new Intrests();
        intrest42.setName("IEEE");
        intrestsArrayList.add(intrest42);

        Intrests intrest43 = new Intrests();
        intrest43.setName("CSI");
        intrestsArrayList.add(intrest43);

        Intrests intrest44 = new Intrests();
        intrest44.setName("Tinker Hub");
        intrestsArrayList.add(intrest44);

        Intrests intrest45 = new Intrests();
        intrest45.setName("ISTE");
        intrestsArrayList.add(intrest45);

        Intrests intrest46 = new Intrests();
        intrest46.setName("Coding Club");
        intrestsArrayList.add(intrest46);

        Intrests intrest47 = new Intrests();
        intrest47.setName("Photography Club");
        intrestsArrayList.add(intrest47);

        Intrests intrest48 = new Intrests();
        intrest48.setName("Lattice");
        intrestsArrayList.add(intrest48);

        Intrests intrest49 = new Intrests();
        intrest49.setName("Film&Drama Club");
        intrestsArrayList.add(intrest49);


        adapter.setIntrests(intrestsArrayList);

    }

    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}