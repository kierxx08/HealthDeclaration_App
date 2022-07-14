package com.kierasis.healthdeclarationapp.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.kierasis.healthdeclarationapp.R;
import com.kierasis.healthdeclarationapp.login;
import com.kierasis.healthdeclarationapp.splash;

import cdflynn.android.library.checkview.CheckView;

public class admin_activity_add_doctor_success extends AppCompatActivity {
    CheckView mCheckView;
    Button home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_add_doctor_success);
        mCheckView = findViewById(R.id.check);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCheckView.check();
            }
        }, 1000);

        Toolbar toolbar = (Toolbar) findViewById(R.id.aad_doc_suc_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        admin_activity_add_doctor_success.this.setTitle("");
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.admin_tc), PorterDuff.Mode.SRC_ATOP);

        home = findViewById(R.id.aad_doc_suc_home);
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
}