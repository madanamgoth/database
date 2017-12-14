package com.kabal.qa.quickstart.database.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public String uri;
    public String fcmId;
    public String notificationStatus;
    public String uid;
    public Map<String, Boolean> userkeys = new HashMap<>();

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String uid,String username, String email,String uri,String fcmId,String notificationStatus) {
        this.uid=uid;
        this.username = username;
        this.email = email;
        this.uri=uri;
        this.fcmId=fcmId;
        this.notificationStatus=notificationStatus;
    }


    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("username", username);
        result.put("email", email);
        result.put("uri", uri);
        result.put("fcmId",fcmId);
        result.put("notificationStatus",notificationStatus);
        result.put("userkeys",userkeys);
        return result;
    }
}
// [END blog_user_class]

