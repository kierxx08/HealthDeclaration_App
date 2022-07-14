package com.kierasis.healthdeclarationapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kierasis.healthdeclarationapp.doctor.doctor_activity_main;
import com.kierasis.healthdeclarationapp.doctor.doctor_activity_view_patient_prescribe;
import com.kierasis.healthdeclarationapp.nurse.nurse_activity_add_family;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.kierasis.healthdeclarationapp.profile.profile;
import static java.lang.Integer.parseInt;

public class forgot_password extends AppCompatActivity {
    ArrayAdapter arrayAdapter_type;
    TextView fp_text;
    MaterialEditText fp_pnumber, fp_otp, newpass, confpass;
    Spinner fp_type;
    LinearLayout fp_ll1, fp_ll2, fp_ll3, fp_ll4, fp_ll5;
    Button fp_find, fp_send, fp_verify, fp_update, fp_login;
    public SharedPreferences user_info, forgot_info;
    public ProgressDialog progressDialog;

    String user_account_id, apicode, apipass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        user_info = getSharedPreferences("user-info", Context.MODE_PRIVATE);
        forgot_info = getSharedPreferences("forgot-info", MODE_PRIVATE);
        final String pnumber = user_info.getString("user_pnumber", "");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        progressDialog = new ProgressDialog(forgot_password.this, R.style.default_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);

        fp_text = findViewById(R.id.fp_text);
        fp_ll1 = findViewById(R.id.fp_ll1);
        fp_ll2 = findViewById(R.id.fp_ll2);
        fp_ll3 = findViewById(R.id.fp_ll3);
        fp_ll4 = findViewById(R.id.fp_ll4);
        fp_ll5 = findViewById(R.id.fp_ll5);
        fp_pnumber = findViewById(R.id.fp_pnumber);
        fp_otp = findViewById(R.id.fp_otp);
        fp_find = findViewById(R.id.fp_find);
        fp_send = findViewById(R.id.fp_send);
        fp_verify = findViewById(R.id.fp_verify);
        newpass = findViewById(R.id.fp_new_pass);
        confpass = findViewById(R.id.fp_conf_pass);
        fp_update = findViewById(R.id.fp_update);
        fp_login = findViewById(R.id.fp_login);

        fp_pnumber.setText("0"+pnumber);

