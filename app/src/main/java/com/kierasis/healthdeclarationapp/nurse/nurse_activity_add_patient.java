package com.kierasis.healthdeclarationapp.nurse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import com.kierasis.healthdeclarationapp.splash;
import com.leo.simplearcloader.SimpleArcLoader;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.kierasis.healthdeclarationapp.nurse.nurse_activity_add_member_search.nams;
import static com.kierasis.healthdeclarationapp.nurse.nurse_activity_add_member_search_result.namsr;
import static com.kierasis.healthdeclarationapp.nurse.nurse_activity_add_patient_search_result.napsr;

public class nurse_activity_add_patient extends AppCompatActivity {
    public TextView tvmem_id, tv_fullname, tv_age, tv_add, tv_data, tvdoctor_id;
    public SharedPreferences user_info;
    public String doctor_id, mem_id;
    String data, search, data2, from;
    public Spinner doctor;
    MaterialEditText weight, height, temperature, systolic, diastolic;
    EditText illness;
    Button save;
    SimpleArcLoader loader;
    ImageView no_net, no_data;
    ScrollView scroll;
    int get_doctor_stat, get_info_stat;

    SwipeRefreshLayout refresh;

    public ArrayList<String> arrayList_doctor, arrayList_doctor_id;
    public ArrayAdapter<String> arrayAdapter_doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nurse_activity_add_patient);

        Toolbar toolbar = (Toolbar) findViewById(R.id.nap_toolbar);
        setSupportActionBar(toolbar);
        nurse_activity_add_patient.this.setTitle("Add Patient");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        user_info = getSharedPreferences("user-info", Context.MODE_PRIVATE);

        tvmem_id = findViewById(R.id.nap_mem_id);
        tv_fullname = findViewById(R.id.nap_fullname);
        tv_age = findViewById(R.id.nap_age);
        tv_add = findViewById(R.id.nap_add);
        tv_data = findViewById(R.id.nap_data);
        tvdoctor_id = findViewById(R.id.nap_doctor_id);
        loader = findViewById(R.id.nap_loader);
        no_net = findViewById(R.id.nap_no_net);
        no_data =findViewById(R.id.nap_no_data);
        scroll = findViewById(R.id.nap_scroll);
        refresh = findViewById(R.id.refresh_nap);

        weight = findViewById(R.id.nap_weight);
        height = findViewById(R.id.nap_height);
        temperature = findViewById(R.id.nap_temperature);
        systolic = findViewById(R.id.nap_systolic);
        diastolic = findViewById(R.id.nap_diastolic);
        illness = findViewById(R.id.nap_description);
        save = findViewById(R.id.nap_save);

        get_doctor_stat = 0;
        get_info_stat = 0;

        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        if(from.equals("napsr")) {
            data = intent.getStringExtra("data");
            search = intent.getStringExtra("search");
            data2 = intent.getStringExtra("data2");
            get_doctor();
            get_info(data, search, data2);
        }else if(from.equals("nams") || from.equals("nafs")) {
            mem_id = intent.getStringExtra("mem_id");
            get_doctor();
            get_info2(mem_id);
        }

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //startActivity(new Intent(nurse_activity_add_member_search_result.this,nurse_activity_add_member_search_result.class));
                //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                //finish();

                no_net.setVisibility(View.GONE);
                scroll.setVisibility(View.GONE);
                no_data.setVisibility(View.GONE);
                loader.start();
                loader.setVisibility(View.VISIBLE);
                refresh.setRefreshing(false);

                if(from.equals("napsr")) {
                    get_doctor();
                    get_info(data, search, data2);
                }else if(from.equals("nams") || from.equals("nafs")) {
                    get_doctor();
                    get_info2(mem_id);
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtWeight= weight.getText().toString();
                String txtHeight= height.getText().toString();
                String txtTemperature = temperature.getText().toString();
                String txtSystolic= systolic.getText().toString();
                String txtDiastolic= diastolic.getText().toString();
                String txtIllness= illness.getText().toString();
                String txtDoctor = doctor.getSelectedItem().toString();

                if(txtDoctor.equals("Please Choose")){
                    Toast.makeText(nurse_activity_add_patient.this,"Please Choose Doctor", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(txtWeight) ||TextUtils.isEmpty(txtHeight) || TextUtils.isEmpty(txtTemperature) ||TextUtils.isEmpty(txtSystolic) ||TextUtils.isEmpty(txtDiastolic) ||TextUtils.isEmpty(txtIllness)){
                    Toast.makeText(nurse_activity_add_patient.this,"All Fields Required", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        int wght = Integer.parseInt(txtWeight);
                        int hght = Integer.parseInt(txtHeight);
                        int systlc = Integer.parseInt(txtSystolic);
                        int dstlc  = Integer.parseInt(txtDiastolic);
                        double tmprtr = Double.parseDouble(txtTemperature);
                        if(wght <= 0 || hght <= 0 || systlc< 0 || dstlc< 0 ){
                            Toast.makeText(nurse_activity_add_patient.this,"Some Field has Invalid Input", Toast.LENGTH_SHORT).show();
                        }else if(tmprtr <= 22 || 55 <= tmprtr){
                            Toast.makeText(nurse_activity_add_patient.this,"Some Field has Invalid Input", Toast.LENGTH_SHORT).show();
                        }else{
                            save(mem_id,doctor_id,txtWeight,txtHeight,txtTemperature,txtSystolic,txtDiastolic,txtIllness);
                        }



                    } catch(NumberFormatException nfe) {
                        Toast.makeText(nurse_activity_add_patient.this,"Some Field has Invalid Input", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    private void get_info(final String gi_data,final String gi_search,final String gi_data2) {

        String uRl = "https://hda-server.000webhostapp.com/app/nurse_add_patient_search_result_view.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if(response.contains("Success")){
                    String[] arrayString = response.split(";");
                    mem_id = arrayString[1];
                    String mem_fullname = arrayString[2];
                    String mem_age = arrayString[3];
                    String mem_add = arrayString[4];

                    tv_data.setText("Result No.: " + gi_data2);
                    tvmem_id.setText("Member ID: " + mem_id);
                    tv_fullname.setText("Patient Name: "+ mem_fullname);
                    tv_age.setText("Patient Age: " + mem_age);
                    tv_add.setText("Address: " + mem_add);

                    get_info_stat = 1;
                    if(get_doctor_stat==1 && get_info_stat==1){
                        loader.stop();
                        loader.setVisibility(View.GONE);
                        scroll.setVisibility(View.VISIBLE);
                        no_data.setVisibility(View.GONE);
                        no_net.setVisibility(View.GONE);
                        refresh.setRefreshing(false);
                    }

                }else{
                    Toast.makeText(nurse_activity_add_patient.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(nurse_activity_add_patient.this, error.toString(), Toast.LENGTH_SHORT).show();
                loader.stop();
                loader.setVisibility(View.GONE);
                scroll.setVisibility(View.GONE);
                no_data.setVisibility(View.GONE);
                no_net.setVisibility(View.VISIBLE);
                refresh.setRefreshing(false);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String > param = new  HashMap<>();
                param.put("data", gi_data);
                param.put("search", gi_search);
                param.put("data2", gi_data2);
                param.put("nurse_id", user_info.getString("user_account_id","") );
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        my_singleton.getInstance(nurse_activity_add_patient.this).addToRequestQueue(request);

    }

    private void get_info2(final String mem_id) {

        String uRl = "https://hda-server.000webhostapp.com/app/nurse_add_patient_search_result_view.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if(response.contains("Success")){
                    String[] arrayString = response.split(";");
                    String mem_fullname = arrayString[1];
                    String mem_age = arrayString[2];
                    String mem_add = arrayString[3];

                    tv_data.setText("Result No.: " + "none");
                    tvmem_id.setText("Member ID: " + mem_id);
                    tv_fullname.setText("Patient Name: "+ mem_fullname);
                    tv_age.setText("Patient Age: " + mem_age);
                    tv_add.setText("Address: " + mem_add);

                    get_info_stat = 1;
                    if(get_doctor_stat==1 && get_info_stat==1){
                        loader.stop();
                        loader.setVisibility(View.GONE);
                        scroll.setVisibility(View.VISIBLE);
                        no_data.setVisibility(View.GONE);
                        no_net.setVisibility(View.GONE);
                        refresh.setRefreshing(false);
                    }

                }else{
                    Toast.makeText(nurse_activity_add_patient.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(nurse_activity_add_patient.this, error.toString(), Toast.LENGTH_SHORT).show();
                loader.stop();
                loader.setVisibility(View.GONE);
                scroll.setVisibility(View.GONE);
                no_data.setVisibility(View.GONE);
                no_net.setVisibility(View.VISIBLE);
                refresh.setRefreshing(false);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String > param = new  HashMap<>();
                param.put("mem_id", mem_id);
                param.put("nurse_id", user_info.getString("user_account_id","") );
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        my_singleton.getInstance(nurse_activity_add_patient.this).addToRequestQueue(request);

    }


    private void get_doctor() {

        String JSON_URL = "https://hda-server.000webhostapp.com/app/nurse_add_patient_get_doctor.php?nurse_id="+user_info.getString("user_account_id","");
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                if(0<response.length()) {

                    doctor = findViewById(R.id.nap_doctor);
                    arrayList_doctor=new ArrayList<>();
                    arrayList_doctor.add("Please Choose");
                    arrayList_doctor_id = new ArrayList<>();
                    arrayList_doctor_id.add("0");

                    for (int i = 0; i < response.length(); i++) {
                        try {

                            JSONObject caseObject = response.getJSONObject(i);
                            arrayList_doctor.add(caseObject.getString("doctor_name").toString());
                            arrayList_doctor_id.add(caseObject.getString("doctor_id").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    arrayAdapter_doctor=new ArrayAdapter<String>(nurse_activity_add_patient.this,android.R.layout.simple_list_item_1,arrayList_doctor);
                    doctor.setAdapter(arrayAdapter_doctor);


                    doctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            tvdoctor_id.setText("Doctor ID: " + arrayList_doctor_id.get(position));
                            doctor_id = arrayList_doctor_id.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here
                        }

                    });

                    get_doctor_stat = 1;
                    if(get_doctor_stat==1 && get_info_stat==1){
                        loader.stop();
                        loader.setVisibility(View.GONE);
                        scroll.setVisibility(View.VISIBLE);
                        no_data.setVisibility(View.GONE);
                        no_net.setVisibility(View.GONE);
                        refresh.setRefreshing(false);
                    }

                }else {
                    Toast.makeText(nurse_activity_add_patient.this,"No Available Doctor", Toast.LENGTH_SHORT).show();loader.stop();
                    loader.stop();
                    loader.setVisibility(View.GONE);
                    scroll.setVisibility(View.GONE);
                    no_data.setVisibility(View.VISIBLE);
                    no_net.setVisibility(View.GONE);
                    refresh.setRefreshing(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(nurse_activity_add_patient.this,error.toString(), Toast.LENGTH_SHORT).show();
                loader.stop();
                loader.setVisibility(View.GONE);
                scroll.setVisibility(View.GONE);
                no_data.setVisibility(View.GONE);
                no_net.setVisibility(View.VISIBLE);
                refresh.setRefreshing(false);

            }
        });

        queue.add(jsonArrayRequest);

    }



    private void save(final String mem_id,final String doctor_id,final  String txtWeight,final  String txtHeight,final  String txtTemperature,final  String txtSystolic,final  String txtDiastolic,final  String txtIllness) {
        final ProgressDialog progressDialog = new ProgressDialog(nurse_activity_add_patient.this, R.style.default_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Registering Family Member");
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String uRl = "https://hda-server.000webhostapp.com/app/nurse_add_patient.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                if(response.contains("Success")){
                    String[] arrayString = response.split(";");
                    nams.finish();
                    namsr.finish();
                    napsr.finish();
                    Toast.makeText(nurse_activity_add_patient.this,"Put in Queue", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(nurse_activity_add_patient.this, nurse_activity_add_patient_success.class));
                    finish();

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(nurse_activity_add_patient.this,response, Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(nurse_activity_add_patient.this,error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String > param = new  HashMap<>();
                param.put("member_id", mem_id);
                param.put("doctor_id", doctor_id);
                param.put("height", txtHeight);
                param.put("weight", txtWeight);
                param.put("temperature", txtTemperature);
                param.put("systolic", txtSystolic);
                param.put("diastolic", txtDiastolic);
                param.put("illness", txtIllness);
                param.put("nurse_id", user_info.getString("user_account_id","") );
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        my_singleton.getInstance(nurse_activity_add_patient.this).addToRequestQueue(request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}