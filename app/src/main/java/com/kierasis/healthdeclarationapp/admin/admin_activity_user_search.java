package com.kierasis.healthdeclarationapp.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.kierasis.healthdeclarationapp.R;
import com.kierasis.healthdeclarationapp.doctor.doctor_activity_view_patient_prescribe;
import com.kierasis.healthdeclarationapp.doctor.doctor_activity_view_previous;
import com.kierasis.healthdeclarationapp.doctor.doctor_adapter_view_patient_result;
import com.kierasis.healthdeclarationapp.doctor.doctor_ext_view_patient_result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class admin_activity_user_search extends AppCompatActivity implements case_adapter.OnNoteListener{
    Spinner type;
    EditText search_txt;
    Button search_btn;
    ArrayList<String> arrayList_type;
    ArrayAdapter<String> arrayAdapter_type;
    public SharedPreferences user_info;

    List<case_ext> cases;
    case_adapter adapter;

    RecyclerView recyclerView;
    ImageView no_net, no_data;
    RelativeLayout shamer;
    String txt_srch;

    public static Activity user_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_user_search);
        user_search = this;
        user_info = getSharedPreferences("user-info", Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.aus_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        admin_activity_user_search.this.setTitle("Search User");
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.admin_tc), PorterDuff.Mode.SRC_ATOP);

        type = findViewById(R.id.aus_type);
        search_txt = findViewById(R.id.aus_etsearch);
        search_btn = findViewById(R.id.aus_btnsearch);

        arrayList_type=new ArrayList<>();
        arrayList_type.add("Family");
        arrayList_type.add("Doctor");
        arrayList_type.add("Nurse");
        arrayAdapter_type=new ArrayAdapter<>(this,R.layout.admin_spinner_list,arrayList_type);
        type.setAdapter(arrayAdapter_type);

        recyclerView = findViewById(R.id.aus_list);
        cases = new ArrayList<>();
        extractSongs("");

        no_net = findViewById(R.id.aus_no_net);
        no_data = findViewById(R.id.aus_no_data);
        shamer = findViewById(R.id.aus_shame);
        txt_srch = "";
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_srch = search_txt.getText().toString();
                if(TextUtils.isEmpty(txt_srch)){
                    Toast.makeText(admin_activity_user_search.this,"Type to Search", Toast.LENGTH_SHORT).show();
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

        String JSON_URL = "https://hda-server.000webhostapp.com/app/admin_user_search.php?account_id="+user_info.getString("user_account_id","")+"&type="+type.getSelectedItem().toString()+"&search="+search;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if(0<response.length()) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject caseObject = response.getJSONObject(i);
                            case_ext case2 = new case_ext();
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

                    recyclerView.setLayoutManager(new LinearLayoutManager(admin_activity_user_search.this));
                    adapter = new case_adapter(admin_activity_user_search.this, cases, admin_activity_user_search.this);
                    recyclerView.setAdapter(adapter);

                }else {
                    Toast.makeText(admin_activity_user_search.this,"No Data Found", Toast.LENGTH_SHORT).show();
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

        Intent intent = new Intent(admin_activity_user_search.this, admin_activity_user_info.class);
        intent.putExtra("data",  String.valueOf(position));
        intent.putExtra("search",  txt_srch);
        if(txt_srch.isEmpty()) {
            intent.putExtra("type", "none");
        }else {
            intent.putExtra("type", type.getSelectedItem().toString());
        }
        startActivity(intent);

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}