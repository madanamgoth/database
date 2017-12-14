package com.kabal.qa.quickstart.database;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.kabal.qa.quickstart.database.models.Comments;
import com.kabal.qa.quickstart.database.models.User;
import com.kabal.qa.quickstart.database.models.Post;
import com.kabal.qa.quickstart.database.viewholder.CommentViewHolder;

import android.widget.PopupMenu;

public class PostDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "PostDetailActivity";

    public static final String EXTRA_POST_KEY = "post_key";

    private DatabaseReference mPostReference;
    private DatabaseReference mCommentsReference;
    private ValueEventListener mPostListener;
    private String mPostKey;
    //private CommentAdapter mAdapter;

    private TextView mAuthorView;
    private TextView mTitleView;
    private TextView mBodyView;
    private EditText mCommentField;
    CheckBox cpostcheck;
    public int cpostCheck;
    // private Button mCommentButton;
    private RecyclerView mCommentsRecycler;
    private TextView mdate;
    public String city;
    public ImageView imageView;
    public String status;
    //start
    private static Application sApplication;
    private FirebaseRecyclerAdapter<Comments, CommentViewHolder> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        //      android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        //mActionBar.setDisplayShowTitleEnabled(false);

        // Get post key from intent
        mPostKey = getIntent().getStringExtra(EXTRA_POST_KEY);
        status=getIntent().getStringExtra("status");
        city=getIntent().getStringExtra("city");
        if (mPostKey == null) {
            throw new IllegalArgumentException("Must pass EXTRA_POST_KEY");
        }

        // Initialize Database
     /*   mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("posts").child(mPostKey);*/
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child(city).child(mPostKey);
     /*   mCommentsReference = FirebaseDatabase.getInstance().getReference()
                .child("post-comments").child(mPostKey);*/
        mCommentsReference = FirebaseDatabase.getInstance().getReference()
                .child(city+"-comments").child(mPostKey);

        // Initialize Views
        //  imageView=(ImageView) findViewById(R.id.post_author_photo);
        // mAuthorView = (TextView) findViewById(R.id.post_author);
        mTitleView = (TextView) findViewById(R.id.post_title);
        mBodyView = (TextView) findViewById(R.id.post_body);
        // mCommentField = (EditText) findViewById(R.id.field_comment_text);
        //mCommentButton = (Button) findViewById(R.id.button_post_comment);
        mCommentsRecycler = (RecyclerView) findViewById(R.id.recycler_comments);
        /*//touch recyler view by me
        mCommentsRecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(PostDetailActivity.this, mCommentsRecycler ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                       // final DatabaseReference postRef = getRef(position);
                        // do whatever
                        PopupMenu();
                      //  Toast.makeText(getApplicationContext(), "Please Allow Location Access for better App experience !!", Toast.LENGTH_LONG).show();


                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                        PopupMenu();
                       // Toast.makeText(getApplicationContext(), "Please Allow Location Access for better App experience !!!!!!!!!!!", Toast.LENGTH_LONG).show();
                    }
                })
        );*/
        //touch recyler view by me

        // mdate=(TextView) findViewById(R.id.time_date);
        // starView = (ImageView) findViewById(R.id.star);
        //numStarsView = (TextView) findViewById(R.id.post_num_stars);
        //heading start
        android.support.v7.app.ActionBar mActionBar =  getSupportActionBar();
        LayoutInflater mInflater = LayoutInflater.from(this);
        mActionBar.setDisplayShowTitleEnabled(false);
        View mCustomView = mInflater.inflate(R.layout.key, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.balance_text);
        mTitleTextView.setText(city);

        mTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("1".equals(status)){
                    Intent i= new Intent(PostDetailActivity.this, SelectKeys.class);
                    i.putExtra("city",city);
                    i.putExtra("district",city);
                    i.putExtra("state",city);
                    startActivity(i);
                }else{
                    Intent i = new Intent(PostDetailActivity.this, GoogleSignInActivity.class);
                    i.putExtra("click","1");
                    startActivity(i);
                }

            }
        });
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        //heading end

        //backbuttonstart//
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        //backbuttonend//
        //mCommentButton.setOnClickListener(this);
        //in the place of sumbit button
        // Button launches NewPostActivity
        findViewById(R.id.fabb_new_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if("1".equals(status)){
                    Intent i= new Intent(PostDetailActivity.this, NewCommentActivity.class);
                    i.putExtra("key",mPostKey);
                    i.putExtra("city",city);
                    startActivity(i);
                }else{
                    Intent i = new Intent(PostDetailActivity.this, GoogleSignInActivity.class);
                    i.putExtra("click","1");
                    startActivity(i);
                }
                /*startActivity(new Intent(PostDetailActivity.this, NewCommentActivity.class));*/
            }
        });

        //starView.setOnClickListener(starClickListener);
        mCommentsRecycler.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    public void onStart() {
        super.onStart();

        /*// Add value event listener to the post
        // [START post_value_event_listener]
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Post post = dataSnapshot.getValue(Post.class);
                // [START_EXCLUDE]
                if(1==post.postCheck){
                    mAuthorView.setText(post.authoranonymous);
                }else{
                    mAuthorView.setText(post.author);
                }

                mTitleView.setText(post.title);
                mBodyView.setText(post.body);

                if(1==post.postCheck){

                }else{
                    Glide.with(PostDetailActivity.this).load(post.purl)
                            .thumbnail(0.5f)
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(imageView);
                }
               // mdate.setText(String.valueOf(post.date));
                mdate.setText(DateUtils.getRelativeTimeSpanString(post.date, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));
              //  numStarsView.setText(String.valueOf(post.starCount));

                //starView.setOnClickListener(starClickListener);
                // [END_EXCLUDE]
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(PostDetailActivity.this, "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        //mPostListener = postListener;

        // Listen for comments
       // mAdapter = new CommentAdapter(this, mCommentsReference);
      //  mCommentsRecycler.setAdapter(mAdapter);
        */
        //mDatabase = FirebaseDatabase.getInstance().getReference();
        // mManager = new LinearLayoutManager(getActivity());
        // mManager.setReverseLayout(true);
        //mManager.setStackFromEnd(true);
        //mRecycler.setLayoutManager(mManager);

        //firebase recyler
        Query postsQuery = mCommentsReference;
        //by me
        //by me
        mAdapter = new FirebaseRecyclerAdapter<Comments, CommentViewHolder>(Comments.class, R.layout.item_comment,
                CommentViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final CommentViewHolder commentViewHolder,final Comments comment, final int i) {
                final DatabaseReference postRef = getRef(i);
                final String postKey = postRef.getKey();
                // Bind Post to ViewHolder, setting OnClickListener for the star button
                //  commentViewHolder.bindToPost(comment,PostDetailActivity.this);
            }
        };
        mCommentsRecycler.setAdapter(mAdapter);
        //firebase recyler
    }

