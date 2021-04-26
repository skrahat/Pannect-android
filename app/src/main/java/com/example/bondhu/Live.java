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

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    ArrayList<String> friendsArrayID = new ArrayList<>();

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
    FloatingActionButton logout;

    Boolean clicked;
    Boolean starter =false;
    String status1;
    String status2;
    String status3;
    String status4;
    String status5;

    String TAG= "Testing------------xxxx_-----------Testing";
     FirebaseAuth firebaseAuth;
     FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_live);

        //getting UI IDS
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        totalStatusList = (ListView)findViewById(R.id.totalStatusList);
        noUsersText = (TextView)findViewById(R.id.noUsersText);
        currentUser = (TextView)findViewById(R.id.currentUser);
        friend1 = (ExtendedFloatingActionButton )findViewById(R.id.friend1);
        friend2 = (ExtendedFloatingActionButton)findViewById(R.id.friend2);
        friend3 = (ExtendedFloatingActionButton)findViewById(R.id.friend3);
        friend4 = (ExtendedFloatingActionButton)findViewById(R.id.friend4);
        friend5 = (ExtendedFloatingActionButton)findViewById(R.id.friend5);
        friend6 = (ExtendedFloatingActionButton)findViewById(R.id.friend6);
        logout = findViewById(R.id.logout);

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
       /* createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, userT)
                .setSmallIcon(R.drawable.ic_baseline_connect_without_contact_24)
                .setContentTitle("Pannect")
                .setContentText("New Status")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(currentDateandTimeGeneral))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        //create and send notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        */
        ////////////////////-----------------construction notification----------------------------------------------------------


        //generates all users in list
        String url = "https://bondhu-2021-default-rtdb.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
                //displaying current username and display gif
                currentUser.setText(UserDetails.username);
                currentStatusView.setText(UserDetails.currentStatus);
                int idX = getResources().getIdentifier("com.example.bondhu:drawable/" + UserDetails.currentStatus, null, null);
                currentStatusGif.setImageResource(idX);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Live.this);
        rQueue.add(request);

        //gathers friend list
        // display friends
        String urlF = "https://bondhu-2021-default-rtdb.firebaseio.com/users/"+UserDetails.id+"/friends.json";
        //String urlF = "https://bondhu-2021-default-rtdb.firebaseio.com/users/"+UserDetails.username+"/friends.json";
        StringRequest requestF = new StringRequest(Request.Method.GET, urlF, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                //friendsArray.clear();
                //friendsArrayID.clear();
                doOnSuccessF(s);

                statusDbRef2.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            int friendsArrayIDCounter = 0;
                            while(friendsArray.size() > friendsArrayIDCounter){
                                String valuex = task.getResult().child(UserDetails.id).child("friends").child(friendsArray.get(friendsArrayIDCounter)).getValue(String.class);
                                friendsArrayID.add(valuex);

                                friendsArrayIDCounter++;
                            }
                            //String valuex = task.getResult().child(UserDetails.id).child("currentStatus").getValue(String.class);
                            //Long userCount= task.getResult().getChildrenCount();
                            //Log.d("$$$$$$$$$$$$$$$",Long.toString(userCount));
                            //int i = 0;
                   /* while (userCount > i) {
                        String value = task.getResult().getChildren();
                    }*/
                    /*
                    if (task.getResult().exists()) {
                        for (DataSnapshot snapshot : task.getResult().getChildren()) {
                            String statusT = snapshot.child("id").getValue(String.class);
                            //Log.i("$$$$$$$$$$$$$$$",statusT);
                            Log.i("xxx",statusT);

                        }

                    }

                     */
                            friendsStatusArray.clear();
                            int friendsArrayStatusCounter = 0;
                            while(friendsArray.size()> friendsArrayStatusCounter){
                                String valuex = task.getResult().child(friendsArrayID.get(friendsArrayStatusCounter)).child("currentStatus").getValue(String.class);
                                friendsStatusArray.add(valuex);

                                switch(friendsArrayStatusCounter+1) {
                                    case 1:
                                        friend1.setText(friendsArray.get(0)+":"+friendsStatusArray.get(0));
                                        int id1 = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(0), null, null);
                                        friend1Gif.setImageResource(id1);
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
                                friendsArrayStatusCounter++;
                            }

                            starter=true;
                        }
                    }
                });
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueueF = Volley.newRequestQueue(Live.this);
        rQueueF.add(requestF);

        //get all friends status ONCE INITIALLY
        String urlS = "https://bondhu-2021-default-rtdb.firebaseio.com/users.json";
        StringRequest requestS = new StringRequest(Request.Method.GET, urlS, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {

                //getAllFriendsStatus(s);

            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });
        RequestQueue rQueueS = Volley.newRequestQueue(Live.this);
        rQueueS.add(requestS);



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

        pd = new ProgressDialog(Live.this);
        pd.setMessage("Loading...");
        pd.show();




        //realtime updating friends name and status
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("users");
        myRef.addValueEventListener(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                //if condition to make sure friend array is obtained first
                if (starter == true){
                        friendsStatusArray.clear();
                    int count = 0;
                    while (friendsArray.size() > count) {
                        String value = dataSnapshot.child(friendsArrayID.get(count)).child("currentStatus").getValue(String.class);
                        friendsStatusArray.add(value);
                        String w = String.valueOf(count);


                        switch (count + 1) {
                            case 1:
                                friend1.setText(friendsArray.get(0) + ":" + friendsStatusArray.get(0));
                                int id1 = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(0), null, null);
                                friend1Gif.setImageResource(id1);
                                friend1.setVisibility(View.VISIBLE);
                                friend1Gif.setVisibility(View.VISIBLE);
                                if (friendsStatusArray.get(0).equals(UserDetails.currentStatus)) {
                                    friend1.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.red));
                                } else {
                                    friend1.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.light_blue));
                                }

                                break;
                            case 2:
                                friend2.setText(friendsArray.get(1) + ":" + friendsStatusArray.get(1));
                                int id2 = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(1), null, null);
                                friend2Gif.setImageResource(id2);
                                friend2.setVisibility(View.VISIBLE);
                                friend2Gif.setVisibility(View.VISIBLE);
                                if (friendsStatusArray.get(1).equals(UserDetails.currentStatus)) {
                                    friend2.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.red));
                                } else {
                                    friend2.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.light_blue));
                                }
                                break;
                            case 3:
                                friend3.setText(friendsArray.get(2) + ":" + friendsStatusArray.get(2));
                                int id3 = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(2), null, null);
                                friend3Gif.setImageResource(id3);
                                friend3.setVisibility(View.VISIBLE);
                                friend3Gif.setVisibility(View.VISIBLE);
                                if (friendsStatusArray.get(2).equals(UserDetails.currentStatus)) {
                                    friend3.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.red));
                                } else {
                                    friend3.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.light_blue));
                                }
                                break;
                            case 4:
                                friend4.setText(friendsArray.get(3) + ":" + friendsStatusArray.get(3));
                                int id4 = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(3), null, null);
                                friend4Gif.setImageResource(id4);
                                friend4.setVisibility(View.VISIBLE);
                                friend4Gif.setVisibility(View.VISIBLE);
                                if (friendsStatusArray.get(3).equals(UserDetails.currentStatus)) {
                                    friend4.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.red));
                                } else {
                                    friend4.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.light_blue));
                                }
                                break;
                            case 5:
                                friend5.setText(friendsArray.get(4) + ":" + friendsStatusArray.get(4));
                                int id5 = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(4), null, null);
                                friend5Gif.setImageResource(id5);
                                friend5.setVisibility(View.VISIBLE);
                                friend5Gif.setVisibility(View.VISIBLE);
                                if (friendsStatusArray.get(4).equals(UserDetails.currentStatus)) {
                                    friend5.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.red));
                                } else {
                                    friend5.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.light_blue));
                                }
                                break;
                            case 6:
                                friend6.setText(friendsArray.get(5) + ":" + friendsStatusArray.get(5));
                                int id6 = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(5), null, null);
                                friend6Gif.setImageResource(id6);
                                friend6.setVisibility(View.VISIBLE);
                                friend6Gif.setVisibility(View.VISIBLE);
                                if (friendsStatusArray.get(5).equals(UserDetails.currentStatus)) {
                                    friend6.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.red));
                                } else {
                                    friend6.setBackgroundTintList(ContextCompat.getColorStateList(Live.this, R.color.light_blue));
                                }
                                break;
                        }
                        count++;
                    }

                }

                //String value = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        //view user's own status history
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users");
        btncurrentUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query=databaseReference.child(UserDetails.id).child("totalStatus").orderByChild("time").limitToLast(5);
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
        //click fab friend1 button to show total status list
        friend1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Query query=databaseReference.child(friendsArrayID.get(0)).child("totalStatus").orderByChild("time").limitToLast(5);
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
                Query query=databaseReference.child(friendsArrayID.get(1)).child("totalStatus").orderByChild("time").limitToLast(5);
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
                Query query=databaseReference.child(friendsArrayID.get(2)).child("totalStatus").orderByChild("time").limitToLast(5);
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
                Query query=databaseReference.child(friendsArrayID.get(3)).child("totalStatus").orderByChild("time").limitToLast(5);
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
                Query query=databaseReference.child(friendsArrayID.get(4)).child("totalStatus").orderByChild("time").limitToLast(5);
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
                Query query=databaseReference.child(friendsArrayID.get(5)).child("totalStatus").orderByChild("time").limitToLast(5);
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
                reference2.child(UserDetails.id).child("currentStatus").setValue(liveStatus);


                //adding it to totalStatus list with timestamp under users
                Map<String, String> map = new HashMap<String, String>();
                map.put("time", currentDateandTime);
                map.put("status",liveStatus);
                map.put("newNotification","true");
                reference2.child(UserDetails.id).child("totalStatus").push().setValue(map);

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
                reference2.child(UserDetails.id).child("currentStatus").setValue(updateLiveStatus);

                String currentDateandTime = new SimpleDateFormat(" yyyy-MM-dd HH:mm").format(new Date());

                //adding it to totalStatus list with timestamp
                Map<String, String> map = new HashMap<String, String>();
                map.put("time", currentDateandTime);
                map.put("status",updateLiveStatus);
                map.put("newNotification","true");
                reference2.child(UserDetails.id).child("totalStatus").push().setValue(map);

                String url2 = "https://bondhu-2021-default-rtdb.firebaseio.com/users.json";
                int idX = getResources().getIdentifier("com.example.bondhu:drawable/" + updateLiveStatus, null, null);
                currentStatusGif.setImageResource(idX);

                currentStatusView.setText(updateLiveStatus +"\n"+ currentDateandTime);
                UserDetails.currentStatus= updateLiveStatus;
                Toast.makeText(Live.this, "status added successfully", Toast.LENGTH_LONG).show();



                // notificationId is a unique int for each notification that you must define
                //notificationManager.notify(100, builder.build());


            }});

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Live.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });



    }

    //////////////////////////////////////////////////////////
    //////////////////////METHODS
    //////////////////////////////////////////////////////////

    //gets users list from database
    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";
            //Log.i(TAG, i.toString());

            while(i.hasNext()){
                key = i.next().toString();
                if(!key.equals(UserDetails.id)) {
                    String key2 = obj.getJSONObject(key).getString("currentStatus");
                    String nameT = obj.getJSONObject(key).getString("userName");
                    al2.add(key2);
                    al.add(nameT);
                }
                if(key.equals(UserDetails.id)){
                    UserDetails.username=obj.getJSONObject(key).getString("userName");
                    UserDetails.currentStatus=obj.getJSONObject(key).getString("currentStatus");
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

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();
                String key2 =obj.getString(key);

                friendsArray.add(key);
                //friendsArrayID.add(key2);
                Log.i("testing friend array",key);
                Log.i("testing friend array",key2);




                totalFriends++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalFriends <1){
        }
        else{


        }


        pd.dismiss();
    }
    //get every friends status
    public void getAllFriendsStatus(String s){
        // fetching current status from the friendsArray List
        try{
            JSONObject obj = new JSONObject(s);
            Iterator i = obj.keys();
            String key = "";

            while (i.hasNext()) {
                key = i.next().toString();
                int countX = 0;
                while (friendsArrayID.size() > countX) {


                    if(key.equals(friendsArrayID.get(countX))) {
                        String key2 = obj.getJSONObject(key).getString("currentStatus");
                        friendsStatusArray.add(key2);
                        Log.i("friend stat testID", friendsArrayID.get(countX));
                        Log.i("friend stat testStat", key2);
                    }
                    if(key.equals(UserDetails.id)){
                        UserDetails.username=obj.getJSONObject(key).getString("userName");
                    }
                    countX++;
                }
            }
            for(int z =1;friendsArrayID.size() +1 >z;z++)
                switch(z) {
                    case 1:
                        friend1.setText(friendsArray.get(0)+":"+friendsStatusArray.get(0));
                        int id1 = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(0), null, null);
                        Log.i("friendsArray.get(0)--", friendsArray.get(0));
                        Log.i("friendsStatAray.get(0)-", friendsStatusArray.get(0));
                        Log.i("testing friend ID 1--", friendsArrayID.get(0));
                        friend1Gif.setImageResource(id1);
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
                        Log.i("friendsArray.get(1)--", friendsArray.get(1));
                        Log.i("friendsStatAray.get(1)-", friendsStatusArray.get(1));
                        Log.i("testing friend ID 1--", friendsArrayID.get(1));
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
                        Log.i("friendsArray.get(2)--", friendsArray.get(2));
                        Log.i("friendsStatAray.get(2)-", friendsStatusArray.get(2));
                        Log.i("testing friend ID 3--", friendsArrayID.get(2));
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