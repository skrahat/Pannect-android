package com.example.bondhu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

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

public class Live extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ListView totalStatusList;
    TextView noUsersText;
    TextView currentUser;
    TextView currentStatusView;
    TextView friend1Score;
    TextView friend2Score;
    TextView friend3Score;
    TextView friend4Score;
    TextView friend5Score;
    TextView friend6Score;

    TextView testing2;
    Button friend1;
    Button friend2;
    Button friend3;
    Button friend4;
    Button friend5;
    Button friend6;
    ArrayList<String> al = new ArrayList<>();
    ArrayList<String> al2 = new ArrayList<>();
    ArrayList<String> friendsArray = new ArrayList<>();
    ArrayList<String> friendsStatusArray = new ArrayList<>();
    ArrayList<String> gifListArray = new ArrayList<>();
    ArrayList<String> totalStatus = new ArrayList<>();
    ArrayList<String> currentUserTotalStatus = new ArrayList<>();
    ArrayList<String> friendsArrayID = new ArrayList<>();

    CountDownTimer timer;

    private boolean rotate = false;
    int totalFriends = 0;
    int totalUsers = 0;
    int counter;
    ProgressDialog pd;
    Button btnLiveStatusData2;
    Button btnLiveStatusSelectAdd;
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

    DrawerLayout drawerLayout;
    NavigationView nav_view;
    Toolbar toolbar;

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
        friend1Score = (TextView)findViewById(R.id.friend1Score);
        friend2Score = (TextView)findViewById(R.id.friend2Score);
        friend3Score = (TextView)findViewById(R.id.friend3Score);
        friend4Score = (TextView)findViewById(R.id.friend4Score);
        friend5Score = (TextView)findViewById(R.id.friend5Score);
        friend6Score = (TextView)findViewById(R.id.friend6Score);
        final List<TextView> friendScoreList = Arrays.asList(friend1Score, friend2Score, friend3Score, friend4Score, friend5Score,friend6Score);

        //testing2 = (TextView)findViewById(R.id.testing2);
        friend1 = (Button )findViewById(R.id.friend1);
        friend2 = (Button)findViewById(R.id.friend2);
        friend3 = (Button)findViewById(R.id.friend3);
        friend4 = (Button)findViewById(R.id.friend4);
        friend5 = (Button)findViewById(R.id.friend5);
        friend6 = (Button)findViewById(R.id.friend6);
        final List<Button> friendBtnList = Arrays.asList(friend1, friend2, friend3, friend4, friend5,friend6);

        FloatingActionButton fabOption1 = findViewById(R.id.fabOption1);
        FloatingActionButton fabOption2 = findViewById(R.id.fabOption2);
        FloatingActionButton fabOption3 = findViewById(R.id.fabOption3);
        FloatingActionButton btncurrentUser = findViewById(R.id.btncurrentUser);
        FloatingActionButton fabCheckScore = findViewById(R.id.fabCheckScore);

        etLiveStatus = findViewById(R.id.etLiveStatus);
        btnLiveStatusData2 = findViewById(R.id.btnLiveStatusData2);
        btnLiveStatusSelectAdd = findViewById(R.id.btnLiveStatusSelectAdd);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        currentStatusView = (TextView)findViewById(R.id.currentStatusView);
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

        //navbar tools
        drawerLayout = findViewById(R.id.drawer_layout);
        nav_view = findViewById(R.id.nav_view);
        toolbar =  findViewById(R.id.toolbar);

        String currentDateandTimeGeneral = new SimpleDateFormat(" yyyy-MM-dd HH:mm").format(new Date());


        statusDbRef = FirebaseDatabase.getInstance().getReference().child("status");
        statusDbRef2 = FirebaseDatabase.getInstance().getReference().child("users");
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users");

        //navbar tools
        nav_view.bringToFront();
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nav_view.setNavigationItemSelectedListener(this);

        //nav_view.setCheckedItem(R.id.nav_home);

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


        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = token;
                        Log.d("----------------------", token);
                        Toast.makeText(Live.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        if(getIntent().getExtras() != null){
            for(String key : getIntent().getExtras().keySet()){
                if (key.equals("title")){
                    testing.setText((getIntent().getExtras().getString(key)));
                }else if(key.equals("message")) {
                    testing2.setText((getIntent().getExtras().getString(key)));
                }
            }
        }

        */

        ////////////////////-----------------construction notification----------------------------------------------------------

        pd = new ProgressDialog(Live.this);
        pd.setMessage("Loading...");
        pd.show();

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
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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






        //realtime updating friends name and status
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("users");
        myRef.addValueEventListener(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
//////////------------working on center status buttons---------
        //status button from center selection FAB
        initShowOut(fabOption1);
        initShowOut(fabOption2);
        initShowOut(fabOption3);

        btncurrentUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotate = rotateFab(view, !rotate);
                if (rotate) {
                    showIn(fabOption1);
                    showIn(fabOption2);
                    showIn(fabOption3);
                } else {
                    showOut(fabOption1);
                    showOut(fabOption2);
                    showOut(fabOption3);
                }
            }
        });

        fabOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Live.this, "option1 clicked", Toast.LENGTH_SHORT).show();
            }
        });
        fabOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Live.this, "option2 clicked", Toast.LENGTH_SHORT).show();
            }
        });
        fabOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Live.this, "option3 clicked", Toast.LENGTH_SHORT).show();
            }
        });