/*    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mPostListener != null) {
            mPostReference.removeEventListener(mPostListener);
        }

        // Clean up comments listener
        mAdapter.cleanupListener();
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        // if (i == R.id.button_post_comment) {
        postComment();
        // }
    }

    private void postComment() {
        final String uid = getUid();
        FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user information
                        User user = dataSnapshot.getValue(User.class);
                        String authorName = user.username;

                        String authoranonymous=authorName;
                        //DateFormat df = new SimpleDateFormat("yyMMddHHmmssZ");
                        cpostCheck=0;
                        if(cpostcheck.isChecked()){
                            authoranonymous="anonymous";
                            cpostCheck=1;
                        }


                        // Create new comment object
                        String commentText = mCommentField.getText().toString();
                       // Comments comment = new Comments(uid, authorName, commentText,System.currentTimeMillis(),user.uri,0,"Y",);

                        // Push the comment, it will appear in the list
                        //mCommentsReference.push().setValue(comment);
                        int count=0;
                        // Clear the field
                        mCommentField.setText(null);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        FirebaseDatabase.getInstance().getReference().child(city).child(mPostKey).child("answerCount")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long role = (Long) dataSnapshot.getValue();
                        FirebaseDatabase.getInstance().getReference().child(city).child(mPostKey).child("answerCount").setValue(role+1);
                        FirebaseDatabase.getInstance().getReference().child("user-posts").child(uid).child(mPostKey).child("answerCount").setValue(role+1);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    /*private static class CommentViewHolder extends RecyclerView.ViewHolder{

        public TextView authorView;
        public TextView bodyView;
        public TextView comment_time_date;
        public ImageView cstarView;
        public TextView cnumStarsView;
        public ImageView imageView;
        private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        public CommentViewHolder(View itemView) {
            super(itemView);

            authorView = (TextView) itemView.findViewById(R.id.comment_author);
            bodyView = (TextView) itemView.findViewById(R.id.comment_body);
            comment_time_date=(TextView) itemView.findViewById(R.id.comment_time_date);
            imageView=(ImageView) itemView.findViewById(R.id.comment_photo);
          //  cstarView = (ImageView) itemView.findViewById(R.id.c_star);
           // cnumStarsView = (TextView) itemView.findViewById(R.id.c_post_num_stars);
           // cstarView.setOnClickListener(this);
        }

        //@Override
        //public void onClick(View view) {

        //}

    }
*/
    /*private static class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

        private Context mContext;
        private DatabaseReference mDatabaseReference;
        private ChildEventListener mChildEventListener;

        private List<String> mCommentIds = new ArrayList<>();
        private List<Comment> mComments = new ArrayList<>();
        public CommentAdapter(final Context context, DatabaseReference ref) {
            mContext = context;
            mDatabaseReference = ref;

            // Create child event listener
            // [START child_event_listener_recycler]
            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                    // A new comment has been added, add it to the displayed list
                    Comment comment = dataSnapshot.getValue(Comment.class);

                    // [START_EXCLUDE]
                    // Update RecyclerView
                    mCommentIds.add(dataSnapshot.getKey());
                    mComments.add(comment);
                    notifyItemInserted(mComments.size() - 1);
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so displayed the changed comment.
                    Comment newComment = dataSnapshot.getValue(Comment.class);
                    String commentKey = dataSnapshot.getKey();

                    // [START_EXCLUDE]
                    int commentIndex = mCommentIds.indexOf(commentKey);
                    if (commentIndex > -1) {
                        // Replace with the new data
                        mComments.set(commentIndex, newComment);

                        // Update the RecyclerView
                        notifyItemChanged(commentIndex);
                    } else {
                        Log.w(TAG, "onChildChanged:unknown_child:" + commentKey);
                    }
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so remove it.
                    String commentKey = dataSnapshot.getKey();

                    // [START_EXCLUDE]
                    int commentIndex = mCommentIds.indexOf(commentKey);
                    if (commentIndex > -1) {
                        // Remove data from the list
                        mCommentIds.remove(commentIndex);
                        mComments.remove(commentIndex);

                        // Update the RecyclerView
                        notifyItemRemoved(commentIndex);
                    } else {
                        Log.w(TAG, "onChildRemoved:unknown_child:" + commentKey);
                    }
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                    // A comment has changed position, use the key to determine if we are
                    // displaying this comment and if so move it.
                    Comment movedComment = dataSnapshot.getValue(Comment.class);
                    String commentKey = dataSnapshot.getKey();

                    // ...
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                    Toast.makeText(mContext, "Failed to load comments.",
                            Toast.LENGTH_SHORT).show();
                }
            };
            ref.addChildEventListener(childEventListener);
            // [END child_event_listener_recycler]

            // Store reference to listener so it can be removed on app stop
            mChildEventListener = childEventListener;
        }

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.item_comment, parent, false);
            return new CommentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder, int position) {




            Comment comment = mComments.get(position);
            if(1==comment.postCheck){
                holder.authorView.setText(comment.authoranonymous);
            }else{
                holder.authorView.setText(comment.author);
            }

            holder.bodyView.setText(comment.text);
            holder.comment_time_date.setText(DateUtils.getRelativeTimeSpanString(comment.date, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));

            if(1==comment.postCheck){

            }else{
                Glide.with(mContext).load(comment.purl)
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(holder.imageView);
            }
            //holder.cnumStarsView.setText(String.valueOf(comment.cstarCount));
            //holder.cstarView.setOnClickListener(starClickListener);
            // Determine if the current user has liked this post and set UI accordingly
            //added by madan

            //added by madan
        }

        @Override
        public int getItemCount() {
            return mComments.size();
        }

        public void cleanupListener() {
            if (mChildEventListener != null) {
                mDatabaseReference.removeEventListener(mChildEventListener);
            }
        }

    }*/


    // [START post_stars_transaction]
    private void onCStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post p = mutableData.getValue(Post.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.stars.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    p.starCount = p.starCount - 1;
                    p.stars.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    p.starCount = p.starCount + 1;
                    //p.stars.put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }
    // [END post_stars_transaction]
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()== android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
    public void PopupMenu(){
        PopupMenu popupMenu = new PopupMenu(PostDetailActivity.this,mBodyView);
        // Set a click listener for menu item click
        popupMenu.getMenuInflater().inflate(R.menu.textview_popup_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    // Handle the non group menu items here
                    case R.id.red:
                        Toast.makeText(getApplicationContext(), "Please Allow Location Access for better App experience !!", Toast.LENGTH_LONG).show();
                        return true;
                    case R.id.green:
                        // Set the text color to green
                        Toast.makeText(getApplicationContext(), "Please Allow Location Access for better App experience !!", Toast.LENGTH_LONG).show();
                        return true;
                    case R.id.blue:
                        // Set the text color to blue
                        Toast.makeText(getApplicationContext(), "Please Allow Location Access for better App experience !!", Toast.LENGTH_LONG).show();
                        return true;
                    default:
                        return false;
                }
            }
        });

        // Finally, show the popup menu
        popupMenu.show();
    }
}