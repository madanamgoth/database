package com.kabal.qa.quickstart.database.fragment;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MyTopPostsFragment extends PostListFragment {
    @Override
    public Query getQuery(FirebaseFirestore databaseReference) {
        Query recentPostsQuery;
        if("ALL".equals(getcTag())){
            recentPostsQuery = databaseReference.collection("post").orderBy("trendCount");
        }else {
            /*recentPostsQuery=  databaseReference.collection("post").whereGreaterThan("userTags."+getcTag()+"", 0)
                    .orderBy("userTags."+getcTag()+"");*/
            recentPostsQuery=  databaseReference.collection("post").orderBy("trendCount")
                    .orderBy("userTags."+getcTag()+"");
        }
        return recentPostsQuery;
    }
}