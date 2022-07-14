package com.kierasis.healthdeclarationapp.nurse;

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
import com.kierasis.healthdeclarationapp.my_singleton;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class nurse_activity_add_patient_search_result extends AppCompatActivity  implements nurse_adapter_add_patient_result.OnNoteListener{
    RecyclerView recyclerView;
    RecyclerView caselist;
    List<nurse_ext_add_patient_result> cases;
    nurse_adapter_add_patient_result adapter;

    SimpleArcLoader simpleArcLoader3;

    SwipeRefreshLayout refresh;
    ImageView no_net,no_data;
    public static Activity napsr;
    public SharedPreferences user_info;

    String data, search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nurse_activity_add_patient_search_result);
        napsr = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.napsr_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        nurse_activity_add_patient_search_result.this.setTitle("Choose Family Member");

        user_info = getSharedPreferences("user-info", Context.MODE_PRIVATE);

        Intent intent = getIntent();
        data = intent.getStringExtra("data");
        search = intent.getStringExtra("search");
        get_info(data, search);


        recyclerView = findViewById(R.id.napsr_list);
        cases = new ArrayList<>();
        //extractSongs(search);


        simpleArcLoader3 = findViewById(R.id.napsr_loader);
        caselist = findViewById(R.id.napsr_list);
        simpleArcLoader3.start();

        refresh = findViewById(R.id.refresh_napsr_list);
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
                get_info(data, search);

            }
        });

        no_net = findViewById(R.id.napsr_no_net);
        no_data = findViewById(R.id.napsr_no_data);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void get_info(final String data, final String search) {

        String uRl = "https://hda-server.000webhostapp.com/app/nurse_add_patient_search_result.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if(response.contains("Success")){
                    String[] arrayString = response.split(";");
                    String fam_id = arrayString[1];
                    extractSongs(fam_id);

                }else{
                    Toast.makeText(nurse_activity_add_patient_search_result.this,"No Data Found", Toast.LENGTH_SHORT).show();
                    simpleArcLoader3.stop();
                    simpleArcLoader3.setVisibility(View.GONE);
                    no_data.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                simpleArcLoader3.stop();
                simpleArcLoader3.setVisibility(View.GONE);
                no_net.setVisibility(View.VISIBLE);
                refresh.setRefreshing(false);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String > param = new  HashMap<>();
                param.put("data", data);
                param.put("search", search);
                param.put("nurse_id", user_info.getString("user_account_id","") );
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        my_singleton.getInstance(nurse_activity_add_patient_search_result.this).addToRequestQueue(request);
    }

    private void extractSongs(final String search) {

        String JSON_URL = "https://hda-server.000webhostapp.com/app/nurse_add_patient_search_result2.php?search="+search;
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

                        simpleArcLoader3.stop();
                        simpleArcLoader3.setVisibility(View.GONE);
                        no_net.setVisibility(View.GONE);
                        caselist.setVisibility(View.VISIBLE);

                    }

                    recyclerView.setLayoutManager(new LinearLayoutManager(nurse_activity_add_patient_search_result.this));
                    adapter = new nurse_adapter_add_patient_result(nurse_activity_add_patient_search_result.this, cases, nurse_activity_add_patient_search_result.this);
                    recyclerView.setAdapter(adapter);

                    refresh.setRefreshing(false);
                }else {
                    Toast.makeText(nurse_activity_add_patient_search_result.this,"No Data Found", Toast.LENGTH_SHORT).show();
                    simpleArcLoader3.stop();
                    simpleArcLoader3.setVisibility(View.GONE);
                    no_data.setVisibility(View.VISIBLE);
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

            Intent intent = new Intent(nurse_activity_add_patient_search_result.this, nurse_activity_add_patient.class);
            intent.putExtra("data", data);
            intent.putExtra("search", search);
            intent.putExtra("data2",  String.valueOf(position));
            intent.putExtra("from", "napsr");
            startActivity(intent);

    }

}