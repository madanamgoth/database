package com.kabal.qa.quickstart.database;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
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
import com.kabal.qa.quickstart.database.Util.ConnectivityReceiver;
import com.kabal.qa.quickstart.database.Util.MyApplication;
import com.kabal.qa.quickstart.database.models.Keys;
import com.kabal.qa.quickstart.database.models.Post;
import com.kabal.qa.quickstart.database.models.Total;
import com.kabal.qa.quickstart.database.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.richeditor.RichEditor;

public class NewPostActivity extends BaseActivity implements  ConnectivityReceiver.ConnectivityReceiverListener{

    private static final String TAG = "NewPostActivity";
    private static final String REQUIRED = "Required";
   // private EditText tagField;
    boolean isConnected=true;
    private CoordinatorLayout coordinatorLayout;
    public int postCheck;
    private RichEditor mEditor;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<String> userTags;
    String Body;
    String toW;
    Map<String,Long> mapUserTags;
    //String cTag;
    String status;
    Button addTagButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //backbuttonstart//
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        //backbuttonend//
        mapUserTags=new HashMap<String, Long>();
       // cTag=getIntent().getStringExtra("cTag");
        status=getIntent().getStringExtra("status");
        userTags = getIntent().getStringArrayListExtra("userTags");
        Body= getIntent().getStringExtra("Body");
        toW=getIntent().getStringExtra("toW");
        //tagField=(EditText) findViewById(R.id.tags_create);
        addTagButton=(Button) findViewById(R.id.button2);
        if(userTags!=null && ! userTags.isEmpty()) {
           /* if("toTags".equals(toW)){
                tagField.setText(userTags.toString());
            }*/
            for(int i=0;i<userTags.size();i++){
                mapUserTags.put(userTags.get(i), 324234234L);
            }
        }
        addTagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call tag class
                Intent i = new Intent(NewPostActivity.this, SelectKeys.class);
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
         //       i.putExtra("cTag",cTag);
                i.putExtra("status",status);
                i.putExtra("toW","toTags");
                i.putStringArrayListExtra("userTags", (ArrayList<String>) userTags);
                i.putExtra("from","newpost");
                i.putExtra("status",status);
                startActivity(i);
            }
        });



        //new start
        mEditor = (RichEditor) findViewById(R.id.editor);
        mEditor.setEditorHeight(200);
        mEditor.setEditorFontSize(15);
        mEditor.setEditorFontColor(Color.BLACK);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder("Insert text here...");
        //String text="<a href=\"http://example.com/\" style=\"color:#FF0000;\">Red Link</a>";
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
    }
    private void submitPost() {

        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
        final String userId = getUid();
        DocumentReference docRef = db.collection("users").document(userId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);

                // [START_EXCLUDE]
                if (user == null) {
                    // User is null, error out
                    Log.e(TAG, "User " + userId + " is unexpectedly null");
                    Toast.makeText(NewPostActivity.this,
                            "Error: could not fetch user.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Write new post
                    //add transaction star
                    /*final CollectionReference getDocRef = db.collection("post");
                    final DocumentReference addDoc=db.collection("post").document(getDocRef.getId());
                    final DocumentReference updateCount=db.collection("count").document("Total");
                    final DocumentReference updateKeys=db.collection("keys").document(commentId);
                    db.runTransaction(new com.google.firebase.firestore.Transaction.Function<Void>() {

                        @Override
                        public Void apply(com.google.firebase.firestore.Transaction transaction) throws FirebaseFirestoreException {
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
                    //add transaction end*/
                   writeNewPost(userId, user.username,mEditor.getHtml().toString(),user.uri);
                    updateCount();
                    //addTags();
                }
                // Finish this Activity, back to the stream
                setEditingEnabled(true);
                finish();
                // [END_EXCLUDE]
            }
        });

        Intent i = new Intent(this, MainActivity.class);
        //i.putExtra("cTag",cTag);
        i.putExtra("status",status);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    private void setEditingEnabled(boolean enabled) {
        //mBodyField.setEnabled(enabled);
    }

    // [START write_fan_out]
    private void writeNewPost(String userId, String username, String body,String url) {
        Post post = new Post(userId, username,body,System.currentTimeMillis(),0,0,url,0,mapUserTags,"Y");
                       // public Post(uid,author,body,date,answerCount,purl,trendCount,userTags,postStatus) {
        db.collection("post")
                .add(post)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
    // [END write_fan_out]
    public void updateCount(){
        DocumentReference docRef = db.collection("count").document("Total");
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Total total = documentSnapshot.toObject(Total.class);
                if (total.count >= 0) {
                    db.collection("count").document("Total")
                            .update("count", total.count + 1);
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()== android.R.id.home){
            finish();
        }
        if (item.getItemId() == R.id.action_name) {
            submitPost();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                Intent i = new Intent(NewPostActivity.this, GoogleSignInActivity.class);
                startActivity(i);
                finish();
            }
        });
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.imageView), "Sorry! Not connected to internet", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }
}