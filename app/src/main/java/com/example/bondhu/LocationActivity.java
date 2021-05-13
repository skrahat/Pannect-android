package com.example.bondhu;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.protobuf.StringValue;


import org.jetbrains.annotations.NotNull;

public class LocationActivity extends AppCompatActivity {

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        //
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);

        //Initialize fused location
        client = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(LocationActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //when permission granted
            //call method
            getCurrentLocation();
        } else {
            //when permission denied
            //request permission
            ActivityCompat.requestPermissions((LocationActivity.this),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                //when sucess
                if (location != null){
                    //sync map
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {

                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            //initialize lattitude , longitude
                            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

                            //Create Marker option
                            MarkerOptions options = new MarkerOptions().position(latLng).title("skrahat");

                            //zoom map
                            googleMap.animateCamera((CameraUpdateFactory.newLatLngZoom(latLng, 10)));

                            //add marker on map
                            googleMap.addMarker(options);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        if (requestCode == 44){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //when permission granted
                //call method
                getCurrentLocation();
            }
        }
    }
    /*
    @Override
public void onMapReady(GoogleMap googleMap) {
   mMap = googleMap;

   ...

   refDatabase.addChildEventListener(new ChildEventListener() {
     @Override
     public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
          LatLng newLocation = new LatLng(
                        dataSnapshot.child("latitude").getValue(Long.class),
                        dataSnapshot.child("longitude").getValue(Long.class)
                    );
          mMap.addMarker(new MarkerOptions()
              .position(newLocation)
              .title(dataSnapshot.getKey()));
     }

     @Override
     public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

     @Override
     public void onChildRemoved(DataSnapshot dataSnapshot) {}

     @Override
     public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

     @Override
     public void onCancelled(DatabaseError databaseError) {}
  });
}
     */

}
