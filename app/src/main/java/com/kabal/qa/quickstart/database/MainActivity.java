/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kabal.qa.quickstart.database;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kabal.qa.quickstart.database.Util.ConnectivityReceiver;
import com.kabal.qa.quickstart.database.Util.MyApplication;
import com.kabal.qa.quickstart.database.fragment.MyPostsFragment;
import com.kabal.qa.quickstart.database.fragment.MyTopPostsFragment;
import com.kabal.qa.quickstart.database.fragment.NotificationFragment;
import com.kabal.qa.quickstart.database.fragment.RecentPostsFragment;
import com.kabal.qa.quickstart.database.models.Keys;

public class  MainActivity extends BaseActivity implements  ConnectivityReceiver.ConnectivityReceiverListener,View.OnClickListener ,GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "MainActivity";
    public static final String test="test";

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    RecentPostsFragment rp;
    MyTopPostsFragment  mtp;
    MyPostsFragment mp;
    boolean isConnected=true;
    private CoordinatorLayout coordinatorLayout;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    public String status;
    AlertDialog alertDialog1;
    CharSequence[] values = {" Ask Question "," Create Poll "};
    public String cTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        status=getIntent().getStringExtra("status");
        cTag=getIntent().getStringExtra("cTag");
        if("".equals(cTag) || cTag==null){
            cTag="ALL";
            //cTag="test";
        }

        if("1".equals(status)){

            setContentView(R.layout.activity_main);
        }else {
            setContentView(R.layout.activity_main_without_login);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        showProgressDialog();
/*        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowCustomEnabled(true);*/
        LayoutInflater mInflater = LayoutInflater.from(this);
        View customView = mInflater.inflate(R.layout.key, null);
        TextView customTitle = (TextView) customView.findViewById(R.id.app_text);
        customTitle.setText(R.string.app_name);
        customTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    showProgressDialog();
                    Intent i = new Intent(MainActivity.this, GoogleSignInActivity.class);
                    startActivity(i);
                    finish();
            }
        });
        // Apply the custom view
       /* mActionBar.setCustomView(customView);*/

        isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            //by google
            //by me
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            mAuth = FirebaseAuth.getInstance();
            //by me
            //by google
            status=getIntent().getStringExtra("status");
            TextView mTitleTextView = (TextView) customView.findViewById(R.id.balance_text);
           // mTitleTextView.setText("city");

            mTitleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, SelectKeys.class);
                    i.putExtra("status",status);
                    startActivity(i);
                }
            });

          /*  mActionBar.setCustomView(customView);
            mActionBar.setDisplayShowCustomEnabled(true);*/

            //end
            //start to set city
            // Create the adapter that will return a fragment for each section
            if("1".equals(status)){
                rp = new RecentPostsFragment();
                Bundle args = new Bundle();
                args.putString("cTag",cTag);
                args.putString("status",status);
                args.putString("delete","0");
                rp.setArguments(args);
                //2nd
                mtp = new MyTopPostsFragment();
                Bundle args2 = new Bundle();
                args2.putString("cTag",cTag);
                args2.putString("status",status);
                args2.putString("delete","0");
                mtp.setArguments(args2);

                //3rd
                mp = new MyPostsFragment();
                Bundle args3 = new Bundle();
                args3.putString("cTag",cTag);
                args3.putString("status",status);
                args3.putString("delete","1");
                mp.setArguments(args3);

                mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

                    private final Fragment[] mFragments = new Fragment[]{
                            rp,
                            mtp,
                            mp,
                    };
                    private final String[] mFragmentNames = new String[]{
                            getString(R.string.heading_recent),
                            getString(R.string.heading_my_top_posts),
                            getString(R.string.heading_my_posts)
                    };

                    @Override
                    public Fragment getItem(int position) {
                        return mFragments[position];
                    }

                    @Override
                    public int getCount() {
                        return mFragments.length;
                    }

                    @Override
                    public CharSequence getPageTitle(int position) {
                        return mFragmentNames[position];
                    }
                };

            }else{
                rp = new RecentPostsFragment();
                Bundle args = new Bundle();
                args.putString("cTag",cTag);
                args.putString("status",status);
                args.putString("delete","0");
                rp.setArguments(args);

                mtp = new MyTopPostsFragment();
                Bundle args2 = new Bundle();
                args2.putString("delete","0");
                args2.putString("cTag",cTag);
                args2.putString("status",status);
                mtp.setArguments(args2);

                mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

                    private final Fragment[] mFragments = new Fragment[]{
                            rp,
                            mtp,
                    };
                    private final String[] mFragmentNames = new String[]{
                            getString(R.string.heading_recent),
                            getString(R.string.heading_my_top_posts)
                    };
                    @Override
                    public Fragment getItem(int position) {
                        return mFragments[position];
                    }

                    @Override
                    public int getCount() {
                        return mFragments.length;
                    }

                    @Override
                    public CharSequence getPageTitle(int position) {
                        return mFragmentNames[position];
                    }
                };
            }
            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mPagerAdapter);
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);

            if(! "1".equals(status)) {
                // Button launches NewPostActivity
                findViewById(R.id.fab_new_post).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isConnected = ConnectivityReceiver.isConnected();
                        if (!isConnected) {
                            showError();
                        } else {
                            hideProgressDialog();
                            Intent i = new Intent(MainActivity.this, GoogleSignInActivity.class);
                            i.putExtra("click", "1");
                            startActivity(i);
                        }
                    }
                });
            }
            //bottom naviga
            if("1".equals(status)) {
                BottomNavigationView bottomNavigationView = (BottomNavigationView)
                        findViewById(R.id.home_bottom_navigation);
                bottomNavigationView.setOnNavigationItemSelectedListener(
                        new BottomNavigationView.OnNavigationItemSelectedListener() {
                            @Override
                            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.home_action_favorites:
                                        //new post
                                        isConnected = ConnectivityReceiver.isConnected();
                                        if (!isConnected) {
                                            showError();
                                        } else {
                                            if ("1".equals(status)) {
                                                //add choice for q and p start
                                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                                builder.setTitle("Select Your Choice");
                                                builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

                                                    public void onClick(DialogInterface dialog, int item) {

                                                        switch(item)
                                                        {
                                                            case 0:
                                                                Intent i = new Intent(MainActivity.this, NewPostActivity.class);
                                                                i.putExtra("status",status);
                                                                startActivity(i);
                                                                //Toast.makeText(MainActivity.this, "First Item Clicked", Toast.LENGTH_LONG).show();
                                                                break;
                                                            case 1:
                                                                Intent p = new Intent(MainActivity.this, CreatePoll.class);
                                                                startActivity(p);
                                                                break;
                                                        }
                                                        alertDialog1.dismiss();
                                                    }
                                                });
                                                alertDialog1 = builder.create();
                                                alertDialog1.show();
                                            } else {
                                                hideProgressDialog();
                                                Intent i = new Intent(MainActivity.this, GoogleSignInActivity.class);
                                                i.putExtra("click", "1");
                                                startActivity(i);
                                            }
                                        }
                                        //new post
                                        break;
                                    case R.id.home_action_schedules:
                                        Intent i = new Intent(MainActivity.this, ReadUserNotification.class);
                                        i.putExtra("status",status);
                                        startActivity(i);
                                        break;
                                    case R.id.home_action_music:
                                        //profile
                                        Intent p = new Intent(MainActivity.this, Profile.class);
                                        p.putExtra("uId",getUId());
                                        startActivity(p);
                                        break;
                                }
                                return false;
                            }
                        });
            }
            //bottom navigation
        }else{
            showError();
        }
        hideProgressDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if("1".equals(status)){
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }else{
            return false;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if(isConnected){
            if (i == R.id.action_logout) {
                //FirebaseAuth.getInstance().signOut();
                FirebaseUser user = mAuth.getCurrentUser();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("users").child(user.getUid()).child("fcmId").setValue("null");
                mAuth.signOut();
                // Google sign out
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(@NonNull Status status) {
                                //need some code
                            }
                        });

                Intent intent = new Intent(this, GoogleSignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return true;
            } else {
                return super.onOptionsItemSelected(item);
            }
        }
        else{
            showError();
            return false;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }
    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
    }
    public void showError(){
        setContentView(R.layout.internet_connection);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.imageView), "Sorry! Not connected to internet", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    public static String getUId(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
