package com.kabal.qa.quickstart.database;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.kabal.qa.quickstart.database.Util.ConnectivityReceiver;
import com.kabal.qa.quickstart.database.models.Comments;
import com.kabal.qa.quickstart.database.models.ParcelableComment;
import com.kabal.qa.quickstart.database.models.ParcelablePost;
import com.kabal.qa.quickstart.database.models.Post;
import com.kabal.qa.quickstart.database.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by amgoth.naik on 10/15/2017.
 */

public class FullCommentMainActivity extends BaseActivity implements  ConnectivityReceiver.ConnectivityReceiverListener,View.OnClickListener{
    public static final String EXTRA_POST_KEY = "post_key";
    String status;
    TextView full_comment_post_title;
    ImageView editview;
    ImageView delete_comment;
    ImageView comment_post_author_photo;
    TextView comment_post_author;
    TextView comment_time_date;
    public String postid;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean isConnected=true;
    private CoordinatorLayout coordinatorLayout;
    public String commentId;
   // public String postBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_comment);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View customView = mInflater.inflate(R.layout.key, null);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //TextView customTitle = (TextView) customView.findViewById(R.id.app_text);
        //customTitle.setText(R.string.app_name);
        //mActionBar.setCustomView(customView);

        //back press start
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        //back press end
       // postBody=getIntent().getStringExtra("postBody");
        status=getIntent().getStringExtra("status");
        commentId=getIntent().getStringExtra("commentId");
        postid=getIntent().getStringExtra("post_key");
        full_comment_post_title= (TextView) findViewById(R.id.full_comment_post_title);
        editview=(ImageView) findViewById(R.id.edit_comment);
        delete_comment=(ImageView) findViewById(R.id.delete_comment);
        comment_post_author_photo=(ImageView) findViewById(R.id.comment_post_author_photo);
        comment_post_author=(TextView) findViewById(R.id.comment_post_author);
        comment_time_date=(TextView) findViewById(R.id.comment_time_date);

        //fetch comment start
        DocumentReference docRef = db.collection(postid).document(commentId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                final Comments comments = documentSnapshot.toObject(Comments.class);

                // [START_EXCLUDE]
                if (comments == null) {
                    Toast.makeText(FullCommentMainActivity.this,
                            "Error: could not fetch .",
                            Toast.LENGTH_SHORT).show();
                } else {
                    setTextViewHTML(full_comment_post_title,comments.text);
                    //full_comment_post_title.setText(Html.fromHtml(comments.text));
                    Glide.with(FullCommentMainActivity.this).load(comments.purl)
                            .thumbnail(0.5f)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(comment_post_author_photo);
                    comment_post_author.setText(comments.author);
                    comment_time_date.setText(DateUtils.getRelativeTimeSpanString(comments.date, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));
                    if("1".equals(status) && 0==comments.starCount){
                        if(getUid().equals(comments.uid)) {
                            editview.setImageResource(R.drawable.ic_edit);
                            delete_comment.setImageResource(R.drawable.ic_dustbinnew);
                        }
                    }
                    //edit click start
                    editview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            isConnected = ConnectivityReceiver.isConnected();
                            if (isConnected) {

                                DocumentReference docRef = db.collection(postid).document(commentId);
                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        final Comments comments = documentSnapshot.toObject(Comments.class);
                                        ArrayList<String> list = new ArrayList<String>();
                                        if(comments.userTags.isEmpty()){

                                        }else {
                                            list = new ArrayList<String>(comments.userTags.keySet());
                                        }
                                        Intent intent = new Intent(FullCommentMainActivity.this, EditComment.class);
                                        intent.putExtra("post_key", postid);
                                        intent.putExtra("status", status);
                                        intent.putExtra("commentId", commentId);
                                        intent.putExtra("Body",comments.text);
                                        intent.putStringArrayListExtra("userTags", list);
                                        startActivity(intent);
                                        finish();
                                    }
                                });


                            }else{
                                showError();
                            }
                        }
                    });
                    //edit click end

                    //delete click start
                    delete_comment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case DialogInterface.BUTTON_POSITIVE:
                                            isConnected = ConnectivityReceiver.isConnected();
                                            if (isConnected) {
                                        //batch transactions
                                                final DocumentReference addDeletedComment = db.collection("deleted-comment").document(commentId);
                                                final DocumentReference deleteComment=db.collection(postid).document(commentId);
                                                final DocumentReference updateAnscount=db.collection("post").document(postid);
                                                db.runTransaction(new Transaction.Function<Void>() {

                                                    @Override
                                                    public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                                        DocumentSnapshot snapshot = transaction.get(updateAnscount);
                                                        Double answerCount=snapshot.getDouble("answerCount")-1;
                                                        transaction.update(updateAnscount,"answerCount",answerCount);
                                                        transaction.set(addDeletedComment,comments);
                                                        transaction.delete(deleteComment);
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
                                                DocumentReference docRef = db.collection("post").document(postid);
                                                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        Post post = documentSnapshot.toObject(Post.class);
                                                        // [START_EXCLUDE]
                                                        if (post == null) {
                                                            Toast.makeText(FullCommentMainActivity.this,
                                                                    "Error: could not fetch user.",
                                                                    Toast.LENGTH_SHORT).show();
                                                        } else {
                                                         Intent intent = new Intent(FullCommentMainActivity.this, CommentMainActivity.class);
                                                            intent.putExtra("postBody",post.body);
                                                            intent.putExtra("post_key", postid);
                                                            intent.putExtra("status", status);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }
                                                });
                                            }else{
                                                showError();
                                            }
                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            //No button clicked
                                            break;
                                    }
                                }
                            };
                            AlertDialog.Builder builder = new AlertDialog.Builder(FullCommentMainActivity.this);
                            builder.setMessage("Are you sure? delete Comment?").setPositiveButton("Yes", dialogClickListener)
                                    .setNegativeButton("No", dialogClickListener).show();
                        }
                    });
                    //delete click end


                }
            }
        });
        //fetch comment end
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()== android.R.id.home)
            onBackPressed();
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FullCommentMainActivity.this, CommentMainActivity.class);
        intent.putExtra("status",status);
        //intent.putExtra("postBody",postBody);
        intent.putExtra("post_key",postid);
        startActivity(intent);
        finish();
    }

    // [START write_fan_out]
    private void writeNewPost(Comments comments,String postid,String commentId) {
        //Post post = new Post(userId, username,body,System.currentTimeMillis(),0,url,0,mapUserTags,"Y");
        // public Post(uid,author,body,date,answerCount,purl,trendCount,userTags,postStatus) {
        db.collection("deleted-comment")
                .add(comments)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                     //   Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       // Log.w(TAG, "Error adding document", e);
                    }
                });
    }
    // [END write_fan_out]
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
                Intent i = new Intent(FullCommentMainActivity.this, MainActivity.class);
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