package com.firebase.firebase_demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * A login screen that offers login via email/password through Firebase.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String FIREBASE_URL = "https://cmsc436-firebasedemo.firebaseio.com";
    private Firebase firebaseRef;

    private EditText emailInput;
    private EditText passwordInput;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        firebaseRef = new Firebase(FIREBASE_URL);

        emailInput = (EditText) findViewById(R.id.editText_email);
        passwordInput = (EditText) findViewById(R.id.editText_password);

        passwordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptFirebaseLogin();
                }
                return false;
            }
        });

        signInButton = (Button) findViewById(R.id.button_sign_in);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide the soft keyboard on button click
                // may be a better way to do this or may not be needed at all
                // InputMethodManager kbm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // kbm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                String userEmail = emailInput.getText().toString();
                String userPassword = passwordInput.getText().toString();

                attemptFirebaseLogin();
            }
        });

    }

    private void attemptFirebaseLogin(){
        String email = this.emailInput.getText().toString();
        String password = this.passwordInput.getText().toString();


        firebaseRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {

            @Override
            public void onAuthenticated(AuthData authData) {
                startActivity(new Intent(LoginActivity.this, StatusActivity.class));
                //LoginActivity.this.finish();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // clear the text fields if wrong
                emailInput.setText("");
                passwordInput.setText("");
            }
        });
    }
}
