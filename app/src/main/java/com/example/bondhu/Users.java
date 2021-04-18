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
    ListView friendList;
    TextView noUsersText;
    TextView searchResult;
    EditText searchUsers;
    String searchUsersByName;
    ArrayList<String> al = new ArrayList<>();
    ArrayList<String> al2 = new ArrayList<>();
    int totalUsers = 0;
    boolean userFound=false;
    ProgressDialog pd;
    Button button;
    Button btnSearchUsers;
    Button btnSendRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        usersList = (ListView)findViewById(R.id.usersList);
        friendList = (ListView)findViewById(R.id.friendList);
        noUsersText = (TextView)findViewById(R.id.noUsersText);
        searchResult = (TextView)findViewById(R.id.searchResult);
        searchUsers = findViewById(R.id.searchUsers);
        button = (Button) findViewById(R.id.button);
        btnSearchUsers = (Button) findViewById(R.id.btnSearchUsers);
        btnSendRequest = (Button) findViewById(R.id.btnSendRequest);


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
        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUsersByName = searchUsers.getText().toString();
                String urlF = "https://bondhu-2021-default-rtdb.firebaseio.com/users.json";

                StringRequest requestF = new StringRequest(Request.Method.GET, urlF, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String s) {
                        sendRequest(s,searchUsersByName);
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
        //////*********add friend under construction *******///////////////////////////////////////////
    }
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

    public void sendRequest(String s,String searchUsersByName){
        Firebase reference2 = new Firebase("https://bondhu-2021-default-rtdb.firebaseio.com/users");
        String TAG = "AAAXXXAAA1111-----1111";
        Log.i(TAG, searchUsersByName);
        reference2.child(searchUsersByName).child("friendRequest").setValue(searchUsersByName);

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
}