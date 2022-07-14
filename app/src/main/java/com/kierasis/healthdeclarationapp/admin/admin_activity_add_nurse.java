package com.kierasis.healthdeclarationapp.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kierasis.healthdeclarationapp.R;
import com.kierasis.healthdeclarationapp.my_singleton;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class admin_activity_add_nurse extends AppCompatActivity {
    MaterialEditText fname, lname, pnumber;
    public String bday2;
    Spinner sex;
    Button save;
    ArrayList<String> arrayList_sex;
    ArrayAdapter<String> arrayAdapter_sex;
    public SharedPreferences user_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_add_nurse);
        user_info = getSharedPreferences("user-info", Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.aan_doc_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        admin_activity_add_nurse.this.setTitle("Add Nurse");
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.admin_tc), PorterDuff.Mode.SRC_ATOP);

        fname = findViewById(R.id.aan_doc_fname);
        lname = findViewById(R.id.aan_doc_lname);
        pnumber = findViewById(R.id.aan_doc_pnumber);
        sex = findViewById(R.id.aan_doc_sex);
        save = findViewById(R.id.aan_doc_save);

        final Calendar myCalendar = Calendar.getInstance();

        final EditText bday = (EditText) findViewById(R.id.aan_doc_bday);
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
                new DatePickerDialog(admin_activity_add_nurse.this, date, 1970, 00,
                        01).show();
            }
        });

        arrayList_sex=new ArrayList<>();
        arrayList_sex.add("Please Choose");
        arrayList_sex.add("Male");
        arrayList_sex.add("Female");
        arrayAdapter_sex=new ArrayAdapter<>(this,R.layout.admin_spinner_list,arrayList_sex);
        sex.setAdapter(arrayAdapter_sex);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtFname= fname.getText().toString();
                String txtLname= lname.getText().toString();
                String txtBday= bday2;
                String txtMobile = pnumber.getText().toString();
                String txtSex= sex.getSelectedItem().toString();

                if(TextUtils.isEmpty(txtFname) || TextUtils.isEmpty(txtLname)){
                    Toast.makeText(admin_activity_add_nurse.this,"All Fields Required", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(bday.getText().toString())){
                    Toast.makeText(admin_activity_add_nurse.this,"Please Select Birthday", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(txtMobile)){
                    Toast.makeText(admin_activity_add_nurse.this,"All Fields Required", Toast.LENGTH_SHORT).show();
                }else if(txtMobile.length()!=10){
                    Toast.makeText(admin_activity_add_nurse.this,"Invalid Phone Number", Toast.LENGTH_SHORT).show();
                }else if(txtSex.equals("Please Choose")){
                    Toast.makeText(admin_activity_add_nurse.this,"Please Choose Sex", Toast.LENGTH_SHORT).show();
                }else{
                    save(txtFname,txtLname,txtBday,txtMobile,txtSex);
                }
            }
        });

    }

    private void save(final String txtFname,final String txtLname,final String txtBday,final String txtMobile,final String txtSex) {
        final ProgressDialog progressDialog = new ProgressDialog(admin_activity_add_nurse.this, R.style.MyAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Registering Account");
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String uRl = "https://hda-server.000webhostapp.com/app/admin_add_nurse.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                if(response.contains("Success")){

                    Toast.makeText(admin_activity_add_nurse.this, "Registered", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(admin_activity_add_nurse.this, admin_activity_add_doctor_success.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(admin_activity_add_nurse.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String > param = new  HashMap<>();
                param.put("fname", txtFname);
                param.put("lname", txtLname);
                param.put("bday", txtBday);
                param.put("pnumber", txtMobile);
                param.put("sex", txtSex);
                param.put("account_id", user_info.getString("user_account_id","") );
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        my_singleton.getInstance(admin_activity_add_nurse.this).addToRequestQueue(request);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}