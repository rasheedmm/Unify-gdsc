package com.nexus.unify;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AdminActivity extends AppCompatActivity {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mAuth = FirebaseAuth.getInstance();
        mEmailEditText = findViewById(R.id.edit_text_email);
        mPasswordEditText = findViewById(R.id.edit_text_password);

        Button loginButton = findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();

                // Check if email and password are equal to specific values
                if (email.equals("latticetkmce12" +
                        "3@gmail.com") && password.equals("lattice@123")) {
                    Toast.makeText(AdminActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                    mAuth.signInAnonymously()
                            .addOnCompleteListener(AdminActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInAnonymously:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        adddata();
                                    } else {

                                    }
                                }
                            });

                } else {
                    Toast.makeText(AdminActivity.this, "Invalid Email or Password!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void adddata() {
        //  FirebaseUser user = mAuth.getCurrentUser();
        //  DatabaseReference reference = FirebaseDatabase.getInstance().getReference("profiles").child(user.getUid());
        //   HashMap<String, Object> hashMap = new HashMap<>();
        //  hashMap.put("uid", user.getUid());


        // reference.updateChildren(hashMap);


        startActivity(new Intent(AdminActivity.this, BasicDetailsActivity.class));
        finishAffinity();


    }
}