package com.kabal.qa.quickstart.database.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kabal.qa.quickstart.database.R;
import com.kabal.qa.quickstart.database.models.Comments;

/**
 * Created by amgoth.naik on 10/12/2017.
 */

public  class  CommentViewHolder extends RecyclerView.ViewHolder  {

    public TextView authorView;
    public TextView bodyView;
    public TextView comment_time_date;
    public ImageView imageView;
    public ImageView cstarView;
    public TextView cnumStarsView;
    public ImageView deletePost;
    public ImageView editPost;

    public CommentViewHolder(View itemView) {
        super(itemView);
        authorView = (TextView) itemView.findViewById(R.id.comment_post_author);
        cstarView = (ImageView) itemView.findViewById(R.id.comment_star);
        cnumStarsView = (TextView) itemView.findViewById(R.id.comment_post_num_stars);
        bodyView = (TextView) itemView.findViewById(R.id.comment_post_title);
        comment_time_date=(TextView) itemView.findViewById(R.id.comment_time_date);
        imageView=(ImageView) itemView.findViewById(R.id.comment_post_author_photo);
        deletePost=(ImageView) itemView.findViewById(R.id.delete_comment);
        editPost=(ImageView) itemView.findViewById(R.id.edit_comment);
    }

    public void bindToPost(Comments comment, Context context, View.OnClickListener starClickListener) {
 /*       if(1==comment.postCheck){
            authorView.setText(comment.authoranonymous);
        }else{*/
            authorView.setText(comment.author);
       // }
        cnumStarsView.setText(String.valueOf(comment.starCount));
        //bodyView.setText(Html.fromHtml(comment.text).toString());
        comment_time_date.setText(DateUtils.getRelativeTimeSpanString(comment.date, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));

        /*if(1==comment.postCheck){

        }else{*/
            Glide.with(context).load(comment.purl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imageView);
        //}
        cstarView.setOnClickListener(starClickListener);
    }
}