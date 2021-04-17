package com.example.bondhu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Live extends AppCompatActivity {
    ListView usersList;
    ListView usersList2;
    TextView noUsersText;
    TextView currentUser;
    TextView currentStatusView;
    ArrayList<String> al = new ArrayList<>();
    ArrayList<String> al2 = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;
    Button btnLiveStatusData;
    Button btnLiveStatusData2;
    EditText etLiveStatus;
    DatabaseReference statusDbRef;
    Spinner spinnerStatus;
    DatabaseReference statusDbRef2;
    String userT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        //getting UI IDS
        usersList = (ListView)findViewById(R.id.usersList);
        usersList2 = (ListView)findViewById(R.id.usersList2);
        noUsersText = (TextView)findViewById(R.id.noUsersText);
        currentUser = (TextView)findViewById(R.id.currentUser);
        etLiveStatus = findViewById(R.id.etLiveStatus);
        btnLiveStatusData = findViewById(R.id.btnLiveStatusData);
        btnLiveStatusData2 = findViewById(R.id.btnLiveStatusData2);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        currentStatusView = (TextView)findViewById(R.id.currentStatusView);
        userT = UserDetails.username;


        statusDbRef = FirebaseDatabase.getInstance().getReference().child("status");
        statusDbRef2 = FirebaseDatabase.getInstance().getReference().child("users");
        //displaying current username
        currentUser.setText(UserDetails.username);
        currentStatusView.setText(UserDetails.currentStatus);


        // /////////Attach a listener to read the data at our posts reference////////***********construction*****
        Firebase reference2 = new Firebase("https://bondhu-2021-default-rtdb.firebaseio.com/users");

        String url2 = "https://bondhu-2021-default-rtdb.firebaseio.com/users/"+UserDetails.username;
        // Read from the database

        StringRequest request2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                Toast.makeText(Live.this, "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", Toast.LENGTH_LONG).show();
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        ////////***********construction*****
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



        //adding liveStatus button
        btnLiveStatusData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertStatusData();
            }
        });
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

    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);
            String TAG = "livexxxxxxxxx";

            Iterator i = obj.keys();
            String key = "";
            Log.i(TAG, i.toString());

            while(i.hasNext()){
                key = i.next().toString();
                if(!key.equals(UserDetails.username)) {
                    String key2 = obj.getJSONObject(key).getString("currentStatus");
                    al2.add(key2);
                    al.add(key);
                }

                totalUsers++;
            }
            Log.i(TAG, String.valueOf(al2));
            Log.i(TAG, String.valueOf(al));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalUsers <=1){
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.VISIBLE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList2.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
            usersList2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al2));

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
}