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
import com.google.android.material.textfield.TextInputLayout;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kierasis.healthdeclarationapp.profile.profile;

public class change_password extends AppCompatActivity {
    public SharedPreferences user_info;
    public Drawable profile_dw, cover_dw;
    MaterialEditText current, newpass,confpass;
    TextInputLayout current_eye, newpass_eye,confpass_eye;
    RelativeLayout main_layout;
    Button cp_button;
    TextView fullname, username;
    ImageView cover;
    CircleImageView icon_profile;

    public ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        user_info = getSharedPreferences("user-info", Context.MODE_PRIVATE);
        final String special_num = user_info.getString("user_special_num", "");
        final String account_id = user_info.getString("user_account_id", "");
        final String uname = user_info.getString("user_name", "");
        final String fname = user_info.getString("user_fname", "");
        final String lname = user_info.getString("user_lname", "");
        final String sex = user_info.getString("user_sex", "");

        cover = findViewById(R.id.cp_cover);
        icon_profile = findViewById(R.id.cp_image);
        current_eye = findViewById(R.id.cp_old_pass_eye);
        current = findViewById(R.id.cp_old_pass);
        newpass_eye = findViewById(R.id.cp_new_pass_eye);
        newpass = findViewById(R.id.cp_new_pass);
        confpass_eye = findViewById(R.id.cp_conf_pass_eye);
        confpass = findViewById(R.id.cp_conf_pass);
        main_layout = findViewById(R.id.cp_main_layout);
        cp_button = findViewById(R.id.cp_button);
        fullname = findViewById(R.id.cp_fullname);
        username = findViewById(R.id.cptv_username);

        fullname.setText(fname + " "+ lname);
        username.setText("@"+uname);

        //Admin
        if (special_num.equals("101720")) {
            progressDialog = new ProgressDialog(change_password.this, R.style.MyAlertDialogStyle);
            fullname.setTextColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));
            username.setTextColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));
            main_layout.setBackgroundResource(R.color.admin_bg);

            current.setTextColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));
            current.setTextCursorDrawable(R.color.admin_tc);
            current.setBaseColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));
            current.setFloatingLabelTextColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));
            current.setHelperTextColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));
            current.setPrimaryColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));
            current.setMetTextColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));
            current.setMetHintTextColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));
            current.setUnderlineColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));

            newpass.setTextColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));
            newpass.setTextCursorDrawable(R.color.admin_tc);
            newpass.setBaseColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));
            newpass.setFloatingLabelTextColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));
            newpass.setHelperTextColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));
            newpass.setPrimaryColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));
            newpass.setMetTextColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));
            newpass.setMetHintTextColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));
            newpass.setUnderlineColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));

            confpass.setTextColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));
            confpass.setTextCursorDrawable(R.color.admin_tc);
            confpass.setBaseColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));
            confpass.setFloatingLabelTextColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));
            confpass.setHelperTextColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));
            confpass.setPrimaryColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));
            confpass.setMetTextColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));
            confpass.setMetHintTextColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));
            confpass.setUnderlineColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));

            cp_button.setBackgroundResource(R.drawable.shape_radius_002);
            cp_button.setTextColor(ContextCompat.getColor(change_password.this, R.color.admin_tc));
        }else{
            progressDialog = new ProgressDialog(change_password.this, R.style.default_dialog);
        }

        cp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtCurrent = current.getText().toString();
                String txtNew = newpass.getText().toString();
                String txtConf = confpass.getText().toString();
                if(txtCurrent.isEmpty() || txtCurrent.trim().isEmpty() || txtNew.isEmpty() || txtNew.trim().isEmpty() || txtConf.isEmpty() || txtConf.trim().isEmpty()){
                    Toast.makeText(change_password.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                }else if(txtCurrent.equals(txtNew)){
                    Toast.makeText(change_password.this, "Use Different New Password", Toast.LENGTH_SHORT).show();
                }else if(txtCurrent.contains(" ") || txtNew.contains(" ") || txtConf.contains(" ")){
                    Toast.makeText(change_password.this, "Space is not allowed.", Toast.LENGTH_SHORT).show();
                }else if(!txtNew.equals(txtConf)){
                    Toast.makeText(change_password.this, "You must enter same password twice.", Toast.LENGTH_SHORT).show();
                }else{
                    update_password(txtCurrent,txtNew);
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

    private void update_password(final String txtCurrent, final String txtNew) {

        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Changing Password");
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String uRl = "https://hda-server.000webhostapp.com/app/change_password.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                if(response.contains("Success")){
                    Toast.makeText(change_password.this, "Password Update", Toast.LENGTH_SHORT).show();
                    profile.finish();
                    startActivity(new Intent(change_password.this, profile.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                }else{
                    Toast.makeText(change_password.this, response, Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(change_password.this, "No Internet", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String > param = new  HashMap<>();
                param.put("old_password", txtCurrent);
                param.put("new_password", txtNew);
                param.put("account_id", user_info.getString("user_account_id","") );
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        my_singleton.getInstance(change_password.this).addToRequestQueue(request);
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
        my_singleton.getInstance(change_password.this).addToRequestQueue(request);
    }
}