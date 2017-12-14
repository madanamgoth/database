package com.kabal.qa.quickstart.database.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START blog_usernotification_class]
@IgnoreExtraProperties
public class UserNotification {

    public String heading;
    public String body;
    public String deeplink;
    public String fcmId;
    public long date;
    public String readstatus;
    public String senderid;
    public String city;
    public String commentId;
    public String receiverId;
    public String postId;
    public String status;

    public UserNotification() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserNotification(String heading, String body,String deeplink,
                            String fcmId,long date,String readstatus, String senderid,String city,String commentId,String receiverId,String postId,String status) {

        this.heading = heading;
        this.body = body;
        this.deeplink=deeplink;
        this.fcmId=fcmId;
        this.date=date;
        this.readstatus=readstatus;
        this.senderid=senderid;
        this.city=city;
        this.commentId=commentId;
        this.receiverId=receiverId;
        this.postId=postId;
        this.status=status;

    }
    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("heading", heading);
        result.put("body", body);
        result.put("deeplink", deeplink);
        result.put("fcmId", fcmId);
        result.put("date", date);
        result.put("readstatus", readstatus);
        result.put("senderid",senderid);
        result.put("city" ,city);
        result.put("commentId",commentId);
        result.put("receiverId",receiverId);
        result.put("postId",postId);
        result.put("status",status);
        return result;
    }
    // [END post_to_map]

}
// [END blog_usernotification_class]

