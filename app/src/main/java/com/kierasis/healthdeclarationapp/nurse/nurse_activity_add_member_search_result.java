package com.kierasis.healthdeclarationapp.nurse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
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
import com.kierasis.healthdeclarationapp.login;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class nurse_activity_add_member_search_result extends AppCompatActivity implements nurse_adapter_add_member_result.OnNoteListener{
    RecyclerView recyclerView;
    RecyclerView caselist;
    List<nurse_ext_add_member_result> cases;
    nurse_adapter_add_member_result adapter;

    SimpleArcLoader simpleArcLoader3;

    SwipeRefreshLayout refresh;
    ImageView no_net,no_data;
    public String search;
    public static Activity namsr;
    public String destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nurse_activity_add_member_search_result);
        namsr = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.namsr_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        nurse_activity_add_member_search_result.this.setTitle("Choose Family");

        Intent get_intent = getIntent();
        search = get_intent.getStringExtra("search");
        destination = get_intent.getStringExtra("destination");

        recyclerView = findViewById(R.id.namsr_list);
        cases = new ArrayList<>();
        extractSongs(search);


        simpleArcLoader3 = findViewById(R.id.namsr_loader);
        caselist = findViewById(R.id.namsr_list);
        simpleArcLoader3.start();

        refresh = findViewById(R.id.refresh_namsr_list);

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
                extractSongs(search);

            }
        });

        no_net = findViewById(R.id.namsr_no_net);
        no_data = findViewById(R.id.namsr_no_data);


    }


    private void extractSongs(final String search) {

        String JSON_URL = "https://hda-server.000webhostapp.com/app/nurse_add_member_search_result.php?search="+search;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(0<response.length()) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject caseObject = response.getJSONObject(i);
                            nurse_ext_add_member_result case2 = new nurse_ext_add_member_result();
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
                        caselist.setVisibility(View.VISIBLE);

                    }

                    recyclerView.setLayoutManager(new LinearLayoutManager(nurse_activity_add_member_search_result.this));
                    adapter = new nurse_adapter_add_member_result(nurse_activity_add_member_search_result.this, cases, nurse_activity_add_member_search_result.this);
                    recyclerView.setAdapter(adapter);

                    refresh.setRefreshing(false);
                }else {
                    Toast.makeText(nurse_activity_add_member_search_result.this,"No Data Found", Toast.LENGTH_SHORT).show();
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

        if(destination.equals("add_member")) {
            Intent intent = new Intent(nurse_activity_add_member_search_result.this, nurse_activity_add_member.class);
            intent.putExtra("data", String.valueOf(position));
            intent.putExtra("search", search);
            intent.putExtra("from", "namsr");
            startActivity(intent);
        }else if(destination.equals("add_patient")){
            Intent intent = new Intent(nurse_activity_add_member_search_result.this, nurse_activity_add_patient_search_result.class);
            intent.putExtra("data", String.valueOf(position));
            intent.putExtra("search", search);
            startActivity(intent);
        }else{
            Toast.makeText(nurse_activity_add_member_search_result.this, "No Destination", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}