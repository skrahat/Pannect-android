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
import com.google.firebase.firestore.auth.User;

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
    ArrayList<String> requestArrayID = new ArrayList<>();
    ArrayList<String> friendsArray = new ArrayList<>();
    ArrayList<String> friendsArrayID = new ArrayList<>();

    int totalUsers = 0;
    int totalRequests = 0;
    int totalFriends = 0;
    String TAG = "TESTING____-----_____";
    String userFoundID="";
    Boolean userFound=false;
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
        //generate userlist--not very neccessary but includes very important current user name update
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
                requestArrayID.clear();
                String urlR = "https://bondhu-2021-default-rtdb.firebaseio.com/users/"+UserDetails.id+"/friendRequest.json";

        StringRequest requestR = new StringRequest(Request.Method.GET, urlR, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                requestArray.clear();
                requestArrayID.clear();
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
                sendRequest(searchUsersByName,userFoundID);
                Toast.makeText(Users.this, "Friend Request Sent!", Toast.LENGTH_LONG).show();
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
                        reference3.child(UserDetails.id).child("friends").child(requestArray.get(position)).setValue(requestArrayID.get(position));
                        reference3.child(requestArrayID.get(position)).child("friends").child(UserDetails.username).setValue(UserDetails.id);
                        reference3.child(UserDetails.id).child("friendRequest").child(requestArray.get(position)).removeValue();
                        Toast.makeText(Users.this, "Friend Request Accepted!", Toast.LENGTH_LONG).show();
                        btnAccept.setVisibility(View.GONE);
                        btnCancel.setVisibility(View.GONE);

                    }
                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Firebase reference3 = new Firebase("https://bondhu-2021-default-rtdb.firebaseio.com/users");
                        reference3.child(UserDetails.id).child("friendRequest").child(requestArray.get(position)).removeValue();
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
                        reference3.child(UserDetails.id).child("friends").child(friendsArray.get(position)).removeValue();
                        reference3.child(friendsArray.get(position)).child("friends").child(UserDetails.username).removeValue();
                        Toast.makeText(Users.this, "Friend Removed!", Toast.LENGTH_LONG).show();
                        btnRemove.setVisibility(View.GONE);
                    }
                });

            }
        });

        friendsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                UserDetails.chatWith =friendsArray.get(pos);
                startActivity(new Intent(Users.this, Chat.class));

                return true;
            }
        });
        // display friends
        //update in realtime

        myRef.addValueEventListener(new ValueEventListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                friendsArray.clear();
                friendsArrayID.clear();
        String urlF = "https://bondhu-2021-default-rtdb.firebaseio.com/users/"+UserDetails.id+"/friends.json";

        StringRequest requestF = new StringRequest(Request.Method.GET, urlF, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                friendsArray.clear();
                friendsArrayID.clear();
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
                String key2 = obj.getJSONObject(key).getString("userName");
                if(key2.equals(searchUsersByName)) {
                    userFoundID = obj.getJSONObject(key).getString("id");
                    userFound =true;
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

    public void sendRequest(String searchUsersByName,String userFoundID){
        Firebase reference2 = new Firebase("https://bondhu-2021-default-rtdb.firebaseio.com/users");
        reference2.child(userFoundID).child("friendRequest").child(UserDetails.username).setValue(UserDetails.id);
    }


    public void openNewActivity(){
        Intent intent = new Intent(Users.this, Live.class);
        startActivity(intent);

    }
    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();
                String key2 = obj.getJSONObject(key).getString("userName");
                if(!key2.equals(UserDetails.id)) {
                    al.add(key2);
                }
                if(key2.equals(UserDetails.id)) {
                    UserDetails.username=obj.getJSONObject(key).getString("userName");
                }
                totalUsers++;

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalUsers <1){
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
                String key2 =obj.getString(key);
                if(!key.equals("123123")) {
                    requestArray.add(key);
                    requestArrayID.add(key2);
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
                String key2 =obj.getString(key);
                if(!key.equals("123123")) {
                    friendsArray.add(key);
                    friendsArrayID.add(key2);
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