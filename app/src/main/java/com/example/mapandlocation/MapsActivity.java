package com.example.mapandlocation;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Double lat;
    Double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        lat = intent.getDoubleExtra("latitude", 0);
        lng = intent.getDoubleExtra("longitude", 0);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        // LatLng sydney = new LatLng(-34, 151);
        LatLng currentPosition = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(currentPosition).title("You are here!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentPosition));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
    }
}