        fp_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fp_ll2.setVisibility(View.GONE);
                String txtfp_pnumber = fp_pnumber.getText().toString();
                if(TextUtils.isEmpty(txtfp_pnumber) || txtfp_pnumber.length()!=11 || !txtfp_pnumber.startsWith("09")){
                    Toast.makeText(forgot_password.this,"Invalid Phone Number", Toast.LENGTH_SHORT).show();
                }else{
                    fp_pnumber.onEditorAction(EditorInfo.IME_ACTION_DONE);
                    find_account(txtfp_pnumber);
                }
            }
        });

        fp_otp.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                fp_text.setText("");
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        fp_pnumber.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                fp_find.setEnabled(true);
                fp_find.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                fp_ll2.setVisibility(View.GONE);
                fp_text.setText("");
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });


        fp_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtCode = fp_otp.getText().toString();
                if(TextUtils.isEmpty(txtCode) || txtCode.length()!=6) {
                    Toast.makeText(forgot_password.this,"Invalid Code", Toast.LENGTH_SHORT).show();
                }else {
                    verify_code(txtCode);
                }
            }
        });

        fp_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String txtNew = newpass.getText().toString();
                String txtConf = confpass.getText().toString();

                if(txtNew.isEmpty() || txtNew.trim().isEmpty() || txtConf.isEmpty() || txtConf.trim().isEmpty()){
                    Toast.makeText(forgot_password.this, "All Fields Required", Toast.LENGTH_SHORT).show();
                }else if(txtNew.contains(" ") || txtConf.contains(" ")){
                    Toast.makeText(forgot_password.this, "Space is not allowed.", Toast.LENGTH_SHORT).show();
                }else if(!txtNew.equals(txtConf)){
                    Toast.makeText(forgot_password.this, "You must enter same password twice.", Toast.LENGTH_SHORT).show();
                }else{
                    update_password(txtNew);
                }
            }
        });

        fp_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        fp_type = findViewById(R.id.fp_type);
        arrayAdapter_type=ArrayAdapter.createFromResource(this,R.array.user_type,R.layout.spinner_list_001);
        arrayAdapter_type.setDropDownViewResource(R.layout.spinner_list_002);
        fp_type.setAdapter(arrayAdapter_type);
    }

    private void find_account(final String txtfp_pnumber) {
        progressDialog.setTitle("Finding Account");
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String uRl = "https://hda-server.000webhostapp.com/app/forgot_password.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String[] arrayResponse = response.split(";");

                if(arrayResponse[0].equals("None")){
                    Toast.makeText(forgot_password.this, "No Account Found", Toast.LENGTH_SHORT).show();
                    fp_text.setText("No Account Found");
                }else if(arrayResponse[0].equals("One")){
                    user_account_id = arrayResponse[1];
                    String fullname = arrayResponse[2];
                    apicode = arrayResponse[3];
                    apipass = arrayResponse[4];
                    fp_text.setText(fullname+",\n We will send otp to your number?");
                    fp_ll2.setVisibility(View.VISIBLE);
                    fp_type.setVisibility(View.INVISIBLE);
                    fp_find.setEnabled(false);
                    fp_find.setBackgroundColor(getResources().getColor(R.color.btn_disabled));
                    fp_send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            check_sms_server("none",txtfp_pnumber,user_account_id);
                            fp_text.setText("");

                        }
                    });

                }else if(arrayResponse[0].equals("Multiple")){
                    apicode = arrayResponse[1];
                    apipass = arrayResponse[2];
                    fp_ll2.setVisibility(View.VISIBLE);
                    fp_type.setVisibility(View.VISIBLE);
                    fp_find.setEnabled(false);
                    fp_find.setBackgroundColor(getResources().getColor(R.color.btn_disabled));
                    fp_text.setText("Please choose account Type");
                    fp_send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String txtType = fp_type.getSelectedItem().toString();
                            if(txtType.equals("Please Choose")){
                                Toast.makeText(forgot_password.this,"Please Choose Account Type", Toast.LENGTH_SHORT).show();
                                fp_text.setText("Please Choose Account Type");
                            }else{
                                check_sms_server(txtType,txtfp_pnumber,"none");
                                fp_text.setText("");
                            }
                        }
                    });

                }else if(arrayResponse[0].equals("Admin")){
                    fp_text.setText("If you are admin\nPlease contact the Main Admin");
                }else{
                    Toast.makeText(forgot_password.this, response, Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(forgot_password.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String > param = new  HashMap<>();
                param.put("pnumber", txtfp_pnumber);
                param.put("to", "find");
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        my_singleton.getInstance(forgot_password.this).addToRequestQueue(request);

    }

    private void check_sms_server(final String txtType, final String txtfp_pnumber, final String account_id) {
        progressDialog.setTitle("Sending OTP");
        progressDialog.setMessage("Checking SMS Server...");
        progressDialog.show();
        String pnumber = txtfp_pnumber;

        String url = "https://www.itexmo.com/php_api/apicode_info.php?apicode="+apicode;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String[] split1 = response.split("MessagesLeft");
                        String cut1 = split1[1];
                        String[] split2 = cut1.split("Footer");
                        String cut2 = split2[0];
                        String MessagesLeft = cut2.replaceAll("[^0-9]", "");

                        if(parseInt(MessagesLeft)>0){
                            generate_code(txtType,txtfp_pnumber,account_id);
                            //Toast.makeText(forgot_password.this, MessagesLeft + " Message Left", Toast.LENGTH_SHORT).show();
                        }else{
                            fp_text.setText("Server Reach maximum Forgot Password\nTry Again Tomorrow");
                            Toast.makeText(forgot_password.this, "Server Limit Reach", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(forgot_password.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void generate_code(final String txtType, final String txtfp_pnumber, final String account_id) {
        progressDialog.setMessage("Generating Code from Server...");

        String uRl = "https://hda-server.000webhostapp.com/app/forgot_password.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String[] arrayResponse = response.split(";");
                if(arrayResponse[0].equals("Success")){
                    String code = arrayResponse[1];
                    String username = arrayResponse[2];
                    sending_otp(code,txtfp_pnumber,account_id,username);
                    //Toast.makeText(forgot_password.this, code +" "+username, Toast.LENGTH_SHORT).show();
                }else if(arrayResponse[0].equals("Select Valid User Type")){
                    fp_text.setText("Select Valid User Type");
                    progressDialog.dismiss();
                }else if(arrayResponse[0].equals("Forgot Reached")){
                    fp_text.setText("You Reach maximum unverified\nOTP Code request. Contact the Admin.");
                    progressDialog.dismiss();
                }else{
                    Toast.makeText(forgot_password.this, response, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(forgot_password.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String > param = new  HashMap<>();
                param.put("type", txtType);
                param.put("pnumber", txtfp_pnumber);
                param.put("account_id", account_id);
                param.put("to", "code");
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        my_singleton.getInstance(forgot_password.this).addToRequestQueue(request);
    }


    private void sending_otp(final String Code, final String txtfp_pnumber, final String account_id,final String username) {
        progressDialog.setMessage("Sending OTP...");
        String uRl = "https://www.itexmo.com/php_api/api.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("0")){
                    Toast.makeText(forgot_password.this, "Sending OTP is Success", Toast.LENGTH_SHORT).show();
                    fp_ll1.setVisibility(View.GONE);
                    fp_ll2.setVisibility(View.GONE);
                    fp_ll3.setVisibility(View.VISIBLE);

                    progressDialog.dismiss();
                    user_account_id = account_id;
                    fp_text.setText("Note: Receiving OTP may takes 10 mins.");
                    SharedPreferences.Editor user_editor = forgot_info.edit();
                    user_editor.putString("forgot_pass","verify");
                    user_editor.putString("pnumber",txtfp_pnumber);
                    user_editor.putString("account_id",account_id);
                    user_editor.apply();

                }else if(response.equals("6")){
                    Toast.makeText(forgot_password.this, "SMS Server is OFFLINE", Toast.LENGTH_SHORT).show();
                    fp_text.setText("SMS Server is OFFLINE");
                    progressDialog.dismiss();
                }else{
                    Toast.makeText(forgot_password.this, "Unknown Error Occur", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(forgot_password.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String > param = new  HashMap<>();
                param.put("1", txtfp_pnumber);
                param.put("2", "Hi "+username+", This is HDA App\nYour OTP is: "+Code+"\n\n");
                param.put("3", apicode);
                param.put("passwd", apipass);
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        my_singleton.getInstance(forgot_password.this).addToRequestQueue(request);

    }

    private void verify_code(final String txtCode) {
        progressDialog.setTitle("Verifying OTP");
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String uRl = "https://hda-server.000webhostapp.com/app/forgot_password.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String[] arrayResponse = response.split(";");
                if(arrayResponse[0].equals("Success")){
                    progressDialog.dismiss();
                    fp_ll3.setVisibility(View.GONE);
                    fp_ll4.setVisibility(View.VISIBLE);
                }else if(arrayResponse[0].equals("Wrong OTP")){
                    fp_text.setText("Wrong OTP\nCheck the code you Received");
                    progressDialog.dismiss();
                }else{
                    Toast.makeText(forgot_password.this, response, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(forgot_password.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String > param = new  HashMap<>();
                param.put("code", txtCode);
                param.put("account_id", user_account_id);
                param.put("to", "verify");
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        my_singleton.getInstance(forgot_password.this).addToRequestQueue(request);
    }



    private void update_password(final String txtNew) {

        progressDialog.setTitle("Changing Password");
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String uRl = "https://hda-server.000webhostapp.com/app/forgot_password.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                if(response.contains("Success")){
                    fp_text.setText("Password Update Successfully\nPlease Login Using new Password");
                    Toast.makeText(forgot_password.this, "Password Update", Toast.LENGTH_SHORT).show();
                    fp_ll4.setVisibility(View.GONE);
                    fp_ll5.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(forgot_password.this, response, Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(forgot_password.this, "No Internet", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String > param = new  HashMap<>();
                param.put("new_password", txtNew);
                param.put("account_id", user_account_id );
                param.put("to", "update");
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        my_singleton.getInstance(forgot_password.this).addToRequestQueue(request);
    }
}