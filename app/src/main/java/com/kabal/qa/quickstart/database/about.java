package com.kabal.qa.quickstart.database;

import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by amgoth.naik on 10/18/2017.
 */

public class about extends BaseActivity {
    private static final String TAG = "about";
    TextView actionbarTitle_sp_about;
    TextView textView2;
    TextView contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        actionbarTitle_sp_about= (TextView) findViewById(R.id.actionbarTitle_sp_about);
        textView2= (TextView) findViewById(R.id.textView2);
        contact= (TextView) findViewById(R.id.contact);
        textView2.setText("Kabal is Location based Quora which let Anyone to ask Question and Answer based on Location.\n" +
                "Location is divided in small buckets by districts and states of India ,Anyone can post Question and Answer in that Location.\n" +
                "Based on Current  Location Kabal Loads Nearest Question and Answers\n" +
                "Explore Question by states and districts in india");
        contact.setText("Contact    :  kabalservice@gmail.com");

    }
}
