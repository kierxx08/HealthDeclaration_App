package com.kierasis.healthdeclarationapp.nurse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kierasis.healthdeclarationapp.R;

public class nurse_activity_add_member_search extends AppCompatActivity {
    EditText search_bar;
    Button search_btn;

    public static Activity nams;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nurse_activity_add_member_search);
        nams = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.nams_toolbar);
        setSupportActionBar(toolbar);
        nurse_activity_add_member_search.this.setTitle("Search Family");

        Intent get_intent = getIntent();
        final String destination = get_intent.getStringExtra("destination");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        search_bar = findViewById(R.id.nams_search_bar);
        search_btn = findViewById(R.id.nams_search_btn);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(search_bar.getText().toString())){
                    Toast.makeText(nurse_activity_add_member_search.this,"Please Input...", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(nurse_activity_add_member_search.this, nurse_activity_add_member_search_result.class);
                    intent.putExtra("search",search_bar.getText().toString());
                    intent.putExtra("destination", destination);
                    startActivity(intent);
                }
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}