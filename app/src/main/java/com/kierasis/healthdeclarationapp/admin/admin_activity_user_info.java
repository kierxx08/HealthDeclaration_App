package com.kierasis.healthdeclarationapp.admin;

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
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kierasis.healthdeclarationapp.R;
import com.kierasis.healthdeclarationapp.doctor.doctor_activity_view_patient_prescribe;
import com.kierasis.healthdeclarationapp.my_singleton;
import com.kierasis.healthdeclarationapp.profile;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kierasis.healthdeclarationapp.admin.admin_activity_user_search.user_search;
import static java.lang.Integer.parseInt;

public class admin_activity_user_info extends AppCompatActivity {
    MaterialEditText i_username,i_fname, i_lname, i_pnumber, i_add_add;
    public String bday2, type2;
    Spinner i_sex, i_status, i_brgy;
    public EditText i_bday;
    Button save;
    ArrayList<String> arrayList_sex, arrayList_status, arrayList_brgy;
    ArrayAdapter<String> arrayAdapter_sex, arrayAdapter_status, arrayAdapter_brgy;
    public SharedPreferences user_info;
    ScrollView scroll;
    LinearLayout shame;
    RelativeLayout brgy_layout;
    String data, type, search;
    CircleImageView icon_profile;

    SwipeRefreshLayout refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_user_info);

        user_info = getSharedPreferences("user-info", Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.ausi_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        admin_activity_user_info.this.setTitle("Edit User Informatiobn");
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.admin_tc), PorterDuff.Mode.SRC_ATOP);

        Intent intent = getIntent();
        data = intent.getStringExtra("data");
        type = intent.getStringExtra("type");
        search = intent.getStringExtra("search");

        get_info(data,type,search);
        i_username = findViewById(R.id.ausi_username);
        i_fname = findViewById(R.id.ausi_fname);
        i_lname = findViewById(R.id.ausi_lname);
        i_pnumber = findViewById(R.id.ausi_pnumber);
        i_sex = findViewById(R.id.ausi_sex);
        i_status = findViewById(R.id.ausi_status);
        save = findViewById(R.id.ausi_save);

        i_bday = (EditText) findViewById(R.id.ausi_bday);


        arrayList_sex=new ArrayList<>();
        arrayList_sex.add("Male");
        arrayList_sex.add("Female");
        arrayAdapter_sex=new ArrayAdapter<>(this,R.layout.admin_spinner_list,arrayList_sex);
        i_sex.setAdapter(arrayAdapter_sex);


        arrayList_status=new ArrayList<>();
        arrayList_status.add("verified");
        arrayList_status.add("blocked");
        arrayAdapter_status=new ArrayAdapter<>(this,R.layout.admin_spinner_list,arrayList_status);
        i_status.setAdapter(arrayAdapter_status);

        brgy_layout = findViewById(R.id.ausi_brgy_layout);
        i_brgy = findViewById(R.id.ausi_brgy);
        arrayList_brgy=new ArrayList<>();
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
        arrayAdapter_brgy=new ArrayAdapter<>(this,R.layout.admin_spinner_list,arrayList_brgy);
        i_brgy.setAdapter(arrayAdapter_brgy);
        i_add_add = findViewById(R.id.ausi_add_add);



        scroll = findViewById(R.id.ausi_scroll);
        shame = findViewById(R.id.ausi_load);

        scroll.setVisibility(View.GONE);

        icon_profile = (CircleImageView) findViewById(R.id.ausi_profile);

        refresh = findViewById(R.id.davpp_refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //startActivity(new Intent(nurse_activity_add_member_search_result.this,nurse_activity_add_member_search_result.class));
                //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                //finish();

                shame.setVisibility(View.VISIBLE);
                scroll.setVisibility(View.GONE);
                get_info(data,type,search);

            }
        });
    }

    private void get_info(final String data,final String type,final String search) {

        String uRl = "https://hda-server.000webhostapp.com/app/admin_user_search_get_info.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if(response.contains("Success")){
                    String[] arrayString = response.split(";");
                    final String user_account_id = arrayString[1];
                    String username = arrayString[2];
                    String fname = arrayString[3];
                    String lname = arrayString[4];
                    final String bday = arrayString[5];
                    final String new_bday = arrayString[6];
                    String pnumber = arrayString[7];
                    String sex = arrayString[8];
                    String status = arrayString[9];
                    String profile_link = arrayString[10];

                    final String brgy = arrayString[11];
                    final String add_add = arrayString[12];

                    i_username.setText(username);
                    i_fname.setText(fname);
                    i_lname.setText(lname);
                    i_bday.setText(new_bday);
                    i_pnumber.setText(pnumber);

                    int spinnerPosition = arrayAdapter_sex.getPosition(sex);
                    i_sex.setSelection(spinnerPosition);

                    int spinnerPosition2 = arrayAdapter_status.getPosition(status);
                    i_status.setSelection(spinnerPosition2);
                    String[] bday_array = bday.split("/");
                    final int bday_month = parseInt(bday_array[0])-1;
                    final int bday_day = parseInt(bday_array[1]);
                    final int bday_year = parseInt(bday_array[2]);

                    final Calendar myCalendar = Calendar.getInstance();

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
                            i_bday.setText(sdf.format(myCalendar.getTime()));

                            String myFormat2 = "MM/dd/yyyy"; //In which you need put here
                            SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2, Locale.US);
                            bday2 = sdf2.format(myCalendar.getTime());
                        }

                    };

                    i_bday.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            new DatePickerDialog(admin_activity_user_info.this, date, bday_year, bday_month,
                                    bday_day).show();
                        }
                    });

                    if(!brgy.equals("none") && !add_add.equals("none")){
                        int spinnerPosition3 = arrayAdapter_brgy.getPosition(brgy);
                        i_brgy.setSelection(spinnerPosition3);
                        i_add_add.setText(add_add);

                        brgy_layout.setVisibility(View.VISIBLE);
                        i_add_add.setVisibility(View.VISIBLE);
                        type2 = "Family";
                    }

                    Drawable profile_dw = getDrawable(R.drawable.profile_all_user);

                    Picasso.get()
                            .load(profile_link)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .placeholder(profile_dw)
                            .into(icon_profile);


                    scroll.setVisibility(View.VISIBLE);
                    shame.setVisibility(View.GONE);

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String txtUsername = i_username.getText().toString();
                            String txtFname= i_fname.getText().toString();
                            String txtLname= i_lname.getText().toString();
                            String txtBday;
                            if(new_bday.equals(i_bday.getText().toString())) {
                                txtBday = bday;
                            }else{
                                txtBday = bday2;
                            }

                            String txtMobile = i_pnumber.getText().toString();
                            String txtSex= i_sex.getSelectedItem().toString();
                            String txtBrgy= i_brgy.getSelectedItem().toString();
                            String txtAdd_add= i_add_add.getText().toString();
                            String txtStatus= i_status.getSelectedItem().toString();

                            if(brgy.equals("none") && add_add.equals("none")){
                                type2 = "Not Family";
                                txtBrgy = "none";
                                txtAdd_add = "none";
                            }

                            if(TextUtils.isEmpty(txtFname) || TextUtils.isEmpty(txtLname) || TextUtils.isEmpty(txtUsername)){
                                Toast.makeText(admin_activity_user_info.this,"1 All Fields Required", Toast.LENGTH_SHORT).show();
                            }else if(TextUtils.isEmpty(i_bday.getText().toString())){
                                Toast.makeText(admin_activity_user_info.this,"Please Select Birthday", Toast.LENGTH_SHORT).show();
                            }else if(TextUtils.isEmpty(txtMobile)){
                                Toast.makeText(admin_activity_user_info.this,"2 All Fields Required", Toast.LENGTH_SHORT).show();
                            }else if(txtMobile.length()!=10){
                                Toast.makeText(admin_activity_user_info.this,"Invalid Phone Number", Toast.LENGTH_SHORT).show();
                            }else if(txtSex.equals("Please Choose")){
                                Toast.makeText(admin_activity_user_info.this,"Please Choose Sex", Toast.LENGTH_SHORT).show();
                            }else if(type2.equals("Family") && txtBrgy.equals("")){
                                Toast.makeText(admin_activity_user_info.this,"Please Choose Barangay", Toast.LENGTH_SHORT).show();
                            }else if(type2.equals("Family") && TextUtils.isEmpty(txtAdd_add)){
                                Toast.makeText(admin_activity_user_info.this,"3 All Fields Required", Toast.LENGTH_SHORT).show();
                            }else{

                                save_info(user_account_id,txtUsername,txtFname,txtLname,txtBday,txtMobile,txtSex,txtBrgy,txtAdd_add,txtStatus);
                                //Toast.makeText(admin_activity_user_info.this, txtBrgy + txtAdd_add, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    refresh.setRefreshing(false);
                    //Toast.makeText(admin_activity_user_info.this, user_account_id, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(admin_activity_user_info.this, response, Toast.LENGTH_SHORT).show();
                    refresh.setRefreshing(false);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(doctor_activity_view_patient_prescribe.this, error.toString(), Toast.LENGTH_SHORT).show();no_net.setVisibility(View.GONE);
                refresh.setRefreshing(false);


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String > param = new  HashMap<>();
                param.put("data", data);
                param.put("type", type);
                param.put("search", search);
                param.put("account_id", user_info.getString("user_account_id","") );
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        my_singleton.getInstance(admin_activity_user_info.this).addToRequestQueue(request);

    }


    private void save_info(final String user_account_id,final String txtUsername,final String txtFname,final String txtLname,final String txtBday,final String txtMobile,final String txtSex,final String txtBrgy,final String txtAdd_add,final String txtStatus) {
        final ProgressDialog progressDialog = new ProgressDialog(admin_activity_user_info.this, R.style.MyAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Updating Account");
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String uRl = "https://hda-server.000webhostapp.com/app/admin_user_search_update_info.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("Success")) {
                    Toast.makeText(admin_activity_user_info.this, "Registered", Toast.LENGTH_SHORT).show();
                    user_search.finish();
                    Intent intent = new Intent(admin_activity_user_info.this, admin_activity_add_doctor_success.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(admin_activity_user_info.this, response, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(admin_activity_user_info.this, txtBday +" "+ txtMobile+" "+ txtSex, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("user_account_id", user_account_id);
                param.put("username", txtUsername);
                param.put("fname", txtFname);
                param.put("lname", txtLname);
                param.put("bday", txtBday);
                param.put("pnumber", txtMobile);
                param.put("sex", txtSex);
                param.put("brgy", txtBrgy);
                param.put("add_add", txtAdd_add);
                param.put("status", txtStatus);
                param.put("account_id", user_info.getString("user_account_id",""));
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        my_singleton.getInstance(admin_activity_user_info.this).addToRequestQueue(request);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}