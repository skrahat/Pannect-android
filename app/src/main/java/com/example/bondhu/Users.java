package com.example.bondhu;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Users extends AppCompatActivity {
    ListView usersList;
    ListView friendRequestList;
    TextView noUsersText;
    TextView searchResult;
    EditText searchUsers;
    String searchUsersByName;
    ArrayList<String> al = new ArrayList<>();
    ArrayList<String> requestArray = new ArrayList<>();
    int totalUsers = 0;
    int totalRequests = 0;
    String TAB = "TESTING____-----_____";
    boolean userFound=false;
    ProgressDialog pd;
    Button button;
    Button btnSearchUsers;
    Button btnSendRequest;
    Button btnAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        usersList = (ListView)findViewById(R.id.usersList);
        friendRequestList = (ListView)findViewById(R.id.friendRequestList);
        noUsersText = (TextView)findViewById(R.id.noUsersText);
        searchResult = (TextView)findViewById(R.id.searchResult);
        searchUsers = findViewById(R.id.searchUsers);
        button = (Button) findViewById(R.id.button);
        btnSearchUsers = (Button) findViewById(R.id.btnSearchUsers);
        btnSendRequest = (Button) findViewById(R.id.btnSendRequest);
        btnAccept = (Button) findViewById(R.id.btnAccept);


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

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = al.get(position);
                startActivity(new Intent(Users.this, Chat.class));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity();
            }
        });
        //////*********add friend under construction *******///////////////////////////////////////////
        //generate friend requests
        String urlR = "https://bondhu-2021-default-rtdb.firebaseio.com/users/"+UserDetails.username+"/friendRequest.json";

        StringRequest requestR = new StringRequest(Request.Method.GET, urlR, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
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
        friendRequestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                btnAccept.setVisibility(View.VISIBLE);
                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAB, requestArray.get(position));
                        Firebase reference3 = new Firebase("https://bondhu-2021-default-rtdb.firebaseio.com/users");
                        reference3.child(UserDetails.username).child("friends").child(requestArray.get(position)).setValue(true);
                        reference3.child(UserDetails.username).child("friendRequest").child(requestArray.get(position)).removeValue();
                        Toast.makeText(Users.this, "Friend Request Accepted!", Toast.LENGTH_LONG).show();
                        btnAccept.setVisibility(View.GONE);
                    }
                });

            }
        });
        // accept friend request

        //////*********add friend under construction *******///////////////////////////////////////////
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
            usersList.setVisibility(View.GONE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
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
}