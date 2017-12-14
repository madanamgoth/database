package com.kabal.qa.quickstart.database.fragment;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyPostsFragment extends PostListFragment {
    public MyPostsFragment() {}
    @Override
    public Query getQuery(FirebaseFirestore databaseReference) {
        Query myPostQuery  = databaseReference.collection("post").whereEqualTo("uid", getUId());
        return myPostQuery;
    }
}