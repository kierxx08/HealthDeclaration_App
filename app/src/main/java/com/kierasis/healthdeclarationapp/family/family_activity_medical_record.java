package com.kierasis.healthdeclarationapp.family;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.kierasis.healthdeclarationapp.nurse.nurse_adapter_add_patient_result;
import com.kierasis.healthdeclarationapp.nurse.nurse_ext_add_patient_result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class family_activity_medical_record extends AppCompatActivity implements nurse_adapter_add_patient_result.OnNoteListener{
    public SharedPreferences user_info;
    RecyclerView recyclerView;

    List<nurse_ext_add_patient_result> cases;
    nurse_adapter_add_patient_result adapter;
    RelativeLayout shame;

    String data, from, search;

    SwipeRefreshLayout refresh;
    ImageView no_net,no_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.family_activity_medical_record);

        Toolbar toolbar = (Toolbar) findViewById(R.id.famr_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        family_activity_medical_record.this.setTitle("Choose Medical Record");

        user_info = getSharedPreferences("user-info", Context.MODE_PRIVATE);

        recyclerView = findViewById(R.id.famr_list);
        refresh = findViewById(R.id.famr_refresh);
        no_net = findViewById(R.id.famr_no_net);
        no_data = findViewById(R.id.famr_no_data);
        shame = findViewById(R.id.famr_load);

        Intent intent = getIntent();
        data = intent.getStringExtra("data");
        from = intent.getStringExtra("from");

        cases = new ArrayList<>();

        if(from.equals("famain")) {
            extractSongs();
        }else if(from.equals("fami")){
            extractSongs2(data);
        }

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //startActivity(new Intent(nurse_activity_add_member_search_result.this,nurse_activity_add_member_search_result.class));
                //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                //finish();

                cases.clear();
                shame.setVisibility(View.VISIBLE);
                no_data.setVisibility(View.GONE);
                no_net.setVisibility(View.GONE);

                if(from.equals("famain")) {
                    extractSongs();
                }else if(from.equals("fami")){
                    extractSongs2(data);
                }

            }
        });
    }

    private void extractSongs() {

        String JSON_URL = "https://hda-server.000webhostapp.com/app/family_get_medical_record.php?family_id="+user_info.getString("position","");
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(0<response.length()) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject caseObject = response.getJSONObject(i);
                            nurse_ext_add_patient_result case2 = new nurse_ext_add_patient_result();
                            case2.setTitle(caseObject.getString("case_id").toString());
                            case2.setArtist(caseObject.getString("case_brgy").toString());
                            case2.setCoverImage(caseObject.getString("case_image").toString());
                            case2.setSongURL(caseObject.getString("url").toString());
                            cases.add(case2);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    recyclerView.setVisibility(View.VISIBLE);
                    no_net.setVisibility(View.GONE);
                    shame.setVisibility(View.GONE);
                    refresh.setRefreshing(false);

                    recyclerView.setLayoutManager(new LinearLayoutManager(family_activity_medical_record.this));
                    adapter = new nurse_adapter_add_patient_result(family_activity_medical_record.this, cases, family_activity_medical_record.this);
                    recyclerView.setAdapter(adapter);

                }else {
                    Toast.makeText(family_activity_medical_record.this,"No Data Found", Toast.LENGTH_SHORT).show();

                    shame.setVisibility(View.GONE);
                    refresh.setRefreshing(false);
                    no_data.setVisibility(View.VISIBLE);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
                shame.setVisibility(View.GONE);
                no_net.setVisibility(View.VISIBLE);
                refresh.setRefreshing(false);
            }
        });

        queue.add(jsonArrayRequest);
    }


    private void extractSongs2(final String member_id) {

        String JSON_URL = "https://hda-server.000webhostapp.com/app/family_get_medical_record.php?member_id="+member_id;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(0<response.length()) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject caseObject = response.getJSONObject(i);
                            nurse_ext_add_patient_result case2 = new nurse_ext_add_patient_result();
                            case2.setTitle(caseObject.getString("case_id").toString());
                            case2.setArtist(caseObject.getString("case_brgy").toString());
                            case2.setCoverImage(caseObject.getString("case_image").toString());
                            case2.setSongURL(caseObject.getString("url").toString());
                            cases.add(case2);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    recyclerView.setVisibility(View.VISIBLE);
                    no_net.setVisibility(View.GONE);
                    shame.setVisibility(View.GONE);
                    refresh.setRefreshing(false);

                    recyclerView.setLayoutManager(new LinearLayoutManager(family_activity_medical_record.this));
                    adapter = new nurse_adapter_add_patient_result(family_activity_medical_record.this, cases, family_activity_medical_record.this);
                    recyclerView.setAdapter(adapter);

                }else {
                    Toast.makeText(family_activity_medical_record.this,"No Data Found", Toast.LENGTH_SHORT).show();

                    shame.setVisibility(View.GONE);
                    refresh.setRefreshing(false);
                    no_data.setVisibility(View.VISIBLE);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse: " + error.getMessage());
                shame.setVisibility(View.GONE);
                no_net.setVisibility(View.VISIBLE);
                refresh.setRefreshing(false);
            }
        });

        queue.add(jsonArrayRequest);
    }

    @Override
    public void onNoteClick(int position) {
        Log.d("tag","onNoteClick: clicked. " + position);
        Intent intent = new Intent(family_activity_medical_record.this, family_activity_medical_record_view.class);
        if(from.equals("famain")) {
            intent.putExtra("data", String.valueOf(position));
            intent.putExtra("from", "famain");
        }else if(from.equals("fami")){
            intent.putExtra("data", String.valueOf(position));
            intent.putExtra("member_id", data);
            intent.putExtra("from", "fami");
        }
        startActivity(intent);



    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}