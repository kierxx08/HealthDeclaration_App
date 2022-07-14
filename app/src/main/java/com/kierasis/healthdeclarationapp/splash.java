package com.kierasis.healthdeclarationapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class splash extends AppCompatActivity {
    public static String PACKAGE_NAME, versionName;
    public SharedPreferences device_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);
        device_info = getSharedPreferences("device-info", MODE_PRIVATE);

        int versionCode = BuildConfig.VERSION_CODE;
        versionName = BuildConfig.VERSION_NAME;
        PACKAGE_NAME = getApplicationContext().getPackageName();
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        int error = 0;
        String error_desc = "";
        if (versionCode != 1) {
            error = 1;
            error_desc = "System Error. Version Code Tampered.";
        } else if (!versionName.equals("1.09")) {
            error = 1;
            error_desc = "System Error. Version Name Tampered.";
        } else if (!PACKAGE_NAME.equals("com.kierasis.healthdeclarationapp")) {
            error = 1;
            error_desc = "System Error. Package Name Tampered.";
        }

        //Remove this for Deployment
        //startActivity(new Intent(splash.this, login.class));
        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        //finish();

        //Remove This for deployment/*

        if(error == 1){

            AlertDialog.Builder builder = new AlertDialog.Builder(splash.this);

            builder.setTitle("App Modified");

            builder.setMessage(error_desc);

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {

                        case DialogInterface.BUTTON_NEUTRAL:
                            // Neutral/Cancel button clicked
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            startActivity(intent);
                            break;
                    }
                }
            };

            // Set the alert dialog cancel/neutral button click listener
            builder.setNeutralButton("Exit", dialogClickListener);

            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            // Display the three buttons alert dialog on interface
            dialog.show();

        }else{
            if (Objects.equals(device_info.getString("device_key", ""), "")) {
                check_app(versionName);
            }else if (networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable()) {
                if(!Objects.equals(device_info.getString("latest_version", ""), "")){
                    String latest_vrsn = device_info.getString("latest_version","");
                    String vrsn_desc = device_info.getString("version_desc","");
                    String vrsn_link = device_info.getString("version_link","");
                    if(!versionName.equals(latest_vrsn)){
                        update_app(latest_vrsn,vrsn_desc,vrsn_link);
                    } else {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(splash.this, login.class));
                                finish();
                            }
                        }, 3000);
                    }
                }else {
                    check_app(versionName);
                }
            } else {

                check_app(versionName);
            }


        }

        //Remove This for deployment*/

        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(splash.this, login.class));
                finish();
            }
        }, 3000);*/
    }



    private void check_app(final String version) {
        String url = "https://hda-server.000webhostapp.com/app/app_version.php";


        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String[] arrayString = response.split(";");
                String server_stats = arrayString[0];
                String latest_version = arrayString[1];
                String version_desc = arrayString[2];
                final String version_link = arrayString[3];
                String error_desc = arrayString[4];
                if(server_stats.equals("ok")) {
                    if (version.equals(latest_version)) {

                        if (Objects.equals(device_info.getString("device_key", ""), "")) {
                            gen_id(android.os.Build.BRAND,android.os.Build.MODEL,versionName);

                        }else if(!Objects.equals(device_info.getString("app_version", ""), versionName)){
                            update_version_online(device_info.getString("device_key", ""),versionName);
                        }else if(!Objects.equals(device_info.getString("device_key", ""), "")){
                            startActivity(new Intent(splash.this, login.class));
                            finish();

                        }

                        SharedPreferences.Editor editor = device_info.edit();
                        editor.putString("latest_version",latest_version);
                        editor.putString("version_desc",version_desc);
                        editor.putString("version_link",version_link);
                        editor.apply();
                    } else {
                        update_app(latest_version,version_desc,version_link);
                    }
                }else if(server_stats.equals("error")){

                    // Build an AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(splash.this);

                    // Set a title for alert dialog
                    builder.setTitle("Server Error");

                    // Ask the final question
                    builder.setMessage(error_desc);

                    // Set click listener for alert dialog buttons
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    // User clicked the Yes button
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://hda-server.000webhostapp.com/redirect/error_status.php")));
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

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //On Error
                check_net();

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        request.setShouldCache(false);
        requestQueue.add(request);

    }

    private void update_app(String latest_version, String version_desc, final String version_link) {


        // Build an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(splash.this);

        // Set a title for alert dialog
        builder.setTitle("Please Update");

        // Ask the final question
        builder.setMessage("Version: " +latest_version + "\n\n" + version_desc);

        // Set click listener for alert dialog buttons
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        // User clicked the Yes button
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(version_link)));
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
        builder.setPositiveButton("Update", dialogClickListener);

        // Set the alert dialog cancel/neutral button click listener
        builder.setNeutralButton("Exit", dialogClickListener);

        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        // Display the three buttons alert dialog on interface
        dialog.show();

        SharedPreferences.Editor editor = device_info.edit();
        editor.putString("latest_version",latest_version);
        editor.putString("version_desc",version_desc);
        editor.putString("version_link",version_link);
        editor.apply();
    }

    private void check_net() {

        if(!Objects.equals(device_info.getString("device_key", ""), "")){
            Toast.makeText(splash.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(splash.this, login.class));
            finish();
        }else{

            // Build an AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(splash.this);

            // Set a title for alert dialog
            builder.setTitle("No Internet Connection");

            // Set click listener for alert dialog buttons
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            // User clicked the Yes button
                            if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT <= 22) {
                                startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                            } else {
                                Intent openWirelessSettings = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                startActivity(openWirelessSettings);
                            }
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
            builder.setPositiveButton("Connect", dialogClickListener);

            // Set the alert dialog cancel/neutral button click listener
            builder.setNeutralButton("Exit", dialogClickListener);

            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            // Display the three buttons alert dialog on interface
            dialog.show();
        }
    }

    private void gen_id(final String device_brand, final String device_model,  final String device_app_version){

        String uRl = "https://hda-server.000webhostapp.com/app/gen_device_id.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if(response.contains("Success")){
                    String[] arrayString = response.split(";");
                    String key = arrayString[1];

                    SharedPreferences.Editor editor = device_info.edit();
                    editor.putString("device_key",key);
                    editor.putString("app_version",versionName);
                    editor.apply();

                    startActivity(new Intent(splash.this, login.class));
                    finish();

                }else{
                    Toast.makeText(splash.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Build an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(splash.this);

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
                param.put("device_brand", device_brand);
                param.put("device_model", device_model);
                param.put("device_app_version", device_app_version);
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        my_singleton.getInstance(splash.this).addToRequestQueue(request);

    }



    private void update_version_online(final String device_key,  final String device_app_version){

        String uRl = "https://hda-server.000webhostapp.com/app/update_device_version.php";
        StringRequest request = new StringRequest(Request.Method.POST, uRl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                if(response.contains("Success")){

                    SharedPreferences.Editor editor = device_info.edit();
                    editor.putString("app_version",versionName);
                    editor.apply();

                    show_update();

                }else{
                    Toast.makeText(splash.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Build an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(splash.this);

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
                param.put("device_key", device_key);
                param.put("device_app_version", device_app_version);
                return param;
            }

        };
        request.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setShouldCache(false);
        my_singleton.getInstance(splash.this).addToRequestQueue(request);

    }


    private void show_update(){

        AlertDialog.Builder builder = new AlertDialog.Builder(splash.this);

        builder.setTitle("New Update Info");

        builder.setMessage("\nVersion: 1.09\n" +
                "\nBug Fixed");

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {

                    case DialogInterface.BUTTON_POSITIVE:
                        // User clicked the Yes button
                        dialog.cancel();
                        startActivity(new Intent(splash.this, login.class));
                        finish();
                        break;
                }
            }
        };

        // Set the alert dialog yes button click listener
        builder.setPositiveButton("Ok", dialogClickListener);

        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        // Display the three buttons alert dialog on interface
        dialog.show();

    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        startActivity(new Intent(splash.this,splash.class));
        finish();

    }

}