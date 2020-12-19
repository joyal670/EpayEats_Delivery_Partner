package com.epayeats.epayeatsdeliverypartner.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.epayeats.epayeatsdeliverypartner.Map.Sample_MapsActivity;
import com.epayeats.epayeatsdeliverypartner.R;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class GetLocation_Activity extends AppCompatActivity implements LocationListener
{

    LocationManager locationManager;
    boolean GpsStatus;
    String lang;
    String lot;

    String user_lat;
    String user_long;
    String userLocation;

    TextView getloc_location;
    Button getloc_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location_);

        user_lat = getIntent().getExtras().getString("user_lat");
        user_long = getIntent().getExtras().getString("user_long");
        userLocation = getIntent().getExtras().getString("userLocation");

        getloc_btn = findViewById(R.id.getloc_btn);
        getloc_location = findViewById(R.id.getloc_location);

        checkPermission();

        getloc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(GetLocation_Activity.this, Sample_MapsActivity.class);
                intent.putExtra("user_lat", user_lat);
                intent.putExtra("user_long", user_long);
                intent.putExtra("userLocation", userLocation);

                intent.putExtra("getloc_location", getloc_location.getText().toString());
                intent.putExtra("lang", lang);
                intent.putExtra("lot", lot);
                startActivity(intent);
            }
        });
    }

    private void checkPermission()
    {
        Permissions.check(GetLocation_Activity.this, Manifest.permission.ACCESS_FINE_LOCATION, null, new PermissionHandler() {
            @Override
            public void onGranted()
            {
                getCurrentLocation();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions)
            {
                Toast.makeText(GetLocation_Activity.this, "Pemission Denied", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onBlocked(Context context, ArrayList<String> blockedList)
            {
                Toast.makeText(GetLocation_Activity.this, "Permission Blocked", Toast.LENGTH_SHORT).show();
                return super.onBlocked(context, blockedList);

            }
        });
    }

    private void getCurrentLocation()
    {

        if(CheckGpsStatus())
        {
            try {

                locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(GetLocation_Activity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, GetLocation_Activity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {
            SweetAlertDialog dialog1 = new SweetAlertDialog(GetLocation_Activity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops....")
                    .setContentText("Location permission not granted, please turn on location")
                    .showCancelButton(false)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog)
                        {
                            Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent1);
                            sweetAlertDialog.dismiss();
                        }
                    });

            dialog1.show();

        }

    }

    public boolean CheckGpsStatus()
    {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        return GpsStatus;
    }

    @Override
    public void onLocationChanged(Location location)
    {
        lang = String.valueOf(location.getLatitude());
        lot = String.valueOf(location.getLongitude());

        try {
            Geocoder geocoder = new Geocoder(GetLocation_Activity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);
            getloc_location.setText(address);
            getloc_btn.setVisibility(View.VISIBLE);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }


}