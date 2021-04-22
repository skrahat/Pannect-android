package com.example.bondhu;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class Live extends AppCompatActivity {
    ListView totalStatusList;
    TextView noUsersText;
    TextView currentUser;
    TextView currentStatusView;
    ExtendedFloatingActionButton friend1;
    ExtendedFloatingActionButton friend2;
    ExtendedFloatingActionButton friend3;
    ExtendedFloatingActionButton friend4;
    ExtendedFloatingActionButton friend5;
    ExtendedFloatingActionButton friend6;
    ArrayList<String> al = new ArrayList<>();
    ArrayList<String> al2 = new ArrayList<>();
    ArrayList<String> friendsArray = new ArrayList<>();
    ArrayList<String> friendsStatusArray = new ArrayList<>();
    ArrayList<String> gifListArray = new ArrayList<>();
    ArrayList<String> totalStatus = new ArrayList<>();

    CountDownTimer timer;

    int totalFriends = 0;
    int totalUsers = 0;
    int counter;
    ProgressDialog pd;
    Button btnLiveStatusData2;
    Button btnLiveStatusSelectAdd;
    Button btnFriends;
    EditText etLiveStatus;
    DatabaseReference statusDbRef;
    Spinner spinnerStatus;
    DatabaseReference statusDbRef2;
    String userT;
    GifImageView gifImageView2;
    GifImageView friend1Gif;
    GifImageView friend2Gif;
    GifImageView friend3Gif;
    GifImageView friend4Gif;
    GifImageView friend5Gif;
    GifImageView friend6Gif;
    GifImageView currentStatusGif;
    ExtendedFloatingActionButton btncurrentUser;

    Boolean clicked;
    String status1;
    String status2;
    String status3;
    String status4;
    String status5;

    String TAG= "Testing------------xxxx_-----------Testing";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        //getting UI IDS
        totalStatusList = (ListView)findViewById(R.id.totalStatusList);
        noUsersText = (TextView)findViewById(R.id.noUsersText);
        currentUser = (TextView)findViewById(R.id.currentUser);
        friend1 = (ExtendedFloatingActionButton )findViewById(R.id.friend1);
        friend2 = (ExtendedFloatingActionButton)findViewById(R.id.friend2);
        friend3 = (ExtendedFloatingActionButton)findViewById(R.id.friend3);
        friend4 = (ExtendedFloatingActionButton)findViewById(R.id.friend4);
        friend5 = (ExtendedFloatingActionButton)findViewById(R.id.friend5);
        friend6 = (ExtendedFloatingActionButton)findViewById(R.id.friend6);
        etLiveStatus = findViewById(R.id.etLiveStatus);
        btnLiveStatusData2 = findViewById(R.id.btnLiveStatusData2);
        btnLiveStatusSelectAdd = findViewById(R.id.btnLiveStatusSelectAdd);
        btnFriends = findViewById(R.id.btnFriends);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        currentStatusView = (TextView)findViewById(R.id.currentStatusView);
        btncurrentUser = findViewById(R.id.btncurrentUser);
        userT = UserDetails.username;
        clicked = false;

        gifImageView2 = (GifImageView) findViewById(R.id.gifImageView2);
        friend1Gif = (GifImageView) findViewById(R.id.friend1Gif);
        friend2Gif = (GifImageView) findViewById(R.id.friend2Gif);
        friend3Gif = (GifImageView) findViewById(R.id.friend3Gif);
        friend4Gif = (GifImageView) findViewById(R.id.friend4Gif);
        friend5Gif = (GifImageView) findViewById(R.id.friend5Gif);
        friend6Gif = (GifImageView) findViewById(R.id.friend6Gif);
        currentStatusGif = (GifImageView) findViewById(R.id.currentStatusGif);


        String currentDateandTimeGeneral = new SimpleDateFormat(" yyyy-MM-dd HH:mm").format(new Date());


        statusDbRef = FirebaseDatabase.getInstance().getReference().child("status");
        statusDbRef2 = FirebaseDatabase.getInstance().getReference().child("users");
        //displaying current username and display gif
        currentUser.setText(UserDetails.username);
        currentStatusView.setText(UserDetails.currentStatus);
        int idX = getResources().getIdentifier("com.example.bondhu:drawable/" + UserDetails.currentStatus, null, null);
        currentStatusGif.setImageResource(idX);



        //////spinner values added from array

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.statuses, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerStatus.setAdapter(adapter);

        ////////////////////-----------------construction notification----------------------------------------------------------
        //notification update for status update
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, userT)
                .setSmallIcon(R.drawable.ic_baseline_connect_without_contact_24)
                .setContentTitle("Pannect")
                .setContentText("New Status")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(currentDateandTimeGeneral))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        //create and send notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        ////////////////////-----------------construction notification----------------------------------------------------------

        //view user's own status history
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users");
        btncurrentUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query=databaseReference.child(UserDetails.username).child("totalStatus").orderByChild("time").limitToLast(5);
                //resets tiimer and starts a new timer of 5 seconds with 1 second interval
                if (timer != null){
                    timer.cancel();
                }

                timer = new CountDownTimer(5000, 1000){
                    public void onTick(long millisUntilFinished){

                    }
                    public  void onFinish(){
                        totalStatusList.setVisibility(View.INVISIBLE);
                        clicked=false;

                    }
                }.start();
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        getSingleUserOldStatus(dataSnapshot);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                if(!clicked){
                    totalStatusList.setVisibility(View.VISIBLE);
                    clicked=true;
                }else{

                    totalStatusList.setVisibility(View.INVISIBLE);

                    clicked=false;
                }
            }
        });
        //click fab friend1 button to show total status list
        friend1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Query query=databaseReference.child(friendsArray.get(0)).child("totalStatus").orderByChild("time").limitToLast(5);
                    //resets timer and starts a new timer of 5 seconds with 1 second interval
                    if (timer != null){
                        timer.cancel();
                    }

                    timer = new CountDownTimer(5000, 1000){
                        public void onTick(long millisUntilFinished){

                        }
                        public  void onFinish(){
                            totalStatusList.setVisibility(View.INVISIBLE);
                            clicked=false;

                        }
                    }.start();

                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            getSingleUserOldStatus(dataSnapshot);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    if(!clicked){
                        totalStatusList.setVisibility(View.VISIBLE);
                        clicked=true;
                    }else{

                        totalStatusList.setVisibility(View.INVISIBLE);

                        clicked=false;
                    }
                }
            });
        //click fab friend2 button to show total status list
        friend2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query=databaseReference.child(friendsArray.get(1)).child("totalStatus").orderByChild("time").limitToLast(5);
                //resets tiimer and starts a new timer of 5 seconds with 1 second interval
                if (timer != null){
                    timer.cancel();
                }

                timer = new CountDownTimer(5000, 1000){
                    public void onTick(long millisUntilFinished){

                    }
                    public  void onFinish(){
                        totalStatusList.setVisibility(View.INVISIBLE);
                        clicked=false;

                    }
                }.start();

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        getSingleUserOldStatus(dataSnapshot);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                if(!clicked){
                    totalStatusList.setVisibility(View.VISIBLE);
                    clicked=true;
                }else{

                    totalStatusList.setVisibility(View.INVISIBLE);

                    clicked=false;
                }
            }
        });
        //click fab friend3 button to show total status list
        friend3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query=databaseReference.child(friendsArray.get(2)).child("totalStatus").orderByChild("time").limitToLast(5);
                //resets tiimer and starts a new timer of 5 seconds with 1 second interval
                if (timer != null){
                    timer.cancel();
                }

                timer = new CountDownTimer(5000, 1000){
                    public void onTick(long millisUntilFinished){

                    }
                    public  void onFinish(){
                        totalStatusList.setVisibility(View.INVISIBLE);
                        clicked=false;

                    }
                }.start();

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        getSingleUserOldStatus(dataSnapshot);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                if(!clicked){
                    totalStatusList.setVisibility(View.VISIBLE);
                    clicked=true;
                }else{

                    totalStatusList.setVisibility(View.INVISIBLE);

                    clicked=false;
                }
            }
        });
        //click fab friend4 button to show total status list
        friend4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query=databaseReference.child(friendsArray.get(3)).child("totalStatus").orderByChild("time").limitToLast(5);
                //resets tiimer and starts a new timer of 5 seconds with 1 second interval
                if (timer != null){
                    timer.cancel();
                }

                timer = new CountDownTimer(5000, 1000){
                    public void onTick(long millisUntilFinished){

                    }
                    public  void onFinish(){
                        totalStatusList.setVisibility(View.INVISIBLE);
                        clicked=false;

                    }
                }.start();

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        getSingleUserOldStatus(dataSnapshot);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                if(!clicked){
                    totalStatusList.setVisibility(View.VISIBLE);
                    clicked=true;
                }else{

                    totalStatusList.setVisibility(View.INVISIBLE);

                    clicked=false;
                }
            }
        });
        //click fab friend5 button to show total status list
        friend5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query=databaseReference.child(friendsArray.get(4)).child("totalStatus").orderByChild("time").limitToLast(5);
                //resets tiimer and starts a new timer of 5 seconds with 1 second interval
                if (timer != null){
                    timer.cancel();
                }

                timer = new CountDownTimer(5000, 1000){
                    public void onTick(long millisUntilFinished){

                    }
                    public  void onFinish(){
                        totalStatusList.setVisibility(View.INVISIBLE);
                        clicked=false;

                    }
                }.start();

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        getSingleUserOldStatus(dataSnapshot);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                if(!clicked){
                    totalStatusList.setVisibility(View.VISIBLE);
                    clicked=true;
                }else{

                    totalStatusList.setVisibility(View.INVISIBLE);

                    clicked=false;
                }
            }
        });
        //click fab friend6 button to show total status list
        friend6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query=databaseReference.child(friendsArray.get(5)).child("totalStatus").orderByChild("time").limitToLast(5);
                //resets tiimer and starts a new timer of 5 seconds with 1 second interval
                if (timer != null){
                    timer.cancel();
                }

                timer = new CountDownTimer(5000, 1000){
                    public void onTick(long millisUntilFinished){

                    }
                    public  void onFinish(){
                        totalStatusList.setVisibility(View.INVISIBLE);
                        clicked=false;

                    }
                }.start();

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        getSingleUserOldStatus(dataSnapshot);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                if(!clicked){
                    totalStatusList.setVisibility(View.VISIBLE);
                    clicked=true;
                }else{

                    totalStatusList.setVisibility(View.INVISIBLE);

                    clicked=false;
                }
            }
        });

        //open users activity
        btnFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity();
            }
        });

        //gathers friend list
        // display friends
        String urlF = "https://bondhu-2021-default-rtdb.firebaseio.com/users.json";
        //String urlF = "https://bondhu-2021-default-rtdb.firebaseio.com/users/"+UserDetails.username+"/friends.json";
        StringRequest requestF = new StringRequest(Request.Method.GET, urlF, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccessF(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueueF = Volley.newRequestQueue(Live.this);
        rQueueF.add(requestF);


        //realtime updating colours of friends if linked
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.addValueEventListener(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        ////////////////////-----------------construction notification----------------------------------------------------------

        //create realtime notification for status update
        DatabaseReference statusRef = database.getReference("status");
        Query query=databaseReference.orderByChild("time").limitToLast(1);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            // notificationId is a unique int for each notification that you must define
                int random_int = (int)Math.floor(Math.random()*(1000-1+1)+1);
                //notificationManager.notify(random_int, builder.build());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ////////////////////-----------------construction notification----------------------------------------------------------

        //realtime updating friends name and status
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("users");
        myRef.addValueEventListener(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.



                friendsStatusArray.clear();
                int count = 0;
                while (friendsArray.size() > count) {
                    String value = dataSnapshot.child(friendsArray.get(count)).child("currentStatus").getValue(String.class);
                    friendsStatusArray.add(value);
                    String w =String.valueOf(count);


                    switch(count+1) {
                        case 1:
                            friend1.setText(friendsArray.get(0)+":"+friendsStatusArray.get(0));
                            int id = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(0), null, null);
                            friend1Gif.setImageResource(id);
                            friend1.setVisibility(View.VISIBLE);
                            friend1Gif.setVisibility(View.VISIBLE);
                            if(friendsStatusArray.get(0).equals(UserDetails.currentStatus)){
                                friend1.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.red));
                            }else{
                                friend1.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.light_blue));
                            }

                            break;
                        case 2:
                            friend2.setText(friendsArray.get(1)+":"+friendsStatusArray.get(1));
                            int id2 = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(1), null, null);
                            friend2Gif.setImageResource(id2);
                            friend2.setVisibility(View.VISIBLE);
                            friend2Gif.setVisibility(View.VISIBLE);
                            if(friendsStatusArray.get(1).equals(UserDetails.currentStatus)){
                                friend2.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.red));
                            }else{
                                friend2.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.light_blue));
                            }
                            break;
                        case 3:
                            friend3.setText(friendsArray.get(2)+":"+friendsStatusArray.get(2));
                            int id3 = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(2), null, null);
                            friend3Gif.setImageResource(id3);
                            friend3.setVisibility(View.VISIBLE);
                            friend3Gif.setVisibility(View.VISIBLE);
                            if(friendsStatusArray.get(2).equals(UserDetails.currentStatus)){
                                friend3.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.red));
                            }else{
                                friend3.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.light_blue));
                            }
                            break;
                        case 4:
                            friend4.setText(friendsArray.get(3)+":"+friendsStatusArray.get(3));
                            int id4 = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(3), null, null);
                            friend4Gif.setImageResource(id4);
                            friend4.setVisibility(View.VISIBLE);
                            friend4Gif.setVisibility(View.VISIBLE);
                            if(friendsStatusArray.get(3).equals(UserDetails.currentStatus)){
                                friend4.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.red));
                            }else{
                                friend4.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.light_blue));
                            }
                            break;
                        case 5:
                            friend5.setText(friendsArray.get(4)+":"+friendsStatusArray.get(4));
                            int id5 = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(4), null, null);
                            friend5Gif.setImageResource(id5);
                            friend5.setVisibility(View.VISIBLE);
                            friend5Gif.setVisibility(View.VISIBLE);
                            if(friendsStatusArray.get(4).equals(UserDetails.currentStatus)){
                                friend5.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.red));
                            }else{
                                friend5.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.light_blue));
                            }
                            break;
                        case 6:
                            friend6.setText(friendsArray.get(5)+":"+friendsStatusArray.get(5));
                            int id6 = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(5), null, null);
                            friend6Gif.setImageResource(id6);
                            friend6.setVisibility(View.VISIBLE);
                            friend6Gif.setVisibility(View.VISIBLE);
                            if(friendsStatusArray.get(5).equals(UserDetails.currentStatus)){
                                friend6.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.red));
                            }else{
                                friend6.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.light_blue));
                            }
                            break;
                    }
                    count++;
                }



                //String value = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        //create and update new Current Status
        btnLiveStatusData2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase reference2 = new Firebase("https://bondhu-2021-default-rtdb.firebaseio.com/users");
                Firebase statusRef = new Firebase("https://bondhu-2021-default-rtdb.firebaseio.com/status");
                String liveStatus = etLiveStatus.getText().toString();
                String currentDateandTime = new SimpleDateFormat(" yyyy-MM-dd HH:mm").format(new Date());

                //inserting status seperately under status
                Map<String, String> mapStatus = new HashMap<String, String>();
                mapStatus.put("user", UserDetails.username);
                mapStatus.put("status", liveStatus);
                mapStatus.put("time", currentDateandTime);
                statusRef.push().setValue(mapStatus);

                //adding currentstatus under user
                reference2.child(userT).child("currentStatus").setValue(liveStatus);


                //adding it to totalStatus list with timestamp under users
                Map<String, String> map = new HashMap<String, String>();
                map.put("time", currentDateandTime);
                map.put("status",liveStatus);
                map.put("newNotification","true");
                reference2.child(userT).child("totalStatus").push().setValue(map);

                String url2 = "https://bondhu-2021-default-rtdb.firebaseio.com/users.json";
                int idX = getResources().getIdentifier("com.example.bondhu:drawable/" + liveStatus, null, null);
                currentStatusGif.setImageResource(idX);

                currentStatusView.setText(liveStatus +"\n"+ currentDateandTime);
                UserDetails.currentStatus= liveStatus;
                Toast.makeText(Live.this, "status added successfully", Toast.LENGTH_LONG).show();
                etLiveStatus.setText("");


            }});
        // update status from spinner list
        btnLiveStatusSelectAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase reference2 = new Firebase("https://bondhu-2021-default-rtdb.firebaseio.com/users");
                String updateLiveStatus = spinnerStatus.getSelectedItem().toString();
                reference2.child(userT).child("currentStatus").setValue(updateLiveStatus);

                String currentDateandTime = new SimpleDateFormat(" yyyy-MM-dd HH:mm").format(new Date());

                //adding it to totalStatus list with timestamp
                Map<String, String> map = new HashMap<String, String>();
                map.put("time", currentDateandTime);
                map.put("status",updateLiveStatus);
                map.put("newNotification","true");
                reference2.child(userT).child("totalStatus").push().setValue(map);

                String url2 = "https://bondhu-2021-default-rtdb.firebaseio.com/users.json";
                int idX = getResources().getIdentifier("com.example.bondhu:drawable/" + updateLiveStatus, null, null);
                currentStatusGif.setImageResource(idX);

                currentStatusView.setText(updateLiveStatus +"\n"+ currentDateandTime);
                UserDetails.currentStatus= updateLiveStatus;
                Toast.makeText(Live.this, "status added successfully", Toast.LENGTH_LONG).show();



                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(100, builder.build());


            }});



        pd = new ProgressDialog(Live.this);
        pd.setMessage("Loading...");
        pd.show();
        //generates all users in list
        String url = "https://bondhu-2021-default-rtdb.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Live.this);
        rQueue.add(request);



    }
    //gets users list from database
    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";
            //Log.i(TAG, i.toString());

            while(i.hasNext()){
                key = i.next().toString();
                if(!key.equals(UserDetails.username)) {
                    String key2 = obj.getJSONObject(key).getString("currentStatus");
                    al2.add(key2);
                    al.add(key);
                }

                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalUsers <=1){
            //noUsersText.setVisibility(View.VISIBLE);
        }
        else{
            noUsersText.setVisibility(View.GONE);

        }

        pd.dismiss();
    }
    //get 10 most recent status
    public void getSingleUserOldStatus(DataSnapshot dataSnapshot){

        totalStatus.clear();
        if (dataSnapshot.exists()) {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                TotalStatus statusT = snapshot.getValue(TotalStatus.class);
                totalStatus.add(statusT.status);

            }

        }

        totalStatusList.setAdapter(new ArrayAdapter<String>(Live.this, android.R.layout.simple_list_item_1, totalStatus));


    }

    //generating array of friend
    public void doOnSuccessF(String s){
        try {
            JSONObject obj = new JSONObject(s);
            JSONObject obj2 = obj.getJSONObject(UserDetails.username).getJSONObject("friends");
            Iterator i = obj2.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();

                if(!key.equals("123123")) {
                    friendsArray.add(key);
                }

                totalFriends++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalFriends <1){
        }
        else{


        }
        // fetching current status from the friendsArray List
        try{
            JSONObject obj = new JSONObject(s);
            Iterator i = obj.keys();
            String key = "";

            while (i.hasNext()) {
                key = i.next().toString();
                int countX = 0;
                while (friendsArray.size() > countX) {


                    if(key.equals(friendsArray.get(countX))) {
                        String key2 = obj.getJSONObject(key).getString("currentStatus");
                        friendsStatusArray.add(key2);

                }
                    countX++;
                }
            }
            for(int z =1;friendsArray.size() +1 >z;z++)
            switch(z) {
                case 1:
                    friend1.setText(friendsArray.get(0)+":"+friendsStatusArray.get(0));
                    int id = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(0), null, null);
                    friend1Gif.setImageResource(id);
                    friend1.setVisibility(View.VISIBLE);
                    friend1Gif.setVisibility(View.VISIBLE);
                    if(friendsStatusArray.get(0).equals(UserDetails.currentStatus)){
                        friend1.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.red));
                    }else{
                        friend1.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.light_blue));
                    }

                    break;
                case 2:
                    friend2.setText(friendsArray.get(1)+":"+friendsStatusArray.get(1));
                    int id2 = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(1), null, null);
                    friend2Gif.setImageResource(id2);
                    friend2.setVisibility(View.VISIBLE);
                    friend2Gif.setVisibility(View.VISIBLE);
                    if(friendsStatusArray.get(1).equals(UserDetails.currentStatus)){
                        friend2.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.red));
                    }else{
                        friend2.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.light_blue));
                    }
                    break;
                case 3:
                    friend3.setText(friendsArray.get(2)+":"+friendsStatusArray.get(2));
                    int id3 = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(2), null, null);
                    friend3Gif.setImageResource(id3);
                    friend3.setVisibility(View.VISIBLE);
                    friend3Gif.setVisibility(View.VISIBLE);
                    if(friendsStatusArray.get(2).equals(UserDetails.currentStatus)){
                        friend3.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.red));
                    }else{
                        friend3.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.light_blue));
                    }
                    break;
                case 4:
                    friend4.setText(friendsArray.get(3)+":"+friendsStatusArray.get(3));
                    int id4 = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(3), null, null);
                    friend4Gif.setImageResource(id4);
                    friend4.setVisibility(View.VISIBLE);
                    friend4Gif.setVisibility(View.VISIBLE);
                    if(friendsStatusArray.get(3).equals(UserDetails.currentStatus)){
                        friend4.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.red));
                    }else{
                        friend4.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.light_blue));
                    }
                    break;
                case 5:
                    friend5.setText(friendsArray.get(4)+":"+friendsStatusArray.get(4));
                    int id5 = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(4), null, null);
                    friend5Gif.setImageResource(id5);
                    friend5.setVisibility(View.VISIBLE);
                    friend5Gif.setVisibility(View.VISIBLE);
                    if(friendsStatusArray.get(4).equals(UserDetails.currentStatus)){
                        friend5.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.red));
                    }else{
                        friend5.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.light_blue));
                    }
                    break;
                case 6:
                    friend6.setText(friendsArray.get(5)+":"+friendsStatusArray.get(5));
                    int id6 = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(5), null, null);
                    friend6Gif.setImageResource(id6);
                    friend6.setVisibility(View.VISIBLE);
                    friend6Gif.setVisibility(View.VISIBLE);
                    if(friendsStatusArray.get(5).equals(UserDetails.currentStatus)){
                        friend6.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.red));
                    }else{
                        friend6.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.light_blue));
                    }
                    break;
            }
        } catch (JSONException e) {
        e.printStackTrace();
        }

        pd.dismiss();
    }
    //redirects to friends setting activity
    public void openNewActivity(){
        Intent intent = new Intent(Live.this, Users.class);
        startActivity(intent);
    }

    ////////////////////-----------------construction notification----------------------------------------------------------

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(userT, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    Log.i("notification ", "checking");
    }

}