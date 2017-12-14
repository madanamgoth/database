package com.kabal.qa.quickstart.database.fragment;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class RecentPostsFragment extends PostListFragment {

    public RecentPostsFragment() {}

    @Override
    public Query getQuery(FirebaseFirestore databaseReference) {
        Query recentPostsQuery;
        if("ALL".equals(getcTag())){
             recentPostsQuery = databaseReference.collection("post").orderBy("date");
        }else {
             //recentPostsQuery=  databaseReference.collection("post").whereGreaterThan("userTags."+getcTag()+"", 0)
               //     .orderBy("userTags."+getcTag()+"");
            recentPostsQuery=  databaseReference.collection("post").orderBy("date")
                    .orderBy("userTags."+getcTag()+"");
        }
        return recentPostsQuery;
    }
}