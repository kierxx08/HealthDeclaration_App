package com.kierasis.healthdeclarationapp.nurse;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kierasis.healthdeclarationapp.R;
import com.kierasis.healthdeclarationapp.login;
import com.kierasis.healthdeclarationapp.my_singleton;
import com.kierasis.healthdeclarationapp.splash;
import com.leo.simplearcloader.SimpleArcLoader;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.kierasis.healthdeclarationapp.nurse.nurse_activity_add_member_search.nams;
import static com.kierasis.healthdeclarationapp.nurse.nurse_activity_add_member_search_result.namsr;

public class nurse_activity_add_member extends AppCompatActivity {
    public TextView tvfam_id, tvfam_fullname, tvfam_add, tvnam_data;
    String data, search, from;
    MaterialEditText fname, lname;
    Spinner pitf;
    Button btn_save;
    ArrayList<String> arrayList_pitf;
    ArrayAdapter<String> arrayAdapter_pitf;

    public String bday2, fam_id;
    public SharedPreferences user_info;

    SimpleArcLoader simpleArcLoader4;
    ImageView no_net;
    ScrollView scroll;
    SwipeRefreshLayout refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nurse_activity_add_member);


        Toolbar toolbar = (Toolbar) findViewById(R.id.nam_toolbar);
        setSupportActionBar(toolbar);
        nurse_activity_add_member.this.setTitle("Add Family Member");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        user_info = getSharedPreferences("user-info", Context.MODE_PRIVATE);

        //nam_tv = findViewById(R.id.nam_tv);
        tvnam_data = findViewById(R.id.nam_data);
        tvfam_id = findViewById(R.id.nam_fam_id);
        tvfam_fullname = findViewById(R.id.nam_fam_fullname);
        tvfam_add = findViewById(R.id.nam_fam_add);
        no_net = findViewById(R.id.nam_no_net);
        scroll = findViewById(R.id.nam_scroll);
        simpleArcLoader4 = findViewById(R.id.nam_loader);

        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        if(from.equals("namsr")) {
            data = intent.getStringExtra("data");
            search = intent.getStringExtra("search");
            get_info(data, search);
        }else if(from.equals("nafs")) {
            data = intent.getStringExtra("data");
            get_info2(data);
        }

        fname = findViewById(R.id.nam_fname);
        lname = findViewById(R.id.nam_lname);

        final Calendar myCalendar = Calendar.getInstance();

        final EditText bday = (EditText) findViewById(R.id.nam_bday);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "MMM. dd, yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                bday.setText(sdf.format(myCalendar.getTime()));

                String myFormat2 = "MM/dd/yyyy"; //In which you need put here
                SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2, Locale.US);
                bday2 = sdf2.format(myCalendar.getTime());
            }

        };

        bday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(nurse_activity_add_member.this, date, 2000, 00,
                        01).show();
            }
        });
        pitf = findViewById(R.id.nam_pitf);
        arrayList_pitf=new ArrayList<>();
        arrayList_pitf.add("Please Choose");
        arrayList_pitf.add("Father");
        arrayList_pitf.add("Mother");
        arrayList_pitf.add("Son");
        arrayList_pitf.add("Daughter");
        arrayAdapter_pitf=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayList_pitf);
        pitf.setAdapter(arrayAdapter_pitf);

        btn_save = findViewById(R.id.nam_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtFname= fname.getText().toString();
                String txtLname= lname.getText().toString();
                String txtBday= bday2;
                String txtPitf= pitf.getSelectedItem().toString();

                if(TextUtils.isEmpty(txtFname) || TextUtils.isEmpty(txtLname)){
                    Toast.makeText(nurse_activity_add_member.this,"All Fields Required", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(bday.getText().toString())){
                    Toast.makeText(nurse_activity_add_member.this,"Please Select Birthday", Toast.LENGTH_SHORT).show();
                }else if(txtPitf.equals("Please Choose")){
                    Toast.makeText(nurse_activity_add_member.this,"Please Choose\nPosition in the Family", Toast.LENGTH_SHORT).show();
                }else{
                    String sex;
                    if(txtPitf.equals("Father") || txtPitf.equals("Son")){
                        sex = "Male";
                    }else{
                        sex = "Female";
                    }
                    save(txtFname,txtLname,txtBday,txtPitf,sex);
                }
            }
        });

        refresh = findViewById(R.id.refresh_nam_list);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //startActivity(new Intent(nurse_activity_add_member_search_result.this,nurse_activity_add_member_search_result.class));
                //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                //finish();

                no_net.setVisibility(View.GONE);
                scroll.setVisibility(View.GONE);
                simpleArcLoader4.start();
                simpleArcLoader4.setVisibility(View.VISIBLE);
                if(from.equals("namsr")) {
                    get_info(data, search);
                }else if(from.equals("nafs")) {
                    get_info2(data);
                }

            }
        });

    }

    private void get_info(final String data, final String search){

        String uRl = "https://hda-server.000webhostapp.com/app/nurse_add_member_search_result_view.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if(response.contains("Success")){
                    String[] arrayString = response.split(";");
                    fam_id = arrayString[1];
                    String fam_fullname = arrayString[2];
                    String fam_add = arrayString[3];

                    tvnam_data.setText("Result No.: "+data);
                    tvfam_id.setText("Family Number: "+fam_id);
                    tvfam_fullname.setText("Head of the Family: "+fam_fullname);
                    tvfam_add.setText("Address: "+fam_add);

                    scroll.setVisibility(View.VISIBLE);
                    simpleArcLoader4.stop();
                    simpleArcLoader4.setVisibility(View.GONE);
                    refresh.setRefreshing(false);

                }else{
                    Toast.makeText(nurse_activity_add_member.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                simpleArcLoader4.stop();
                simpleArcLoader4.setVisibility(View.GONE);
                no_net.setVisibility(View.VISIBLE);
                scroll.setVisibility(View.GONE);
                refresh.setRefreshing(false);
                Toast.makeText(nurse_activity_add_member.this, "Error", Toast.LENGTH_SHORT).show();

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
        my_singleton.getInstance(nurse_activity_add_member.this).addToRequestQueue(request);

    }



    private void get_info2(final String search_fam_id){

        String uRl = "https://hda-server.000webhostapp.com/app/nurse_add_member_search_result_view.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if(response.contains("Success")){
                    String[] arrayString = response.split(";");
                    fam_id = arrayString[1];
                    String fam_fullname = arrayString[2];
                    String fam_add = arrayString[3];

                    tvnam_data.setText("Result No.: "+data);
                    tvfam_id.setText("Family Number: "+fam_id);
                    tvfam_fullname.setText("Head of the Family: "+fam_fullname);
                    tvfam_add.setText("Address: "+fam_add);

                    scroll.setVisibility(View.VISIBLE);
                    simpleArcLoader4.stop();
                    simpleArcLoader4.setVisibility(View.GONE);
                    refresh.setRefreshing(false);

                }else{
                    Toast.makeText(nurse_activity_add_member.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                simpleArcLoader4.stop();
                simpleArcLoader4.setVisibility(View.GONE);
                no_net.setVisibility(View.VISIBLE);
                scroll.setVisibility(View.GONE);
                refresh.setRefreshing(false);
                Toast.makeText(nurse_activity_add_member.this, "Error", Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String > param = new  HashMap<>();
                param.put("search_fam_id", search_fam_id);
                param.put("nurse_id", user_info.getString("user_account_id","") );
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        my_singleton.getInstance(nurse_activity_add_member.this).addToRequestQueue(request);

    }

    private void save(final String fname, final  String lname, final  String bday, final  String pitf, final  String sex) {
        final ProgressDialog progressDialog = new ProgressDialog(nurse_activity_add_member.this, R.style.default_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Registering Family Member");
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String uRl = "https://hda-server.000webhostapp.com/app/nurse_add_member.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if(response.contains("Success")){
                    String[] arrayString = response.split(";");
                    String mem_id = arrayString[1];
                    nams.finish();
                    namsr.finish();
                    Toast.makeText(nurse_activity_add_member.this, "Registered", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(nurse_activity_add_member.this, nurse_activity_add_member_success.class);
                    intent.putExtra("pitf", pitf);
                    intent.putExtra("mem_id", mem_id);
                    startActivity(intent);
                    finish();

                }else{
                    Toast.makeText(nurse_activity_add_member.this, response, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(nurse_activity_add_member.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String > param = new  HashMap<>();
                param.put("fname", fname);
                param.put("lname", lname);
                param.put("bday", bday);
                param.put("pitf", pitf);
                param.put("sex", sex);
                param.put("fam_id", fam_id);
                param.put("nurse_id", user_info.getString("user_account_id","") );
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        my_singleton.getInstance(nurse_activity_add_member.this).addToRequestQueue(request);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}