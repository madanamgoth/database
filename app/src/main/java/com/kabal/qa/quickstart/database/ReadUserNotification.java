package com.kabal.qa.quickstart.database;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.kabal.qa.quickstart.database.fragment.NotificationFragment;

/**
 * Created by amgoth.naik on 11/4/2017.
 */

public class ReadUserNotification extends BaseActivity{
    private static final String TAG = "ReadUserNotification";
    NotificationFragment nfp;
    private FragmentPagerAdapter nmPagerAdapter;
    private ViewPager nmViewPager;
    String city;
    String status;
    String delete;
    String notification;
    String state;
    public ReadUserNotification() {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_main);
        city=getIntent().getStringExtra("city");
        status=getIntent().getStringExtra("status");
        delete=getIntent().getStringExtra("delete");
        state=getIntent().getStringExtra("state");
        notification=getIntent().getStringExtra("notification");

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //notification
        nfp = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString("city", city);
        args.putString("status",status);
        args.putString("delete",delete);
        args.putString("state",state);
        nfp.setArguments(args);

        nmPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            private final Fragment[] nmFragments = new Fragment[]{
                    nfp,
            };
            private final String[] nmFragmentNames = new String[]{
                    getString(R.string.notification),
            };

            @Override
            public Fragment getItem(int position) {
                return nmFragments[position];
            }

            @Override
            public int getCount() {
                return nmFragments.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return nmFragmentNames[position];
            }
        };
        nmViewPager = (ViewPager) findViewById(R.id.notification_container);
        nmViewPager.setAdapter(nmPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.notification_tabs);
        tabLayout.setupWithViewPager(nmViewPager);

    }
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()== android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if("Y".equals(notification)){
            switch (item.getItemId()) {
                case android.R.id.home:
                    onBackPressed();
                    return true;
            }
            return false;
        }else{
            if(item.getItemId()== android.R.id.home)
                finish();
            return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ReadUserNotification.this, GoogleSignInActivity.class);
        startActivity(intent);
        finish();
    }
}