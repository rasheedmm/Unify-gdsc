package com.nexus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nexus.unify.R;

public class PayoutActivity extends AppCompatActivity {
       TextView tv_earned, tv_balance, tv_pending;
       Button   btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout);

        tv_earned=findViewById(R.id.tv_earned);
        tv_balance=findViewById(R.id.tv_balance);
        tv_pending=findViewById(R.id.tv_pending);

        btn_submit=findViewById(R.id.button2);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(String.valueOf(tv_balance.getText()))<500){

                    Toast.makeText(PayoutActivity.this, "Minimum Withdraw amount is 500", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}