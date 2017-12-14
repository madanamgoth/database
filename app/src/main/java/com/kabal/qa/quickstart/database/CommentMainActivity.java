package com.kabal.qa.quickstart.database;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kabal.qa.quickstart.database.Util.ConnectivityReceiver;
import com.kabal.qa.quickstart.database.Util.MyApplication;
import com.kabal.qa.quickstart.database.fragment.MyTopCommentFragment;
import com.kabal.qa.quickstart.database.fragment.RecentCommentFragment;
import com.kabal.qa.quickstart.database.models.Comments;
import com.kabal.qa.quickstart.database.models.ParcelablePost;
import com.kabal.qa.quickstart.database.models.Post;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

/**
 * Created by amgoth.naik on 10/13/2017.
 */

public class CommentMainActivity extends BaseActivity implements  ConnectivityReceiver.ConnectivityReceiverListener,View.OnClickListener{
    private static final String TAG = "CommentMainActivity";
    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    public String city;
    RecentCommentFragment rp;
    MyTopCommentFragment mtp;
    boolean isConnected=true;
    private CoordinatorLayout coordinatorLayout;
    public String status;
    public static final String EXTRA_POST_KEY = "post_key";
    private String mPostKey;
    Button answer_image;
    String postBody;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    //public ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        answer_image=(Button) findViewById(R.id.answer_image);
        isConnected = ConnectivityReceiver.isConnected();
      //  mProgressBar = (ProgressBar) findViewById(R.id.comment_progressbar_main);
        if (isConnected) {

            status=getIntent().getStringExtra("status");
            postBody=getIntent().getStringExtra("postBody");
            mPostKey = getIntent().getStringExtra("post_key");
            showProgressDialog();
          //  mProgressBar.setVisibility(RecyclerView.VISIBLE);
           // if(postBody==null){
                DocumentReference docRef = db.collection("post").document(mPostKey);
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        final Post post = documentSnapshot.toObject(Post.class);
                        toolbar.setTitle(Html.fromHtml(post.body).toString());

                      //  mProgressBar.setVisibility(RecyclerView.GONE);
                        answer_image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(CommentMainActivity.this, NewCommentActivity.class);
                                i.putExtra("post_key",mPostKey);
                                i.putExtra("postBody", postBody);
                                startActivity(i);
                            }
                        });
                        hideProgressDialog();
                        if("1".equals(status)){
                            rp = new RecentCommentFragment();
                            Bundle args = new Bundle();
                            args.putString("status",status);
                            args.putString("post_key",mPostKey);
                            args.putString("postBody", postBody);
                            rp.setArguments(args);
                            //2nd
                            mtp = new MyTopCommentFragment();
                            Bundle args2 = new Bundle();
                            args2.putString("status",status);
                            args2.putString("post_key",mPostKey);
                            args2.putString("postBody", postBody);
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

                        }else{
                            rp = new RecentCommentFragment();
                            Bundle args = new Bundle();
                            args.putString("status",status);
                            args.putString("post_key",mPostKey);
                            args.putString("postBody", postBody);
                            rp.setArguments(args);

                            mtp = new MyTopCommentFragment();
                            Bundle args2 = new Bundle();
                            args2.putString("status",status);
                            args2.putString("post_key",mPostKey);
                            args2.putString("postBody", postBody);
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
                        mViewPager = (ViewPager) findViewById(R.id.comment_container);
                        mViewPager.setAdapter(mPagerAdapter);
                        TabLayout tabLayout = (TabLayout) findViewById(R.id.comment_tabs);
                        tabLayout.setupWithViewPager(mViewPager);
                    }
                });
            /*}else {
                toolbar.setTitle(Html.fromHtml(postBody).toString());
            }*/


        }else{
            showError();
        }
        //backbuttonstart//
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        //backbuttonend//
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()== android.R.id.home)

            finish();
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onClick(View view) {

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
}
