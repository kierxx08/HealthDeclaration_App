package com.kierasis.healthdeclarationapp.doctor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kierasis.healthdeclarationapp.R;
import com.kierasis.healthdeclarationapp.my_singleton;
import com.kierasis.healthdeclarationapp.nurse.nurse_activity_add_member;
import com.kierasis.healthdeclarationapp.nurse.nurse_activity_add_member_success;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

import static com.kierasis.healthdeclarationapp.nurse.nurse_activity_add_member_search.nams;
import static com.kierasis.healthdeclarationapp.nurse.nurse_activity_add_member_search_result.namsr;

public class doctor_activity_view_patient_medicine extends AppCompatActivity {
    public SharedPreferences user_info;
    public String checkup_id;
    ImageView finish;
    MaterialEditText illness, medicine, amount, time, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_activity_view_patient_medicine);
        Toolbar toolbar = (Toolbar) findViewById(R.id.davpm_toolbar);
        setSupportActionBar(toolbar);
        doctor_activity_view_patient_medicine.this.setTitle("Add Medicine");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        user_info = getSharedPreferences("user-info", Context.MODE_PRIVATE);

        illness = findViewById(R.id.davpm_illness);
        medicine = findViewById(R.id.davpm_medicine);
        amount = findViewById(R.id.davpm_amount);
        time = findViewById(R.id.davpm_time);
        day = findViewById(R.id.davpm_days);

        Intent intent = getIntent();
        checkup_id = intent.getStringExtra("checkup_id");

        finish = findViewById(R.id.davpm_finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtIllness = illness.getText().toString();
                String txtMedicine = medicine.getText().toString();
                String txtAmount = amount.getText().toString();
                String txtTime = time.getText().toString();
                String txtDay = day.getText().toString();
                if(TextUtils.isEmpty(txtIllness) || TextUtils.isEmpty(txtMedicine) || TextUtils.isEmpty(txtAmount) || TextUtils.isEmpty(txtTime) || TextUtils.isEmpty(txtDay)){
                    Toast.makeText(doctor_activity_view_patient_medicine.this,"All Fields Required", Toast.LENGTH_SHORT).show();
                }else{
                    save(txtIllness,txtMedicine,txtAmount,txtTime,txtDay);
                }
            }
        });
    }

    private void save(final String illness, final String medicine, final String amount, final String time, final String day) {
        final ProgressDialog progressDialog = new ProgressDialog(doctor_activity_view_patient_medicine.this, R.style.default_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Adding Medicine");
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String uRl = "https://hda-server.000webhostapp.com/app/doctor_view_patient_medicine.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if(response.contains("Success")){
                    Toast.makeText(doctor_activity_view_patient_medicine.this, "Successfull", Toast.LENGTH_SHORT).show();

                    onBackPressed();

                }else{
                    Toast.makeText(doctor_activity_view_patient_medicine.this, response, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(doctor_activity_view_patient_medicine.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String > param = new  HashMap<>();
                param.put("illness", illness);
                param.put("medicine", medicine);
                param.put("amount", amount);
                param.put("time", time);
                param.put("day", day);
                param.put("checkup_id", checkup_id);
                param.put("account_id", user_info.getString("user_account_id","") );
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        my_singleton.getInstance(doctor_activity_view_patient_medicine.this).addToRequestQueue(request);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}