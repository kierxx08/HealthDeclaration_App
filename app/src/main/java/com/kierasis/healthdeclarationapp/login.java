package com.kierasis.healthdeclarationapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;
import com.kierasis.healthdeclarationapp.family.*;
import com.kierasis.healthdeclarationapp.nurse.*;
import com.kierasis.healthdeclarationapp.doctor.*;
import com.kierasis.healthdeclarationapp.admin.*;

public class login extends AppCompatActivity {
    private long backPressedTime;
    private Toast backToast;
    Button login_btn;
    MaterialEditText username, password;
    public SharedPreferences user_info, device_info;
    ImageView question;

    TextView forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }



        user_info = getSharedPreferences("user-info", Context.MODE_PRIVATE);
        device_info = getSharedPreferences("device-info", MODE_PRIVATE);

        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        login_btn = findViewById(R.id.login_button);
        question = findViewById(R.id.question);

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(login.this);

                builder.setTitle("Login Credentials");

                builder.setMessage("Your lastname is your default Username (lowercase).\nEx: dela cruz\n" +
                        "Your birthday is your default Password (dd-mm-yyyy).\nEx: 28-01-2000");

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {

                            case DialogInterface.BUTTON_POSITIVE:
                                // User clicked the Yes button
                                dialog.cancel();
                                break;
                        }
                    }
                };

                // Set the alert dialog yes button click listener
                builder.setPositiveButton("Ok", dialogClickListener);

                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                // Display the three buttons alert dialog on interface
                dialog.show();
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtUsername = username.getText().toString();
                String txtPassword = password.getText().toString();


                if (TextUtils.isEmpty(txtUsername) || TextUtils.isEmpty(txtPassword)){
                    Toast.makeText(login.this, "All Fields required", Toast.LENGTH_SHORT).show();
                }else{
                    login(txtUsername,txtPassword);
                }
            }
        });


        String loginStatus = user_info.getString("login_state","");
        String special_num = user_info.getString("user_special_num","");

        if(loginStatus.equals("loggedin")){

            if(special_num.equals("101719")){
                startActivity(new Intent(login.this,family_activity_main.class));
                finish();
            }else if(special_num.equals("101720")){
                startActivity(new Intent(login.this,admin_activity_main.class));
                finish();
            }else if(special_num.equals("101721")){
                startActivity(new Intent(login.this,doctor_activity_main.class));
                finish();
            }else if(special_num.equals("101722")){
                startActivity(new Intent(login.this,nurse_activity_main.class));
                finish();
            }else{
                Toast.makeText(login.this, "Saved Data Error", Toast.LENGTH_SHORT).show();
            }
        }

        forgot = findViewById(R.id.login_forgot);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this,forgot_password.class));

            }
        });

    }

    private void login(final String username, final String password){
        final ProgressDialog progressDialog = new ProgressDialog(login.this, R.style.default_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Logging in");
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String uRl = "https://hda-server.000webhostapp.com/app/login.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.contains("Login Success")){
                    progressDialog.dismiss();
                    String[] arrayString = response.split(";");
                    String special_num = arrayString[1];
                    String id = arrayString[2];
                    String fname = arrayString[3];
                    String lname = arrayString[4];
                    String bday = arrayString[5];
                    String pnumber = arrayString[6];
                    String sex = arrayString[7];
                    String brgy = arrayString[8];
                    String add_add = arrayString[9];
                    String pitf = arrayString[10];
                    String position = arrayString[11];


                    SharedPreferences.Editor user_editor = user_info.edit();
                    user_editor.putString("login_state","loggedin");
                    user_editor.putString("user_special_num",special_num);
                    user_editor.putString("user_account_id",id);
                    user_editor.putString("user_name",username);
                    user_editor.putString("user_fname",fname);
                    user_editor.putString("user_lname",lname);
                    user_editor.putString("user_bday",bday);
                    user_editor.putString("user_pnumber",pnumber);
                    user_editor.putString("user_sex",sex);
                    user_editor.putString("user_brgy",brgy);
                    user_editor.putString("user_add_add",add_add);
                    user_editor.putString("user_pitf",pitf);
                    user_editor.putString("position",position);
                    user_editor.apply();

                    if(special_num.equals("101719")){
                        startActivity(new Intent(login.this,family_activity_main.class));
                        finish();
                        Toast.makeText(login.this, "Login Success", Toast.LENGTH_SHORT).show();
                    }else if(special_num.equals("101720")){
                        startActivity(new Intent(login.this,admin_activity_main.class));
                        finish();
                        Toast.makeText(login.this, "Login Success", Toast.LENGTH_SHORT).show();
                    }else if(special_num.equals("101721")){
                        startActivity(new Intent(login.this,doctor_activity_main.class));
                        finish();
                        Toast.makeText(login.this, "Login Success", Toast.LENGTH_SHORT).show();
                    }else if(special_num.equals("101722")){
                        startActivity(new Intent(login.this,nurse_activity_main.class));
                        finish();
                        Toast.makeText(login.this, "Login Success", Toast.LENGTH_SHORT).show();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(login.this, response, Toast.LENGTH_SHORT).show();
                    }


                }else {
                    progressDialog.dismiss();
                    Toast.makeText(login.this, response, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if((error.toString().contains("html"))||(error.toString().contains("<!DOCTYPE html>"))){
                    Toast.makeText(login.this, "App Expired. Please Update.", Toast.LENGTH_SHORT).show();
                }else if(error.toString().contains("end of stream")){
                    Toast.makeText(login.this, "Runtime Timeout", Toast.LENGTH_SHORT).show();
                }else if (error.toString().contains("Unable to resolve host")) {
                    Toast.makeText(login.this, "Server Error. Please Try Again.", Toast.LENGTH_SHORT).show();
                }else if(error.toString().contains("NoConnectionError")) {
                    //    Toast.makeText(MainActivity.this, "No Connection", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(login.this, error.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String > param = new  HashMap<>();
                param.put("username", username);
                param.put("psw", password);
                param.put("device_key", device_info.getString("device_key",""));
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        my_singleton.getInstance(login.this).addToRequestQueue(request);

    }

    @Override
    public void onBackPressed() {
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