package com.nexus.unify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nexus.unify.ModelClasses.BugReport;

public class BugActivity extends AppCompatActivity {

  EditText name, contact, desc;
    private Button buttonReportBug;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bug);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        name = (EditText) findViewById(R.id.name);
        contact = (EditText) findViewById(R.id.contact);
        desc = (EditText) findViewById(R.id.desc);
        buttonReportBug = (Button) findViewById(R.id.btn_submit);

        buttonReportBug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportBug();
            }
        });
    }

    private void reportBug() {
        String name1 = name.getText().toString();
        String contact1 = contact.getText().toString();
        String desc1= desc.getText().toString();

        if (!TextUtils.isEmpty(desc1)) {
            String id = mDatabase.push().getKey();
            BugReport bugReport = new BugReport(id,name1,contact1, desc1);

            mDatabase.child("bug_reports").child(id).setValue(bugReport);

            Toast.makeText(this, "Bug reported successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Please enter a bug description.", Toast.LENGTH_SHORT).show();
        }
    }
}
