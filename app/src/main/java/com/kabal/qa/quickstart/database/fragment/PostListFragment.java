package com.kabal.qa.quickstart.database.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.kabal.qa.quickstart.database.CommentMainActivity;
import com.kabal.qa.quickstart.database.EditPost;
import com.kabal.qa.quickstart.database.GPSTracker;
import com.kabal.qa.quickstart.database.GoogleSignInActivity;
import com.kabal.qa.quickstart.database.MainActivity;
import com.kabal.qa.quickstart.database.NewPostActivity;
import com.kabal.qa.quickstart.database.Profile;
import com.kabal.qa.quickstart.database.R;
import com.kabal.qa.quickstart.database.ReadUserNotification;
import com.kabal.qa.quickstart.database.SelectKeys;
import com.kabal.qa.quickstart.database.TestEdit;
import com.kabal.qa.quickstart.database.UlTagHandler;
import com.kabal.qa.quickstart.database.Util.ConnectivityReceiver;
import com.kabal.qa.quickstart.database.Util.HandlerBackGround;
import com.kabal.qa.quickstart.database.models.Keys;
import com.kabal.qa.quickstart.database.models.ParcelablePost;
import com.kabal.qa.quickstart.database.models.Post;
import com.kabal.qa.quickstart.database.models.Total;
import com.kabal.qa.quickstart.database.models.User;
import com.kabal.qa.quickstart.database.models.UserNotification;
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
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public abstract class PostListFragment extends Fragment {

    private static final String TAG = "PostListFragment";

    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private FirestoreRecyclerAdapter<Post,PostViewHolder> adapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    public String cTag;
    public String status;
    private ProgressBar mProgressBar;
    public String state;
    public TextView textView;
    private FirebaseFirestore db;
    public String delete;
    public String from;

    //BottomNavigationView bottomNavigationView;
    public PostListFragment() {

    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_all_posts, container, false);
        delete=getArguments().getString("delete");
        status=getArguments().getString("status",status);
        cTag=getArguments().getString("cTag", cTag);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        // [END create_database_reference]
        mRecycler = (RecyclerView) rootView.findViewById(R.id.messages_list);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressbar);
        textView= (TextView) rootView.findViewById(R.id.empty_question);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Set up Layout Manager, reverse layout
        mDatabase = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);
       // mProgressBar.setVisibility(RecyclerView.VISIBLE);
        Query query = getQuery(FirebaseFirestore.getInstance());
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>()
                .setQuery(query, Post.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Post, PostViewHolder>(options) {
            @Override
            public void onBindViewHolder(final PostViewHolder holder, int position, final Post model) {
                final String docId = getSnapshots().getSnapshot(position).getId();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity
                        ParcelablePost parcelablePost=new ParcelablePost(model);
                       Intent intent = new Intent(getActivity(), CommentMainActivity.class);
                        //Intent intent = new Intent(getActivity(), TestEdit.class);
                        //intent.putExtra("parcel_data", parcelablePost);
                        intent.putExtra("postBody", parcelablePost.body.toString());
                        intent.putExtra(CommentMainActivity.EXTRA_POST_KEY, docId);
                        intent.putExtra("ansCount",String.valueOf(model.answerCount));
                        intent.putExtra("status",status);
                        startActivity(intent);
                    }
                });
                //bind star
                if("1".equals(status)){
                    if (model.stars.containsKey(getUId().toString())) {
                        holder.starView.setImageResource(R.drawable.ic_toggle_star_24);
                    } else {
                        holder.starView.setImageResource(R.drawable.ic_toggle_star_outline_24);
                    }
                        //enable delete and edit start
                    if("1".equals(delete)) {
                        if(model.answerCount>0 || model.starCount >0){

                        }else {
                            holder.deletePost.setImageResource(R.drawable.ic_dustbinnew);
                            holder.editPost.setImageResource(R.drawable.ic_edit);
                        }
                    }
                    //enable delete abd edit end
                }else{
                    holder.starView.setImageResource(R.drawable.ic_toggle_star_outline_24);
                }

                //bind tags start

                setTextViewHTML(holder.bodyView,model.body);
                //onclick on start for  elements
                holder.bindToPost(model,getContext()

                        /*, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //click on tags
                            }
                        }*/
                        , new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //profi image
                                Intent p = new Intent(getContext(), Profile.class);
                                p.putExtra("uId",model.uid);
                                startActivity(p);
                            }
                        }
                        , new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //click on profile name
                                Intent p = new Intent(getContext(), Profile.class);
                                p.putExtra("uId",model.uid);
                                startActivity(p);
                            }
                        }
                        , new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //edit post
                                ArrayList<String> list = new ArrayList<String>();
                                if(model.userTags.isEmpty()){

                                }else {
                                    list = new ArrayList<String>(model.userTags.keySet());
                                }

                                Intent i = new Intent(getContext(), EditPost.class);
                                i.putExtra("postId",docId);
                                i.putExtra("Body",model.body);
                                i.putExtra("status",status);
                                i.putStringArrayListExtra("userTags", list);
                                startActivity(i);

                            }
                        }
                        , new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //delete post
                                //dialog

                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case DialogInterface.BUTTON_POSITIVE:
                                                deletePost(docId,model);
                                                break;

                                            case DialogInterface.BUTTON_NEGATIVE:
                                                //No button clicked
                                                break;
                                        }
                                    }
                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("Are you sure? delete post?").setPositiveButton("Yes", dialogClickListener)
                                        .setNegativeButton("No", dialogClickListener).show();

                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View starView) {
                                //star clicked

                                if("1".equals(status)){
                                    // Need to write to both places the post is stored
                                    DocumentReference sfDocRef = db.collection("post").document(docId);
                                    onStarClicked(model,"1",docId);
                                }else{
                                    Intent i = new Intent(getContext(), GoogleSignInActivity.class);
                                    i.putExtra("click","1");
                                    startActivity(i);
                                }
                            }
                        });
                //onclick on end for elements

            }
            @Override
            public PostViewHolder onCreateViewHolder(ViewGroup group, int i) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.item_post, group, false);
                return new PostViewHolder(view);
            }
        };

        mRecycler.setAdapter(adapter);
    }

    // [START post_stars_transaction]
    private void onStarClicked(Post post,final String i,final String postId) {
        if (post.stars.containsKey(getUId().toString())) {
            db.collection("post").document(postId)
                    .update(
                            "starCount", post.starCount-1,
                            "trendCount", post.trendCount-1
                    );

            Map<String, String> stars = new HashMap<>();
            stars=post.stars;
            stars.remove(getUId().toString());
            db.collection("post").document(postId)
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
            db.collection("post").document(postId)
                    .update(
                            "starCount", post.starCount+1,
                            "trendCount", post.trendCount+1
                            //"stars",post.stars.put(getUid(),true)
                    );
            Map<String, String> stars = new HashMap<>();
            stars=post.stars;
            stars.put(getUId().toString(),getUId().toString());
            db.collection("post").document(postId)
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
    public String getDisplayName(){
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }

    // [START write_fan_out]
    private void writeNewPost(Post post) {
       // String key = mDatabase.child(post.city).push().getKey();
        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        //childUpdates.put("/"+post.city+"-deleted"+"/" +key, postValues);
        mDatabase.updateChildren(childUpdates);
    }
    // [END write_fan_out]

    public static String getUId(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    public String Say(){
        return "posts";
    }

   // public abstract Query getQuery(DatabaseReference databaseReference);
    public abstract Query getQuery(FirebaseFirestore databaseReference);

    public String getcTag() {
        return cTag;
    }

    public void setcTag(String cTag) {
        this.cTag = cTag;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
            info.put("sound","default");
            info.put("icon","ic_launcher");
            // body

      /*      //data start
            JSONObject dataInfo = new JSONObject();
            dataInfo.put("product_id","123");
            json.put("data",dataInfo);
            //data end*/

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
                mDatabase.child("users").child(to1).child("notificationStatus").setValue("Y");
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
    //onQuestionDeleted(cglobalPostRef);
    //mOnQuestionDeleted(cmGlobalPostRef);
    //delete count
    private void onQuestionDeleted(DatabaseReference postRef, final String state1, final String city1) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Keys p = mutableData.getValue(Keys.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }
                // Star the post and add self to stars
              /*  if(p.Total>0) {
                    long count = p.Total - 1;*/
                    //mDatabase.child("keys").child(state1).child("district").child(city1).setValue(count);
                  //  p.Total = p.Total - 1;
               // }
                //mDatabase.child("keys").child(state).child("district").child(city).setValue(count);
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



    private void mOnQuestionDeleted(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Total p = mutableData.getValue(Total.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }
                // Star the post and add self to stars
               /* if(p.Total>0) {
                    p.Total = p.Total - 1;
                }*/
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
    //delete count

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
//delete post start
    public void deletePost(String postid, final Post post){
        final DocumentReference addDeletedPost = db.collection("deleted-post").document(postid);
        final DocumentReference deletePost=db.collection("post").document(postid);
        final DocumentReference updatePostcount=db.collection("count").document("Total");
        db.runTransaction(new com.google.firebase.firestore.Transaction.Function<Void>() {
            @Override
            public Void apply(com.google.firebase.firestore.Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(updatePostcount);
                Double answerCount=snapshot.getDouble("count")-1;
                transaction.update(updatePostcount,"count",answerCount);
                transaction.set(addDeletedPost,post);
                transaction.delete(deletePost);
                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Log.d(TAG, "Transaction success!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //      Log.w(TAG, "Transaction failure.", e);
                    }
                });
    }
//delete post end


}