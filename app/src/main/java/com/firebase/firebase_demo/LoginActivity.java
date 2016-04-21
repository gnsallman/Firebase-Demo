package com.firebase.firebase_demo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password through Firebase.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String FIREBASE_URL = "https://436firebasedemo.firebaseio.com";
    private Firebase squadPayFBRef;

    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Firebase.setAndroidContext(this);

        setContentView(R.layout.activity_login);

        squadPayFBRef = new Firebase(FIREBASE_URL);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager kbm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                // this line below apparently will throw exceptions, the comment tells android to shutup about it for now
                //noinspection ConstantConditions
                kbm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                squadPayFBRef.authWithPassword(email.getText().toString(), password.getText().toString(), new Firebase.AuthResultHandler() {
                    Snackbar loginSnackbar = Snackbar.make(findViewById(android.R.id.content), "Invalid Login", Snackbar.LENGTH_SHORT);

                    @Override
                    public void onAuthenticated(AuthData authData) {
                        loginSnackbar.setText("Login Successful");
                        loginSnackbar.setCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar s, int event) {
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                LoginActivity.this.finish();
                            }
                        });
                        loginSnackbar.show();
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        loginSnackbar.setCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar s, int event) {
                                email.setText("");
                                password.setText("");
                            }
                        });
                        loginSnackbar.show();
                    }
                });
            }
        });

    }
}