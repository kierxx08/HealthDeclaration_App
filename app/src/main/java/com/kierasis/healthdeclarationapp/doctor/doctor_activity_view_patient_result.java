package com.kierasis.healthdeclarationapp.doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.kierasis.healthdeclarationapp.R;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class doctor_activity_view_patient_result extends AppCompatActivity implements doctor_adapter_view_patient_result.OnNoteListener{
    RecyclerView recyclerView;
    List<doctor_ext_view_patient_result> cases;
    doctor_adapter_view_patient_result adapter;

    public SharedPreferences user_info;

    SimpleArcLoader simpleArcLoader3;
    SwipeRefreshLayout refresh;
    ImageView no_net,no_data;

    public static Activity patient_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_activity_view_patient_result);
        patient_list = this;

        user_info = getSharedPreferences("user-info", Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.damvp_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        doctor_activity_view_patient_result.this.setTitle("Choose Patient");

        recyclerView = findViewById(R.id.damvp_list);
        cases = new ArrayList<>();
        extractSongs();

        simpleArcLoader3 = findViewById(R.id.damvp_loader);
        simpleArcLoader3.start();

        refresh = findViewById(R.id.refresh_damvp_list);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //startActivity(new Intent(nurse_activity_add_member_search_result.this,nurse_activity_add_member_search_result.class));
                //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                //finish();

                cases.clear();
                simpleArcLoader3.start();
                no_data.setVisibility(View.GONE);
                no_net.setVisibility(View.GONE);
                extractSongs();

            }
        });

        no_net = findViewById(R.id.damvp_no_net);
        no_data = findViewById(R.id.damvp_no_data);
    }

    private void extractSongs() {

        String JSON_URL = "https://hda-server.000webhostapp.com/app/doctor_view_patient.php?doctor_id="+user_info.getString("user_account_id","");
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

                        simpleArcLoader3.stop();
                        simpleArcLoader3.setVisibility(View.GONE);
                        no_net.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                    }

                    recyclerView.setLayoutManager(new LinearLayoutManager(doctor_activity_view_patient_result.this));
                    adapter = new doctor_adapter_view_patient_result(doctor_activity_view_patient_result.this, cases, doctor_activity_view_patient_result.this);
                    recyclerView.setAdapter(adapter);

                    refresh.setRefreshing(false);
                }else {
                    //Toast.makeText(doctor_activity_view_patient_result.this,"No Data Found", Toast.LENGTH_SHORT).show();
                    simpleArcLoader3.stop();
                    simpleArcLoader3.setVisibility(View.GONE);
                    no_data.setVisibility(View.VISIBLE);
                    refresh.setRefreshing(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                simpleArcLoader3.stop();
                simpleArcLoader3.setVisibility(View.GONE);
                no_net.setVisibility(View.VISIBLE);
                refresh.setRefreshing(false);
                Log.d("tag", "onErrorResponse: " + error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }

    @Override
    public void onNoteClick(int position) {
        Log.d("tag","onNoteClick: clicked.");

        Intent intent = new Intent(doctor_activity_view_patient_result.this, doctor_activity_view_patient_prescribe.class);
        intent.putExtra("data",  String.valueOf(position));
        intent.putExtra("from",  "davpr");
        intent.putExtra("search",  "");
        startActivity(intent);

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}