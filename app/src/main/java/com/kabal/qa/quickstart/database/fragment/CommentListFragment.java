package com.kabal.qa.quickstart.database.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.kabal.qa.quickstart.database.CommentMainActivity;
import com.kabal.qa.quickstart.database.EditPost;
import com.kabal.qa.quickstart.database.FullCommentMainActivity;
import com.kabal.qa.quickstart.database.GPSTracker;
import com.kabal.qa.quickstart.database.GoogleSignInActivity;
import com.kabal.qa.quickstart.database.MainActivity;
import com.kabal.qa.quickstart.database.Profile;
import com.kabal.qa.quickstart.database.R;
import com.kabal.qa.quickstart.database.UlTagHandler;
import com.kabal.qa.quickstart.database.models.Comments;
import com.kabal.qa.quickstart.database.models.ParcelableComment;
import com.kabal.qa.quickstart.database.models.ParcelablePost;
import com.kabal.qa.quickstart.database.models.Post;
import com.kabal.qa.quickstart.database.models.User;
import com.kabal.qa.quickstart.database.models.UserNotification;
import com.kabal.qa.quickstart.database.viewholder.CommentViewHolder;
import com.kabal.qa.quickstart.database.viewholder.PostViewHolder;

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
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by amgoth.naik on 10/13/2017.
 */

public abstract class CommentListFragment extends Fragment {

    private static final String TAG = "CommentListFragment";

    public String mPostKey;

    private FirestoreRecyclerAdapter<Comments, CommentViewHolder> adapter;
    public String status;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    public ProgressBar mProgressBar;
    //public  ParcelablePost parcelablePost;
    private FirebaseFirestore db;
    public TextView empty_answers;
    public CommentListFragment() {

    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.comment_fragment_all_posts, container, false);
        status=getArguments().getString("status",status);
        mPostKey = getArguments().getString("post_key");
        db = FirebaseFirestore.getInstance();
        mRecycler = (RecyclerView) rootView.findViewById(R.id.recycler_comments);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.comment_progressbar);
        //parcelablePost = (ParcelablePost) getArguments().getParcelable("parcel_data");
        empty_answers=rootView.findViewById(R.id.empty_answers);
        return rootView;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        mProgressBar.setVisibility(RecyclerView.VISIBLE);

        DocumentReference docRef = db.collection("post").document(mPostKey);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                final Post post = documentSnapshot.toObject(Post.class);
                if(0==post.answerCount){
                    empty_answers.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(RecyclerView.GONE);
                }else{
                    empty_answers.setVisibility(View.GONE);
                }
            }
        });
        Query query = getQuery(FirebaseFirestore.getInstance());
        FirestoreRecyclerOptions<Comments> options = new FirestoreRecyclerOptions.Builder<Comments>()
                .setQuery(query, Comments.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Comments, CommentViewHolder>(options) {
            @Override
            public void onBindViewHolder(final CommentViewHolder holder, int position, final Comments model) {
                final String commentId = getSnapshots().getSnapshot(position).getId();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch full comment activity
                        //ParcelableComment parcelableComment=new ParcelableComment(model);
                        Intent intent = new Intent(getActivity(), FullCommentMainActivity.class);
                        intent.putExtra("commentId", commentId);
                        intent.putExtra("status",status);
                        intent.putExtra("post_key",mPostKey);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        //intent.putExtra("ansCount",ansCount);
                        //intent.putExtra("post_parcel_data",parcelablePost);
                        //intent.putExtra("parcel_data", parcelableComment);
                        startActivity(intent);
                    }
                });
                setTextViewHTML(holder.bodyView,model.text);

                //bind to post start
                holder.bindToPost(model, getContext() , new View.OnClickListener(){
                    @Override
                    public void onClick(View starView) {
                        if("1".equals(status)){
                           // onCommentStarClicked(globalCommentRef,mPostKey,postRef.getKey());
                            onStarClicked(model,"1",mPostKey,commentId);
                        }else{
                            Intent i = new Intent(getContext(), GoogleSignInActivity.class);
                            i.putExtra("click","1");
                            startActivity(i);
                        }

                    }
                });
                //bind to post end
                mProgressBar.setVisibility(RecyclerView.GONE);
                if("1".equals(status)){
                    if (model.stars.containsKey(getUid())) {
                        holder.cstarView.setImageResource(R.drawable.ic_toggle_star_24);
                    } else {
                        holder.cstarView.setImageResource(R.drawable.ic_toggle_star_outline_24);
                    }
                }else{
                    holder.cstarView.setImageResource(R.drawable.ic_toggle_star_outline_24);
                }
            }


            @Override
            public CommentViewHolder onCreateViewHolder(ViewGroup group, int i) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.item_comment, group, false);
                return new CommentViewHolder(view);
            }

        };


        mRecycler.setAdapter(adapter);
    }


    // [START post_stars_transaction]
    private void onStarClicked(Comments comments,final String i,final String postId,final String commentId) {
        if (comments.stars.containsKey(getUId().toString())) {
            db.collection(postId).document(commentId)
                    .update(
                            "starCount", comments.starCount-1
                            //"trendCount", .trendCount-1
                    );

            Map<String, String> stars = new HashMap<>();
            stars=comments.stars;
            stars.remove(getUId().toString());
            db.collection(postId).document(commentId)
                    .update("stars", stars)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                        }
                    });

        } else {
            db.collection(postId).document(commentId)
                    .update(
                            "starCount", comments.starCount+1
                            //"trendCount", c.trendCount+1
                            //"stars",post.stars.put(getUid(),true)
                    );
            Map<String, String> stars = new HashMap<>();
            stars=comments.stars;
            stars.put(getUId().toString(),getUId().toString());
            db.collection(postId).document(commentId)
                    .update("stars", stars)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error updating document", e);
                        }
                    });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public String getUid() {
        if("1".equals(status)){
            return FirebaseAuth.getInstance().getCurrentUser().getUid();
        }else{
            return null;
        }
    }

    // [END write_fan_out]
    public static String getUId()
    {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(FirebaseFirestore databaseReference);

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getmPostKey() {
        return mPostKey;
    }

    public void setmPostKey(String mPostKey) {
        this.mPostKey = mPostKey;
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

    /*public void writeNotification(UserNotification userNotification, String to){
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


    public void prepareNotification( String title,  String body,  String from, String to,  String city,String postId,String commentId){
        final String  title1 =title;
        final String body1=body;
        final String from1=from;
        final String city1=city;
        final String to1=to;
        final String postId1=postId;
        final String commentId1=commentId;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(to).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                UserNotification userNotification= new UserNotification(title1,body1,"deeplink",user.fcmId,System.currentTimeMillis(),"Y",from1,city1,commentId1,to1,postId1,"Y");
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
    }*/
    //handel notification
    public String getDisplayName(){
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }

    //tags clickble  start

    public void setTextViewHTML(TextView text, String html)
    {
        CharSequence sequence = Html.fromHtml(html,null,new UlTagHandler());
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for(URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }
        text.setText(strBuilder);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }


    protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span)
    {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                // Do something with span.getURL() to handle the link click...
                //Toast.makeText(getContext(), span.getURL(),
                //      Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), MainActivity.class);
                i.putExtra("cTag",span.getURL());
                i.putExtra("status",status);
                startActivity(i);
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    //tags clickble end

}