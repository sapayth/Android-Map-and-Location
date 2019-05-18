package com.example.mapandlocation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_LOCATION = 101;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    TextView tvLocation;
    Double lat;
    Double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvLocation = findViewById(R.id.tv_locaiton);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(500);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (locationResult == null) {
                    return;
                }

                for(Location location : locationResult.getLocations()) {
                    if(location != null) {
                        lat = location.getLatitude();
                        lng = location.getLongitude();
                        tvLocation.setText("Lat: " + lat + ", Lng: " + lng);
                    } else {
                        tvLocation.setText("Location NOT found!");
                    }
                }
            }
        };


        getLocation();

    }

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        } else {
//            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//                @Override
//                public void onSuccess(Location location) {
//                    if (location != null) {
//                        lat = location.getLatitude();
//                        lng = location.getLongitude();
//                        tvLocation.setText("Lat: " + lat + ", Lng: " + lng);
//                    } else {
//                        tvLocation.setText("Location NOT found!");
//                    }
//                }
//            });

            mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                PERMISSION_REQUEST_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestLocationPermission();
            } else {
                mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                        } else {
                            tvLocation.setText("Location NOT found!");
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        } else {
            getLocation();
        }
    }

    public void openMap(View view) {
        Intent mapIntent = new Intent(this, MapsActivity.class);
        mapIntent.putExtra("latitude", lat);
        mapIntent.putExtra("longitude", lng);
        startActivity(mapIntent);
    }

    private void stopLocationUpdates() {
        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
}
