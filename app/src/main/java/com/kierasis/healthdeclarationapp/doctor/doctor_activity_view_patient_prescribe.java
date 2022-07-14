package com.kierasis.healthdeclarationapp.doctor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kierasis.healthdeclarationapp.R;
import com.kierasis.healthdeclarationapp.login;
import com.kierasis.healthdeclarationapp.my_singleton;
import com.kierasis.healthdeclarationapp.nurse.nurse_activity_add_member;
import com.kierasis.healthdeclarationapp.splash;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class doctor_activity_view_patient_prescribe extends AppCompatActivity implements doctor_adapter_view_patient_prescribe.OnNoteListener {
    RecyclerView recyclerView;
    List<doctor_ext_view_patient_prescribe> cases;
    doctor_adapter_view_patient_prescribe adapter;
    public SharedPreferences user_info;
    String data, from, search;

    public TextView p_id,p_name,p_age,p_height,p_weight,p_temp,p_bp,p_sick;
    public String checkup_id;
    ImageView no_net, no_data,add,finish;
    SimpleArcLoader loader;
    ScrollView scroll;
    SwipeRefreshLayout refresh;
    RelativeLayout loader2;

    int get_patient_stat, get_info_stat;
    public static Activity patient_prescribe;
    public ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_activity_view_patient_prescribed);
        patient_prescribe = this;

        progressDialog = new ProgressDialog(doctor_activity_view_patient_prescribe.this, R.style.default_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);

        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.dvpp_toolbar);
        setSupportActionBar(toolbar);
        doctor_activity_view_patient_prescribe.this.setTitle("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        user_info = getSharedPreferences("user-info", Context.MODE_PRIVATE);

        p_id = findViewById(R.id.davp_mem_id);
        p_name = findViewById(R.id.davp_fullname);
        p_age = findViewById(R.id.davp_age);
        p_height = findViewById(R.id.davp_height);
        p_weight = findViewById(R.id.davp_weight);
        p_temp = findViewById(R.id.davp_temp);
        p_bp = findViewById(R.id.davp_bp);
        p_sick = findViewById(R.id.davp_sick);

        Intent intent = getIntent();
        data = intent.getStringExtra("data");
        from = intent.getStringExtra("from");
        search = intent.getStringExtra("search");
        get_info(from,data);


        get_patient_stat = 0;
        get_info_stat = 0;

        recyclerView = findViewById(R.id.damvp_prescribe_list);
        cases = new ArrayList<>();

        no_net = findViewById(R.id.davpp_no_net);
        no_data = findViewById(R.id.davpp_no_data);
        loader = findViewById(R.id.davpp_loader);
        loader2 = findViewById(R.id.davpp_med_load);
        scroll = findViewById(R.id.davpp_scroll);
        refresh = findViewById(R.id.davpp_refresh);
        add = findViewById(R.id.davpp_add);
        finish = findViewById(R.id.davpp_finish);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //startActivity(new Intent(nurse_activity_add_member_search_result.this,nurse_activity_add_member_search_result.class));
                //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                //finish();

                cases.clear();
                loader.start();
                loader2.setVisibility(View.VISIBLE);
                no_data.setVisibility(View.GONE);
                no_net.setVisibility(View.GONE);
                get_info(from,data);

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(doctor_activity_view_patient_prescribe.this, doctor_activity_view_patient_medicine.class);
                intent.putExtra("checkup_id",  checkup_id);
                startActivity(intent);
            }
        });

        if(from.equals("davp")){
            add.setVisibility(View.GONE);
            finish.setVisibility(View.GONE);
        }

    }

    private void get_info(final String from, final String data) {
        String uRl = "https://hda-server.000webhostapp.com/app/doctor_view_patient_get_info.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if(response.contains("Success")){
                    String[] arrayString = response.split(";");
                    String mem_id = arrayString[1];
                    String name = arrayString[2];
                    String age = arrayString[3];
                    checkup_id = arrayString[4];
                    String height = arrayString[5];
                    String weight = arrayString[6];
                    String temp = arrayString[7];
                    String bp = arrayString[8];
                    String sick = arrayString[9];

                    p_id.setText("Member ID: "+mem_id);
                    p_name.setText("Patient Name: "+name);
                    p_age.setText("Patient Age: "+age);
                    p_height.setText("Patient Height: "+height);
                    p_weight.setText("Patient Weight: "+weight);
                    p_temp.setText("Patient Temp.: "+temp);
                    p_bp.setText("Patient BP: "+bp);
                    p_sick.setText("Patient Illness: \n"+sick);


                        no_net.setVisibility(View.GONE);
                        no_data.setVisibility(View.GONE);
                        loader.stop();
                        loader.setVisibility(View.GONE);
                        scroll.setVisibility(View.VISIBLE);
                        if(from.equals("davpr")){
                            add.setVisibility(View.VISIBLE);
                            finish.setVisibility(View.VISIBLE);
                        }
                        refresh.setRefreshing(false);

                    extractSongs(checkup_id);
                    finish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(doctor_activity_view_patient_prescribe.this);
                            builder.setTitle("Finish Consultation?");
                            //builder.setMessage("");

                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            // User clicked the Yes button
                                            finish(checkup_id);
                                            break;

                                        case DialogInterface.BUTTON_NEUTRAL:
                                            // Neutral/Cancel button clicked
                                            dialog.cancel();
                                            break;
                                    }
                                }
                            };

                            builder.setPositiveButton("Yes", dialogClickListener);

                            builder.setNeutralButton("No", dialogClickListener);

                            builder.setCancelable(false);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });
                }else{
                    Toast.makeText(doctor_activity_view_patient_prescribe.this, response, Toast.LENGTH_SHORT).show();
                    no_net.setVisibility(View.GONE);
                    no_data.setVisibility(View.VISIBLE);
                    loader.stop();
                    loader.setVisibility(View.GONE);
                    scroll.setVisibility(View.GONE);
                    add.setVisibility(View.GONE);
                    finish.setVisibility(View.GONE);
                    refresh.setRefreshing(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(doctor_activity_view_patient_prescribe.this, error.toString(), Toast.LENGTH_SHORT).show();no_net.setVisibility(View.GONE);
                no_net.setVisibility(View.VISIBLE);
                no_data.setVisibility(View.GONE);
                loader.stop();
                loader.setVisibility(View.GONE);
                scroll.setVisibility(View.GONE);
                add.setVisibility(View.GONE);
                finish.setVisibility(View.GONE);
                refresh.setRefreshing(false);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String > param = new  HashMap<>();
                param.put("data", data);
                param.put("from", from);
                param.put("search", search);
                param.put("account_id", user_info.getString("user_account_id","") );
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        my_singleton.getInstance(doctor_activity_view_patient_prescribe.this).addToRequestQueue(request);
    }

    private void extractSongs(final String checkup_id) {

        String JSON_URL = "https://hda-server.000webhostapp.com/app/doctor_view_patient_get_prescription.php?account_id="+user_info.getString("user_account_id","")+"&checkup_id="+checkup_id;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(0<response.length()) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject caseObject = response.getJSONObject(i);
                            doctor_ext_view_patient_prescribe case2 = new doctor_ext_view_patient_prescribe();
                            case2.setMedicine(caseObject.getString("med_name").toString());
                            case2.setAmount(caseObject.getString("amount").toString());
                            case2.setTime(caseObject.getString("time").toString());
                            case2.setDayNum(caseObject.getString("day_num").toString());
                            case2.setDayText(caseObject.getString("day_text").toString());
                            case2.setImage(caseObject.getString("img").toString());
                            cases.add(case2);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                    recyclerView.setLayoutManager(new LinearLayoutManager(doctor_activity_view_patient_prescribe.this));
                    adapter = new doctor_adapter_view_patient_prescribe(doctor_activity_view_patient_prescribe.this, cases, doctor_activity_view_patient_prescribe.this);
                    recyclerView.setAdapter(adapter);

                    loader2.setVisibility(View.GONE);
                }else {
                    Log.d("tag", "onErrorResponse: " + response.toString());
                    loader2.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
                loader2.setVisibility(View.GONE);
            }
        });

        queue.add(jsonArrayRequest);
    }



    private void finish(final String id) {

        progressDialog.setTitle("Finishing Consultation");
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String uRl = "https://hda-server.000webhostapp.com/app/doctor_view_patient_prescribe_finish.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if(response.contains("Success")){
                    startActivity(new Intent(doctor_activity_view_patient_prescribe.this,doctor_activity_view_patient_finish.class));
                    finish();
                }else{
                    Toast.makeText(doctor_activity_view_patient_prescribe.this, response, Toast.LENGTH_SHORT).show();no_net.setVisibility(View.GONE);
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(doctor_activity_view_patient_prescribe.this, "Error", Toast.LENGTH_SHORT).show();no_net.setVisibility(View.GONE);
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String > param = new  HashMap<>();
                param.put("checkup_id", id);
                param.put("account_id", user_info.getString("user_account_id","") );
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        my_singleton.getInstance(doctor_activity_view_patient_prescribe.this).addToRequestQueue(request);
    }

    @Override
    public void onNoteClick(int position) {
        Log.d("tag","onNoteClick: clicked.");

        //Intent intent = new Intent(doctor_activity_view_patient_prescribe.this, doctor_activity_view_patient_prescribe.class);
        //intent.putExtra("data",  String.valueOf(position));
        //startActivity(intent);


        /*
        AlertDialog.Builder builder = new AlertDialog.Builder(doctor_activity_view_patient_prescribe.this);
        builder.setTitle("Are you sure to Delete");
        builder.setMessage("Position: " + position);
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        delete();
                        break;

                    case DialogInterface.BUTTON_NEUTRAL:
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                        break;
                }
            }
        };

        builder.setPositiveButton("Delete", dialogClickListener);
        builder.setNeutralButton("No", dialogClickListener);

        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();

         */

    }

    private void delete() {
        progressDialog.setTitle("Deleting");
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                extractSongs(checkup_id);
                progressDialog.cancel();
                cases.clear();
                loader.start();
                loader2.setVisibility(View.VISIBLE);

            }
        }, 3000);

    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        cases.clear();
        loader.start();
        loader2.setVisibility(View.VISIBLE);
        no_data.setVisibility(View.GONE);
        no_net.setVisibility(View.GONE);
        get_info(from,data);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}