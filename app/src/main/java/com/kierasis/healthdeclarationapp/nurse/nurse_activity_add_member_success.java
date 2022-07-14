package com.kierasis.healthdeclarationapp.nurse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.kierasis.healthdeclarationapp.R;

import static com.kierasis.healthdeclarationapp.nurse.nurse_activity_main.nurse_main;

public class nurse_activity_add_member_success extends AppCompatActivity {
    ImageView image;
    Button add_patient, home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nurse_activity_add_member_success);

        Intent intent = getIntent();
        final String pitf = intent.getStringExtra("pitf");
        final String mem_id = intent.getStringExtra("mem_id");

        add_patient = findViewById(R.id.namsuc_ap);
        add_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(nurse_activity_add_member_success.this, nurse_activity_add_patient.class);
                intent.putExtra("from", "nams");
                intent.putExtra("mem_id", mem_id);
                startActivity(intent);
                finish();
            }
        });

        home = findViewById(R.id.namsuc_bckhome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nurse_main.finish();
                startActivity(new Intent(nurse_activity_add_member_success.this, nurse_activity_main.class));
                finish();
            }
        });

        image = findViewById(R.id.namsuc_img);
        if(pitf.equals("Father")) {
            image.setImageResource(R.drawable.img_father_001);
        }else if(pitf.equals("Mother")) {
            image.setImageResource(R.drawable.img_mother_001);
        }else if(pitf.equals("Son")) {
            image.setImageResource(R.drawable.img_son_001);
        }else if(pitf.equals("Daughter")) {
            image.setImageResource(R.drawable.img_daughter_001);
        }
    }
}