//////////------------working on center status buttons---------


        //////////-----------/////check socre construction button------------------------------
        fabCheckScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query queryCurrentUser = databaseReference.child(UserDetails.id).child("totalStatus").orderByChild("time").limitToLast(5);


                queryCurrentUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        getCurrentUserOldStatus(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                for(int z =1;friendsArrayID.size() +1 >z;z++) {
                    Query query = databaseReference.child(friendsArrayID.get(z)).child("totalStatus").orderByChild("time").limitToLast(5);


                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            getSingleUserOldStatus(dataSnapshot);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    currentUserTotalStatus.retainAll(totalStatus);

                    friendBtnList.get(z).setText("score:" + currentUserTotalStatus.size() + "/5");
                }
            }
        });

        //////////-----------/////check socre construction button------------------------------

        //view user's own status history

        /*
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
        */
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
    //get 10 most recent status of current user
    public void getCurrentUserOldStatus(DataSnapshot dataSnapshot){

        currentUserTotalStatus.clear();
        if (dataSnapshot.exists()) {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                TotalStatus statusT = snapshot.getValue(TotalStatus.class);
                currentUserTotalStatus.add(statusT.status);

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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
        pd.dismiss();
    }
    //redirects to friends setting activity
    public void openNewActivity(){
        Intent intent = new Intent(Live.this, Users.class);
        startActivity(intent);

    }

    //nav toolbar
    @Override
    public void onBackPressed(){

        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                break;
            case R.id.nav_friends:
                Intent intent = new Intent(Live.this, Users.class);
                startActivity(intent);
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent2 = new Intent(Live.this, LoginActivity.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent2);
                finish();
                break;
        }
        return true;
    }

    //FAB button selection functions
    public static void initShowOut(final View v) {
        v.setVisibility(View.GONE);
        v.setTranslationY(v.getHeight());
        v.setAlpha(0f);
    }
    //FAB button selection functions
    public static boolean rotateFab(final View v, boolean rotate) {
        v.animate().setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .rotation(rotate ? 135f : 0f);
        return rotate;
    }
    //FAB button selection functions
    public static void showIn(final View v) {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(0f);
        v.setTranslationY(v.getHeight());
        v.animate()
                .setDuration(200)
                .translationY(0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .alpha(1f)
                .start();
    }
    //FAB button selection functions
    public static void showOut(final View v) {
        v.setVisibility(View.VISIBLE);
        v.setAlpha(1f);
        v.setTranslationY(0);
        v.animate()
                .setDuration(200)
                .translationY(v.getHeight())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        v.setVisibility(View.GONE);
                        super.onAnimationEnd(animation);
                    }
                }).alpha(0f)
                .start();
    }
}