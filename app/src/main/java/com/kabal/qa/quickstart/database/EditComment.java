package com.kabal.qa.quickstart.database;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kabal.qa.quickstart.database.Util.ConnectivityReceiver;
import com.kabal.qa.quickstart.database.models.Comments;
import com.kabal.qa.quickstart.database.models.ParcelablePost;
import com.kabal.qa.quickstart.database.models.Post;
import com.kabal.qa.quickstart.database.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.richeditor.RichEditor;

/**
 * Created by amgoth.naik on 10/15/2017.
 */

public class EditComment extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    // private EditText mBodyField;
    private static final String REQUIRED = "Required";
    private static final String TAG = "EditComment";
    private DatabaseReference mCommentsReference;
    private String mPostKey;
    //public  ParcelablePost parcelablePost;
    boolean isConnected=true;
    private CoordinatorLayout coordinatorLayout;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public TextView post_title_comment;
    private RichEditor mEditor;
    String Body;
    String status;
    Map<String,Long> mapUserTags;
    private List<String> userTags;
    private Button button2;
    //String postBody;
    String commentId;
    //String ansCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_comment);
        isConnected = ConnectivityReceiver.isConnected();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //backbuttonstart//
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        //backbuttonend//

        //tags start
        //backbuttonend//
        mapUserTags=new HashMap<String, Long>();
        //back start
        userTags = getIntent().getStringArrayListExtra("userTags");
        status=getIntent().getStringExtra("status");
        Body= getIntent().getStringExtra("Body");
        // mBodyField.setText(Body);

        //tags end

        if(isConnected) {
            //parcelablePost = (ParcelablePost) getIntent().getParcelableExtra("parcel_data");
            // mBodyField = (EditText) findViewById(R.id.editor_comment);
            commentId=getIntent().getStringExtra("commentId");
         //   postBody=getIntent().getStringExtra("postBody");
            mPostKey = getIntent().getStringExtra("post_key");
           /* if (mPostKey == null) {
                throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
            }*/
            post_title_comment=(TextView) findViewById(R.id.post_title_comment);
            //post_title_comment.setText(Html.fromHtml(postBody).toString());
            button2=(Button) findViewById(R.id.button2);
            //new start
            mEditor = (RichEditor) findViewById(R.id.editor_comment);
            mEditor.setEditorHeight(200);
            mEditor.setEditorFontSize(20);
            mEditor.setEditorFontColor(Color.BLACK);
            mEditor.setPadding(10, 10, 10, 10);
            mEditor.setPlaceholder("Insert text here...");
            if(userTags!=null && ! userTags.isEmpty()) {
                for (int i = 0; i < userTags.size(); i++) {
                    if(Body!=null){
                        if(Body.contains("&nbsp;<a href=\"" + userTags.get(i) + "\">" + userTags.get(i) + "</a>&nbsp;")){

                        }else{
                            Body = Body + "&nbsp;<a href=\"" + userTags.get(i) + "\">" + userTags.get(i) + "</a>&nbsp;";
                            //mEditor.setTag(userTags.get(i));
                            mapUserTags.put(userTags.get(i), 324234234L);
                        }
                    }else{
                        if(Body.contains("&nbsp;<a href=\"" + userTags.get(i) + "\">" + userTags.get(i) + "</a>&nbsp;")){

                        }else{
                            Body = Body + "&nbsp;<a href=\"" + userTags.get(i) + "\">" + userTags.get(i) + "</a>&nbsp;";
                            //mEditor.setTag(userTags.get(i));
                            mapUserTags.put(userTags.get(i), 324234234L);
                        }
                    }

                }
            }
            if(Body!=null){
                mEditor.setHtml(Body);
            }



            findViewById(R.id.action_undo).setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    mEditor.undo();
                }
            });

            findViewById(R.id.action_redo).setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    mEditor.redo();
                }
            });

            findViewById(R.id.action_bold).setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    mEditor.setBold();
                }
            });

            findViewById(R.id.action_italic).setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    mEditor.setItalic();
                }
            });
            findViewById(R.id.action_underline).setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    mEditor.setUnderline();
                }
            });
            findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
                private boolean isChanged;

                @Override public void onClick(View v) {
                    mEditor.setTextColor(isChanged ? Color.RED : Color.BLACK);
                    isChanged = !isChanged;
                }
            });
            findViewById(R.id.action_align_left).setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    mEditor.setAlignLeft();
                }
            });

            findViewById(R.id.action_align_center).setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    mEditor.setAlignCenter();
                }
            });

            findViewById(R.id.action_align_right).setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    mEditor.setAlignRight();
                }
            });
            findViewById(R.id.action_insert_bullets).setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    mEditor.setBullets();
                }
            });
            findViewById(R.id.action_insert_link).setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    mEditor.insertLink("https://github.com/wasabeef", "wasabeef");
                }
            });
            //new end
        }else{
            error();
        }
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call tag class
                Intent i = new Intent(EditComment.this, EditKeys.class);
                if(mEditor.getHtml()!=null){
                    if(userTags!=null && ! userTags.isEmpty()) {
                        for (int k = 0; k < userTags.size(); k++) {
                            if(mEditor.getHtml().toString().contains("&nbsp;<a href=\"" + userTags.get(k) + "\">" + userTags.get(k) + "</a>&nbsp;")){

                            }else{
                                userTags.remove(userTags.get(k));
                            }
                        }
                    }
                    i.putExtra("Body",mEditor.getHtml().toString());
                }else{
                    i.putExtra("Body","");
                }
               i.putExtra("postBody","");
                i.putExtra("commentId",commentId);
                i.putExtra("post_key",mPostKey);
                i.putExtra("status",status);
                i.putExtra("toW","toTags");
                i.putStringArrayListExtra("userTags", (ArrayList<String>) userTags);
                i.putExtra("from","newcomment");
                i.putExtra("status",status);
                startActivity(i);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }

    private void postComment() {
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        final String commentText = mEditor.getHtml().toString();
        /*if (TextUtils.isEmpty(commentText)) {
            mBodyField.setError(REQUIRED);
            return;
        }*/
        //new comment start
        DocumentReference docRef = db.collection("users").document(getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);

                // [START_EXCLUDE]
                if (user == null) {
                    // User is null, error out
                    Log.e(TAG, "User " + getUid() + " is unexpectedly null");
                    Toast.makeText(EditComment.this,
                            "Error: could not fetch user.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    String authorName = user.username;
                    Comments comment = new Comments(getUid(), authorName, commentText, System.currentTimeMillis(), user.uri, 0,"Y",mapUserTags);
                    writeNewCommnet(comment);
                   // updateAnsCount();
                }
            }
        });

        Intent intent = new Intent(EditComment.this, FullCommentMainActivity.class);
        //intent.putExtra("postBody", postBody);
        intent.putExtra("post_key", mPostKey);
        intent.putExtra("commentId",commentId);
        intent.putExtra("status",status);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }
    public void writeNewCommnet(Comments comments){

        db.collection(mPostKey).document(commentId)
                .set(comments)
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
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        if (item.getItemId() == R.id.action_name) {
            postComment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
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
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View customView = mInflater.inflate(R.layout.key, null);
        TextView customTitle = (TextView) customView.findViewById(R.id.app_text);
        customTitle.setText(R.string.app_name);
        customTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditComment.this, GoogleSignInActivity.class);
                startActivity(i);
                finish();
            }
        });
        // Apply the custom view
        mActionBar.setCustomView(customView);
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.imageView), "Sorry! Not connected to internet", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}
