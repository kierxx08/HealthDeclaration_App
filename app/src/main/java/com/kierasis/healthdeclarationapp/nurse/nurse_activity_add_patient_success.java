package com.kierasis.healthdeclarationapp.nurse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kierasis.healthdeclarationapp.R;

import static com.kierasis.healthdeclarationapp.nurse.nurse_activity_main.nurse_main;

public class nurse_activity_add_patient_success extends AppCompatActivity {
    Button home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nurse_activity_add_patient_success);
        home = findViewById(R.id.naps_bckhome);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nurse_main.finish();
                startActivity(new Intent(nurse_activity_add_patient_success.this, nurse_activity_main.class));
                finish();
            }
        });
    }
}