package com.nexus.unify;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nexus.unify.ModelClasses.User;


import java.util.ArrayList;
import java.util.Random;

public class BasicDetailsActivity extends AppCompatActivity {

    AutoCompleteTextView text_districts;
    ArrayList<String> arraylist_types, arraylist_btech, arraylist_mtech,arrayuser_types;
    ArrayAdapter<String> arraylist_adapter, arraylist_adapter_btech, arraylist_adapter_mtech,arrayuser_adapter;
    String district, input_dist;
    LinearLayout ll_neaxt;
    EditText eT_name, eT_username;
    boolean user = false;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    long count;
    lottiedialogfragment lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_details);
        text_districts = findViewById(R.id.crs);

        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        eT_name = findViewById(R.id.editTextName);
        eT_username = findViewById(R.id.editTextUName);
        ll_neaxt = findViewById(R.id.ll_next);
        arraylist_types = new ArrayList<>();
        arrayuser_types=new ArrayList<>();
        arraylist_types.add("Home Bakers");
        arraylist_types.add("Handicrafts");
        arraylist_types.add("Ayurvedic products");
        arraylist_types.add("Home Made");
        arraylist_types.add("Others");


        arraylist_adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item_layout, arraylist_types);
        text_districts.setAdapter(arraylist_adapter);
        text_districts.setThreshold(1);

        arrayuser_adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item_layout, arrayuser_types);


        input_dist = text_districts.getText().toString().trim();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("profiles").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (dataSnapshot.hasChild("name") && dataSnapshot.hasChild("usernamename")) {
                    eT_name.setText(user.getName());
                    eT_username.setText(user.getUsername());
                    count = dataSnapshot.getChildrenCount();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        text_districts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos,
                                    long id) {
                if (pos == 0) {
                    arraylist_btech = new ArrayList<>();



                    arraylist_adapter_btech = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item_layout, arraylist_btech);

                } else if (pos == 1) {
                    arraylist_btech = new ArrayList<>();


                    arraylist_adapter_btech = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item_layout, arraylist_btech);


                } else if (pos == 2) {
                    arraylist_btech = new ArrayList<>();



                    arraylist_adapter_btech = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item_layout, arraylist_btech);

                } else if (pos == 3) {
                    arraylist_btech = new ArrayList<>();



                    arraylist_adapter_btech = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item_layout, arraylist_btech);


                } else if (pos == 4) {
                    arraylist_btech = new ArrayList<>();



                    arraylist_adapter_btech = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item_layout, arraylist_btech);

                }

            }
        });


        ll_neaxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (eT_name.getText().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter Your name", Toast.LENGTH_SHORT).show();
                }
                if (eT_name.getText().length() < 4) {
                    Toast.makeText(getApplicationContext(), "Please Enter atleast 4 characters name", Toast.LENGTH_SHORT).show();
                }
                if (text_districts.getText().toString().equals("")) {

                }

                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference userNameRef = rootRef.child("profiles");
                String name = eT_name.getText().toString().toLowerCase();
                Query queries = userNameRef.orderByChild("username").equalTo(name);
                ValueEventListener eventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            user = false;


                        } else {
                            user = true;
                            Toast.makeText(BasicDetailsActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(BasicDetailsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                };
                queries.addListenerForSingleValueEvent(eventListener);

                if (!eT_name.getText().equals("") && eT_name.getText().length() >= 4 && !text_districts.getText().toString().equals("") && user == false) {

                    lottie = new lottiedialogfragment(BasicDetailsActivity.this);
                    lottie.show();


                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);

                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    Random rand = new Random();
                    String id = String.format("%04d", rand.nextInt(10000));
                    myEdit.putString("name", eT_name.getText().toString());
                    String name1 = eT_name.getText().toString().toLowerCase();
                    myEdit.putString("username", name1.replace(" ", ""));
                    myEdit.putString("course", text_districts.getText().toString().trim());
                    myEdit.putString("anmsname", "unifyuser" + id);
                    myEdit.putString("bio", "Book Ease User");
                    myEdit.commit();


                    lottie.dismiss();
                    startActivity(new Intent(BasicDetailsActivity.this, AddPhotosActivity.class));
                    finish();


                }
            }
        });


    }
}