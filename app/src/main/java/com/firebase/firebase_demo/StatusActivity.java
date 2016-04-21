package com.firebase.firebase_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class StatusActivity extends AppCompatActivity {

    private static final String FIREBASE_URL = "https://cmsc436-firebasedemo.firebaseio.com";
    private Firebase firebaseRef;
    private TextView nameField;
    private TextView ageField;
    private TextView senderField;
    private TextView currentStatusField;
    private TextView newStatusField;
    private Button updateStatusButton;

    private String currentUserId;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        firebaseRef = new Firebase(FIREBASE_URL);
        currentUserId = firebaseRef.getAuth().getUid();

        nameField = (TextView)findViewById(R.id.textView_name);
        ageField = (TextView)findViewById(R.id.textView_age);
        senderField = (TextView)findViewById(R.id.textView_sender);
        currentStatusField = (TextView)findViewById(R.id.textView_last_status);
        newStatusField = (EditText)findViewById(R.id.editText_new_status);
        updateStatusButton = (Button)findViewById(R.id.button_send);


        final Firebase usersRef = firebaseRef.child("users");

        usersRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                nameField.setText(dataSnapshot.child("name").getValue().toString());
                ageField.setText(dataSnapshot.child("age").getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Firebase statusRef = firebaseRef.child("status");
        statusRef.addValueEventListener(new ValueEventListener() {
            protected String message;
            @Override
            public void onDataChange(DataSnapshot messageSnapshot) {
                message = messageSnapshot.child("message").getValue().toString();
                String senderId = messageSnapshot.child("user").getValue().toString();
                usersRef.child(senderId).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot userSnapshot) {
                        String senderName = userSnapshot.getValue().toString();
                        currentStatusField.setText(message);
                        senderField.setText(senderName);
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });



            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        updateStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> statusData = new HashMap<>();
                statusData.put("message", newStatusField.getText().toString());
                statusData.put("user", currentUserId);

                firebaseRef.child("status").setValue(statusData);
            }
        });

    }


}
