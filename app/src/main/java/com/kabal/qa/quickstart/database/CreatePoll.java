package com.kabal.qa.quickstart.database;

/**
 * Created by amgoth.naik on 11/21/2017.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kabal.qa.quickstart.database.Util.ConnectivityReceiver;
import com.kabal.qa.quickstart.database.Util.MyApplication;
import com.kabal.qa.quickstart.database.models.Model;
import com.kabal.qa.quickstart.database.models.Post;
import com.kabal.qa.quickstart.database.models.Total;
import com.kabal.qa.quickstart.database.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import jp.wasabeef.richeditor.RichEditor;

import static com.kabal.qa.quickstart.database.CreatePoll.ChangeData.DAY;
import static com.kabal.qa.quickstart.database.CreatePoll.ChangeData.HOUR;
import static com.kabal.qa.quickstart.database.CreatePoll.ChangeData.MINUTE;

public class CreatePoll extends BaseActivity implements  ConnectivityReceiver.ConnectivityReceiverListener{

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
    TextView pollLength;
   List<String> list1;
    private TagContainerLayout mTagContainerLayout1;

    private NumberPicker day;
    private NumberPicker hour;
    private NumberPicker minute;
    private TextView displayTime;
    int pickedDay = 1;
    int pickedHour = 0;
    int pickedMin = 0;

    public enum ChangeData{
        DAY, HOUR, MINUTE;
    }
    private ChangeData changeData;

    public long startDate;
    public long endDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_poll);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //backbuttonstart//
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        mEditor = (RichEditor) findViewById(R.id.editor_comment);
        mEditor.setEditorHeight(100);
        mEditor.setEditorFontSize(20);
        mEditor.setEditorFontColor(Color.BLACK);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder("Insert text here...");
        list1 = new ArrayList<String>();
        list1 = getIntent().getStringArrayListExtra("answerTags");
        if(list1==null){
            list1 = new ArrayList<String>();
        }
        mTagContainerLayout1 = (TagContainerLayout) findViewById(R.id.tagcontainerLayout1);
        //on tag click start
        final EditText text = (EditText) findViewById(R.id.text_tag);
        pollLength=(TextView) findViewById(R.id.polllength);
        pollLength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertBox = new AlertDialog.Builder(CreatePoll.this);
                View myView = CreatePoll.this.getLayoutInflater().inflate(R.layout.time_picker, null);
                alertBox.setView(myView);
                alertBox.setCancelable(false);
                displayTime = (TextView) myView.findViewById(R.id.display);

                day = (NumberPicker) myView.findViewById(R.id.day);
                day.setMinValue(0);
                day.setMaxValue(7);
                day.setValue(1);
                day.setVerticalScrollBarEnabled(true);
                day.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        changeData = DAY;
                        updateView(day.getValue());
                        if(day.getValue()==7){
                            hour.setEnabled(false);
                            minute.setEnabled(false);
                        }
                        else{
                            hour.setEnabled(true);
                            minute.setEnabled(true);
                        }
                    }
                });

                hour = (NumberPicker) myView.findViewById(R.id.hour);
                hour.setMinValue(0);
                hour.setMaxValue(23);
                hour.setValue(0);
                hour.setVerticalScrollBarEnabled(true);
                hour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        changeData = HOUR;
                        updateView(hour.getValue());
                    }
                });

                minute = (NumberPicker) myView.findViewById(R.id.minute);
                minute.setMinValue(0);
                minute.setMaxValue(59);
                minute.setValue(0);
                minute.setVerticalScrollBarEnabled(true);
                minute.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        changeData = MINUTE;
                        updateView(minute.getValue());
                    }
                });

                Button cancel = (Button) myView.findViewById(R.id.cancelButton);
                Button submit = (Button) myView.findViewById(R.id.submitButton);
                final AlertDialog alertDialog = alertBox.create();
                alertDialog.show();

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(CreatePoll.this, "Successfully set", Toast.LENGTH_LONG).show();
                        alertDialog.dismiss();
                        pickedDay = day.getValue();
                        pickedHour = hour.getValue();
                        pickedMin = minute.getValue();
                        if (pickedDay == 7) {
                            pickedHour = 0;
                            pickedMin = 0;
                            pollLength.setText("Poll Length \n" + pickedDay+ " days ");
                        }
                        else if(pickedDay==0){
                            pollLength.setText("Poll Length \n" + pickedHour+ " hours " + pickedMin+ " minutes ");
                        }
                        else if(pickedHour==0&& pickedMin==0){
                            pollLength.setText("Poll Length \n" + pickedDay+ " days ");
                        }
                        else if(pickedDay==0 && pickedMin==0){
                            pollLength.setText("Poll Length \n" + pickedHour+ " hours ");
                        }
                        else if(pickedDay==0 && pickedHour ==0){
                            pollLength.setText("Poll Length \n" + pickedMin+ " minutes ");
                        }
                        else {
                            pollLength.setText("Poll Length \n" + pickedDay+ " days "+pickedHour+" hours "+pickedMin+ " minutes ");
                        }
                    }
                });


            }
        });
        //on tag click
        mTagContainerLayout1.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(final int position, final String text) {
               /* Toast.makeText(SelectKeys.this, "click-position:" + position + ", text:" + text,
                        Toast.LENGTH_SHORT).show();*/

                AlertDialog dialog = new AlertDialog.Builder(CreatePoll.this)
                        .setTitle("long click")
                        .setMessage("You will delete this tag!")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (position < mTagContainerLayout1.getChildCount()) {
                                    mTagContainerLayout1.removeTag(position);
                                    list1.remove(text);
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
            }

            //long Click
            @Override
            public void onTagLongClick(final int position, final String text) {
                AlertDialog dialog = new AlertDialog.Builder(CreatePoll.this)
                        .setTitle("long click")
                        .setMessage("You will delete this tag!")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (position < mTagContainerLayout1.getChildCount()) {
                                    mTagContainerLayout1.removeTag(position);
                                    list1.remove(text);
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
            }


            @Override
            public void onTagCrossClick(final int position) {
                AlertDialog dialog = new AlertDialog.Builder(CreatePoll.this)
                        .setTitle("long click")
                        .setMessage("You will delete this tag!")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (position < mTagContainerLayout1.getChildCount()) {
                                    mTagContainerLayout1.removeTag(position);
                                    list1.remove(position);
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
            }
        });
        mTagContainerLayout1.setTags(list1);
        List<int[]> colors = new ArrayList<int[]>();
        int[] col1 = {Color.parseColor("#ff0000"), Color.parseColor("#000000"), Color.parseColor("#ffffff")};
        int[] col2 = {Color.parseColor("#0000ff"), Color.parseColor("#000000"), Color.parseColor("#ffffff")};

        colors.add(col1);
        colors.add(col2);
        //on tag click end
        ImageView btnAddTag = (ImageView) findViewById(R.id.btn_add_tag);


        btnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //myString.replaceAll("^\\s+|\\s+$", "");
                //if (text.getText().toString().replaceAll("^\\s+|\\s+$", "").equals("")) {
                if(text.getText().toString().replaceAll("\\s+","").equals("")){
                    text.setText("");
                } else {
                    if (list1.size() < 4) {
                       // if ("#".equals(text.getText().toString().charAt(0))) {
                            mTagContainerLayout1.addTag(text.getText().toString());
                            list1.add(text.getText().toString());
                       // } else {
                        //    mTagContainerLayout1.addTag("#" + text.getText().toString().replaceAll("\\s+",""));
                         //   list1.add("#" + text.getText().toString().replaceAll("\\s+",""));
                       // }
                        text.setText("");
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(), "Only 4 tags permitted",
                                Toast.LENGTH_SHORT);
                        toast.show();
                        text.setText("");
                    }
                }
                // Add tag in the specified position
