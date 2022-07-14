package com.kierasis.healthdeclarationapp.doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.kierasis.healthdeclarationapp.R;
import com.kierasis.healthdeclarationapp.nurse.nurse_activity_add_family;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class doctor_activity_view_previous extends AppCompatActivity implements doctor_adapter_view_patient_result.OnNoteListener{
    public SharedPreferences user_info;
    public String txt_srch;
    List<doctor_ext_view_patient_result> cases;
    doctor_adapter_view_patient_result adapter;

    RecyclerView recyclerView;
    ImageView no_net, no_data;
    RelativeLayout shamer;

    EditText et_srch;
    Button  btn_srch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_activity_view_previous);

        user_info = getSharedPreferences("user-info", Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.davprev_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        doctor_activity_view_previous.this.setTitle("Choose Patient");

        recyclerView = findViewById(R.id.davprev_list);
        cases = new ArrayList<>();
        extractSongs("");

        no_net = findViewById(R.id.davprev_no_net);
        no_data = findViewById(R.id.davprev_no_data);
        shamer = findViewById(R.id.davprev_shame);
        et_srch = findViewById(R.id.davprev_etsearch);
        btn_srch = findViewById(R.id.davprev_btnsearch);
        txt_srch = "";
        btn_srch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_srch = et_srch.getText().toString();
                if(TextUtils.isEmpty(txt_srch)){
                    Toast.makeText(doctor_activity_view_previous.this,"Type to Search", Toast.LENGTH_SHORT).show();
                }else{
                    cases.clear();
                    no_net.setVisibility(View.GONE);
                    no_data.setVisibility(View.GONE);
                    shamer.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    extractSongs(txt_srch);
                }
            }
        });
    }

    private void extractSongs(String search) {

        String JSON_URL = "https://hda-server.000webhostapp.com/app/doctor_view_previous.php?account_id="+user_info.getString("user_account_id","")+"&search="+search;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(0<response.length()) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject caseObject = response.getJSONObject(i);
                            doctor_ext_view_patient_result case2 = new doctor_ext_view_patient_result();
                            case2.setTitle(caseObject.getString("case_id").toString());
                            case2.setArtist(caseObject.getString("case_brgy").toString());
                            case2.setCoverImage(caseObject.getString("case_image").toString());
                            case2.setSongURL(caseObject.getString("url").toString());
                            cases.add(case2);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        no_net.setVisibility(View.GONE);
                        shamer.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                    }

                    recyclerView.setLayoutManager(new LinearLayoutManager(doctor_activity_view_previous.this));
                    adapter = new doctor_adapter_view_patient_result(doctor_activity_view_previous.this, cases, doctor_activity_view_previous.this);
                    recyclerView.setAdapter(adapter);

                }else {
                    Toast.makeText(doctor_activity_view_previous.this,"No Data Found", Toast.LENGTH_SHORT).show();
                    no_data.setVisibility(View.VISIBLE);
                    shamer.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                no_net.setVisibility(View.VISIBLE);
                shamer.setVisibility(View.GONE);
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }

    @Override
    public void onNoteClick(int position) {
        Log.d("tag","onNoteClick: clicked.");

        Intent intent = new Intent(doctor_activity_view_previous.this, doctor_activity_view_patient_prescribe.class);
        intent.putExtra("data",  String.valueOf(position));
        intent.putExtra("search",  txt_srch);
        intent.putExtra("from",  "davp");
        startActivity(intent);

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}