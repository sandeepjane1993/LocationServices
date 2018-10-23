package com.example.sande.locationservices;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainAct";
    double lat,lon;
    Location mLastLocation;
    TextView tv_location, tv_address;
    Button btn_location;
    Handler handler;
    List<Address> addresses;
    Geocoder geocoder;
    String myLocation = "";

    FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_location = findViewById(R.id.tView_location);
        btn_location = findViewById(R.id.btn_location);
        tv_address = findViewById(R.id.tView_address);
        handler = new Handler();

        requestPermission();
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getLocation();
        geocoder = new Geocoder(this, Locale.getDefault());
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    geocoder.getFromLocation(lat,lon,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                try {
                    addresses = geocoder.getFromLocation(lat, lon, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
                tv_address.setText(addresses.get(0).getFeatureName() + "\n" + addresses.get(0).getLocality() +"\n"+ addresses.get(0).getAdminArea()
                        + "\n" + addresses.get(0).getCountryName());
            }
        });

    }

    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10);
        }
    }

    public void getLocation() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();

            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        mLastLocation = location;
                         lat = mLastLocation.getLatitude();
                         lon = mLastLocation.getLongitude();
                        tv_location.setText("Latitude : " + lat + "\n" + "Longitude : " + lon);

                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10);
        }
    }

}
