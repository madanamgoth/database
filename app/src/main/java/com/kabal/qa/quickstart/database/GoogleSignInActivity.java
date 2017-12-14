/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kabal.qa.quickstart.database;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kabal.qa.quickstart.database.Util.ConnectivityReceiver;
import com.kabal.qa.quickstart.database.Util.MyApplication;
import com.kabal.qa.quickstart.database.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */
public class GoogleSignInActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener,ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    // private TextView mDetailTextView;
    //by me
    private DatabaseReference mDatabase;
    //  private FirebaseAuth mAuth;
    public String city;
    public String  district;
    public String state;
    public double latitude;
    public double longitude;
    List<Address> addresses = null;
    boolean isConnected=true;
    GPSTracker gps;
    private CoordinatorLayout coordinatorLayout;
    private LocationManager locationManager;
    private LocationListener listener;
    public TextView skipLogin;
    public String click;
    public String notification;
    private static final int PERMISSION_REQUEST_CODE = 200;
    //private View view;
    //by me

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //by me
        isConnected = ConnectivityReceiver.isConnected();
        if(isConnected) {
            //by google
            showProgressDialog();
            setContentView(R.layout.activity_google);
            click=getIntent().getStringExtra("click");
            notification=getIntent().getStringExtra("notification");
            mDatabase = FirebaseDatabase.getInstance().getReference();
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            // [END config_signin]
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            // [START initialize_auth]
            mAuth = FirebaseAuth.getInstance();
            findViewById(R.id.sign_in_button).setOnClickListener(this);
        }else{
            setContentView(R.layout.internet_connection);
            coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                    .coordinatorLayout);
            //android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
            //mActionBar.setDisplayShowTitleEnabled(false);
            //mActionBar.setDisplayShowCustomEnabled(true);
            LayoutInflater mInflater = LayoutInflater.from(this);
            View customView = mInflater.inflate(R.layout.key, null);
           // TextView customTitle = (TextView) customView.findViewById(R.id.app_text);
            /*customTitle.setText(R.string.app_name);
            customTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(GoogleSignInActivity.this, GoogleSignInActivity.class);
                    startActivity(i);
                    finish();
                }
            });*/
           // mActionBar.setCustomView(customView);
        }
        hideProgressDialog();
        //by me
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        isConnected = ConnectivityReceiver.isConnected();
        if(!isConnected) {
            error();
        }else {
            // Check if user is signed in (non-null) and update UI accordingly.
            showProgressDialog();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);
        }
    }
    // [END on_start_check_user]
    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]
    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(GoogleSignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]
    // [START signin]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]
    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            String username = user.getDisplayName();
            String token = FirebaseInstanceId.getInstance().getToken();
            writeNewUser(user.getUid(), username, user.getEmail(), String.valueOf(user.getPhotoUrl()),token);
            //getValue();
            if("Y".equals(notification)){
                Intent i= new Intent(GoogleSignInActivity.this, ReadUserNotification.class);
                i.putExtra("city",city);
                i.putExtra("status","1");
                i.putExtra("delete",0);
                i.putExtra("notification","Y");
                //i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                //finish();
            }else{
                Intent i= new Intent(GoogleSignInActivity.this, MainActivity.class);
                i.putExtra("city",city);
                i.putExtra("status","1");
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
            //by me
        }else if("1".equals(click)){
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        }else {
            //getValue();
            Intent i= new Intent(GoogleSignInActivity.this, MainActivity.class);
            i.putExtra("city","all");
            i.putExtra("status","0");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        }
    }
    //by me
    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
    }
    public void error(){
        setContentView(R.layout.internet_connection);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
       // android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
       // mActionBar.setDisplayShowTitleEnabled(false);
        //mActionBar.setDisplayShowCustomEnabled(true);
        //LayoutInflater mInflater = LayoutInflater.from(this);
        //View customView = mInflater.inflate(R.layout.key, null);
        //TextView customTitle = (TextView) customView.findViewById(R.id.app_text);
        //customTitle.setText(R.string.app_name);
        /*customTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GoogleSignInActivity.this, GoogleSignInActivity.class);
                startActivity(i);
                finish();
            }
        });*/
        // Apply the custom view
        //mActionBar.setCustomView(customView);
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.imageView), "Sorry! Not connected to internet", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    // [START basic_write]
    private void writeNewUser(String userId, String name, String email,String uri,String token) {
        User user = new User(userId,name, email,uri,token,"Y");
        //mDatabase.child("users").child(userId).setValue(user);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
       /* DocumentReference contact = db.collection("users").document(userId);
        contact.update("uid", userId);
        contact.update("username", name);
        contact.update("email", email);
        contact.update("uri", uri);
        contact.update("fcmId", token);
        contact.update("notificationStatus", "Y")
                .addOnSuccessListener(new OnSuccessListener < Void > () {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                });*/

        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
        /*
        Map<String, Object> childUpdates = new HashMap<>();
        //childUpdates.put( "/user-notifications/" + to + "/"+notificationId, postValues);
        childUpdates.put("uid",userId);
        childUpdates.put("username",name);
        childUpdates.put("email",email);
        childUpdates.put("uri",uri);
        childUpdates.put("fcmId",token);
        childUpdates.put("notificationStatus","Y");
        mDatabase.child("users").child(userId).updateChildren(childUpdates);*/
    }
    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }
    //by me

    }
//}