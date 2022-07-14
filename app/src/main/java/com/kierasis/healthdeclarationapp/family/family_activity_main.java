package com.kierasis.healthdeclarationapp.family;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.navigation.NavigationView;
import com.kierasis.healthdeclarationapp.R;
import com.kierasis.healthdeclarationapp.about;
import com.kierasis.healthdeclarationapp.login;
import com.kierasis.healthdeclarationapp.my_singleton;
import com.kierasis.healthdeclarationapp.nurse.nurse_activity_add_member_search;
import com.kierasis.healthdeclarationapp.nurse.nurse_activity_main;
import com.kierasis.healthdeclarationapp.profile;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class family_activity_main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private long backPressedTime;
    private Toast backToast;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    CardView fam_mem, fam_med_rec;

    public SharedPreferences user_info, device_info;
    public CircleImageView icon_profile;
    public String account_id;
    public Drawable profile_dw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.family_activity_main);

        user_info = getSharedPreferences("user-info", Context.MODE_PRIVATE);
        device_info = getSharedPreferences("device-info", MODE_PRIVATE);
        account_id = user_info.getString("user_account_id", "");
        final String fname = user_info.getString("user_fname", "");
        final String lname = user_info.getString("user_lname", "");
        final String brgy = user_info.getString("user_brgy", "");
        final String add_add = user_info.getString("user_add_add", "");
        final String sex = user_info.getString("user_sex", "");

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        Window window = family_activity_main.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(Color.TRANSPARENT);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);

        TextView nav_user = (TextView)hView.findViewById(R.id.uname);
        nav_user.setText("Family " + lname);
        TextView nav_address = (TextView)hView.findViewById(R.id.uaddress);
        nav_address.setText(add_add+", "+brgy);

        final LinearLayout header_bg = (LinearLayout) hView.findViewById(R.id.header_bg);

        Glide.with(this).load(getDrawable(R.drawable.family_cover)).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                header_bg.setBackground(resource);
            }
        });

        icon_profile = (CircleImageView)hView.findViewById(R.id.icon_profile);
        profile_dw = getDrawable(R.drawable.profile_family);



        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {

            icon_profile.setImageDrawable(profile_dw);

        }else {
            get_profile(account_id);
        }

        fam_mem = findViewById(R.id.fam_mem);
        fam_mem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(family_activity_main.this, family_activity_member.class);
                startActivity(intent);
            }
        });

        fam_med_rec = findViewById(R.id.fam_med_rec);
        fam_med_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(family_activity_main.this, family_activity_medical_record.class);
                intent.putExtra("from", "famain");
                startActivity(intent);
            }
        });

    }

    private void get_profile(final String account_id) {

        String uRl = "https://hda-server.000webhostapp.com/app/get_profile.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.contains("Found")){
                    String[] arrayString = response.split(";");
                    String profile_link = arrayString[1];
                    /*
                    Glide.with(nurse_activity_main.this)
                            .load(profile_link)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .override(200, 200)
                            .dontAnimate()
                            .placeholder(profile_dw)
                            .into(icon_profile);
                    */
                    Picasso.get()
                            .load(profile_link)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .placeholder(profile_dw)
                            .into(icon_profile);

                }else{
                    icon_profile.setImageDrawable(profile_dw);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                icon_profile.setImageDrawable(profile_dw);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String > param = new  HashMap<>();
                param.put("account_id", account_id );
                param.put("from", "user" );
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        my_singleton.getInstance(family_activity_main.this).addToRequestQueue(request);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {

            if (backPressedTime + 2000 >System.currentTimeMillis()){
                backToast.cancel();
                super.onBackPressed();
                return;
            }else{
                backToast = Toast.makeText(getBaseContext(),"Press back again to exit",Toast.LENGTH_SHORT);
                backToast.show();
            }
            backPressedTime = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.nav_home:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_profile:
                //Toast.makeText(this,"Coming Soon",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(family_activity_main.this, profile.class));
                break;

            case R.id.nav_logout:
                String login_status = user_info.getString("login_state","");
                if(login_status.equals("loggedin")) {
                    logout();
                }
                break;

            case R.id.nav_about:
                startActivity(new Intent(family_activity_main.this, about.class));
                break;

        }

        return false;
    }

    private void logout() {
        SharedPreferences.Editor editor = user_info.edit();
        editor.putString("login_state","loggedout");
        editor.apply();
        startActivity(new Intent(family_activity_main.this, login.class));
        finish();
        Toast.makeText(family_activity_main.this,"Logout Success",Toast.LENGTH_SHORT).show();
    }
}