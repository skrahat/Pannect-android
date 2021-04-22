package com.example.bondhu;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
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
    TextView friend1;
    TextView friend2;
    TextView friend3;
    TextView friend4;
    TextView friend5;
    ArrayList<String> al = new ArrayList<>();
    ArrayList<String> al2 = new ArrayList<>();
    ArrayList<String> friendsArray = new ArrayList<>();
    ArrayList<String> friendsStatusArray = new ArrayList<>();
    ArrayList<String> gifListArray = new ArrayList<>();
    ArrayList<String> totalStatus = new ArrayList<>();

    int totalFriends = 0;
    int totalUsers = 0;
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
    GifImageView currentStatusGif;
    FloatingActionButton floatingActionButton;
    //FloatingActionButton floatingActionButton2;
    //FloatingActionButton floatingActionButton3;
    //FloatingActionButton floatingActionButton4;
    //FloatingActionButton floatingActionButton5;
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
        friend1 = (TextView)findViewById(R.id.friend1);
        friend2 = (TextView)findViewById(R.id.friend2);
        friend3 = (TextView)findViewById(R.id.friend3);
        friend4 = (TextView)findViewById(R.id.friend4);
        friend5 = (TextView)findViewById(R.id.friend5);
        etLiveStatus = findViewById(R.id.etLiveStatus);
        btnLiveStatusData2 = findViewById(R.id.btnLiveStatusData2);
        btnLiveStatusSelectAdd = findViewById(R.id.btnLiveStatusSelectAdd);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        currentStatusView = (TextView)findViewById(R.id.currentStatusView);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        userT = UserDetails.username;
        clicked = false;
        gifImageView2 = (GifImageView) findViewById(R.id.gifImageView2);
        friend1Gif = (GifImageView) findViewById(R.id.friend1Gif);
        friend2Gif = (GifImageView) findViewById(R.id.friend2Gif);
        friend3Gif = (GifImageView) findViewById(R.id.friend3Gif);
        friend4Gif = (GifImageView) findViewById(R.id.friend4Gif);
        friend5Gif = (GifImageView) findViewById(R.id.friend5Gif);
        currentStatusGif = (GifImageView) findViewById(R.id.currentStatusGif);


        //String currentDateandTime = new SimpleDateFormat("MM-dd HH:mm").format(new Date());

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



        ////fab button for animation testing... temp////////***construction*****////
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!clicked){
                    gifImageView2.setVisibility(View.VISIBLE);
                    gifImageView2.setImageResource(R.drawable.texting);
                    clicked=true;
                }else{

                    gifImageView2.setVisibility(View.VISIBLE);
                    gifImageView2.setImageResource(R.drawable.gym);

                    clicked=false;
                }

            }
        });

        //-------construction total status display----

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users");
        Query query=databaseReference.child(userT).child("totalStatus").orderByChild("time").limitToLast(2);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            totalStatus.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TotalStatus statusT = snapshot.getValue(TotalStatus.class);
                    totalStatus.add(statusT.status);
                    Log.i("status checking", statusT.status);
                }
                adapter.notifyDataSetChanged();
            }
            totalStatusList.setVisibility(View.VISIBLE);

            totalStatusList.setAdapter(new ArrayAdapter<String>(Live.this, android.R.layout.simple_list_item_1, totalStatus));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //-------construction total status display----

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

        //realtime updating friends name and status
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
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
                    Log.i(TAG,w);

                    switch(count+1) {
                        case 1:
                            friend1.setText(friendsArray.get(0)+":\n"+friendsStatusArray.get(0));
                            int id = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(0), null, null);
                            friend1Gif.setImageResource(id);
                            friend1.setVisibility(View.VISIBLE);
                            friend1Gif.setVisibility(View.VISIBLE);

                            break;
                        case 2:
                            friend2.setText(friendsArray.get(1)+":\n"+friendsStatusArray.get(1));
                            int id2 = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(1), null, null);
                            friend2Gif.setImageResource(id2);
                            friend2.setVisibility(View.VISIBLE);
                            friend2Gif.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            friend3.setText(friendsArray.get(2)+":\n"+friendsStatusArray.get(2));
                            int id3 = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(2), null, null);
                            friend3Gif.setImageResource(id3);
                            friend3.setVisibility(View.VISIBLE);
                            friend3Gif.setVisibility(View.VISIBLE);
                            break;
                        case 4:
                            friend4.setText(friendsArray.get(3)+":\n"+friendsStatusArray.get(3));
                            int id4 = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(3), null, null);
                            friend4Gif.setImageResource(id4);
                            friend4.setVisibility(View.VISIBLE);
                            friend4Gif.setVisibility(View.VISIBLE);
                            break;
                        case 5:
                            friend5.setText(friendsArray.get(4)+":\n"+friendsStatusArray.get(4));
                            int id5 = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(4), null, null);
                            friend5Gif.setImageResource(id5);
                            friend5.setVisibility(View.VISIBLE);
                            friend5Gif.setVisibility(View.VISIBLE);
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
                String liveStatus = etLiveStatus.getText().toString();

                reference2.child(userT).child("currentStatus").setValue(liveStatus);
                String url2 = "https://bondhu-2021-default-rtdb.firebaseio.com/users.json";

                String currentDateandTime = new SimpleDateFormat("HH:mm MM-dd").format(new Date());
                reference2.child(userT).child("currentStatus").child("updateTime").setValue(currentDateandTime);

                currentStatusView.setText(liveStatus);
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
                //reference2.child(userT).child("totalStatus").child("updateTime").setValue(currentDateandTime);

                //adding it to totalStatus list with timestamp
                Map<String, String> map = new HashMap<String, String>();
                map.put("time", currentDateandTime);
                map.put("status",updateLiveStatus);
                reference2.child(userT).child("totalStatus").push().setValue(map);

                String url2 = "https://bondhu-2021-default-rtdb.firebaseio.com/users.json";
                int idX = getResources().getIdentifier("com.example.bondhu:drawable/" + updateLiveStatus, null, null);
                currentStatusGif.setImageResource(idX);

                currentStatusView.setText(updateLiveStatus +"\n"+ currentDateandTime);
                UserDetails.currentStatus= updateLiveStatus;
                Toast.makeText(Live.this, "status added successfully", Toast.LENGTH_LONG).show();



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
            noUsersText.setVisibility(View.VISIBLE);
        }
        else{
            noUsersText.setVisibility(View.GONE);

        }

        pd.dismiss();
    }
    //get 10 most recent status


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
                    friend1.setText(friendsArray.get(0)+":\n"+friendsStatusArray.get(0));
                    int id = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(0), null, null);
                    friend1Gif.setImageResource(id);
                    friend1.setVisibility(View.VISIBLE);
                    friend1Gif.setVisibility(View.VISIBLE);

                    break;
                case 2:
                    friend2.setText(friendsArray.get(1)+":\n"+friendsStatusArray.get(1));
                    int id2 = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(1), null, null);
                    friend2Gif.setImageResource(id2);
                    friend2.setVisibility(View.VISIBLE);
                    friend2Gif.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    friend3.setText(friendsArray.get(2)+":\n"+friendsStatusArray.get(2));
                    int id3 = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(2), null, null);
                    friend3Gif.setImageResource(id3);
                    friend3.setVisibility(View.VISIBLE);
                    friend3Gif.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    friend4.setText(friendsArray.get(3)+":\n"+friendsStatusArray.get(3));
                    int id4 = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(3), null, null);
                    friend4Gif.setImageResource(id4);
                    friend4.setVisibility(View.VISIBLE);
                    friend4Gif.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    friend5.setText(friendsArray.get(4)+":\n"+friendsStatusArray.get(4));
                    int id5 = getResources().getIdentifier("com.example.bondhu:drawable/" + friendsStatusArray.get(4), null, null);
                    friend5Gif.setImageResource(id5);
                    friend5.setVisibility(View.VISIBLE);
                    friend5Gif.setVisibility(View.VISIBLE);
                    break;
            }
        } catch (JSONException e) {
        e.printStackTrace();
        }

        pd.dismiss();
    }
    //add status to TotalStatuslist


}