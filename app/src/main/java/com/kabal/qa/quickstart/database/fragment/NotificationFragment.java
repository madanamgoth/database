package com.kabal.qa.quickstart.database.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by amgoth.naik on 11/8/2017.
 */

public class NotificationFragment extends NotificationListFragment{
    public NotificationFragment(){
    }
    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        Query recentNotificationQuery = FirebaseDatabase.getInstance().getReference()
                .child("user-notifications").child(getUId()).orderByChild("status").startAt("Y").endAt("Y");
        return recentNotificationQuery;
    }
}
