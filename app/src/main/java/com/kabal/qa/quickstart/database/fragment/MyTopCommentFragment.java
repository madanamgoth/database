package com.kabal.qa.quickstart.database.fragment;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * Created by amgoth.naik on 10/13/2017.
 */

public class MyTopCommentFragment extends CommentListFragment {
    public MyTopCommentFragment(){}
    @Override
    public Query getQuery(FirebaseFirestore databaseReference) {
        Query recentPostsQuery =databaseReference.collection(getmPostKey()).orderBy("starCount");
        return recentPostsQuery;
    }
}