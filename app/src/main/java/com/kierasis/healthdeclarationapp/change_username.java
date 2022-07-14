package com.kierasis.healthdeclarationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kierasis.healthdeclarationapp.admin.admin_activity_add_doctor;
import com.kierasis.healthdeclarationapp.admin.admin_activity_add_doctor_success;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import static com.kierasis.healthdeclarationapp.profile.profile;
public class change_username extends AppCompatActivity {
    public SharedPreferences user_info;
    public Drawable profile_dw, cover_dw;
    MaterialEditText change_et;
    RelativeLayout main_layout;
    Button cu_button;
    TextView fullname, username;
    ImageView cover;
    CircleImageView  icon_profile;

    public ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_username);
        user_info = getSharedPreferences("user-info", Context.MODE_PRIVATE);
        final String special_num = user_info.getString("user_special_num", "");
        final String account_id = user_info.getString("user_account_id", "");
        final String uname = user_info.getString("user_name", "");
        final String fname = user_info.getString("user_fname", "");
        final String lname = user_info.getString("user_lname", "");
        final String sex = user_info.getString("user_sex", "");

        cover = findViewById(R.id.cu_cover);
        icon_profile = findViewById(R.id.cu_image);
        change_et = findViewById(R.id.cu_username);
        cu_button = findViewById(R.id.cu_button);
        main_layout = findViewById(R.id.cu_main_layout);
        fullname = findViewById(R.id.cu_fullname);
        username = findViewById(R.id.cutv_username);

        fullname.setText(fname + " "+ lname);
        username.setText("@"+uname);
        change_et.setText(uname);

        //Admin
        if (special_num.equals("101720")) {
            progressDialog = new ProgressDialog(change_username.this, R.style.MyAlertDialogStyle);
            fullname.setTextColor(ContextCompat.getColor(change_username.this, R.color.admin_tc));
            username.setTextColor(ContextCompat.getColor(change_username.this, R.color.admin_tc));
            main_layout.setBackgroundResource(R.color.admin_bg);
            change_et.setTextColor(ContextCompat.getColor(change_username.this, R.color.admin_tc));
            change_et.setTextCursorDrawable(R.color.admin_tc);
            change_et.setBaseColor(ContextCompat.getColor(change_username.this, R.color.admin_tc));
            change_et.setFloatingLabelTextColor(ContextCompat.getColor(change_username.this, R.color.admin_tc));
            change_et.setHelperTextColor(ContextCompat.getColor(change_username.this, R.color.admin_tc));
            change_et.setPrimaryColor(ContextCompat.getColor(change_username.this, R.color.admin_tc));
            change_et.setMetTextColor(ContextCompat.getColor(change_username.this, R.color.admin_tc));
            change_et.setMetHintTextColor(ContextCompat.getColor(change_username.this, R.color.admin_tc));
            change_et.setUnderlineColor(ContextCompat.getColor(change_username.this, R.color.admin_tc));
            cu_button.setBackgroundResource(R.drawable.shape_radius_002);
            cu_button.setTextColor(ContextCompat.getColor(change_username.this, R.color.admin_tc));
        }else{
            progressDialog = new ProgressDialog(change_username.this, R.style.default_dialog);
        }

        cu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtUsername = change_et.getText().toString();
                if(txtUsername.isEmpty() || txtUsername.trim().isEmpty()){
                    Toast.makeText(change_username.this, "Type New Username", Toast.LENGTH_SHORT).show();
                }else if(txtUsername.contains(" ")){
                    Toast.makeText(change_username.this, "Space is not allowed.", Toast.LENGTH_SHORT).show();
                }else{
                    update_username(txtUsername);
                }
            }
        });



        //Family
        if (special_num.equals("101719")) {
            cover_dw = getDrawable(R.drawable.family_cover);
            profile_dw = getDrawable(R.drawable.profile_family);
            //Admin
        } else if (special_num.equals("101720")) {
            cover_dw = getDrawable(R.drawable.admin_cover);
            if (sex.equals("Female")) {
                profile_dw = getDrawable(R.drawable.profile_admin_female);
            } else {
                profile_dw = getDrawable(R.drawable.profile_admin_male);
            }
            //Doctor
        } else if (special_num.equals("101721")) {
            cover_dw = getDrawable(R.drawable.doctor_cover);
            if (sex.equals("Female")) {
                profile_dw = getDrawable(R.drawable.profile_doctor_female);
            } else {
                profile_dw = getDrawable(R.drawable.profile_doctor_male);
            }
            //Nurse
        } else if (special_num.equals("101722")) {
            cover_dw = getDrawable(R.drawable.nurse_cover);
            if (sex.equals("Female")) {
                profile_dw = getDrawable(R.drawable.profile_nurse_female);
            } else {
                profile_dw = getDrawable(R.drawable.profile_nurse_male);
            }
        }

        cover.setBackground(cover_dw);
        icon_profile.setImageDrawable(profile_dw);
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {

            icon_profile.setImageDrawable(profile_dw);

        } else {
            get_profile(account_id);
        }
    }



    private void get_profile(final String account_id) {

        String uRl = "https://hda-server.000webhostapp.com/app/get_profile.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("Found")) {
                    String[] arrayString = response.split(";");
                    String profile_link = arrayString[1];

                    Picasso.get()
                            .load(profile_link)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .placeholder(profile_dw)
                            .into(icon_profile);

                } else {
                    icon_profile.setImageDrawable(profile_dw);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                icon_profile.setImageDrawable(profile_dw);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("account_id", account_id);
                param.put("from", "user");
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        my_singleton.getInstance(change_username.this).addToRequestQueue(request);
    }

    private void update_username(final String txtUsername) {
        final ProgressDialog progressDialog = new ProgressDialog(change_username.this, R.style.default_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Changing Username");
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String uRl = "https://hda-server.000webhostapp.com/app/change_username.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                if(response.contains("Success")){
                    Toast.makeText(change_username.this, "Username Update", Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor user_editor = user_info.edit();
                    user_editor.putString("user_name",txtUsername);
                    user_editor.apply();

                    profile.finish();
                    startActivity(new Intent(change_username.this, profile.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                }else{
                    Toast.makeText(change_username.this, response, Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(change_username.this, "No Internet", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String > param = new  HashMap<>();
                param.put("new_username", txtUsername);
                param.put("account_id", user_info.getString("user_account_id","") );
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        my_singleton.getInstance(change_username.this).addToRequestQueue(request);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}