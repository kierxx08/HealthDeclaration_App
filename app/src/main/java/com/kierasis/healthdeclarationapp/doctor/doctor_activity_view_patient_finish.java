package com.kierasis.healthdeclarationapp.doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.kierasis.healthdeclarationapp.R;

import static com.kierasis.healthdeclarationapp.doctor.doctor_activity_main.doctor_main;
import static com.kierasis.healthdeclarationapp.doctor.doctor_activity_view_patient_prescribe.patient_prescribe;
import static com.kierasis.healthdeclarationapp.doctor.doctor_activity_view_patient_result.patient_list;

public class doctor_activity_view_patient_finish extends AppCompatActivity {
    ImageView home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_activity_view_patient_finish);


        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.davpf_toolbar);
        setSupportActionBar(toolbar);
        doctor_activity_view_patient_finish.this.setTitle("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        home = findViewById(R.id.davpm_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        patient_list.finish();
        patient_prescribe.finish();
        super.onBackPressed();
    }
}