package com.example.bondhu;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class Live extends AppCompatActivity {
    ListView usersList;
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
    int totalFriends = 0;
    int totalUsers = 0;
    ProgressDialog pd;
    Button btnLiveStatusData2;
    EditText etLiveStatus;
    DatabaseReference statusDbRef;
    Spinner spinnerStatus;
    DatabaseReference statusDbRef2;
    String userT;
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
        noUsersText = (TextView)findViewById(R.id.noUsersText);
        currentUser = (TextView)findViewById(R.id.currentUser);
        friend1 = (TextView)findViewById(R.id.friend1);
        friend2 = (TextView)findViewById(R.id.friend2);
        friend3 = (TextView)findViewById(R.id.friend3);
        friend4 = (TextView)findViewById(R.id.friend4);
        friend5 = (TextView)findViewById(R.id.friend5);
        etLiveStatus = findViewById(R.id.etLiveStatus);
        btnLiveStatusData2 = findViewById(R.id.btnLiveStatusData2);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        currentStatusView = (TextView)findViewById(R.id.currentStatusView);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        //floatingActionButton2 = findViewById(R.id.floatingActionButton2);
        //floatingActionButton3 = findViewById(R.id.floatingActionButton3);
        //floatingActionButton4 = findViewById(R.id.floatingActionButton4);
        //floatingActionButton5 = findViewById(R.id.floatingActionButton5);
        userT = UserDetails.username;
        clicked = false;
        GifImageView gifImageView2 = (GifImageView) findViewById(R.id.gifImageView2);


        statusDbRef = FirebaseDatabase.getInstance().getReference().child("status");
        statusDbRef2 = FirebaseDatabase.getInstance().getReference().child("users");
        //displaying current username
        currentUser.setText(UserDetails.username);
        currentStatusView.setText(UserDetails.currentStatus);

        //////spinner values added from array

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.statuses, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerStatus.setAdapter(adapter);

///////////////////---------------------construction for status displays--//////////////////////


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
                            friend1.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            friend2.setText(friendsArray.get(1)+":\n"+friendsStatusArray.get(1));
                            friend2.setVisibility(View.VISIBLE);
                            break;
                        case 3:
                            friend3.setText(friendsArray.get(2)+":\n"+friendsStatusArray.get(2));
                            friend3.setVisibility(View.VISIBLE);
                            break;
                        case 4:
                            friend4.setText(friendsArray.get(3)+":\n"+friendsStatusArray.get(3));
                            friend4.setVisibility(View.VISIBLE);
                            break;
                        case 5:
                            friend5.setText(friendsArray.get(4)+":\n"+friendsStatusArray.get(4));
                            friend5.setVisibility(View.VISIBLE);
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
///////////////////---------------------construction for status displays--//////////////////////

        //update Current Status
        btnLiveStatusData2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase reference2 = new Firebase("https://bondhu-2021-default-rtdb.firebaseio.com/users");
                String liveStatus = etLiveStatus.getText().toString();

                reference2.child(userT).child("currentStatus").setValue(liveStatus);
                String url2 = "https://bondhu-2021-default-rtdb.firebaseio.com/users.json";

                currentStatusView.setText(liveStatus);
                UserDetails.currentStatus= liveStatus;
                Toast.makeText(Live.this, "status added successfully", Toast.LENGTH_LONG).show();
                etLiveStatus.setText("");


            }});



        pd = new ProgressDialog(Live.this);
        pd.setMessage("Loading...");
        pd.show();

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
    //adding liveStatus in database
    private void insertStatusData(){
        String liveStatus = etLiveStatus.getText().toString();

        Status status = new Status(liveStatus,UserDetails.username, UserDetails.currentStatus);
        statusDbRef.push().setValue(status);

        Toast.makeText(Live.this,"status updated",Toast.LENGTH_SHORT).show();
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

            //friendsList.setVisibility(View.GONE);
        }
        else{

            //friendsList.setVisibility(View.VISIBLE);
            //friendsList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, friendsArray));
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

                    //String w =String.valueOf(friendsArray.size());
                    //Log.i(TAG,w);
                    if(key.equals(friendsArray.get(countX))) {
                        String key2 = obj.getJSONObject(key).getString("currentStatus");
                        friendsStatusArray.add(key2);

                        //Log.i(TAG,friendsStatusArray.get(countX));
                        //String z =String.valueOf(countX);
                        //Log.i(TAG,z);
                }
                    countX++;
                }
            }
            for(int z =1;friendsArray.size() +1 >z;z++)
            switch(z) {
                case 1:
                    friend1.setText(friendsArray.get(0)+":\n"+friendsStatusArray.get(0));
                    friend1.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    friend2.setText(friendsArray.get(1)+":\n"+friendsStatusArray.get(1));
                    friend2.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    friend3.setText(friendsArray.get(2)+":\n"+friendsStatusArray.get(2));
                    friend3.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    friend4.setText(friendsArray.get(3)+":\n"+friendsStatusArray.get(3));
                    friend4.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    friend5.setText(friendsArray.get(4)+":\n"+friendsStatusArray.get(4));
                    friend5.setVisibility(View.VISIBLE);
                    break;
            }
        } catch (JSONException e) {
        e.printStackTrace();
        }

        pd.dismiss();
    }

}