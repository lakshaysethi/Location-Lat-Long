package com.lakshaysethi.locationlatlong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {


    private static final int LOCATION_REQUEST_CODE = 1278;
    FusedLocationProviderClient flsc;
    double latitude =0;
    double longitude=0;

    private TextView textView;
    Button getLocationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flsc = new FusedLocationProviderClient(this);
        textView = findViewById(R.id.textView);
        getLocationBtn = findViewById(R.id.getLocationButton);


        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    getLastLocation();

                }else{
                    askLocationPermission();
                }
            }
        });

    }



    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "LOCATION PERMISSION NOT GRANTED PLEASE GRANT LOCATION PERMISSION", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },LOCATION_REQUEST_CODE);
        }
    }

    private void getLastLocation() {
        Task<Location> locationTask  = flsc.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location !=null){
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Date currentTime = Calendar.getInstance().getTime();
                    String locationString = "Your LATITUDE: "+String.valueOf(latitude)+"\n"+"Your LONGITUDE: "
                            +String.valueOf(longitude)+"\n"
                            +"At TIME: "+ currentTime.toString();
                    textView.setText(locationString);
                    return;

                } else{
                    textView.setText("getting location please wait... \n make sure device Location is turned on in the settings");

                    getLastLocation();
                }
            }
        });
        locationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String errMsg = "ERROR!"+ "Could not get the location, \nturn on location or try again..." + e.getMessage();
                textView.setText(errMsg);
            }
        });

    }


}


