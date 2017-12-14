package com.kabal.qa.quickstart.database.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kabal.qa.quickstart.database.CommentMainActivity;
import com.kabal.qa.quickstart.database.FullCommentMainActivity;
import com.kabal.qa.quickstart.database.GoogleSignInActivity;
import com.kabal.qa.quickstart.database.R;
import com.kabal.qa.quickstart.database.models.Comments;
import com.kabal.qa.quickstart.database.models.ParcelableComment;
import com.kabal.qa.quickstart.database.models.ParcelablePost;
import com.kabal.qa.quickstart.database.models.Post;
import com.kabal.qa.quickstart.database.models.User;
import com.kabal.qa.quickstart.database.models.UserNotification;
import com.kabal.qa.quickstart.database.viewholder.NotificationViewHolder;

import org.w3c.dom.Comment;

/**
 * Created by amgoth.naik on 11/8/2017.
 */

public abstract class NotificationListFragment extends Fragment {
    private static final String TAG = "NotificationListFragment";
    // [START define_database_reference]
    private DatabaseReference mDatabase;
    // [END define_database_reference]
    private RecyclerView mRecycler;
    public ProgressBar mProgressBar;
    private LinearLayoutManager mManager;
    String status;
    String userId;
    private FirebaseRecyclerAdapter<UserNotification, NotificationViewHolder> mAdapter;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.notification_fragment, container, false);
        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]
        status=getArguments().getString("status",status);
        mRecycler = (RecyclerView) rootView.findViewById(R.id.recycler_notification);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.notification_progressbar);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Set up Layout Manager, reverse layout
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        mProgressBar.setVisibility(RecyclerView.VISIBLE);
        Query recentNotificationQuery = getQuery(mDatabase);
        mAdapter = new FirebaseRecyclerAdapter<UserNotification, NotificationViewHolder>(UserNotification.class, R.layout.notification_layout,
                NotificationViewHolder.class, recentNotificationQuery) {

            @Override
            protected void populateViewHolder(NotificationViewHolder notificationViewHolder, final UserNotification userNotification, int i) {
                notificationViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch full notification
                        //get post
                        //notification start post
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        mDatabase.child(userNotification.city).child(userNotification.postId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final Post post = dataSnapshot.getValue(Post.class);
                                ParcelablePost parcelablePost=new ParcelablePost(post);
                                Intent intent = new Intent(getActivity(), CommentMainActivity.class);
                                intent.putExtra("parcel_data", parcelablePost);
                                intent.putExtra(CommentMainActivity.EXTRA_POST_KEY, userNotification.postId);
                                intent.putExtra("ansCount",String.valueOf(post.answerCount));
                                //intent.putExtra("city",post.city);
                                intent.putExtra("status",status);
                                intent.putExtra("postuserid",post.uid);
                                startActivity(intent);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        // notification end

                    }
                });
                notificationViewHolder.bindToNotification(userNotification, getContext() , new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {

                    }
                });
                mProgressBar.setVisibility(RecyclerView.GONE);
            }
        };
        mRecycler.setAdapter(mAdapter);

    }
    public abstract Query getQuery(DatabaseReference databaseReference);
    public static String getUId(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }
}
