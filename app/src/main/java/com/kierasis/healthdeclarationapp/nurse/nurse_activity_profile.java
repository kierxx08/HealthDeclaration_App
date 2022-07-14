package com.kierasis.healthdeclarationapp.nurse;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.kierasis.healthdeclarationapp.R;

public class nurse_activity_profile extends AppCompatActivity {
    TextView p_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nurse_activity_profile);

        p_tv = findViewById(R.id.p_tv);
        String balik = calculateSc(80);
        p_tv.setText(balik);
    }

    public static String calculateSc(int point) {
        if (point <= 100 && point >= 80) {
            return "You got A+";
        } else if (point <= 60 && point >= 70) {
            return  "You got A";
        } else {
            return "You got F (Failed)";
        }
    }

}