package com.firebase.firebase_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
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


        // connects to firebase users/uid and gets a value once, does not create more listeners
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


        // creates a listener that looks at the "status" object and is run once
        // and then every time that someone makes an update to it
        Firebase statusRef = firebaseRef.child("status");
        statusRef.addValueEventListener(new ValueEventListener() {
            protected String message;
            protected String senderId;

            @Override
            public void onDataChange(DataSnapshot messageSnapshot) {
                message = messageSnapshot.child("message").getValue().toString();
                senderId = messageSnapshot.child("user").getValue().toString();

                // nested call here takes the id found above and does a one-time
                // call to firebase to lookup the username from the userID
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

        // listen for the send button to be pushed, then make a hashmap and send
        // it to firebase
        updateStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Map<String, String> statusData = new HashMap<>();
//                String message = newStatusField.getText().toString();
//                statusData.put("message", message);
//                statusData.put("user", currentUserId);
//
//                firebaseRef.child("status").setValue(statusData);
//
//                // clear field
//
//                newStatusField.setText("");
//
//                // add new message to archive
//                Map<String, Object> archiveData = new HashMap<>();
//                archiveData.put("user", currentUserId);
//                archiveData.put("message", message);
//                archiveData.put("time", ServerValue.TIMESTAMP);
//                firebaseRef.child("archive").push().setValue(archiveData);

                sendStatusUpdate();
            }
        });

    }

    private void sendStatusUpdate(){
        Map<String, String> statusData = new HashMap<>();
        String message = newStatusField.getText().toString();
        statusData.put("message", message);
        statusData.put("user", currentUserId);

        firebaseRef.child("status").setValue(statusData);

        // clear field

        newStatusField.setText("");

        // add new message to archive
        Map<String, Object> archiveData = new HashMap<>();
        archiveData.put("user", currentUserId);
        archiveData.put("message", message);
        archiveData.put("time", ServerValue.TIMESTAMP);
        firebaseRef.child("archive").push().setValue(archiveData);

    }


}
