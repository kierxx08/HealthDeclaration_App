package com.kierasis.healthdeclarationapp.nurse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kierasis.healthdeclarationapp.R;

public class nurse_activity_add_family_success extends AppCompatActivity {
    Button home, add_member, add_patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nurse_activity_add_family_success);
        home = findViewById(R.id.naf_bckhome);
        add_member = findViewById(R.id.naf_afm);
        add_patient = findViewById(R.id.naf_ap);

        Intent intent = getIntent();
        final String fam_id = intent.getStringExtra("fam_id");
        final String mem_id = intent.getStringExtra("mem_id");

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(nurse_activity_add_family_success.this, nurse_activity_main.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(nurse_activity_add_family_success.this, nurse_activity_add_member.class);
                intent.putExtra("from", "nafs");
                intent.putExtra("data",fam_id);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        add_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(nurse_activity_add_family_success.this, nurse_activity_add_patient.class);
                intent.putExtra("from", "nafs");
                intent.putExtra("mem_id",mem_id);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        return true;
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(nurse_activity_add_family_success.this,nurse_activity_main.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}