package com.kabal.qa.quickstart.database;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.kabal.qa.quickstart.database.models.ParcelablePost;
import com.kabal.qa.quickstart.database.models.Post;
import com.kabal.qa.quickstart.database.models.User;
import com.kabal.qa.quickstart.database.models.UserNotification;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by amgoth.naik on 10/16/2017.
 */

public class FullQuestion extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "FullQuestion";
    ImageView post_author_photo;
    TextView post_author;
    TextView time_date;
    TextView post_title;
    TextView post_body;
    TextView answer_number;
   // ImageView full_star;
    TextView full_post_num_stars;
    String status;
    String key;
    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_item_post);
        final ParcelablePost parcelablePost = (ParcelablePost) getIntent().getParcelableExtra("parcel_data");
        post_author_photo=(ImageView) findViewById(R.id.post_author_photo);
        post_author=(TextView) findViewById(R.id.post_author);
        time_date=(TextView)findViewById(R.id.time_date);
        post_title=(TextView)findViewById(R.id.post_title);
        post_body=(TextView) findViewById(R.id.post_body);
        answer_number=(TextView)findViewById(R.id.answer_number);
       // full_star=(ImageView) findViewById(R.id.full_star);
       // full_post_num_stars=(TextView)findViewById(R.id.full_post_num_stars);
        status=getIntent().getStringExtra("status");
        key=getIntent().getStringExtra("key");
        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]
        //back press start
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        //back press end

//onclick start
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(parcelablePost.city).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Post post = dataSnapshot.getValue(Post.class);
                /*if("1".equals(status)){
                    if (post.stars.containsKey(getUid())) {
                        full_star.setImageResource(R.drawable.ic_toggle_star_24);
                    } else {
                        full_star.setImageResource(R.drawable.ic_toggle_star_outline_24);
                    }
                }else{
                    full_star.setImageResource(R.drawable.ic_toggle_star_outline_24);
                }
                full_post_num_stars.setText(String.valueOf(post.starCount));
*/
                //onclick on star
                /*full_star.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {
                        //star clicked
                        if("1".equals(status)){
                            // Need to write to both places the post is stored
                            DatabaseReference globalPostRef = mDatabase.child(post.city).child(key);
                            DatabaseReference userPostRef = mDatabase.child("user-posts").child(post.uid).child(key);
                            // Run two transactions

                            onStarClicked(globalPostRef,"1",key);
                            onStarClicked(userPostRef,"2",key);
                            //send notification.
                            Intent i = new Intent(getBaseContext(), FullQuestion.class);

                            //send notification.
                        }else{
                            Intent i = new Intent(getBaseContext(), GoogleSignInActivity.class);
                            i.putExtra("click","1");
                            startActivity(i);
                        }

                    }
                });*/
                //onclick on star
                //post_title.setText(post.title);
                //if(1==post.postCheck) {
                    //post_author.setText(post.authoranonymous);
                //}else {
                    post_author.setText(post.author);
                //}
                post_body.setText(post.body);

                //date.setText(String.valueOf(post.date));
                time_date.setText(DateUtils.getRelativeTimeSpanString(post.date, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));
                //DAY_IN_MILLIS
                answer_number.setText(String.valueOf(post.answerCount)+ " Answers");
                //if(1==post.postCheck) {
                  //  post_author_photo.setImageResource(R.drawable.ic_action_account_circle_40);
               // }else{
                    Glide.with(FullQuestion.this).load(post.purl)
                            .thumbnail(0.5f)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(post_author_photo);
                //}
                //on click listner end
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    public void onClick(View view) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()== android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }


    // [START post_stars_transaction]
    private void onStarClicked(DatabaseReference postRef,final String i,final String postId) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post p = mutableData.getValue(Post.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.stars.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    p.starCount = p.starCount - 1;
                    p.trendCount=p.trendCount - 1;
                    p.stars.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    p.starCount = p.starCount + 1;
                    p.trendCount=p.trendCount + 1;
                    //p.stars.put(getUid(), true);
                    //send notification start
                    if("1".equals(i)) {
                        String title = getDisplayName() + "  Liked your Question Posted in  ";
                        String body = "Question: " + p.body;
                        //HandlerBackGround handlerBackGround=new HandlerBackGround();
                        prepareNotification(title, body, getUid(), p.uid, "",postId);
                    }
                    //send notification end
                }
                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }
    public String getDisplayName(){
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }



    public void prepareNotification( String title,  String body,  String from, String to,  String city,String postId){
        final String  title1 =title;
        final String body1=body;
        final String from1=from;
        final String city1=city;
        final String to1=to;
        final String postId1=postId;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(to).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                UserNotification userNotification= new UserNotification(title1,body1,"deeplink",user.fcmId,System.currentTimeMillis(),"Y",from1,city1,postId1,to1,postId1,"Y");
                writeNotification(userNotification,to1);
                final String fcmId=user.fcmId;
                new Thread() {
                    public void run() {
                        sendNotification(fcmId,title1,body1);
                    }
                }.start();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    //handel notification

    public void writeNotification(UserNotification userNotification, String to){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDatabase.child("/user-notifications/" + to + "/");
        Map<String, Object> postValues = userNotification.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        String notificationId = mDatabase.push().getKey();
        //.push().setValue(comment);
        //with key start
        childUpdates.put( "/user-notifications/" + to + "/"+notificationId, postValues);
        //with key end
        mDatabase.updateChildren(childUpdates);
    }


    //handel notification
    private void sendNotification(String fcmID,String title,String body)  {
        String FMCurl = "https://fcm.googleapis.com/fcm/send";
        String authKey="AAAApR2IiFI:APA91bGe6hW1KYyMpr_CVpc3QxtauhZj3LVGbrlLmUc45TAZ0mGxZ_Mdyiy2d_rP6o-NyuKcaC9UUcBgJ_EsMwhloeZWTdxmU3EQaH3sG8K_L92Soi9CZStehQrewyp7-tfUfjEbfWSg";
        try {
            URL url = new URL(FMCurl);
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "key=" + authKey);
            conn.setRequestProperty("Content-Type", "application/json");
            JSONObject json = new JSONObject();
            json.put("to", fcmID.trim());
            JSONObject info = new JSONObject();
            info.put("title", title); // Notification title
            info.put("body", body); // Notification
            info.put("click_action",".DisplayNotification");
            // body
            json.put("notification", info);
            try {
                OutputStreamWriter wr = new OutputStreamWriter(
                        conn.getOutputStream());
                wr.write(json.toString());
                wr.flush();

                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));

                String output;
                System.out.println("Output from Server .... \n");
                while ((output = br.readLine()) != null) {
                    System.out.println(output);
                }
                // result = CommonConstants.SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
                // result = CommonConstants.FAILURE;
            }
            System.out.println("GCM Notification is sent successfully");
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        //return result;
    }

}
