package com.kabal.qa.quickstart.database.viewholder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kabal.qa.quickstart.database.GoogleSignInActivity;
import com.kabal.qa.quickstart.database.MainActivity;
import com.kabal.qa.quickstart.database.R;
import com.kabal.qa.quickstart.database.models.Post;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.lujun.androidtagview.TagContainerLayout;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView bodyView;
    public TextView date;
    public TextView answerCount;
    public ImageView postAuthorPhoto;
    public ImageView deletePost;
    public ImageView editPost;
    public TextView txtData;
    //private TagContainerLayout mTagContainerLayout1;
    List<String> list1;
    LinearLayout ll;

    public PostViewHolder(View itemView) {
        super(itemView);

        titleView = (TextView) itemView.findViewById(R.id.post_title);
        authorView = (TextView) itemView.findViewById(R.id.post_author);
        starView = (ImageView) itemView.findViewById(R.id.star);
        numStarsView = (TextView) itemView.findViewById(R.id.post_num_stars);
        bodyView = (TextView) itemView.findViewById(R.id.post_body);
        date=(TextView) itemView.findViewById(R.id.time_date);
        answerCount=(TextView) itemView.findViewById(R.id.answer_number);
        postAuthorPhoto=(ImageView) itemView.findViewById(R.id.post_author_photo);
        deletePost=(ImageView) itemView.findViewById(R.id.delete_post);
        editPost=(ImageView) itemView.findViewById(R.id.edit_post);
       // txtData=(TextView) itemView.findViewById(R.id.myText);
    }

    public void bindToPost(Post post, final Context context, View.OnClickListener  profileClickListener, View.OnClickListener  profileNameClickListener, View.OnClickListener  editClickListener, View.OnClickListener  deleteClickListener, View.OnClickListener starClickListener) {
        //test start

        /*String text = post.userTags.keySet().toString().replaceAll("[<>\\[\\],-]"," ");

        SpannableString spanString = new SpannableString(text);
        Matcher matcher = Pattern.compile("#([A-Za-z0-9_-]+)").matcher(spanString);
        while (matcher.find())
        {
            spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#0084b4")), matcher.start(), matcher.end(), 0); //to highlight word havgin '@'
            final String tag = matcher.group(0);
            ClickableSpan clickableSpan = new ClickableSpan()
            {
                @Override
                public void onClick(View textView)
                {
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("status",1);
                    intent.putExtra("cTag",tag);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                    //finish();
                    Toast.makeText(context, tag,
                            Toast.LENGTH_SHORT).show();
                }
                @Override
                public void updateDrawState(TextPaint ds)
                {
                    super.updateDrawState(ds);

                }
            };
            spanString.setSpan(clickableSpan, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        txtData.setText(spanString);
        txtData.setMovementMethod(LinkMovementMethod.getInstance());
        //test end*/

        //titleView.setText(post.title);
        authorView.setText(post.author);

        numStarsView.setText(String.valueOf((int)(post.starCount)));
       // bodyView.setText((Html.fromHtml(post.body)));
        date.setText(DateUtils.getRelativeTimeSpanString(post.date, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));
        //DAY_IN_MILLIS
        answerCount.setText(String.valueOf((int)post.answerCount)+ " Answers");
        postAuthorPhoto.setImageResource(R.drawable.ic_action_account_circle_40);
            Glide.with(context).load(post.purl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(postAuthorPhoto);
       // txtData.setOnClickListener(tagClickListener);
        authorView.setOnClickListener(profileNameClickListener);
        postAuthorPhoto.setOnClickListener(profileClickListener);
        starView.setOnClickListener(starClickListener);
        deletePost.setOnClickListener(deleteClickListener);
        editPost.setOnClickListener(editClickListener);

    }
}