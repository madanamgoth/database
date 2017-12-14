package com.kabal.qa.quickstart.database;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kabal.qa.quickstart.database.models.Keys;
import com.kabal.qa.quickstart.database.models.Total;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
/**
 * Created by amgoth.naik on 9/20/2017.
 */

public class SelectKeys extends AppCompatActivity {

   private TagContainerLayout mTagContainerLayout1;
    private ListView lv;
    ArrayAdapter<String> adapter;
    LinearLayout container;
    ArrayList<CharSequence> itemList;
    String Body;
    String from;
    String toW;
    String status;
    //String cTag;
    List<String> list1;
    public Map<String,Long> mapTags;
    String flow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.row_container);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list1 = new ArrayList<String>();
        list1 = getIntent().getStringArrayListExtra("userTags");
        if(list1==null){
            list1 = new ArrayList<String>();
        }
      //  cTag=getIntent().getStringExtra("cTag");

        toW=getIntent().getStringExtra("toW");
        Body= getIntent().getStringExtra("Body");
        status=getIntent().getStringExtra("status");
        //backbuttonstart//
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        lv = (ListView) findViewById(R.id.list_view);
        container = (LinearLayout) findViewById(R.id.container);
       final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("keys");
        mapTags=new HashMap<String, Long>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Long> map = (HashMap<String, Long>) dataSnapshot.getValue();
                //mapTags= map.keySet().toArray(new String[map.size()]);
                mapTags= map;
               // itemList = new ArrayList<CharSequence>();
                // Adding items to listview
                adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.list_item, R.id.product_name, mapTags.values().toArray(new String[mapTags.size()]));
                lv.setAdapter(adapter);

                   }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final EditText text = (EditText) findViewById(R.id.text_tag);

      //  if("N".equals()){

       // }
  /*              lv = (ListView) findViewById(R.id.list_view);
                container = (LinearLayout) findViewById(R.id.container);
                itemList = new ArrayList<CharSequence>();

                // Adding items to listview
                adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.list_item, R.id.product_name, mapTags.keySet().toArray(new String[mapTags.size()]));
                lv.setAdapter(adapter);*/
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

                        AlertDialog dialog = new AlertDialog.Builder(SelectKeys.this)
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
                        AlertDialog dialog = new AlertDialog.Builder(SelectKeys.this)
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
                        AlertDialog dialog = new AlertDialog.Builder(SelectKeys.this)
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
                    }
                });

                //by me start
                lv.setOnItemClickListener(new OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if ("newpost".equals("newpost")) {
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
                        } else {
                            Intent i = new Intent(SelectKeys.this, MainActivity.class);
                            i.putExtra("city", adapter.getItem(position));
                            //i.putExtra("status", status);
                            startActivity(i);
                            finish();
                        }
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
                            if (35!=cs.toString().charAt(0)) {
                                    cs = "#" + cs;
                            }
                        }
                        // When user changed the Text
                        SelectKeys.this.adapter.getFilter().filter(cs);

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


          /*  }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

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
            Intent i = new Intent(this, NewPostActivity.class);
        //    i.putExtra(cTag,cTag);
            i.putExtra("Body",Body);
            i.putExtra("toW",toW);
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
