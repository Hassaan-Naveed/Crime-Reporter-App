package com.ppm.crimetrackerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, MarkerDialogue.MarkerDialogueListener {

    private GoogleMap map;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location location ;

    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //Find map object
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById((R.id.map));

        //Ensure map is not null, and set map object
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        //Find button, and set click function to call openDialogue()
        FloatingActionButton reportButton = findViewById(R.id.reportButton);
        reportButton.setOnClickListener(v -> openDialogue());
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Stop location updates when app is paused for battery saving
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    @SuppressLint("MissingPermission")
    public void onMapReady(GoogleMap map) {
        this.map = map;

        //Create LocationRequest object
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(1000);

        //Create FusedLocationProvideClient object and request updates
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            //When a location is found, set Location variable to the last GPS location
            super.onLocationResult(locationResult);
            location = locationResult.getLastLocation();
        }
    };

    private void createMarker(Location location, String name){
        //Ensure location is not null
        if (location != null) {
            Toast toast = Toast.makeText(this, "Making Report", Toast.LENGTH_SHORT);
            toast.show();
            //Move camera to users position
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(),
                            location.getLongitude()), 15));

            //Create marker at current location
            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            map.addMarker(new MarkerOptions()
                    .position(currentLocation)
                    .title(name));
        }
    }

    private void openDialogue() {
        //Create new instance of dialogue class
        MarkerDialogue markerDialogue = new MarkerDialogue();
        markerDialogue.show(getSupportFragmentManager(), "Marker Dialogue");
    }

    @Override
    public void applyText(String name) {
        //Call createMarker with current location and given name
        createMarker(location, name);
    }
}