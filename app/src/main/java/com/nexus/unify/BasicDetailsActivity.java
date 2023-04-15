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

    AutoCompleteTextView text_districts, text_btech,text_iam;
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
        text_iam = findViewById(R.id.i_am);
        text_btech = findViewById(R.id.deprts);
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        eT_name = findViewById(R.id.editTextName);
        eT_username = findViewById(R.id.editTextUName);
        ll_neaxt = findViewById(R.id.ll_next);
        arraylist_types = new ArrayList<>();
        arrayuser_types=new ArrayList<>();
        arraylist_types.add("B Tech");
        arraylist_types.add("M Tech");


        arraylist_types.add("B Arch");
        arraylist_types.add("M Arch");
        arraylist_types.add("MCA");
        arraylist_types.add("Others");

        arrayuser_types.add("Student");
        arrayuser_types.add("Club/Community");
        arrayuser_types.add("Alumini");
        arrayuser_types.add("Company");

        arraylist_adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item_layout, arraylist_types);
        text_districts.setAdapter(arraylist_adapter);
        text_districts.setThreshold(1);

        arrayuser_adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item_layout, arrayuser_types);
       text_iam.setAdapter(arrayuser_adapter);
        text_iam .setThreshold(1);

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
                    arraylist_btech.add("Mechanical Engineering");
                    arraylist_btech.add("Civil Engineering");
                    arraylist_btech.add("Computer Science and Engineering");
                    arraylist_btech.add(" Electrical and Electronics Engineering");
                    arraylist_btech.add("Electronics and Communication Engineering");
                    arraylist_btech.add("Electrical and Computer");
                    arraylist_btech.add("Chemical Engineering");
                    arraylist_btech.add("Others");


                    arraylist_adapter_btech = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item_layout, arraylist_btech);
                    text_btech.setAdapter(arraylist_adapter_btech);
                    text_btech.setThreshold(1);
                } else if (pos == 1) {
                    arraylist_btech = new ArrayList<>();
                    arraylist_btech.add("Computer Science and Engineering");
                    arraylist_btech.add("Artificial Intelligence");
                    arraylist_btech.add("Communication Systems");
                    arraylist_btech.add("Industrial Refrigeration & Cryogenics");
                    arraylist_btech.add("Engineering & Construction Management Structural");
                    arraylist_btech.add("Transportation Engineering");
                    arraylist_btech.add("Others");

                    arraylist_adapter_btech = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item_layout, arraylist_btech);
                    text_btech.setAdapter(arraylist_adapter_btech);
                    text_btech.setThreshold(1);

                } else if (pos == 2) {
                    arraylist_btech = new ArrayList<>();
                    arraylist_btech.add("B Arch");


                    arraylist_adapter_btech = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item_layout, arraylist_btech);
                    text_btech.setAdapter(arraylist_adapter_btech);
                    text_btech.setThreshold(1);

                } else if (pos == 3) {
                    arraylist_btech = new ArrayList<>();
                    arraylist_btech.add("M Arch");


                    arraylist_adapter_btech = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item_layout, arraylist_btech);
                    text_btech.setAdapter(arraylist_adapter_btech);
                    text_btech.setThreshold(1);

                } else if (pos == 4) {
                    arraylist_btech = new ArrayList<>();
                    arraylist_btech.add("MCA");


                    arraylist_adapter_btech = new ArrayAdapter<>(getApplicationContext(), R.layout.list_item_layout, arraylist_btech);
                    text_btech.setAdapter(arraylist_adapter_btech);
                    text_btech.setThreshold(1);

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
                    Toast.makeText(getApplicationContext(), "Please select your course ", Toast.LENGTH_SHORT).show();
                }
                if (text_btech.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please select your Department", Toast.LENGTH_SHORT).show();
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

                if (!eT_name.getText().equals("") && eT_name.getText().length() >= 4 && !text_btech.getText().toString().equals("") && !text_districts.getText().toString().equals("") && user == false) {

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
                    myEdit.putString("type", text_iam.getText().toString().trim());
                    myEdit.putString("dep", text_btech.getText().toString().trim());
                    myEdit.putString("anmsname", "unifyuser" + id);
                    myEdit.putString("bio", "Unify User");
                    myEdit.commit();


                    lottie.dismiss();
                    startActivity(new Intent(BasicDetailsActivity.this, AddPhotosActivity.class));
                    finish();


                }
            }
        });


    }
}