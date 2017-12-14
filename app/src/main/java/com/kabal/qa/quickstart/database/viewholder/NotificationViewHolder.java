package com.kabal.qa.quickstart.database.viewholder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;
import com.kabal.qa.quickstart.database.R;
import com.kabal.qa.quickstart.database.models.UserNotification;

/**
 * Created by amgoth.naik on 11/4/2017.
 */

public class NotificationViewHolder extends RecyclerView.ViewHolder  {

    public TextView title;
    public TextView body;
    public TextView time_date;

    public NotificationViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.notification_post_title);
        body = (TextView) itemView.findViewById(R.id.notification_post_body);
        time_date = (TextView) itemView.findViewById(R.id.notification_time_date);
    }

    public void bindToNotification(UserNotification userNotification, Context context,View.OnClickListener starClickListener) {
        title.setText(userNotification.heading);
        body.setText(userNotification.body);
        time_date.setText(DateUtils.getRelativeTimeSpanString(userNotification.date, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));
    }
}