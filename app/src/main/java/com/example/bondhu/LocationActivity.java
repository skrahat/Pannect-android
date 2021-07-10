package com.example.bondhu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.client.Firebase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.protobuf.StringValue;


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

public class LocationActivity extends AppCompatActivity {

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    ArrayList<String> latList = new ArrayList<>();
    ArrayList<String> lngList = new ArrayList<>();
    ArrayList<String> timeList = new ArrayList<>();
    ArrayList<String> userNameList = new ArrayList<>();
    ArrayList<String> friendsArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        // !!!
        // this section needs to be refactored and imported from previous page to minimize data usage
        // gathers friend list
        // display friends
        String urlF = "https://bondhu-2021-default-rtdb.firebaseio.com/users/" + UserDetails.id + "/friends.json";
        //String urlF = "https://bondhu-2021-default-rtdb.firebaseio.com/users/"+UserDetails.username+"/friends.json";
        StringRequest requestF = new StringRequest(Request.Method.GET, urlF, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //friendsArray.clear();
                //friendsArrayID.clear();
                doOnSuccessF(s);
            }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    System.out.println("" + volleyError);
                }
            });

        //get users name and location
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.addValueEventListener(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again

                latList.clear();
                lngList.clear();
                timeList.clear();
                userNameList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        try
                        {
                            String name = snapshot.child("userName").getValue(String.class);
                            UserLocation location = snapshot.child("location").getValue(UserLocation.class);
                            timeList.add(location.time);
                            latList.add(location.lat);
                            lngList.add(location.lng);
                            userNameList.add(name);
                            if(name != null  && location.lat != null && location.time != null) {
                                //Log.i("testing Name", name);
                                //Log.i("testing lat", location.lat);
                                //Log.i("testing time", location.time);
                            }
                        }
                        catch (NullPointerException nullPointer)
                        {
                           Log.i("error at data", "null value in database");
                        }

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        //getting location-starts here-------
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
        getCurrentLocation();
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
                if (location != null) {
                    //sync map
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {

                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            //initialize lattitude , longitude
                            setLocation(location.getLatitude(), location.getLongitude());
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            //LatLng latLng2 = new LatLng( Double.parseDouble(latList.get(0))+1, Double.parseDouble(lngList.get(0))+1);
                            //LatLng latLng3 = new LatLng( Double.parseDouble(latList.get(1))-1, Double.parseDouble(lngList.get(1))-1);
                            //Create Marker option
                            MarkerOptions options = new MarkerOptions().position(latLng).title(UserDetails.username);
                            //MarkerOptions options2 = new MarkerOptions().position(latLng2).title(userNameList.get(0));
                            //MarkerOptions options3 = new MarkerOptions().position(latLng3).title(userNameList.get(1));

                            //zoom map
                            googleMap.animateCamera((CameraUpdateFactory.newLatLngZoom(latLng, 10)));

                            //add marker on map
                            googleMap.addMarker(options);
                            //googleMap.addMarker(options2);
                            //googleMap.addMarker(options3);
                            //printing all friends location on map
                            int count = 0;
                            while (userNameList.size() > count) {
                                Log.i("location Testing", Integer.toString(count));
                                //if(latList.get(count) != null  && userNameList.get(count) != null) {
                                    LatLng latLngF = new LatLng(Double.parseDouble(latList.get(count) ), Double.parseDouble(lngList.get(count) ));
                                    MarkerOptions optionsF = new MarkerOptions().position(latLngF).title(userNameList.get(count)); //.icon(BitmapFromVector(getApplicationContext(), R.drawable.marker_icon));
                                    googleMap.addMarker(optionsF);
                                //}
                                count++;
                            }
                        }
                    });
                }
            }
        });

    }
    //draw marker to draw on map
    private BitmapDescriptor BitmapFromVector(Context context, int vectorResId) {
        // below line is use to generate a drawable.
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        // below line is use to set bounds to our vector drawable.
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        // below line is use to create a bitmap for our
        // drawable which we have added.
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // below line is use to add bitmap in our canvas.
        Canvas canvas = new Canvas(bitmap);

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas);

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    //generating array of friend
    public void doOnSuccessF(String s) {

        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while (i.hasNext()) {
                key = i.next().toString();
                friendsArray.add(key);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    //updates users current location on firebase database
    private void setLocation(double lat, double lng) {
        Firebase reference2 = new Firebase("https://bondhu-2021-default-rtdb.firebaseio.com/users");
        //reference2.child(UserDetails.id).child("location").child("lnglat").setValue("updateLiveStatus");

        //adding timestamp to current status
        String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date());

        //reference2.child(UserDetails.id).child("currentStatus").child("time").setValue(currentDateandTime);
        //adding it to totalStatus list with timestamp
        Map<String, String> map = new HashMap<String, String>();
        map.put("lat", Double.toString(lat));
        map.put("lng", Double.toString(lng));
        map.put("time", currentDateandTime);
        //map.put("newNotification","true");
        reference2.child(UserDetails.id).child("location").setValue(map);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        if (requestCode == 44) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //when permission granted
                //call method
                getCurrentLocation();
            }
        }
    }
}
