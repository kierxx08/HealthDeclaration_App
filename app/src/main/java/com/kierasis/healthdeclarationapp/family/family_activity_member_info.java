package com.kierasis.healthdeclarationapp.family;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.kierasis.healthdeclarationapp.EndPoints;
import com.kierasis.healthdeclarationapp.R;
import com.kierasis.healthdeclarationapp.VolleyMultipartRequest;
import com.kierasis.healthdeclarationapp.my_singleton;
import com.kierasis.healthdeclarationapp.nurse.nurse_activity_add_patient;
import com.kierasis.healthdeclarationapp.profile;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class family_activity_member_info extends AppCompatActivity {
    public SharedPreferences user_info;
    public Drawable profile_dw, cover_dw;
    SwipeRefreshLayout refresh;
    String data, member_id;
    CircleImageView icon_profile, change_profile;
    ScrollView scroll;
    LinearLayout load;
    ImageView no_net, cover;
    TextView tv_fullname, tv_bday, tv_weight, tv_height, tv_sex, tv_last, tv_count;
    Button med_rec;

    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.family_activity_member_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.fami_toolbar);
        setSupportActionBar(toolbar);
        family_activity_member_info.this.setTitle("");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        user_info = getSharedPreferences("user-info", Context.MODE_PRIVATE);

        scroll = findViewById(R.id.fami_scroll);
        load = findViewById(R.id.fami_load);
        tv_fullname = findViewById(R.id.fami_name);
        tv_bday = findViewById(R.id.fami_bday);
        tv_weight = findViewById(R.id.fami_weight);
        tv_height = findViewById(R.id.fami_height);
        tv_sex = findViewById(R.id.fami_sex);
        tv_last = findViewById(R.id.fami_last_consult);
        tv_count = findViewById(R.id.fami_consult_count);
        cover = findViewById(R.id.prfl_cover);
        no_net = findViewById(R.id.fami_no_net);
        refresh = findViewById(R.id.fami_refresh);
        med_rec =findViewById(R.id.fami_view_med_rec);

        change_profile = findViewById(R.id.fami_change_profile);
        change_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(family_activity_member_info.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestForCamera();
                } else {
                    CropImage.startPickImageActivity(family_activity_member_info.this);
                }
            }
        });

        med_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(family_activity_member_info.this, family_activity_medical_record.class);
                intent.putExtra("data", member_id);
                intent.putExtra("from", "fami");
                startActivity(intent);
            }
        });

        profile_dw = getDrawable(R.drawable.profile_family);
        icon_profile = (CircleImageView) findViewById(R.id.fami_profile);
        Intent intent = getIntent();
        data = intent.getStringExtra("data");

        get_info(data);



        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //startActivity(new Intent(nurse_activity_add_member_search_result.this,nurse_activity_add_member_search_result.class));
                //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                //finish();

                scroll.setVisibility(View.GONE);
                load.setVisibility(View.VISIBLE);
                cover.setVisibility(View.VISIBLE);
                icon_profile.setVisibility(View.GONE);
                change_profile.setVisibility(View.GONE);
                no_net.setVisibility(View.GONE);
                get_info(data);

            }
        });

    }


    private void get_info(final String data) {

        String uRl = "https://hda-server.000webhostapp.com/app/family_get_member_info.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if(response.contains("Success")){
                    String[] arrayString = response.split(";");
                    member_id = arrayString[1];
                    String fullname = arrayString[2];
                    String bday = arrayString[3];
                    String sex = arrayString[4];
                    String weight = arrayString[5];
                    String height = arrayString[6];
                    String consult_count = arrayString[7];
                    String last_consult = arrayString[8];
                    String profile_link = arrayString[9];

                    tv_fullname.setText(fullname);
                    tv_bday.setText(bday);
                    tv_sex.setText(sex);
                    tv_weight.setText(weight);
                    tv_height.setText(height);
                    tv_count.setText(consult_count);
                    tv_last.setText(last_consult);

                    Picasso.get()
                            .load(profile_link)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .placeholder(profile_dw)
                            .into(icon_profile);

                    scroll.setVisibility(View.VISIBLE);
                    load.setVisibility(View.GONE);
                    cover.setVisibility(View.VISIBLE);
                    icon_profile.setVisibility(View.VISIBLE);
                    change_profile.setVisibility(View.VISIBLE);
                    no_net.setVisibility(View.GONE);
                    refresh.setRefreshing(false);

                }else{
                    icon_profile.setImageDrawable(profile_dw);
                    scroll.setVisibility(View.GONE);
                    load.setVisibility(View.GONE);
                    cover.setVisibility(View.GONE);
                    icon_profile.setVisibility(View.GONE);
                    change_profile.setVisibility(View.GONE);
                    no_net.setVisibility(View.VISIBLE);
                    refresh.setRefreshing(false);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                icon_profile.setImageDrawable(profile_dw);
                scroll.setVisibility(View.GONE);
                load.setVisibility(View.GONE);
                cover.setVisibility(View.GONE);
                icon_profile.setVisibility(View.GONE);
                change_profile.setVisibility(View.GONE);
                no_net.setVisibility(View.VISIBLE);
                refresh.setRefreshing(false);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String > param = new  HashMap<>();
                param.put("data", data );
                param.put("account_id", user_info.getString("user_account_id", ""));
                param.put("from", "user" );
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        my_singleton.getInstance(family_activity_member_info.this).addToRequestQueue(request);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                //displaying selected image to imageview
                imageView.setImageBitmap(bitmap);

                //calling the method uploadBitmap to upload image
                uploadBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        */
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageuri = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageuri)) {
                uri = imageuri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }
            } else {
                startCrop(imageuri);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //imageView.setImageURI(result.getUri());
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result.getUri());
                    icon_profile.setImageBitmap(bitmap);
                    uploadBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void startCrop(Uri imageuri) {
        CropImage.activity(imageuri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setFixAspectRatio(true)
                .start(this);
    }

    /*
     * The method is taking Bitmap as an argument
     * then it will return the byte[] array for the given bitmap
     * and we will send this array to the server
     * here we are using PNG Compression with 80% quality
     * you can give quality between 0 to 100
     * 0 means worse quality
     * 100 means best quality
     * */
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {
        final ProgressDialog progressDialog = new ProgressDialog(family_activity_member_info.this, R.style.default_dialog);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        //getting the tag from the edittext
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, EndPoints.UPLOAD_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("account_id", user_info.getString("user_account_id", ""));
                params.put("member_id", member_id);
                params.put("from", "member");
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);

    }

    private void requestForCamera() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        //Toast.makeText(profile.this,"Camera Permission Granted",Toast.LENGTH_SHORT).show();
                        CropImage.startPickImageActivity(family_activity_member_info.this);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(family_activity_member_info.this, "Camera Permission is Required", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }




    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}