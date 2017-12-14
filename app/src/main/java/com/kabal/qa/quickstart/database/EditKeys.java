package com.kabal.qa.quickstart.database;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;

/**
 * Created by amgoth.naik on 9/22/2017.
 */
public class EditKeys extends AppCompatActivity {

    private TagContainerLayout mTagContainerLayout1;
    private ListView lv;
    ArrayAdapter<String> adapter;
    LinearLayout container;
    ArrayList<CharSequence> itemList;
    //String Title;
    String Body;
    String status;
    //String from;
    List<String> list1;
    String postBody;
    String commentId;
    String mPostKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.row_container);
        list1 = new ArrayList<String>();
        list1 = getIntent().getStringArrayListExtra("userTags");
        if(list1==null){
            list1 = new ArrayList<String>();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Title=getIntent().getStringExtra("Title");
        commentId=getIntent().getStringExtra("commentId");
        mPostKey = getIntent().getStringExtra("post_key");
        postBody=getIntent().getStringExtra("postBody");
        status=getIntent().getStringExtra("status");
        Body= getIntent().getStringExtra("Body");
        //backbuttonstart//
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        //final String products[] = new String[1000000000];
        //tags=new HashMap<String, Boolean>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("keys");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Long> map = (HashMap<String, Long>) dataSnapshot.getValue();
                String products[] = map.keySet().toArray(new String[map.size()]);
            /*}
                   @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

                final EditText text = (EditText) findViewById(R.id.text_tag);
            /*final String  products[] = {"@India", "@Andaman and Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", "Chandigarh",
                "Chhattisgarh", "#Dadra and Nagar Haveli", "#Daman and Diu", "Delhi", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir",
                "Jharkhand", "Karnataka", "Kerala", "Lakshadweep", "Madhya Pradesh", "@Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha",
                "Puducherry", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura",
                "Uttar Pradesh", "Uttarakhand", "West Bengal"};*/
                //added start

                lv = (ListView) findViewById(R.id.list_view);
                container = (LinearLayout) findViewById(R.id.container);
                itemList = new ArrayList<CharSequence>();

                // Adding items to listview
                adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.list_item, R.id.product_name, products);
                lv.setAdapter(adapter);
                //add click on items start//
                //by me end
                mTagContainerLayout1 = (TagContainerLayout) findViewById(R.id.tagcontainerLayout1);
                // Set custom click listener
                //on tag click
                mTagContainerLayout1.setOnTagClickListener(new TagView.OnTagClickListener() {
                    @Override
                    public void onTagClick(final int position, final String text) {
               /* Toast.makeText(SelectKeys.this, "click-position:" + position + ", text:" + text,
                        Toast.LENGTH_SHORT).show();*/

                        AlertDialog dialog = new AlertDialog.Builder(EditKeys.this)
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
                        AlertDialog dialog = new AlertDialog.Builder(EditKeys.this)
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
                        AlertDialog dialog = new AlertDialog.Builder(EditKeys.this)
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
                //mTagcontainerLayout5.setTags(list5, colors);
                ImageView btnAddTag = (ImageView) findViewById(R.id.btn_add_tag);


                btnAddTag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //myString.replaceAll("^\\s+|\\s+$", "");
                        //if (text.getText().toString().replaceAll("^\\s+|\\s+$", "").equals("")) {
                        if(text.getText().toString().replaceAll("\\s+","").equals("")){
                            text.setText("");
                        } else {
                            if (list1.size() < 5) {
                                if ("#".equals(text.getText().toString().charAt(0))) {
                                    mTagContainerLayout1.addTag(text.getText().toString().replaceAll("\\s+",""));
                                    list1.add(text.getText().toString().replaceAll("\\s+",""));
                                } else {
                                    mTagContainerLayout1.addTag("#" + text.getText().toString().replaceAll("\\s+",""));
                                    list1.add("#" + text.getText().toString().replaceAll("\\s+",""));
                                }
                                text.setText("");
                            }else{
                                Toast toast = Toast.makeText(getApplicationContext(), "Only 5 tags permitted",
                                        Toast.LENGTH_SHORT);
                                toast.show();
                                text.setText("");
                            }
                        }
                        // Add tag in the specified position
//                mTagContainerLayout1.addTag(text.getText().toString(), 4);
                    }
                });

                //by me start
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        // if ("newpost".equals("newpost")) {
                        if(list1.size()<5){
                            if (!adapter.getItem(position).equals("")) {
                                if ("#".equals(adapter.getItem(position).charAt(0))) {
                                    mTagContainerLayout1.addTag(adapter.getItem(position));
                                    list1.add(adapter.getItem(position));
                                } else {
                                    mTagContainerLayout1.addTag("#" + adapter.getItem(position));
                                    list1.add("#" + adapter.getItem(position));
                                }
                                text.setText("");
                            }
                        }else{
                            Toast toast = Toast.makeText(getApplicationContext(), "Only 5 tags permitted",
                                    Toast.LENGTH_SHORT);
                            toast.show();
                            text.setText("");
                        }
                        //} else {
                        //  Intent i = new Intent(SelectSubKeys.this, MainActivity.class);
                        // i.putExtra("city", adapter.getItem(position));
                        //i.putExtra("status", status);
                        //startActivity(i);
                        //finish();
                        //}
                    }
                });
                //by me start

                /**
                 * Enabling Search Filter
                 * */
                text.addTextChangedListener(new TextWatcher() {


                    @Override
                    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                        cs =cs.toString().replaceAll("^\\s+", "");
                        if(! cs.toString().equals("")) {
                            if (!"#".equals(cs.toString().charAt(0))) {
                                cs = "#" + cs;
                            }
                        }
                        // When user changed the Text
                        EditKeys.this.adapter.getFilter().filter(cs);

                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                  int arg3) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        // TODO Auto-generated method stub
                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_name) {
            Intent i = new Intent(this, EditComment.class);
            //i.putExtra("Title",Title);
            i.putExtra("commentId",commentId);
            i.putExtra("post_key",mPostKey);
            i.putExtra("postBody",postBody);
            i.putExtra("Body",Body);
            i.putExtra("status",status);
            i.putStringArrayListExtra("userTags", (ArrayList<String>) list1);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
