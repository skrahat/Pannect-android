package com.example.bondhu;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Users extends AppCompatActivity {
    ListView friendRequestList;
    ListView friendsList;
    TextView noUsersText;
    TextView searchResult;
    EditText searchUsers;
    String searchUsersByName;
    ArrayList<String> al = new ArrayList<>();
    ArrayList<String> requestArray = new ArrayList<>();
    ArrayList<String> friendsArray = new ArrayList<>();
    int totalUsers = 0;
    int totalRequests = 0;
    int totalFriends = 0;
    String TAG = "TESTING____-----_____";
    boolean userFound=false;
    ProgressDialog pd;
    Button btnLive;
    Button btnSearchUsers;
    Button btnSendRequest;
    Button btnAccept;
    Button btnCancel;
    Button btnRemove;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        friendRequestList = (ListView)findViewById(R.id.friendRequestList);
        friendsList = (ListView)findViewById(R.id.friendsList);
        noUsersText = (TextView)findViewById(R.id.noUsersText);
        searchResult = (TextView)findViewById(R.id.searchResult);
        searchUsers = findViewById(R.id.searchUsers);
        btnLive = (Button) findViewById(R.id.btnLive);
        btnSearchUsers = (Button) findViewById(R.id.btnSearchUsers);
        btnSendRequest = (Button) findViewById(R.id.btnSendRequest);
        btnAccept = (Button) findViewById(R.id.btnAccept);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnRemove = (Button) findViewById(R.id.btnRemove);


        pd = new ProgressDialog(Users.this);
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

        RequestQueue rQueue = Volley.newRequestQueue(Users.this);
        rQueue.add(request);

        //open live activity
        btnLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity();
            }
        });
        //generate friend requests
        //update in realtime
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.addValueEventListener(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requestArray.clear();
        String urlR = "https://bondhu-2021-default-rtdb.firebaseio.com/users/"+UserDetails.username+"/friendRequest.json";

        StringRequest requestR = new StringRequest(Request.Method.GET, urlR, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                requestArray.clear();
                doOnSuccessR(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueueR = Volley.newRequestQueue(Users.this);
        rQueueR.add(requestR);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        ///search users
        btnSearchUsers.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String searchUsersByName = searchUsers.getText().toString();
                        String urlF = "https://bondhu-2021-default-rtdb.firebaseio.com/users.json";

                        StringRequest requestF = new StringRequest(Request.Method.GET, urlF, new Response.Listener<String>(){
                            @Override
                            public void onResponse(String s) {
                                searchUsers(s,searchUsersByName);
                            }
                        },new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                System.out.println("" + volleyError);
                            }
                        });

                        RequestQueue rQueueF = Volley.newRequestQueue(Users.this);
                        rQueueF.add(requestF);
                    }
        });

        //sending request
        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUsersByName = searchUsers.getText().toString();
                String urlF = "https://bondhu-2021-default-rtdb.firebaseio.com/users.json";

                String searchUsersByName = searchResult.getText().toString();
                sendRequest(searchUsersByName);

            }
        });
        // accept/cancel friend request
        friendRequestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                btnAccept.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Firebase reference3 = new Firebase("https://bondhu-2021-default-rtdb.firebaseio.com/users");
                        reference3.child(UserDetails.username).child("friends").child(requestArray.get(position)).setValue(true);
                        reference3.child(requestArray.get(position)).child("friends").child(UserDetails.username).setValue(true);
                        reference3.child(UserDetails.username).child("friendRequest").child(requestArray.get(position)).removeValue();
                        Toast.makeText(Users.this, "Friend Request Accepted!", Toast.LENGTH_LONG).show();
                        btnAccept.setVisibility(View.GONE);
                        btnCancel.setVisibility(View.GONE);

                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Firebase reference3 = new Firebase("https://bondhu-2021-default-rtdb.firebaseio.com/users");
                        reference3.child(UserDetails.username).child("friendRequest").child(requestArray.get(position)).removeValue();
                        Toast.makeText(Users.this, "Friend Request Accepted!", Toast.LENGTH_LONG).show();
                        btnCancel.setVisibility(View.GONE);
                        btnAccept.setVisibility(View.GONE);
                    }
                });

            }
        });
        //remove friend
        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                btnRemove.setVisibility(View.VISIBLE);
                btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Firebase reference3 = new Firebase("https://bondhu-2021-default-rtdb.firebaseio.com/users");
                        reference3.child(UserDetails.username).child("friends").child(friendsArray.get(position)).removeValue();
                        reference3.child(friendsArray.get(position)).child("friends").child(UserDetails.username).removeValue();
                        Toast.makeText(Users.this, "Friend Removed!", Toast.LENGTH_LONG).show();
                        btnRemove.setVisibility(View.GONE);
                    }
                });

            }
        });
        // display friends
        //update in realtime

        myRef.addValueEventListener(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendsArray.clear();
        String urlF = "https://bondhu-2021-default-rtdb.firebaseio.com/users/"+UserDetails.username+"/friends.json";

        StringRequest requestF = new StringRequest(Request.Method.GET, urlF, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                friendsArray.clear();
                doOnSuccessF(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueueF = Volley.newRequestQueue(Users.this);
        rQueueF.add(requestF);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }




    //----------------------------------------------------------------------------------------------------
    public void searchUsers(String s,String searchUsersByName){
        try {
            JSONObject obj = new JSONObject(s);
            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();
                if(key.equals(searchUsersByName)) {
                    userFound = true;
                    btnSendRequest.setVisibility(View.VISIBLE);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(userFound){
            searchResult.setText(searchUsersByName);
            searchUsers.setText("");
        }
        else{
            searchResult.setText("NOT FOUND");
            searchUsers.setText("");
        }
        userFound = false;

        pd.dismiss();
    }

    public void sendRequest(String searchUsersByName){
        Firebase reference2 = new Firebase("https://bondhu-2021-default-rtdb.firebaseio.com/users");
        reference2.child(searchUsersByName).child("friendRequest").child(UserDetails.username).setValue(true);
        Toast.makeText(Users.this, "Friend Request Sent!", Toast.LENGTH_LONG).show();
    }

    //////*********add friend under construction *******///////////////////////////////////////////

    public void openNewActivity(){
        Intent intent = new Intent(this, Live.class);
        startActivity(intent);
    }
    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();

                if(!key.equals(UserDetails.username)) {
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
    //generating array of friend requests
    public void doOnSuccessR(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();

                if(!key.equals("123123")) {
                    requestArray.add(key);
                }

                totalRequests++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalRequests <1){

            friendRequestList.setVisibility(View.GONE);
        }
        else{

            friendRequestList.setVisibility(View.VISIBLE);
            friendRequestList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, requestArray));
        }

        pd.dismiss();
    }
    //generating array of friend
    public void doOnSuccessF(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
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

            friendsList.setVisibility(View.GONE);
        }
        else{

            friendsList.setVisibility(View.VISIBLE);
            friendsList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, friendsArray));
        }

        pd.dismiss();
    }
}