//                mTagContainerLayout1.addTag(text.getText().toString(), 4);
            }
        });
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
                Intent i = new Intent(CreatePoll.this, CreatePollKeys.class);
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
                i.putStringArrayListExtra("answerTags", (ArrayList<String>) list1);
                i.putExtra("from","newpoll");
                i.putExtra("status",status);
                startActivity(i);
            }
        });



        //new start
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
                    Toast.makeText(CreatePoll.this,
                            "Error: could not fetch user.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Write new post
                    writeNewPost(userId, user.username,mEditor.getHtml().toString(),user.uri);
                    updateCount();
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
                Intent i = new Intent(CreatePoll.this, GoogleSignInActivity.class);
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

    private void updateView(int newValue) {

        switch (changeData){
            case DAY:
                if(day.getValue() == 0 && hour.getValue() == 0){
                    minute.setMinValue(5);
                }
                else{
                    minute.setMinValue(0);
                }
                if(newValue==7){
                    displayTime.setText(newValue+" days ");
                    break;
                }
                else {
                    displayTime.setText(newValue + " days " + hour.getValue() + " hours " + minute.getValue()+" minutes ");
                    break;
                }
            case HOUR:
                if(day.getValue() == 0 && hour.getValue() == 0){
                    minute.setMinValue(5);
                }
                else{
                    minute.setMinValue(0);
                }
                displayTime.setText(day.getValue()+" days "+newValue+" hours "+minute.getValue()+" minutes");
                break;
            case MINUTE:
                displayTime.setText(day.getValue()+" days "+hour.getValue()+" hours "+newValue+" minutes ");
                break;
        }
    }
}