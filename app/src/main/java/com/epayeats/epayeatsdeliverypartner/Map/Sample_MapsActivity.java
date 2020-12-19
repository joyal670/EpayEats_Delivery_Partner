package com.epayeats.epayeatsdeliverypartner.Map;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import com.epayeats.epayeatsdeliverypartner.Activity.Sample_Activity;
import com.epayeats.epayeatsdeliverypartner.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Sample_MapsActivity extends FragmentActivity implements OnMapReadyCallback
{

    private GoogleMap mMap;

    BottomSheetBehavior behavior;
    TextView map_selected_source;
    TextView map_selected_destination;
    TextView map_selected_km;

    // source
    String lang;
    String lot;
    String getloc_location;

    // destination
    String user_lat;
    String user_long;
    String userLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample__maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        map_selected_source = findViewById(R.id.map_selected_source);
        map_selected_destination = findViewById(R.id.map_selected_destination);
        map_selected_km = findViewById(R.id.map_selected_km);

        // destination
        user_lat = getIntent().getExtras().getString("user_lat");
        user_long = getIntent().getExtras().getString("user_long");
        userLocation = getIntent().getExtras().getString("userLocation");

        // source
        lang = getIntent().getExtras().getString("lang");
        lot = getIntent().getExtras().getString("lot");
        getloc_location = getIntent().getExtras().getString("getloc_location");


        map_selected_source.setText(getloc_location);
        map_selected_destination.setText(userLocation);

    }



    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        LatLng destination = new LatLng(Double.parseDouble(user_lat), Double.parseDouble(user_long));
        mMap.addMarker(new MarkerOptions().position(destination).title("Your Destination"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(destination));

        LatLng source = new LatLng(Double.parseDouble(lang), Double.parseDouble(lot));
        mMap.addMarker(new MarkerOptions().position(source).title("Your Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(source));

        Double km  = distance(Double.parseDouble(lang), Double.parseDouble(lot), Double.parseDouble(user_lat), Double.parseDouble(user_long));

        String roKm = String.valueOf(Math.round(km));

        int temp = Integer.parseInt(roKm);
        int n = temp + 4;

        map_selected_km.setText(roKm);

        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .color(Color.RED)
                .add(
                        new LatLng(Double.parseDouble(lang), Double.parseDouble(lot)),
                        new LatLng(Double.parseDouble(user_lat), Double.parseDouble(user_long))));

    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        double km = dist / 0.62137;

        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}