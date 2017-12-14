package com.kabal.qa.quickstart.database;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by amgoth.naik on 10/18/2017.
 */

public class PrepareKeys  extends BaseActivity{
    //by me
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Map<String, Boolean>[] stars = new Map[]{new HashMap<>()};
        FirebaseDatabase.getInstance().getReference().child("test")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        stars[0] = (Map<String, Boolean>) dataSnapshot.getValue();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
}
