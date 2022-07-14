package com.kierasis.healthdeclarationapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.kierasis.healthdeclarationapp.family.family_activity_main;
import com.kierasis.healthdeclarationapp.nurse.nurse_activity_main;
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

public class profile extends AppCompatActivity {
    public SharedPreferences user_info;
    public Drawable profile_dw, cover_dw;
    RelativeLayout admin_bg;
    ImageView cover;

    Uri uri;
    CircleImageView change, icon_profile;

    TextView fullname, username, fam_id, address, fam_head, phone, bday, sex2;
    LinearLayout not_family, yes_family;

    Button change_username, change_password;
    public static Activity profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        profile = this;
        user_info = getSharedPreferences("user-info", Context.MODE_PRIVATE);
        final String special_num = user_info.getString("user_special_num", "");
        final String account_id = user_info.getString("user_account_id", "");
        final String uname = user_info.getString("user_name", "");
        final String fname = user_info.getString("user_fname", "");
        final String lname = user_info.getString("user_lname", "");
        final String pnumber = user_info.getString("user_pnumber", "");
        final String brgy = user_info.getString("user_brgy", "");
        final String add_add = user_info.getString("user_add_add", "");
        final String b_day = user_info.getString("user_bday", "");
        final String sex = user_info.getString("user_sex", "");
        final String position = user_info.getString("position", "");

        //initializing views
        icon_profile = (CircleImageView) findViewById(R.id.profile_image);
        cover = findViewById(R.id.prfl_cover);
        fullname = findViewById(R.id.tv_fullname);
        username = findViewById(R.id.tv_username);
        fam_id = findViewById(R.id.tv_fam_id);
        address = findViewById(R.id.tv_address);
        fam_head = findViewById(R.id.tv_fam_head);
        phone = findViewById(R.id.tv_phone);
        bday = findViewById(R.id.tv_bday);
        sex2 = findViewById(R.id.tv_sex);
        not_family = findViewById(R.id.prfl_not_family);
        yes_family = findViewById(R.id.prfl_family);
        change_username = findViewById(R.id.change_username);
        change_password = findViewById(R.id.change_password);

        change_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(profile.this, change_username.class));
            }
        });

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(profile.this, change_password.class));
            }
        });

        change = findViewById(R.id.prfl_change_profile);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(profile.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestForCamera();
                } else {
                    CropImage.startPickImageActivity(profile.this);
                }
            }
        });

        //Admin
        if (special_num.equals("101720")) {
            admin_bg = findViewById(R.id.prfl_main_layout);
            TextView phone_label = findViewById(R.id.prfl_lbl_no);
            TextView bday_label = findViewById(R.id.prfl_lbl_bday);
            TextView sex_label = findViewById(R.id.prfl_lbl_sex);
            admin_bg.setBackgroundResource(R.color.admin_bg);
            phone_label.setTextColor(ContextCompat.getColor(profile.this, R.color.admin_tc));
            bday_label.setTextColor(ContextCompat.getColor(profile.this, R.color.admin_tc));
            sex_label.setTextColor(ContextCompat.getColor(profile.this, R.color.admin_tc));
            username.setTextColor(ContextCompat.getColor(profile.this, R.color.admin_tc));
            fullname.setTextColor(ContextCompat.getColor(profile.this, R.color.admin_tc));
            address.setTextColor(ContextCompat.getColor(profile.this, R.color.admin_tc));
            phone.setTextColor(ContextCompat.getColor(profile.this, R.color.admin_tc));
            bday.setTextColor(ContextCompat.getColor(profile.this, R.color.admin_tc));
            sex2.setTextColor(ContextCompat.getColor(profile.this, R.color.admin_tc));
            change_username.setBackgroundResource(R.drawable.shape_radius_002);
            change_username.setTextColor(ContextCompat.getColor(profile.this, R.color.admin_tc));
            change_password.setBackgroundResource(R.drawable.shape_radius_002);
            change_password.setTextColor(ContextCompat.getColor(profile.this, R.color.admin_tc));
        }

        //Family
        if (special_num.equals("101719")) {

            not_family.setVisibility(View.GONE);
            fullname.setText("Family " + lname);
            username.setText("@"+uname);
            fam_id.setText(position);
            fam_head.setText(fname + " " + lname);
            address.setText(add_add + ", " + brgy);
            phone.setText("+63" + pnumber);

        } else {
            yes_family.setVisibility(View.GONE);
            fullname.setText(fname + " " + lname);
            username.setText("@"+uname);
            phone.setText("+63" + pnumber);
            bday.setText(b_day);
            sex2.setText(sex);

        }

        //Family
        if (special_num.equals("101719")) {
            cover_dw = getDrawable(R.drawable.family_cover);
            profile_dw = getDrawable(R.drawable.profile_family);
            //Admin
        } else if (special_num.equals("101720")) {
            cover_dw = getDrawable(R.drawable.admin_cover);
            if (sex.equals("Female")) {
                profile_dw = getDrawable(R.drawable.profile_admin_female);
            } else {
                profile_dw = getDrawable(R.drawable.profile_admin_male);
            }
            //Doctor
        } else if (special_num.equals("101721")) {
            cover_dw = getDrawable(R.drawable.doctor_cover);
            if (sex.equals("Female")) {
                profile_dw = getDrawable(R.drawable.profile_doctor_female);
            } else {
                profile_dw = getDrawable(R.drawable.profile_doctor_male);
            }
            //Nurse
        } else if (special_num.equals("101722")) {
            cover_dw = getDrawable(R.drawable.nurse_cover);
            if (sex.equals("Female")) {
                profile_dw = getDrawable(R.drawable.profile_nurse_female);
            } else {
                profile_dw = getDrawable(R.drawable.profile_nurse_male);
            }
        }

        cover.setBackground(cover_dw);
        icon_profile.setImageDrawable(profile_dw);
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {

            icon_profile.setImageDrawable(profile_dw);

        } else {
            get_profile(account_id);
        }
    }

    private void get_profile(final String account_id) {

        String uRl = "https://hda-server.000webhostapp.com/app/get_profile.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("Found")) {
                    String[] arrayString = response.split(";");
                    String profile_link = arrayString[1];

                    Picasso.get()
                            .load(profile_link)
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .placeholder(profile_dw)
                            .into(icon_profile);

                } else {
                    icon_profile.setImageDrawable(profile_dw);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                icon_profile.setImageDrawable(profile_dw);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("account_id", account_id);
                param.put("from", "user");
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        my_singleton.getInstance(profile.this).addToRequestQueue(request);
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
                    //icon_profile.setImageBitmap(bitmap);
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
        final ProgressDialog progressDialog = new ProgressDialog(profile.this, R.style.default_dialog);
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
                            String profile_link = "https://hda-server.000webhostapp.com/app/account/profile/user/"+user_info.getString("user_account_id", "")+".jpg";

                            if(obj.getString("error").equals("false")) {
                                Picasso.get()
                                        .load(profile_link)
                                        .networkPolicy(NetworkPolicy.NO_CACHE)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                                        .placeholder(profile_dw)
                                        .into(icon_profile);
                            }

                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            progressDialog.dismiss();
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
                params.put("from", "user");
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
                        CropImage.startPickImageActivity(profile.this);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(profile.this, "Camera Permission is Required", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
}