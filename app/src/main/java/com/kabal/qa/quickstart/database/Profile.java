package com.kabal.qa.quickstart.database;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kabal.qa.quickstart.database.models.User;

/**
 * Created by amgoth.naik on 11/20/2017.
 */
public class Profile extends BaseActivity {
    public String uId;
    public ImageView author_image;
    public TextView author_name;
    public TextView author_mail;
    public TextView q_count;
    public TextView q_text;
    public TextView a_count;
    public TextView a_text;
    String uri;
    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.profile);
        //backbuttonstart//
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        //backbuttonend//
        uId=getIntent().getStringExtra("uId");
        author_image = (ImageView) findViewById(R.id.user_profile_photo);
        author_name=(TextView) findViewById(R.id.user_profile_name);
        author_mail=(TextView) findViewById(R.id.user_profile_short_bio);
       // q_count=(TextView) findViewById(R.id.q_count);
        q_text=(TextView) findViewById(R.id.q_text);
        //a_count=(TextView) findViewById(R.id.a_count);
        a_text=(TextView) findViewById(R.id.a_text);
        //count test start
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 User user = dataSnapshot.getValue(User.class);
                uri=user.uri;
                Glide.with(getApplicationContext()).load(user.uri)
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(author_image);
                author_name.setText(user.username);
                author_mail.setText(user.email);
                //q_count.setText("12");
                q_text.setText("Questions Asked "+12);
                //a_count.setText("12");
                a_text.setText("Questions Answered "+10);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        //count test end
        /*author_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog builder = new Dialog(Profile.this);
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        //nothing;
                    }
                });

                ImageView imageView = new ImageView(Profile.this);
                imageView.setImageURI(imageUri);
                builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                builder.show();
            }
        });
*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()== android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}