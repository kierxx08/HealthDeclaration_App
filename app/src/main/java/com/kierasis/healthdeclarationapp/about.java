package com.kierasis.healthdeclarationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class about extends AppCompatActivity {
    public SharedPreferences user_info;
    RelativeLayout about_bg;
    TextView version_lbl, version, develop_lbl,develop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        user_info = getSharedPreferences("user-info", Context.MODE_PRIVATE);
        final String special_num = user_info.getString("user_special_num", "");
        String versionName = BuildConfig.VERSION_NAME;

        about_bg = findViewById(R.id.about);
        version_lbl = findViewById(R.id.about_version_label);
        version = findViewById(R.id.about_version);
        develop_lbl = findViewById(R.id.about_develop_label);
        develop = findViewById(R.id.about_develop);

        version.setText(versionName);

        if (special_num.equals("101720")) {
            about_bg.setBackgroundResource(R.color.admin_bg);
            version_lbl.setTextColor(ContextCompat.getColor(about.this, R.color.admin_tc));
            version.setTextColor(ContextCompat.getColor(about.this, R.color.admin_tc));
            develop_lbl.setTextColor(ContextCompat.getColor(about.this, R.color.admin_tc));
            develop.setTextColor(ContextCompat.getColor(about.this, R.color.admin_tc));
        }
    }
}