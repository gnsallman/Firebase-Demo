package com.firebase.firebase_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.firebase.client.Firebase;

public class StatusActivity extends AppCompatActivity {

    Firebase firebaseRef;
    TextView nameField;
    TextView ageField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

    }
}
