package com.kierasis.healthdeclarationapp.nurse;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class nurse_activity_add_family extends AppCompatActivity {
    MaterialEditText fname, lname, pnumber, add_add;
    Spinner brgy, pitf;
    Button btn_save;
    ArrayList<String> arrayList_brgy, arrayList_pitf;
    ArrayAdapter<String> arrayAdapter_brgy, arrayAdapter_pitf;

    public String bday2;
    public SharedPreferences user_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nurse_activity_add_family);

        Toolbar toolbar = (Toolbar) findViewById(R.id.naf_toolbar);
        setSupportActionBar(toolbar);
        nurse_activity_add_family.this.setTitle("Add Family");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        user_info = getSharedPreferences("user-info", Context.MODE_PRIVATE);

        fname = findViewById(R.id.naf_fname);
        lname = findViewById(R.id.naf_lname);
        final Calendar myCalendar = Calendar.getInstance();

        final EditText bday = (EditText) findViewById(R.id.naf_bday);
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
                new DatePickerDialog(nurse_activity_add_family.this, date, 1970, 00,
                        01).show();
            }
        });

        pnumber = findViewById(R.id.naf_pnumber);
        brgy = findViewById(R.id.naf_brgy);
        arrayList_brgy=new ArrayList<>();
        arrayList_brgy.add("Please Choose");
        arrayList_brgy.add("Alangilan");
        arrayList_brgy.add("Calawit");
        arrayList_brgy.add("Looc");
        arrayList_brgy.add("Magapi");
        arrayList_brgy.add("Makina");
        arrayList_brgy.add("Malabanan");
        arrayList_brgy.add("Paligawan");
        arrayList_brgy.add("Palsara");
        arrayList_brgy.add("Poblacion");
        arrayList_brgy.add("Sala");
        arrayList_brgy.add("Sampalocan");
        arrayList_brgy.add("Solis");
        arrayList_brgy.add("San Sebastian");
        arrayAdapter_brgy=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayList_brgy);
        brgy.setAdapter(arrayAdapter_brgy);
        add_add = findViewById(R.id.naf_add_add);

        pitf = findViewById(R.id.naf_pitf);
        arrayList_pitf=new ArrayList<>();
        arrayList_pitf.add("Please Choose");
        arrayList_pitf.add("Father");
        arrayList_pitf.add("Mother");
        arrayList_pitf.add("Son");
        arrayList_pitf.add("Daughter");
        arrayAdapter_pitf=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayList_pitf);
        pitf.setAdapter(arrayAdapter_pitf);


        btn_save = findViewById(R.id.naf_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtFname= fname.getText().toString();
                String txtLname= lname.getText().toString();
                String txtBday= bday2;
                String txtMobile = pnumber.getText().toString();
                String txtBrgy= brgy.getSelectedItem().toString();
                String txtAdd= add_add.getText().toString();
                String txtPitf= pitf.getSelectedItem().toString();

                if(TextUtils.isEmpty(txtFname) || TextUtils.isEmpty(txtLname) || TextUtils.isEmpty(txtAdd)){
                    Toast.makeText(nurse_activity_add_family.this,"All Fields Required", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(bday.getText().toString())){
                    Toast.makeText(nurse_activity_add_family.this,"Please Select Birthday", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(txtMobile)){
                    Toast.makeText(nurse_activity_add_family.this,"All Fields Required", Toast.LENGTH_SHORT).show();
                }else if(txtMobile.length()!=10){
                    Toast.makeText(nurse_activity_add_family.this,"Invalid Phone Number", Toast.LENGTH_SHORT).show();
                }else if(txtBrgy.equals("Please Choose")){
                    Toast.makeText(nurse_activity_add_family.this,"Please Choose Barangay", Toast.LENGTH_SHORT).show();
                }else if(txtPitf.equals("Please Choose")){
                    Toast.makeText(nurse_activity_add_family.this,"Please Choose\nPosition in the Family", Toast.LENGTH_SHORT).show();
                }else{
                    String sex;
                    if(txtPitf.equals("Father") || txtPitf.equals("Son")){
                        sex = "Male";
                    }else{
                        sex = "Female";
                    }
                    save(txtFname,txtLname,txtBday,txtMobile,txtBrgy,txtAdd,txtPitf,sex);
                }
            }
        });

    }

    private void save(final String fname, final String lname, final String bday, final String pnumber, final String brgy,
                      final String add_add,final String pitf, final String sex) {
        final ProgressDialog progressDialog = new ProgressDialog(nurse_activity_add_family.this, R.style.default_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Registering Account");
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String uRl = "https://hda-server.000webhostapp.com/app/nurse_add_family.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                if(response.contains("Success")){
                    String[] arrayString = response.split(";");
                    String fam_id = arrayString[1];
                    String mem_id = arrayString[2];

                    Toast.makeText(nurse_activity_add_family.this, "Registered", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(nurse_activity_add_family.this, nurse_activity_add_family_success.class);
                    intent.putExtra("fam_id", fam_id);
                    intent.putExtra("mem_id", mem_id);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(nurse_activity_add_family.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                // Build an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(nurse_activity_add_family.this);

                // Set a title for alert dialog
                builder.setTitle("Server Error");

                // Ask the final question
                builder.setMessage("Please Contact Support.");

                // Set click listener for alert dialog buttons
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                // User clicked the Yes button
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://hda-server.000webhostapp.com/redirect/contact_support.php")));
                                break;

                            case DialogInterface.BUTTON_NEUTRAL:
                                // Neutral/Cancel button clicked
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                startActivity(intent);
                                break;
                        }
                    }
                };

                // Set the alert dialog yes button click listener
                builder.setPositiveButton("Ok", dialogClickListener);

                // Set the alert dialog cancel/neutral button click listener
                builder.setNeutralButton("Exit", dialogClickListener);

                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                // Display the three buttons alert dialog on interface
                dialog.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String > param = new  HashMap<>();
                param.put("fname", fname);
                param.put("lname", lname);
                param.put("bday", bday);
                param.put("pnumber", pnumber);
                param.put("brgy", brgy);
                param.put("add_add", add_add);
                param.put("pitf", pitf);
                param.put("sex", sex);
                param.put("nurse_id", user_info.getString("user_account_id","") );
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        my_singleton.getInstance(nurse_activity_add_family.this).addToRequestQueue(request);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}